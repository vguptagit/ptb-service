package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *  The <code>UserSettings</code> class holds the user level setting like disciplines,books,questionMetadata 
 *  and printSettings,
 *
 */
@Document
public class UserSettings implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Indicates id of the user
	 */
	@Id
	private String userid;
	
	/**
	 * Indicates disciplines selected by user
	 */
	private List<String> disciplines;
	
	/**
	 * Indicates books selected by user
	 */
	private List<String> books;
	
	/**
	 * Indicates questionMetadata selected by user
	 */
	private List<String> questionMetadata;
	
	/**
	 * Indicates printSettings selected by user
	 */
	private PrintSettings printSettings;
	
	/**
	 * Indicates created date
	 */
	@JsonIgnore
	private Date created;
	
	/**
	 * Indicates modified date
	 */
	@JsonIgnore
	private Date modified;
	
	/** Get {@see #userid}. @return {@link #userid}. */
	public String getUserid() {
		return userid;
	}
	
	/** Set {@see #userid}. @param {@link #userid}. */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	/** Get {@see #created}. @return {@link #created}. */
	public Date getCreated() {
		return created;
	}
	
	/** Set {@see #created}. @param {@link #created}. */
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/** Get {@see #modified}. @return {@link #modified}. */
	public Date getModified() {
		return modified;
	}
	
	/** Set {@see #modified}. @param {@link #modified}. */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/** Get {@see #questionMetadata}. @return {@link #questionMetadata}. */
	public List<String> getQuestionMetadata() {
		return questionMetadata;
	}
	
	/** Set {@see #questionMetadata}. @param {@link #questionMetadata}. */
	public void setQuestionMetadata(List<String> questionMetadata) {
		this.questionMetadata = questionMetadata;
	}

	/** Get {@see #disciplines}. @return {@link #disciplines}. */
	public List<String> getDisciplines() {
		if(disciplines == null) {
			disciplines = new ArrayList<String>();
		}
		return disciplines;
	}
	
	/** Set {@see #disciplines}. @param {@link #disciplines}. */
	public void setDisciplines(List<String> disciplines) {
		this.disciplines = disciplines;
	}
	
	/** Get {@see #books}. @return {@link #books}. */
	public List<String> getBooks() {
		return books;
	}
	
	/** Set {@see #books}. @param {@link #books}. */
	public void setBooks(List<String> books) {
		this.books = books;
	}
	
	/** Get {@see #printSettings}. @return {@link #printSettings}. */
	public PrintSettings getPrintSettings() {
		return printSettings;
	}
	
	/** Set {@see #printSettings}. @param {@link #printSettings}. */
	public void setPrintSettings(PrintSettings printSettings) {
		this.printSettings = printSettings;
	}
	

}
