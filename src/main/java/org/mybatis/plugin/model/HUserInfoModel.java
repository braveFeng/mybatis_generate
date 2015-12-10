package org.mybatis.plugin.model;

import java.io.Serializable;
import java.util.Date;



/**
 * HUserInfo对象定义
 * 
 * 工具自动生成代码
 * 
 * @author Admin
 *
 */
public class HUserInfoModel implements Serializable {

	/** uid */
	private static final long serialVersionUID = 1L;
    /** 递增主键 */
    private Integer	id;
    /** 手机号 */
    private String	phone;
    /** 登录方式(0:账号登录 1:大博登录)*/
    private Integer thirdParty;
    /** 密码 */
    private String	password;
    /** 头像地址 */
    private String	logo;
    /** 昵称 */
    private String	nickName;
    /** 性别(0:男 1:女) */
    private Integer	gender;
    /** 身高(单位:cm) */
    private Integer	height;
    /** 体重(单位:g) */
    private Integer	weight;
    /** 生日 */
    private Date	birthDay;
    /** 创建时间 */
    private Date	createTime;
    /** 更新时间 */
    private Date	updateTime;
    /** 是否删除(0:否 1:是) */
    private Integer	isDelete;
	/** 未读 (1代表有未读动态，0代表没有) */
	private Integer unread;
	
	
	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	/** 取得递增主键 */
	public Integer getId() {
		return id;
	}
	
	/** 设置递增主键 */
	public void setId(Integer id) {
		this.id = id;
	}
	/** 取得手机号 */
	public String getPhone() {
		return phone;
	}
	
	/** 设置手机号 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/** 取得密码 */
	public String getPassword() {
		return password;
	}
	
	/** 设置密码 */
	public void setPassword(String password) {
		this.password = password;
	}
	/** 取得头像地址 */
	public String getLogo() {
		return logo;
	}
	
	/** 设置头像地址 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/** 取得昵称 */
	public String getNickName() {
		return nickName;
	}
	
	/** 设置昵称 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/** 取得性别(0:男 1:女) */
	public Integer getGender() {
		return gender;
	}
	
	/** 设置性别(0:男 1:女) */
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	/** 取得身高(单位:cm) */
	public Integer getHeight() {
		return height;
	}
	
	/** 设置身高(单位:cm) */
	public void setHeight(Integer height) {
		this.height = height;
	}
	/** 取得体重(单位:g) */
	public Integer getWeight() {
		return weight;
	}
	
	/** 设置体重(单位:g) */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	/** 取得生日 */
	public Date getBirthDay() {
		return birthDay;
	}
	
	/** 设置生日 */
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	/** 取得创建时间 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/** 设置创建时间 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/** 取得更新时间 */
	public Date getUpdateTime() {
		return updateTime;
	}
	
	/** 设置更新时间 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/** 取得是否删除(0:否 1:是) */
	public Integer getIsDelete() {
		return isDelete;
	}
	
	/** 设置是否删除(0:否 1:是) */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(Integer thirdParty) {
		this.thirdParty = thirdParty;
	}


}
