package com.lk.myproject.room;

import com.lk.myproject.room.encrypt.StringConverter;
import com.lk.myproject.room.encrypt.StringEntry;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "hotline")
@TypeConverters(StringConverter.class)
public class HotLine implements Serializable {

    public static final String BELONG_USER_ID = "belongUserId";
    public static final String START_IMTE = "startTime";
    public static final String LIVE_ID = "liveId";

    @Nullable
    @PrimaryKey
    @ColumnInfo
    private Integer id;
    // 扩展字段
    @Nullable
    @ColumnInfo
    public Long belongUserId;

    @Nullable
    @ColumnInfo
    private Integer liveId;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private String detailIntroduction;

    @ColumnInfo
    private String backgroudUrl;

    @ColumnInfo
    private String mediaUrl;

    @Nullable
    @ColumnInfo
    private Long count;

    @Nullable
    @ColumnInfo
    private Integer viewCount;

    @Nullable
    @ColumnInfo
    private Integer status; //1,2,3

    @ColumnInfo
    private String city; //主播所在城市

    @ColumnInfo
    private String province; //主播所在省份

    @Nullable
    private Long startTime;

    @Nullable
    private Long watchTime; //观看时长

    @Nullable
    private Integer isAttention;

    @Nullable
    private Integer isSubscribed; //是否已订阅 0,1

    @ColumnInfo
    private String jsonStringHostUser;

    @ColumnInfo
    private String jsonStringShareDetail;

    @Nullable
    private Integer isVideoLive; //是否是视频直播 0否1是

    @ColumnInfo
    private StringEntry encrypt = new StringEntry("");

    @Ignore
    private String notice;

    @Ignore
    private Integer statisticsId;

    @Ignore
    private boolean isMELive = false;
    @Ignore
    private String lid;
    @Ignore
    private String stripeUrl;

    @Ignore
    private boolean isNeedResetAudioUI = true; //更改UI临时使用的属性
    @Ignore
    private int level;

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailIntroduction() {
        return detailIntroduction;
    }

    public void setDetailIntroduction(String detailIntroduction) {
        this.detailIntroduction = detailIntroduction;
    }

    public String getBackgroudUrl() {
        return backgroudUrl;
    }

    public void setBackgroudUrl(String backgroudUrl) {
        this.backgroudUrl = backgroudUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(long watchTime) {
        this.watchTime = watchTime;
    }

    public int getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(int isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public boolean isNeedResetAudioUI() {
        return isNeedResetAudioUI;
    }

    public void setIsNeedResetAudioUI(boolean isNeedResetAudioUI) {
        this.isNeedResetAudioUI = isNeedResetAudioUI;
    }

    public String getJsonStringHostUser() {
        return jsonStringHostUser;
    }

    public void setJsonStringHostUser(String jsonStringHostUser) {
        this.jsonStringHostUser = jsonStringHostUser;
    }

    public String getJsonStringShareDetail() {
        return jsonStringShareDetail;
    }

    public void setJsonStringShareDetail(String jsonStringShareDetail) {
        this.jsonStringShareDetail = jsonStringShareDetail;
    }

    public long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isMELive() {
        return isMELive;
    }

    public void setMELive(boolean isMELive) {
        this.isMELive = isMELive;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public void setIsAttention(int attention) {
        this.isAttention = attention;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public boolean hasReportId() {
        return statisticsId != null;
    }

    public Integer getReportId() {
        return statisticsId;
    }

    public void setStatisticsId(Integer statisticsId) {
        this.statisticsId = statisticsId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getStripeUrl() {
        return stripeUrl;
    }

    public void setStripeUrl(String stripeUrl) {
        this.stripeUrl = stripeUrl;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getIsVideoLive() {
        return isVideoLive;
    }

    public void setIsVideoLive(int isVideoLive) {
        this.isVideoLive = isVideoLive;
    }

    public StringEntry getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(StringEntry encrypt) {
        this.encrypt = encrypt;
    }
}
