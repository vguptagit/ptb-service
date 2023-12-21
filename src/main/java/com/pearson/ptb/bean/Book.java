package com.pearson.ptb.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.jmapper.annotations.JMap;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.util.ExceptionHelper;

/**
 * The <code>Book</code> bean responsible to hold the details of the book
 * 
 * @author prasadbn
 *
 */
@Document(collection = "book")
public class Book extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Indicates Book type
	 */
	@JMap
	private String type;

	/**
	 * Indicates list of search keywords
	 */
	@JMap
	private List<String> keywords;

	/**
	 * Indicates format
	 */
	@JMap
	private String format;

	/**
	 * Indicates name of the authors
	 */
	@JMap
	private List<String> authors;

	/**
	 * Indicates isbn
	 */
	@JMap
	private String isbn;

	/**
	 * Indicates isbn13
	 */
	@JMap
	private String isbn13;

	/**
	 * Indicates isbn10
	 */
	@JMap
	private String isbn10;

	/**
	 * Indicates editionNumber
	 */
	@JMap
	private String editionNumber;

	/**
	 * Indicates created date
	 */
	@JMap
	private Date created;

	/**
	 * Indicates modified date
	 */
	@JMap
	@JsonIgnore
	private Date modified;

	/**
	 * Indicates metadata
	 */
	@JMap
	private Map<String, String> metadata;

	/**
	 * Indicates publisher details
	 */
	@JMap
	private String publisher;

	/**
	 * Indicates discipline type
	 */
	@JMap
	private String discipline;

	/**
	 * Indicates Reference Book Id
	 */
	private String referenceBookId;

	/**
	 * Indicates test Bindings of the book
	 */
	private List<String> testBindings;

	/** Get {@see #type}. @return {@link #type}. */
	public String getType() {
		return type;
	}

	/** Set {@see #type}. @param {@link #type}. */
	public void setType(String type) {
		this.type = type;
	}

	/** Get {@see #keywords}. @return {@link #keywords}. */
	public List<String> getKeywords() {
		return keywords;
	}

	/** Set {@see #keywords}. @param {@link #keywords}. */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	/** Get {@see #format}. @return {@link #format}. */
	public String getFormat() {
		return format;
	}

	/** Set {@see #format}. @param {@link #format}. */
	public void setFormat(String format) {
		this.format = format;
	}

	/** Get {@see #isbn}. @return {@link #isbn}. */
	public String getIsbn() {
		return isbn;
	}

	/** Set {@see #isbn}. @param {@link #isbn}. */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/** Get {@see #editionNumber}. @return {@link #editionNumber}. */
	public String getEditionNumber() {
		return editionNumber;
	}

	/** Set {@see #editionNumber}. @param {@link #editionNumber}. */
	public void setEditionNumber(String editionNumber) {
		this.editionNumber = editionNumber;
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

	/** Get {@see #metadata}. @return {@link #metadata}. */
	public Map<String, String> getMetadata() {
		return metadata;
	}

	/** Set {@see #metadata}. @param {@link #metadata}. */
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/** Get {@see #isbn13}. @return {@link #isbn13}. */
	public String getIsbn13() {
		return isbn13;
	}

	/** Set {@see #isbn13}. @param {@link #isbn13}. */
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	/** Get {@see #publisher}. @return {@link #publisher}. */
	public String getPublisher() {
		return publisher;
	}

	/** Set {@see #publisher}. @param {@link #publisher}. */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/** Get {@see #discipline}. @return {@link #discipline}. */
	public String getDiscipline() {
		return discipline;
	}

	/** Set {@see #discipline}. @param {@link #discipline}. */
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	/** Get {@see #testBindings}. @return {@link #testBindings}. */
	public List<String> getTestBindings() {
		return testBindings;
	}

	/** Set {@see #testBindings}. @param {@link #testBindings}. */
	public void setTestBindings(List<String> testBindings) {
		this.testBindings = testBindings;
	}

	/** Set {@see #authors}. @param {@link #authors}. */
	public void setAuthors(List<String> author) {
		this.authors = author;
	}

	/** Get {@see #authors}. @return {@link #authors}. */
	public List<String> getAuthors() {
		return authors;
	}

	/** Get {@see #isbn10}. @return {@link #isbn10}. */
	public String getIsbn10() {
		return isbn10;
	}

	/** Set {@see #isbn10}. @param {@link #isbn10}. */
	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	/** Get {@see #referenceBookId}. @return {@link #referenceBookId}. */
	public String getReferenceBookid() {
		return referenceBookId;
	}

	/** Set {@see #referenceBookId}. @param {@link #referenceBookId}. */
	public void setReferenceBookid(String referenceBookId) {
		this.referenceBookId = referenceBookId;
	}

	/**
	 * To validate the book id, title, Discipline, Authors and Publisher
	 * 
	 * @throws BadDataException
	 */
	public void validateState() {
		List<String> messages = new ArrayList<String>();
		// Validate Book Id field

		if (StringUtils.isBlank(this.getGuid())) {
			messages.add("book id should not be null or Empty");
		}
		// Validate Book title field
		if (StringUtils.isBlank(this.getTitle())) {
			messages.add("book title should not be null or Empty");
		}

		// Validate Book Discipline field
		if (StringUtils.isBlank(this.getDiscipline())) {
			messages.add("book Discipline should not be null or Empty");
		}
		// Validate Book Authors field
		if (this.getAuthors().isEmpty()) {
			messages.add("book Authors should not be null or Empty");
		}
		// Validate Book Publisher field
		if (StringUtils.isBlank(this.getPublisher())) {
			messages.add("book Publisher should not be null or Empty");
		}

		if (!messages.isEmpty()) {
			throw new BadDataException(ExceptionHelper.getMessage(messages));
		}
	}
}
