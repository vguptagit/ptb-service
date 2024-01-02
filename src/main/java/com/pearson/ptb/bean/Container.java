package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.exception.BadDataException;

/**
 * The <code>Container</code> class holds the details of the Container such as
 * created date, modified date, id and questionBindings in the container
 *
 */
@Document(collection = "container")
public class Container extends BaseEntity implements Serializable {

	/**
	 * Indicate the version id of serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indicate the parentId of Container
	 */
	@JMap
	private String parentId;

	/**
	 * Indicate the Question Bindings of Container
	 */
	@JMap
	private List<String> questionBindings;

	/**
	 * Indicate the created date
	 */
	@JMap
	@JsonIgnore
	private Date created;

	/**
	 * Indicate the modified date
	 */
	@JMap
	@JsonIgnore
	private Date modified;

	/**
	 * Indicate the book id
	 */
	@JMap
	private String bookid;

	/**
	 * Indicate the sequence
	 */
	private double sequence;

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

	/** Get {@see #bookid}. @return {@link #bookid}. */
	public String getBookid() {
		return bookid;
	}

	/** Set {@see #bookid}. @param {@link #bookid}. */
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	/** Get {@see #parentId}. @return {@link #parentId}. */
	public String getParentId() {
		return parentId;
	}

	/** Set {@see #parentId}. @param {@link #parentId}. */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/** Get {@see #sequence}. @return {@link #sequence}. */
	public double getSequence() {
		return sequence;
	}

	/** Set {@see #sequence}. @param {@link #sequence}. */
	public void setSequence(double sequence) {
		this.sequence = sequence;
	}

	/** Get {@see #questionBindings}. @return {@link #questionBindings}. */
	public List<String> getQuestionBindings() {
		return questionBindings;
	}

	/** Set {@see #questionBindings}. @param {@link #questionBindings}. */
	public void setQuestionBindings(List<String> questionBindings) {
		this.questionBindings = questionBindings;
	}

	/**
	 * Validating Container Id field
	 * 
	 * @throws BadDataException
	 */
	public void validateState() {

		// Validate Container Id field
		if (StringUtils.isBlank(this.getTitle())) {
			throw new BadDataException(
					"node title should not be null or Empty");
		}
	}
}
