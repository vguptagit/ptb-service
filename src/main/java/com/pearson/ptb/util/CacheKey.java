package com.pearson.ptb.util;

/**
 * This class contains various constants for cache key.
 * 
 * @author nithinjain
 */
public final class CacheKey {	
	
	/**
	 * This is template string for published questions cache key format
	 */
	public static final String PUBLISHED_QUESTIONS_FORMAT = "books:%s:nodes:%s:questions";

	/**
	 * This is template string for metadata (test / question) cache key format
	 */
	public static final String TEST_FORMAT = "%s:test";
	/**
	 * This is template string for metadata (test / question) cache key format
	 */
	public static final String METADATA_FORMAT = "%s:metadata";

	/**
	 * This is template string for published tests cache key format (book level publisher tests)
	 */
	public static final String BOOK_TESTS_FORMAT = "books:%s:tests";
	
	/**
	 * This is template string for question cache key format
	 */
	public static final String QUESTION_XML_FORMAT = "questions:%s:questionXml";
	
	/**
	 * This is template string for question cache key format
	 */
	public static final String USER_QUESTIONS_FORMAT = "userquestions:%s";
	
	/**
	 * This is template string for question cache key format
	 */
	public static final String TEST_QUESTIONS_FORMAT = "testquestions:%s";
	
	/**
	 * This is template string for quiz and book level questions cache key format
	 */
	public static final String BOOKID_QUESTIONS_FORMAT = "bookquestions:%s";
	
	/**
	 * This is template string for child containers cache key format
	 */
	public static final String SUBCONTAINERS_FORMAT = "books:%s:nodes:%s:subnodes";
	
	/**
	 * This is template string for container cache key format
	 */
	public static final String CONTAINERS_FORMAT = "books:%s:nodes:%s";
	
	/**
	 * Making the Constructor private
	 */
	private CacheKey() {

	}
}
