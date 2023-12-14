package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.util.Common;


/**
 * The <code>Metadata</code> class responsible to hold the  metadata 
 *
 */
public class Metadata extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Indicates description
	 */
	@JMap
	private String description;
	
	/**
	 * Indicates quizType
	 */
	@JMap
	private String quizType;
	
	/**
	 * Indicates subject
	 */
	@JMap
	private List<String> subject;
	
	/**
	 * Indicates timeRequired
	 */
	@JMap
	private String timeRequired;
	
	/**
	 * Indicates crawlable
	 */
	@JMap
	private String crawlable;
	
	/**
	 * Indicates keywords
	 */
	@JMap
	private String keywords;
	
	/**
	 * Indicates versionOf
	 */
	@JMap
	private String versionOf;
	
	/**
	 * Indicates version
	 */
	@JMap
	private String version;
	
	/**
	 * Indicates extendedMetadata
	 */
	@JMap
	private List<ExtMetadata> extendedMetadata;
	
	/**
	 * Indicates created
	 */
	@JMap
	private String created;
	
	/**
	 * Indicates modified
	 */
	@JMap
	private String modified ;		

	private static final Logger LOG = LogWrapper.getInstance(Metadata.class);
	
	/** Get {@see #quizType}. @return {@link #quizType}. */
	public String getQuizType() {
		return quizType;
	}

	/** Set {@see #quizType}. @param {@link #quizType}. */
	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}

	/** Get {@see #subject}. @return {@link #subject}. */
	public List<String> getSubject() {
		return subject;
	}

	/** Set {@see #subject}. @param {@link #subject}. */
	public void setSubject(List<String> subject) {
		this.subject = subject;
	}

	/** Get {@see #timeRequired}. @return {@link #timeRequired}. */
	public String getTimeRequired() {
		return timeRequired;
	}

	/** Set {@see #timeRequired}. @param {@link #timeRequired}. */
	public void setTimeRequired(String timeRequired) {
		this.timeRequired = timeRequired;
	}

	/** Get {@see #crawlable}. @return {@link #crawlable}. */
	public String getCrawlable() {
		return crawlable;
	}

	/** Set {@see #crawlable}. @param {@link #crawlable}. */
	public void setCrawlable(String crawlable) {
		this.crawlable = crawlable;
	}

	/** Get {@see #keywords}. @return {@link #keywords}. */
	public String getKeywords() {
		return keywords;
	}

	/** Set {@see #keywords}. @param {@link #keywords}. */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	/** Get {@see #extendedMetadata}. @return {@link #extendedMetadata}. */
	public List<ExtMetadata> getExtendedMetadata() {
		return extendedMetadata;
	}

	/** Set {@see #extendedMetadata}. @param {@link #extendedMetadata}. */
	public void setExtendedMetadata(List<ExtMetadata> extendedMetadata) {
		this.extendedMetadata = extendedMetadata;
	}

	/** Get {@see #description}. @return {@link #description}. */
	public String getDescription() {
		return description;
	}

	/** Set {@see #description}. @param {@link #description}. */
	public void setDescription(String description) {
		this.description = description;
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

	/**
	 * To get the value of specified metadata name 
	 * @param extendedMetadataName
	 * @return ExtMetadata
	 */
	@JsonIgnore
	public ExtMetadata getExtendedMetadataByName(String extendedMetadataName) {
		ExtMetadata metaData = null;
		if (this.extendedMetadata != null) {
			for (ExtMetadata extMetaData : this.extendedMetadata) {
				if (extMetaData.getName()
						.equalsIgnoreCase(extendedMetadataName)) {
					metaData = extMetaData;
					break;
				}
			}
		}
		return metaData;
	}


	/** To get the sequence 
	 * @return sequence in double
	 */
	@JsonIgnore
	public double getSequenceExtendedMetadataValue() {
		ExtMetadata extMetadata = this
				.getExtendedMetadataByName(Common.EXTENDED_METADATA_SEQUENCE);
		double sequence = 0.0;
		if (extMetadata != null) {
			try {
				sequence = Double.parseDouble(extMetadata.getValue());
			} catch (NumberFormatException e) {
				LOG.error(e.getMessage());
			}
		}
		return sequence;
	}

	/** To add Extended Metadata
	 * @param extMetadata
	 */
	public void addExtendedMetadata(ExtMetadata extMetadata) {
		if (this.extendedMetadata == null) {
			this.extendedMetadata = new ArrayList<ExtMetadata>();
		}
		this.extendedMetadata.add(extMetadata);
	}
	
}
