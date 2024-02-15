package com.pearson.ptb.proxy.aws.bean;

import com.pearson.ptb.bean.BaseEntity;

public class QuestionResponse extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public QuestionResponse(String questionGuid) {
		super();
		super.setGuid(questionGuid);
	}
	
	public QuestionResponse(String questionGuid, String awsId) {
		super();
		super.setGuid(questionGuid);
		this.setBody(awsId);
	}
	
	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
}
