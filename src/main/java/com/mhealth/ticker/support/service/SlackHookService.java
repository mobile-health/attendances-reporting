package com.mhealth.ticker.support.service;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mhealth.ticker.support.data.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.TextUtils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

import javax.annotation.Generated;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhonghai on 10/3/16.
 */
public class SlackHookService {

    private static final String BASE_URL = "https://hooks.slack.com/";

    private static  SlackHookService instance;

    private Retrofit retrofit;

    private SlackHookService(Retrofit retrofit) {
        this.retrofit = retrofit;
    }


    public static SlackHookService getInstance() {
        if (instance == null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setPrettyPrinting().create()))
                    .build();
            instance = new SlackHookService(retrofit);

        }
        return instance;
    }

    public void hookError(String error, Throwable e) {
        HookBody hookBody = new HookBody(error + (e == null ? "" : (". Message: " + (ExceptionUtils.getStackTrace(e)))));
        try {
            if (Config.getInstance().getSlack() != null && !TextUtils.isEmpty(Config.getInstance().getSlack().getHookError())) {
                api().hook(Config.getInstance().getSlack().getHookError(), hookBody).execute();
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }

    public Api api() {
        return retrofit.create(Api.class);
    }

    public interface Api {
        @POST()
        Call<HookResponse> hook(@Url String url, @Body HookBody body);
    }

    public static class HookResponse {

    }

    public static class HookBody {

        private String text;

        private String username = "Attendance Reporter";

        @SerializedName("icon_emoji")
        private String iconEmoji = ":nerd_face:";

        @SerializedName("link_names")
        private Integer linkNames = 1;

        private List<Attachment> attachments = new ArrayList<>();

        public HookBody() {

        }

        public HookBody(String text) {
            this.text = text;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIconEmoji() {
            return iconEmoji;
        }

        public void setIconEmoji(String iconEmoji) {
            this.iconEmoji = iconEmoji;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }

        public void addAttachment(Attachment attachment) {
            if (attachments == null) attachments = new ArrayList<>();
            attachments.add(attachment);
        }

        public Integer getLinkNames() {
            return linkNames;
        }

        public void setLinkNames(Integer linkNames) {
            this.linkNames = linkNames;
        }
    }

    @Generated("org.jsonschema2pojo")
    public static class Attachment {

        @SerializedName("fallback")
        @Expose
        private String fallback;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("pretext")
        @Expose
        private String pretext;
        @SerializedName("author_name")
        @Expose
        private String authorName;
        @SerializedName("author_link")
        @Expose
        private String authorLink;
        @SerializedName("author_icon")
        @Expose
        private String authorIcon;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("title_link")
        @Expose
        private String titleLink;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("fields")
        @Expose
        private List<Field> fields = new ArrayList<Field>();
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("thumb_url")
        @Expose
        private String thumbUrl;
        @SerializedName("footer")
        @Expose
        private String footer;
        @SerializedName("footer_icon")
        @Expose
        private String footerIcon;
        @SerializedName("ts")
        @Expose
        private Integer ts;

        /**
         *
         * @return
         * The fallback
         */
        public String getFallback() {
            return fallback;
        }

        /**
         *
         * @param fallback
         * The fallback
         */
        public void setFallback(String fallback) {
            this.fallback = fallback;
        }

        /**
         *
         * @return
         * The color
         */
        public String getColor() {
            return color;
        }

        /**
         *
         * @param color
         * The color
         */
        public void setColor(String color) {
            this.color = color;
        }

        /**
         *
         * @return
         * The pretext
         */
        public String getPretext() {
            return pretext;
        }

        /**
         *
         * @param pretext
         * The pretext
         */
        public void setPretext(String pretext) {
            this.pretext = pretext;
        }

        /**
         *
         * @return
         * The authorName
         */
        public String getAuthorName() {
            return authorName;
        }

        /**
         *
         * @param authorName
         * The author_name
         */
        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        /**
         *
         * @return
         * The authorLink
         */
        public String getAuthorLink() {
            return authorLink;
        }

        /**
         *
         * @param authorLink
         * The author_link
         */
        public void setAuthorLink(String authorLink) {
            this.authorLink = authorLink;
        }

        /**
         *
         * @return
         * The authorIcon
         */
        public String getAuthorIcon() {
            return authorIcon;
        }

        /**
         *
         * @param authorIcon
         * The author_icon
         */
        public void setAuthorIcon(String authorIcon) {
            this.authorIcon = authorIcon;
        }

        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The titleLink
         */
        public String getTitleLink() {
            return titleLink;
        }

        /**
         *
         * @param titleLink
         * The title_link
         */
        public void setTitleLink(String titleLink) {
            this.titleLink = titleLink;
        }

        /**
         *
         * @return
         * The text
         */
        public String getText() {
            return text;
        }

        /**
         *
         * @param text
         * The text
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         *
         * @return
         * The fields
         */
        public List<Field> getFields() {
            return fields;
        }

        /**
         *
         * @param fields
         * The fields
         */
        public void setFields(List<Field> fields) {
            this.fields = fields;
        }


        public void addField(Field field) {
            if (this.fields == null) this.fields = new ArrayList<>();
            this.fields.add(field);
        }
        /**
         *
         * @return
         * The imageUrl
         */
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         *
         * @param imageUrl
         * The image_url
         */
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        /**
         *
         * @return
         * The thumbUrl
         */
        public String getThumbUrl() {
            return thumbUrl;
        }

        /**
         *
         * @param thumbUrl
         * The thumb_url
         */
        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        /**
         *
         * @return
         * The footer
         */
        public String getFooter() {
            return footer;
        }

        /**
         *
         * @param footer
         * The footer
         */
        public void setFooter(String footer) {
            this.footer = footer;
        }

        /**
         *
         * @return
         * The footerIcon
         */
        public String getFooterIcon() {
            return footerIcon;
        }

        /**
         *
         * @param footerIcon
         * The footer_icon
         */
        public void setFooterIcon(String footerIcon) {
            this.footerIcon = footerIcon;
        }

        /**
         *
         * @return
         * The ts
         */
        public Integer getTs() {
            return ts;
        }

        /**
         *
         * @param ts
         * The ts
         */
        public void setTs(Integer ts) {
            this.ts = ts;
        }

    }

    @Generated("org.jsonschema2pojo")
    public static class Field {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("short")
        @Expose
        private Boolean _short;

        public Field() {

        }
        public Field(String title, String value) {
            this(title, value, true);
        }

        public Field(String title, String value, boolean isShort) {
            this._short = isShort;
            this.title = title;
            this.value = value;
        }
        /**
         *
         * @return
         * The title
         */
        public String getTitle() {
            return title;
        }

        /**
         *
         * @param title
         * The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         *
         * @return
         * The value
         */
        public String getValue() {
            return value;
        }

        /**
         *
         * @param value
         * The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         *
         * @return
         * The _short
         */
        public Boolean getShort() {
            return _short;
        }

        /**
         *
         * @param _short
         * The short
         */
        public void setShort(Boolean _short) {
            this._short = _short;
        }

    }

    public static void main(String[] args) throws IOException {
        SlackHookService slackHookService = SlackHookService.getInstance();
        HookBody hookBody = new HookBody("For testing only");
        Attachment attachment = new Attachment();
        attachment.setTitle("Testing");
        attachment.getFields().add(new Field("f1", "2"));
        attachment.getFields().add(new Field("f2", "3"));
        hookBody.getAttachments().add(attachment);
        Config.Slack slack = Config.getInstance().getSlack();
        try {
            slackHookService.api().hook(slack.getHookError(), hookBody).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        slackHookService.api().hook(slack.getHookHealthCheck(), hookBody).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

