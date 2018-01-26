package com.mhealth.ticker.support.job;

import com.google.gson.Gson;
import com.mhealth.ticker.support.data.Attendance;
import com.mhealth.ticker.support.data.Attendances;
import com.mhealth.ticker.support.data.Config;
import com.mhealth.ticker.support.data.User;
import com.mhealth.ticker.support.data.mongo.MongoHelper;
import com.mhealth.ticker.support.service.AttendanceService;
import com.mhealth.ticker.support.service.SlackHookService;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

import java.math.BigDecimal;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class AttendanceJob extends BaseJob {

    @Override
    void run(SlackHookService slackHookService, String session) throws Exception {
        AttendanceService attendanceService = AttendanceService.Factory.create();
        Attendances attendances = attendanceService.getAttendances().execute().body();
        List<User> users = User.get();
        MongoCollection<Document> collection = MongoHelper.getCollection(Attendance.class);
        long totalRecord = collection.count();
        boolean willNotify = totalRecord != 0;
        log("Total record " + totalRecord);
        log("Will notify " + willNotify);
        log("User size " + users.size());
        log("Last updated " + attendances.getLastUpdated());
        List<Attendance> attendanceList = attendances.getAttendances();
        if (attendanceList != null && attendanceList.size() > 0) {
            log("Attendance size " + attendanceList.size());
            for (Attendance attendance : attendanceList) {
                if (attendance.getTimestamp() != null) {
                    attendance.setId(BigDecimal.valueOf(attendance.getTimestamp().getTime() / 1000).setScale(0, BigDecimal.ROUND_FLOOR));
                }
                if (collection.find(eq("id", attendance.getId().doubleValue())).first() == null) {
                    MongoHelper.insert(attendance, Attendance.class);
                    log("Insert new attendance record " + new Gson().toJson(attendance));
                    if (willNotify) {
                        SlackHookService.HookBody hookBody = new SlackHookService.HookBody();
                        User user = User.get(attendance.getUserId());
                        if (user != null) {
                            hookBody.setText(user.getName() + " vừa chấm vân tay!");
                            try {
                                slackHookService.api().hook(Config.getInstance().getSlack().getHookAlert(), hookBody).execute();
                            } catch (Exception e) {}
                        } else {
                            error("Could not found user id " + attendance.getUserId());
                        }
                    }
                }
            }
        }
    }

    @Override
    ScheduleBuilder scheduleBuilder() {
        return SimpleScheduleBuilder.repeatMinutelyForever(1);
    }

    public static void main(String[] args) throws JobExecutionException {
        AttendanceJob attendanceJob = new AttendanceJob();
        attendanceJob.execute(null);
    }
}
