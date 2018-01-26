package com.mhealth.ticker.support.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            Gson gson = new Gson();
            try {
                String configPath = System.getenv().get("CUSTOM_CONFIG_PATH");
                String source;
                if (TextUtils.isEmpty(configPath)) {
                    source = IOUtils.toString(Config.class.getClassLoader().getResourceAsStream("config.json"), "UTF-8");
                } else {
                    source = FileUtils.readFileToString(new File(configPath), "UTF-8");
                }
                logger.info("Read config " + source);
                instance = gson.fromJson(source, Config.class);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not read config", e);
            }
        }
        return instance;
    }

    public Slack getSlack() {
        return slack;
    }

    public void setSlack(Slack slack) {
        this.slack = slack;
    }

    public static class Slack {

        @SerializedName("hook_error")
        @Expose
        private String hookError;

        @SerializedName("hook_health_check")
        @Expose
        private String hookHealthCheck;

        @SerializedName("hook_alert")
        @Expose
        private String hookAlert;

        public String getHookError() {
            return hookError;
        }

        public void setHookError(String hookError) {
            this.hookError = hookError;
        }

        public String getHookHealthCheck() {
            return hookHealthCheck;
        }

        public void setHookHealthCheck(String hookHealthCheck) {
            this.hookHealthCheck = hookHealthCheck;
        }

        public String getHookAlert() {
            return hookAlert;
        }

        public void setHookAlert(String hookAlert) {
            this.hookAlert = hookAlert;
        }
    }

    @SerializedName("debug")
    @Expose
    private boolean debug;

    @SerializedName("mongo")
    @Expose
    private String mongo;

    @SerializedName("mongo_name")
    @Expose
    private String mongoName;

    @SerializedName("slack")
    @Expose
    private Slack slack;

    public String getMongo() {
        return mongo;
    }

    public void setMongo(String mongo) {
        this.mongo = mongo;
    }

    public String getMongoName() {
        return mongoName;
    }

    public void setMongoName(String mongoName) {
        this.mongoName = mongoName;
    }

    public boolean isDebug() {
        String isDebug = System.getenv("DEBUG");
        if (isDebug != null && isDebug.length() > 0) {
            return Boolean.parseBoolean(isDebug);
        }
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
