/**
 * 
 */
package com.pearson.ptb.util;

/**
 * This class contains various constants for authentication providers.
 * 
 * @author nithinjain
 */
public final class Common {

	/**
	 * This is utf 8 charactor encoding for URL
	 */
	public static final String UTF_8_CHAR_ENCODING = "UTF-8";

	/**
	 * This is template message for request content type
	 */
	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * This is template message for binay octet stram
	 */
	public static final String BINARY_OCTET_STREAM = "binary/octet-stream";

	/**
	 * This is template message for activityType
	 */
	public static final String ACTIVITY_TYPE = "activityType";

	/**
	 * This is template message for Authorization
	 */
	public static final String AUTHORIZATION = "Authorization";

	/**
	 * This is template message for mediatype
	 */
	public static final String MEDIATYPE = "mediatype";

	/**
	 * This is template string for PAF assignment format
	 */
	public static final String PAF_ASSIGNMENT_FORMAT = "application/vnd.pearson.paf.v1.assignment+json";

	/**
	 * This is template string for PAF assignment format
	 */
	public static final String PAF_QUESTION_FORMAT = "application/vnd.pearson.qti.v2p1.asi+xml";

	/**
	 * This is template string for Evalu8 get question by id end point format
	 */
	public static final String QUESTION_END_POINT_FORMAT = "%s/questions/%s";

	/**
	 * This is template string for Evalu8 - universal request id
	 */
	public static final String UNIVERSAL_REQUEST_ID = "universalRequestId";

	/**
	 * This is template string for Evalu8 - External user id
	 */
	public static final String EXT_USER_ID = "extUserId";

	/**
	 * This is template string for Evalu8 - External user id
	 */
	public static final String USER_REQUEST_BODY = "Request body must contain {\"userName\": \"\",\"password\": \"\"}";

	/**
	 * This is template string for PAF activity creation content type
	 */
	public static final String PAF_ACTIVITY_CREATION_CONTENT_TYPE = "application/vnd.pearson.paf.v1.envelope+json;body=\"application/vnd.pearson.paf.v1.assignment+json\"";

	/**
	 * This is template string for Evalu8 get test by id end point format
	 */
	public static final String TEST_END_POINT_FORMAT = "%s/tests/%s";

	/**
	 * This is template string for Evalu8 get PAF update activity end point
	 * format
	 */
	public static final String PAF_UPDATE_ACTIVITY_END_POINT_FORMAT = "%s/%s";

	/**
	 * This is template string for Evalu8 get PAF version activity creation end
	 * point format
	 */
	public static final String PAF_VERSION_ACTIVITY_END_POINT_FORMAT = "%s/%s/versions";

	/**
	 * This is template string for Evalu8 get PAF metadata format
	 */
	public static final String PAF_METADATA_FORMAT = "application/vnd.pearson.content.v1.metadata+json";

	/**
	 * This is template string for Evalu8 extended metadata of an activity or
	 * question
	 */
	public static final String EXTENDED_METADATA_SEQUENCE = "sequence";

	/**
	 * Making the Constructor private
	 */
	private Common() {

	}
}
