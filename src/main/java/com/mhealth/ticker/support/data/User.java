package com.mhealth.ticker.support.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class User {

    private static List<User> LIST_USERS;

    public static List<User> get() throws IOException {
        if (LIST_USERS != null) {
            return LIST_USERS;
        } else {
            InputStream is = null;
            try {
                is = Config.class.getClassLoader().getResourceAsStream("users.json");
                String source = IOUtils.toString(is, "UTF-8");
                LIST_USERS =  new Gson().fromJson(source, new TypeToken<ArrayList<User>>() {
                }.getType());
                return LIST_USERS;
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    public static User get(Integer id) throws IOException {
        List<User> users = get();
        if (users != null && users.size() > 0) {
            for (User u : users) {
                if (u.getId().equals(id)) {
                    return u;
                }
            }
        }
        return null;
    }

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("slack_name")
    private String slackName;

    @SerializedName("slack_id")
    private String slackId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlackName() {
        return slackName;
    }

    public void setSlackName(String slackName) {
        this.slackName = slackName;
    }

    public String getSlackId() {
        return slackId;
    }

    public void setSlackId(String slackId) {
        this.slackId = slackId;
    }
}
