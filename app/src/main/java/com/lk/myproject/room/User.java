package com.lk.myproject.room;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "userId", name = "user_userId_idx", unique = true)})
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -555429051118770619L;
    public static final String USER_ID = "userId";
    @Nullable
    @PrimaryKey
    @ColumnInfo(name = "userId")
    private Long userId;
    @Nullable
    @ColumnInfo
    private Long bilinId;
    //@ColumnInfo(persisterClass = StringEncryptPersister.class)
    private String nickname;
    @Nullable
    @ColumnInfo
    private Integer sex;
    @Nullable
    @ColumnInfo
    private Integer sexOrientation;
    //@ColumnInfo(persisterClass = StringEncryptPersister.class)
    private String birthday;
    @ColumnInfo
    private String smallUrl;      //这个是真实的头像字段
    @ColumnInfo
    private String bigUrl;
    @ColumnInfo
    private String myBigHeadUrl;
    //这个是AI识别真人头像需求增加的字段，未审核通过之前这里放的是蒙层的图片
    //审核通过之后跟smallUrl字段的值是一样的。
    @ColumnInfo
    private String mySmallHeadUrl;
    @ColumnInfo
    private String gifUrl;
    @ColumnInfo
    private String dynamicHeadgearUrl;
    @ColumnInfo
    private String city;
    //ip归属地
    @ColumnInfo
    private String ipZone;
    @ColumnInfo
    private String sign;
    @ColumnInfo
    private String career;
    @ColumnInfo
    private String like;
    @ColumnInfo
    private String notLike;
    @ColumnInfo
    private String introMe;
    @ColumnInfo
    private String classInfo;
    @ColumnInfo
    private String className;
    @Nullable
    @ColumnInfo
    private Integer collectedBadgeTypeNum;
    @Nullable
    @ColumnInfo
    private Integer glamourValue;
    @Nullable
    @ColumnInfo
    private Integer numOfWings;
    @Nullable
    @ColumnInfo
    private Long totalCallTime;
    @Nullable
    @ColumnInfo
    private Integer numOfFlower;
    @Nullable
    @ColumnInfo
    private Boolean isCanCallDirect;
    @ColumnInfo(name = "newAge")
    private String age;
    @Nullable
    @ColumnInfo
    private Integer dynamicNum;
    @ColumnInfo
    private String rcurl;
    @ColumnInfo
    private String rctopic;
    @Nullable
    @ColumnInfo
    private Long lastLoginTime;
    @ColumnInfo
    private String tagIds;
    @ColumnInfo
    private String industry;
    @Nullable
    @ColumnInfo
    private Integer callLikes;
    @Nullable
    @ColumnInfo
    private Integer callNotLikes;
    @ColumnInfo
    private String headgearUrl; //头像框
    @Nullable
    @ColumnInfo
    private Integer memberType;
    @Nullable
    @ColumnInfo
    private Integer canUploadGif;
    @ColumnInfo
    private String memberIcon;

    @Ignore
    public boolean renewSign; //会员是否显示红点
    /**
     * 粉丝数
     */
    @Nullable
    @ColumnInfo
    private Long fans;

    /**
     * 关注数
     */
    @Nullable
    @ColumnInfo
    private Integer attentions;

    @Nullable
    @ColumnInfo
    private Integer headVersion;

    //是否新用户 1是，0不是
    @Nullable
    @ColumnInfo
    private Integer isNewUser;
    //是否有收徒资格 1有，0没有
    @Ignore
    private int hasQualification;
    @Nullable
    @ColumnInfo
    private Integer hasMaster;
    @ColumnInfo
    private String photoWall;

    @Ignore
    private int totalReceiveCnt;
    //漂屏礼物
    @Ignore
    private String svgaUrl;
    @Ignore
    private String mp4Url;
    @Ignore
    private String version;

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public int getCanUploadGif() {
        return canUploadGif;
    }

    public void setCanUploadGif(int canUploadGif) {
        this.canUploadGif = canUploadGif;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

//    private HotLineInfo hotLineInfo;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRcurl() {
        return rcurl;
    }

    public void setRcurl(String rcurl) {
        this.rcurl = rcurl;
    }

    public String getRctopic() {
        return rctopic;
    }

    public void setRctopic(String rctopic) {
        this.rctopic = rctopic;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBilinId() {
        return bilinId == null ? 0 : bilinId.longValue();
    }

    public void setBilinId(long bilinId) {
        this.bilinId = new Long(bilinId);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //男1 女0
    public int getSex() {
        if (sex != 1) {
            return 0;
        }
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSexOrientation() {
        return sexOrientation;
    }

    public void setSexOrientation(int sexOrientation) {
        this.sexOrientation = sexOrientation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getBigUrl() {
        return bigUrl;
    }

    public void setBigUrl(String bigUrl) {
        this.bigUrl = bigUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getDynamicHeadgearUrl() {
        return dynamicHeadgearUrl;
    }

    public void setDynamicHeadgearUrl(String dynamicHeadgearUrl) {
        this.dynamicHeadgearUrl = dynamicHeadgearUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIpZone() {
        return ipZone;
    }

    public void setIpZone(String ipZone) {
        this.ipZone = ipZone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCareer() {
        return TextUtils.isEmpty(career) ? "" : career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getNotLike() {
        return notLike;
    }

    public void setNotLike(String notlike) {
        this.notLike = notlike;
    }

    public String getIntroMe() {
        return introMe;
    }

    public void setIntroMe(String introMe) {
        this.introMe = introMe;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCollectedBadgeTypeNum() {
        return collectedBadgeTypeNum;
    }

    public void setCollectedBadgeTypeNum(int collectedBadgeTypeNum) {
        this.collectedBadgeTypeNum = collectedBadgeTypeNum;
    }

    public long getTotalCallTime() {
        return totalCallTime;
    }

    public void setTotalCallTime(long totalCallTime) {
        this.totalCallTime = totalCallTime;
    }

    public int getNumOfFlower() {
        return numOfFlower;
    }

    public void setNumOfFlower(int numOfFlower) {
        this.numOfFlower = numOfFlower;
    }

    public boolean isCanCallDirect() {
        return isCanCallDirect;
    }

    public void setCanCallDirect(boolean isCanCallDirect) {
        this.isCanCallDirect = isCanCallDirect;
    }

    public int getDynamicNum() {
        return dynamicNum;
    }

    public void setDynamicNum(int dynamicNum) {
        this.dynamicNum = dynamicNum;
    }

    /*public int getAge() {
        if (!TextUtils.isEmpty(age)) {
            try {
                return Integer.parseInt(age);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void setAge(int age) {
        this.age = String.valueOf(age);
    }*/

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGlamourValue() {
        return glamourValue;
    }

    public void setGlamourValue(int glamourValue) {
        this.glamourValue = glamourValue;
    }

    public int getNumOfWings() {
        return numOfWings;
    }

    public void setNumOfWings(int numOfWings) {
        this.numOfWings = numOfWings;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getCallLikes() {
        return callLikes;
    }

    public void setCallLikes(int callLikes) {
        this.callLikes = callLikes;
    }

    public int getCallNotLikes() {
        return callNotLikes;
    }

    public void setCallNotLikes(int callNotLikes) {
        this.callNotLikes = callNotLikes;
    }

    public long getFans() {
        return fans;
    }

    public void setFans(long fans) {
        this.fans = fans;
    }

    public int getAttentions() {
        return attentions;
    }

    public void setAttentions(int attentions) {
        this.attentions = attentions;
    }

//    public HotLineInfo getHotLineInfo() {
//        return hotLineInfo;
//    }
//
//    public void setHotLineInfo(HotLineInfo hotLineInfo) {
//        this.hotLineInfo = hotLineInfo;
//    }

    public int getHeadVersion() {
        return headVersion;
    }

    public void setHeadVersion(int headVersion) {
        this.headVersion = headVersion;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getHeadgearUrl() {
        return headgearUrl;
    }

    public void setHeadgearUrl(String headgearUrl) {
        this.headgearUrl = headgearUrl;
    }

    public int getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(int isNewUser) {
        this.isNewUser = isNewUser;
    }

    public int getHasQualification() {
        return hasQualification;
    }

    public void setHasQualification(int hasQualification) {
        this.hasQualification = hasQualification;
    }

    public int getHasMaster() {
        return hasMaster;
    }

    public void setHasMaster(int hasMaster) {
        this.hasMaster = hasMaster;
    }

    public String getPhotoWall() {
        return photoWall;
    }

    public void setPhotoWall(String photoWall) {
        this.photoWall = photoWall;
    }

    public int getTotalReceiveCnt() {
        return totalReceiveCnt;
    }

    public void setTotalReceiveCnt(int totalReceiveCnt) {
        this.totalReceiveCnt = totalReceiveCnt;
    }

    public String getMyBigHeadUrl() {
        return myBigHeadUrl;
    }

    public void setMyBigHeadUrl(String myBigHeadUrl) {
        this.myBigHeadUrl = myBigHeadUrl;
    }

    public String getMySmallHeadUrl() {
        return mySmallHeadUrl;
    }

    public void setMySmallHeadUrl(String mySmallHeadUrl) {
        this.mySmallHeadUrl = mySmallHeadUrl;
    }

    public String getSvgaUrl() {
        return svgaUrl;
    }

    public void setSvgaUrl(String svgaUrl) {
        this.svgaUrl = svgaUrl;
    }

    public String getMp4Url() {
        return mp4Url;
    }

    public void setMp4Url(String mp4Url) {
        this.mp4Url = mp4Url;
    }

}
