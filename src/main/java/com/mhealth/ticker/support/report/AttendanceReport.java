package com.mhealth.ticker.support.report;

import com.mhealth.ticker.support.data.Attendance;
import com.mhealth.ticker.support.data.Attendances;
import com.mhealth.ticker.support.data.Config;
import com.mhealth.ticker.support.data.User;
import com.mhealth.ticker.support.job.BaseJob;
import com.mhealth.ticker.support.service.AttendanceService;
import com.mhealth.ticker.support.service.SlackHookService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttendanceReport {

    public static final Logger logger = Logger.getLogger(BaseJob.class.getName());

    protected String getName() {
        return this.getClass().getSimpleName();
    }

    public String collect(Date from, Date to) throws IOException {
        return collect(from, to, true, false);
    }

    public String collect(Date from, Date to, boolean onlyFirstCheckedIn, boolean notify) throws IOException {
        SlackHookService slackHookService = SlackHookService.getInstance();
        AttendanceService attendanceService = AttendanceService.Factory.create();
        Attendances attendances = attendanceService.getAttendances().execute().body();
        List<User> users = User.get();
        log("User size " + users.size());
        log("Last updated " + attendances.getLastUpdated());
        List<Attendance> attendanceList = attendances.getAttendances();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        sdfDay.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        StringBuffer sb = new StringBuffer();
        sb.append("User ID, Name, Checked in at\n");
        List<String> checkList = new ArrayList<>();
        if (attendanceList != null && attendanceList.size() > 0) {
            log("Attendance size " + attendanceList.size());
            for (Attendance attendance : attendanceList) {
                if (attendance.getTimestamp() != null) {
                    if (attendance.getTimestamp().after(from) && attendance.getTimestamp().before(to)) {
                        attendance.setId(BigDecimal.valueOf(attendance.getTimestamp().getTime() / 1000).setScale(0, BigDecimal.ROUND_FLOOR));
                        User user = User.get(attendance.getUserId());
                        if (user != null) {
                            String check = user.getId() + "-" + sdfDay.format(attendance.getTimestamp());
                            log("Check string " + check);
                            if (onlyFirstCheckedIn) {
                                if (!checkList.contains(check)) {
                                    sb.append(user.getId()).append(",").append(user.getSlackName()).append(",").append(sdf.format(attendance.getTimestamp()));
                                    sb.append("\n");
                                    checkList.add(check);
                                    if (notify) {
                                        notify(slackHookService, user, sdf, attendance);
                                    }
                                } else {
                                    log("Already checked in this day");
                                }
                            } else {
                                sb.append(user.getId()).append(",").append(user.getSlackName()).append(",").append(sdf.format(attendance.getTimestamp()));
                                sb.append("\n");
                                if (notify) {
                                    notify(slackHookService, user, sdf, attendance);
                                }
                            }
                        } else {
                            error("Could not found user id " + attendance.getUserId());
                        }
                    } else {
                        error("Date " + attendance.getTimestamp() + " is out of range. From " + from + " to " + to);
                    }
                } else {
                    error("Timestamp is null");
                }

            }
        }
        return sb.toString();
    }

    public void notify(SlackHookService slackHookService, User user, SimpleDateFormat sdf, Attendance attendance) {
        SlackHookService.HookBody hookBody = new SlackHookService.HookBody();
        hookBody.setText("@" + user.getSlackName() + " checked in at " + sdf.format(attendance.getTimestamp()));
        try {
            slackHookService.api().hook(Config.getInstance().getSlack().getHookAlert(), hookBody).execute();
        } catch (Exception e) {}
    }

    public String getTag() {
        return "[" + getName() + "]";
    }

    public void log(String log) {
        logger.info(getTag() + " " + log);
    }

    public void error(String message) {
        error(message, null);
    }

    public void error(String message, Throwable e) {
        if (e != null) {
            logger.log(Level.SEVERE, message, e);
        } else {
            logger.log(Level.SEVERE, message);
        }
    }

    public void hookError(SlackHookService slackHookService, String error) {
        hookError(slackHookService, error, null);
    }

    public void hookError(SlackHookService slackHookService, String error, Throwable e) {
        try {
            slackHookService.hookError(error, e);
        } catch (Exception ex) {

        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        File report = new File("/Users/luhonghai/manadr/report/report-only-first-checked-in.csv");
        if (report.exists()) {
            FileUtils.forceDelete(report);
        }
        FileUtils.writeStringToFile(report, new AttendanceReport().collect(sdf.parse("2018/05/01 00:00:00"), sdf.parse("2018/05/31 23:59:59")), "UTF-8");

        report = new File("/Users/luhonghai/manadr/report/report-full.csv");
        if (report.exists()) {
            FileUtils.forceDelete(report);
        }
        FileUtils.writeStringToFile(report, new AttendanceReport().collect(sdf.parse("2018/05/01 00:00:00"), sdf.parse("2018/05/31 23:59:59"), false, false), "UTF-8");
    }
}
