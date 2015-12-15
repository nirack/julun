package com.julun.datas.localdata;

import java.util.Date;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 *
 */
@Table("sysuser")
public class SysUser extends Model {
    @Key
    @AutoIncrement
    @Column("user_id")
    private long userId;

    @Column("user_name")
    public String userName;

    @Column("create_time")
    private Date createTime;

    @Column("last_login")
    private Date lastLogin;


    private String deviceId;

    private Long privateKey;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Long privateKey) {
        this.privateKey = privateKey;
    }

}
