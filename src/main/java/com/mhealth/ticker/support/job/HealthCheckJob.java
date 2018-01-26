package com.mhealth.ticker.support.job;

import com.mhealth.ticker.support.data.*;
import com.mhealth.ticker.support.data.mongo.MongoHelper;
import com.mhealth.ticker.support.service.SlackHookService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.apache.http.util.TextUtils;
import org.bson.Document;
import org.ocpsoft.prettytime.PrettyTime;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

/**
 * Created by luhonghai on 4/10/17.
 */
public class HealthCheckJob extends BaseJob {

    @Override
    public void run(SlackHookService slackHookService,
                    String session) throws Exception {
        if (Config.getInstance().isDebug()) {
            log("Skip health check in debug mode");
            return;
        }
        SlackHookService.HookBody hookBody = new SlackHookService.HookBody();
        MongoCollection<Document> collection = MongoHelper.getCollection(HealthCheck.class);
        for (Class<? extends BaseJob> jobClazz : BaseJob.REGISTERED_JOBS) {
            log("Get health check info of job " + jobClazz.getName());
            Document document = collection.find(eq("name", jobClazz.getSimpleName())).sort(Sorts.descending("$natural")).limit(1).first();
            if (document != null) {
                SlackHookService.Attachment attachment = new SlackHookService.Attachment();

                HealthCheck healthCheck = MongoHelper.convert(document, HealthCheck.class);
                attachment.setText(healthCheck.getName());
                attachment.setColor(healthCheck.isStatus() ? "#859950" : "#D67E74");
                if (!healthCheck.isStatus()) {
                    attachment.addField(new SlackHookService.Field("Error", healthCheck.getMessage()));
                }
                attachment.addField(new SlackHookService.Field("Last updated", new PrettyTime().format(healthCheck.getEndTime())));
                hookBody.addAttachment(attachment);
            }
        }
        try {
            if (Config.getInstance().getSlack() != null && !TextUtils.isEmpty(Config.getInstance().getSlack().getHookHealthCheck())) {
                slackHookService.api().hook(Config.getInstance().getSlack().getHookHealthCheck(), hookBody).execute();
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    @Override
    ScheduleBuilder scheduleBuilder() {
        return SimpleScheduleBuilder.repeatHourlyForever(2);
    }
}
