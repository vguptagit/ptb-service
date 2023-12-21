/**
 * 
 */
package com.pearson.ptb.bean;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.googlecode.jmapper.annotations.JMap;

/**
 * This <code>Activity</code> is entity of PAF to represents the details of the
 * book and chapter such as book title author, ISBN, edition number, subject and
 * description.
 *
 */
public class Activity extends BaseActivity {

	/**
	 * Indicates the activity crawlable
	 */
	@JMap()
	private String crawlable;

	/**
	 * The book title in which the paf activity contains
	 */
	private String bookTitle;

	/**
	 * The book author name who created the activity
	 */
	private String bookAuthor;

	/**
	 * The book ISBN of a activity
	 */
	private String bookISBN;

	/**
	 * The book ISBN of a activity
	 */

	private String chapterTitle;

	/**
	 * The book edition number of a activity
	 */
	private String editionNumber;

	/**
	 * The quiz type of a activity
	 */
	@JMap
	private String quizType;

	/**
	 * The description of a activity
	 */
	@JMap
	private String description;

	/**
	 * The string array into which the subjects are added
	 */
	@JMap
	private List<String> subject;

	/**
	 * The content type tier1 value and it would be(Assessment,Image,RichMedia
	 * and Text)
	 */
	private String contentTypeTier1 = "Assessment";

	/**
	 * The content type tier1 value and it would be (AssessmentItem, Assignment)
	 */
	private String[] contentTypeTier2 = new String[]{"AssessmentItem"};

	/**
	 * The time required take the activity
	 */
	@JMap
	private String timeRequired;

	/**
	 * The activity created date
	 */
	@JMap
	private String created = (new Date()).toString();

	/**
	 * The last modified activity date
	 */
	@JMap
	private String modified;

	/**
	 * The format for a activity
	 */
	private String[] format = new String[]{
			"application/vnd.pearson.qti.v2p1.asi+xml"};

	/**
	 * Indicates the activity is deleted or not
	 */
	private Boolean deleted = false;

	/**
	 * Indicates the activity owner(For MyTest/Evalu8 it should be "evalu8")
	 * This property value is used to get only evalu8 belongs data from PAF
	 */
	private String owner = "evalu8";

	/**
	 * The activity key words
	 */
	@JMap
	private String keywords;

	/**
	 * The extended metadata added as part of activity words
	 */
	@JMap
	private List<ExtMetadata> extendedMetadata;

	/**
	 * The version of activity
	 */
	@JMap
	private String versionOf;

	/**
	 * The activity version
	 */
	@JMap
	private String version;

	/**
	 * The activity content type
	 */
	@SerializedName("@type")
	@JsonProperty("@type")
	private String type = "Metadata";

	/** Get {@see #bookTitle}. @return {@link #bookTitle}. */
	public String getBookTitle() {
		return bookTitle;
	}

	/** Set {@see #bookTitle}. @param {@link #bookTitle}. */
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	/** Get {@see #bookAuthor}. @return {@link #bookAuthor}. */
	public String getBookAuthor() {
		return bookAuthor;
	}

	/** Set {@see #bookAuthor}. @param {@link #bookAuthor}. */
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	/** Get {@see #bookISBN}. @return {@link #bookISBN}. */
	public String getBookISBN() {
		return bookISBN;
	}

	/** Set {@see #bookISBN}. @param {@link #bookISBN}. */
	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}

	/** Get {@see #chapterTitle}. @return {@link #chapterTitle}. */
	public String getChapterTitle() {
		return chapterTitle;
	}

	/** Set {@see #chapterTitle}. @param {@link #chapterTitle}. */
	public void setChapterTitle(String containerTitle) {
		this.chapterTitle = containerTitle;
	}

	/** Get {@see #editionNumber}. @return {@link #editionNumber}. */
	public String getEditionNumber() {
		return editionNumber;
	}

	/** Set {@see #editionNumber}. @param {@link #editionNumber}. */
	public void setEditionNumber(String editionNumber) {
		this.editionNumber = editionNumber;
	}

	/** Get {@see #quizType}. @return {@link #quizType}. */
	public String getQuizType() {
		return quizType;
	}

	/** Set {@see #quizType}. @param {@link #quizType}. */
	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}

	/** Get {@see #description}. @return {@link #description}. */
	public String getDescription() {
		return description;
	}

	/** Set {@see #description}. @param {@link #description}. */
	public void setDescription(String description) {
		this.description = description;
	}

	/** Get {@see #subject}. @return {@link #subject}. */
	public List<String> getSubject() {
		return subject;
	}

	/** Set {@see #subject}. @param {@link #subject}. */
	public void setSubject(List<String> subject) {
		this.subject = subject;
	}

	/** Get {@see #contentTypeTier1}. @return {@link #contentTypeTier1}. */
	public String getContentTypeTier1() {
		return contentTypeTier1;
	}

	/** Set {@see #contentTypeTier1}. @param {@link #contentTypeTier1}. */
	public void setContentTypeTier1(String contentTypeTier1) {
		this.contentTypeTier1 = contentTypeTier1;
	}

	/** Get {@see #contentTypeTier2}. @return {@link #contentTypeTier2}. */
	public String[] getContentTypeTier2() {
		return contentTypeTier2;
	}

	/** Set {@see #contentTypeTier2}. @param {@link #contentTypeTier2}. */
	public void setContentTypeTier2(String[] contentTypeTier2) {
		this.contentTypeTier2 = contentTypeTier2;
	}

	/** Get {@see #timeRequired}. @return {@link #timeRequired}. */
	public String getTimeRequired() {
		return timeRequired;
	}

	/** Set {@see #timeRequired}. @param {@link #timeRequired}. */
	public void setTimeRequired(String timeRequired) {
		this.timeRequired = timeRequired;
	}

	/** Get {@see #created}. @return {@link #created}. */
	public String getCreated() {
		return created;
	}

	/** Set {@see #created}. @param {@link #created}. */
	public void setCreated(String created) {
		this.created = created;
	}

	/** Get {@see #modified}. @return {@link #modified}. */
	public String getModified() {
		return modified;
	}

	/** Set {@see #modified}. @param {@link #modified}. */
	public void setModified(String modified) {
		this.modified = modified;
	}

	/** Get {@see #format}. @return {@link #format}. */
	public String[] getFormat() {
		return format;
	}

	/** Set {@see #format}. @param {@link #format}. */
	public void setFormat(String[] format) {
		this.format = format;
	}

	/** Get {@see #crawlable}. @return {@link #crawlable}. */
	public String getCrawlable() {
		return crawlable;
	}

	/** Set {@see #crawlable}. @param {@link #crawlable}. */
	public void setCrawlable(String crawlable) {
		this.crawlable = crawlable;
	}

	/** Get {@see #deleted}. @return {@link #deleted}. */
	public Boolean getDeleted() {
		return deleted;
	}

	/** Set {@see #deleted}. @param {@link #deleted}. */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	/** Get {@see #owner}. @return {@link #owner}. */
	public String getOwner() {
		return owner;
	}

	/** Set {@see #owner}. @param {@link #owner}. */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/** Get {@see #keywords}. @return {@link #keywords}. */
	public String getKeywords() {
		return keywords;
	}

	/** Set {@see #keywords}. @param {@link #keywords}. */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/** Get {@see #extendedMetadata}. @return {@link #extendedMetadata}. */
	public List<ExtMetadata> getExtendedMetadata() {
		return extendedMetadata;
	}

	/** Set {@see #extendedMetadata}. @param {@link #extendedMetadata}. */
	public void setExtendedMetadata(List<ExtMetadata> extendedMetadata) {
		this.extendedMetadata = extendedMetadata;
	}

	/** Get {@see #versionOf}. @return {@link #versionOf}. */
	public String getVersionOf() {
		return versionOf;
	}

	/** Set {@see #versionOf}. @param {@link #versionOf}. */
	public void setVersionOf(String versionOf) {
		this.versionOf = versionOf;
	}

	/** Get {@see #version}. @return {@link #version}. */
	public String getVersion() {
		return version;
	}

	/** Set {@see #version}. @param {@link #version}. */
	public void setVersion(String version) {
		this.version = version;
	}

	/** Get {@see #type}. @return {@link #type}. */
	public String getType() {
		return type;
	}

	/** Set {@see #type}. @param {@link #type}. */
	public void setType(String type) {
		this.type = type;
	}
}
