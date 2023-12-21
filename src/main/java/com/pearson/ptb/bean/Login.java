package com.pearson.ptb.bean;

import java.util.Date;

/**
 * The <code>Login</code> class is responsible to hold the Login details
 *
 */
public class Login {

	/**
	 * The instructor user id
	 */
	private String userid;

	/**
	 * Login date and time
	 */
	private Date datetime;

	/** Get {@see #userid}. @return {@link #userid}. */
	public String getUserid() {
		return userid;
	}

	/** Set {@see #userid}. @param {@link #userid}. */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/** Get {@see #datetime}. @return {@link #datetime}. */
	public Date getDatetime() {
		return datetime;
	}

	/** Set {@see #datetime}. @param {@link #datetime}. */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
