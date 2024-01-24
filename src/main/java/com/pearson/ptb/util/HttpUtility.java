package com.pearson.ptb.util;

import static org.apache.http.client.params.ClientPNames.ALLOW_CIRCULAR_REDIRECTS;
import static org.apache.http.client.params.ClientPNames.HANDLE_AUTHENTICATION;
import static org.apache.http.client.params.ClientPNames.HANDLE_REDIRECTS;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.apache.http.params.CoreProtocolPNames.USE_EXPECT_CONTINUE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.SyncBasicHttpParams;
import org.slf4j.Logger;

import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.framework.RequestCorrelation;
import com.pearson.ptb.framework.exception.InternalException;

/**
 * Utility for making http request
 * 
 * @author nithinjain
 *
 */
public class HttpUtility {

	/**
	 * This is the default Http Client
	 */
	private DefaultHttpClient client;

	private static final int CONNECTION_TIME_OUT = 120000;

	private static final int SOCKET_TIME_OUT = 12000;

	private static final Logger LOG = LogWrapper.getInstance(HttpUtility.class);

	/**
	 * This constructor initializes the DefaultHttpClient object
	 * 
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file.
	 */
	public HttpUtility() {
		
		client = new DefaultHttpClient(httpParams());

	}

	/**
	 * This method returns the new object of HttpParams
	 * 
	 * @return The object of HttpParams
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file.
	 */
	private org.apache.http.params.HttpParams httpParams() {
		return new SyncBasicHttpParams()
				.setParameter(CONNECTION_TIMEOUT, CONNECTION_TIME_OUT)
				.setParameter(SO_TIMEOUT, SOCKET_TIME_OUT)
				.setParameter(HANDLE_REDIRECTS, true)
				.setParameter(ALLOW_CIRCULAR_REDIRECTS, true)
				.setParameter(HANDLE_AUTHENTICATION, true)
				.setParameter(USE_EXPECT_CONTINUE, true);
	}

	/**
	 * This method builds the url with query string
	 * 
	 * @param url
	 *            The url to add query string.
	 * @param queryString
	 *            The key value pair query string
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the query string type is unsupported.
	 * 
	 * @author nithinjain
	 */
	private String buildQueryString(String url,
			Map<String, String> queryString) {
		String query = "%s=%s";
		StringBuilder sb = new StringBuilder();
		String buildUrl = url;
		
		if (queryString != null && !queryString.isEmpty()) {
			buildUrl = buildUrl.concat("?");

			for (String key : queryString.keySet()) {

				if (sb.length() > 0) {
					sb.append('&');
				}
				try {
					sb.append(String.format(query,
							URLEncoder.encode(key, Common.UTF_8_CHAR_ENCODING),
							URLEncoder.encode(queryString.get(key),
									Common.UTF_8_CHAR_ENCODING)));
				} catch (UnsupportedEncodingException e) {
					throw new InternalException(
							"Error on URL encoding in the method HttpUtility.buildQueryString ",
							e);
				}
			}

		}
		return buildUrl.concat(sb.toString());
	}

	/**
	 * This method will set all the http headers
	 * 
	 * @param httpRequest
	 *            This is request for which headers to be set.
	 * @param contentType
	 *            This is the content type for the request
	 * @param headers
	 *            This is the list of headers
	 * @param cookies
	 *            This is the list of cookies
	 * @author nithinjain
	 */
	private void setRequestHeaders(AbstractHttpMessage httpRequest,
			ContentType contentType, Map<String, String> headers) {

		
		if (contentType != null) {
			httpRequest.setHeader(Common.CONTENT_TYPE, contentType.toString());
		}

		
		if (headers != null && !headers.isEmpty()) {
			for (String key : headers.keySet()) {
				httpRequest.setHeader(key, headers.get(key));
			}
		}
	}

	/**
	 * This method hits the HTTP Get request on the URL.
	 * 
	 * @param headers
	 *            This is the list of headers
	 * @param cookies
	 *            This is the list of cookies
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return The http response
	 * @author nithinjain
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file or input stream.
	 */
	public HttpResponse makeGet(String url, Map<String, String> headers,
			ContentType contentType, Map<String, String> queryString)
			throws IOException {

		
		HttpGet httpGet = new HttpGet(buildQueryString(url, queryString));
		
		setRequestHeaders(httpGet, contentType, headers);

		return getResponse(httpGet, url, "MakeGet");

	}

	/**
	 * This method makes the HTTP POST request with the JSON payload, on the
	 * URL.
	 * 
	 * @param url
	 *            This is the url for the request
	 * @param payload
	 *            This is the payload for the request
	 * @param headers
	 *            This is the list of headers
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return The http response
	 * @author nithinjain
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file or input stream.
	 */
	public HttpResponse makePost(String url, Map<String, String> headers,
			ContentType contentType, Map<String, String> queryString,
			String payload) {

		
		HttpPost httpPost = new HttpPost(buildQueryString(url, queryString));

		
		httpPost.setEntity(createRequestEntity(payload));

		
		setRequestHeaders(httpPost, contentType, headers);

		
		return getResponse(httpPost, url, "MakePost");

	}

	/**
	 * This method makes the HTTP Put request with the JSON payload, on the URL.
	 * 
	 * @param url
	 *            This is the url for the request
	 * @param payload
	 *            This is the payload for the request
	 * @param headers
	 *            This is the list of headers
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return The http response
	 * @author nithinjain
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file or input stream.
	 */
	public HttpResponse makePut(String url, Map<String, String> headers,
			ContentType contentType, Map<String, String> queryString,
			String payload) throws IOException {

		
		HttpPut httpPut = new HttpPut(buildQueryString(url, queryString));

		
		httpPut.setEntity(createRequestEntity(payload));

		
		setRequestHeaders(httpPut, contentType, headers);

		
		try {
			return new HttpResponse(client.execute(httpPut));
		} catch (Exception ex) {
			LOG.debug("Exception is handled in HttpUtility.makePut() method ",
					ex);
			return null;
		}
	}

	/**
	 * This method makes the HTTP DELETE request with the queryString, on the
	 * URL.
	 * 
	 * @param url
	 *            This is the url for the request
	 * @param headers
	 *            This is the list of headers
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return The http response
	 * @author nithinjain
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file or input stream.
	 */
	public HttpResponse makeDelete(String url, Map<String, String> headers,
			ContentType contentType, Map<String, String> queryString)
			throws IOException {

		
		HttpDelete httpDelete = new HttpDelete(
				buildQueryString(url, queryString));

		
		setRequestHeaders(httpDelete, contentType, headers);

		
		return getResponse(httpDelete, url, "MakeDelete");
	}

	/**
	 * This method hits the HTTP PUT request with the JSON payload, on the URL.
	 * 
	 * @param url
	 *            This is the url for the request
	 * @param payload
	 *            This is the payload for the request
	 * @param headers
	 *            This is the list of headers
	 * @param cookies
	 *            This is the list of cookies
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return The http response
	 * @author nithinjain
	 * @throws IOException
	 *             This is thrown if there is an error in the I/O while reading
	 *             property file or input stream.
	 */
	public HttpResponse makePutOnJSON(String url, String payload,
			Map<String, String> headers) throws IOException {

		
		HttpPut httpPut = new HttpPut(url);

		
		httpPut.setEntity(createRequestEntity(payload));

		
		setRequestHeaders(httpPut, ContentType.APPLICATION_JSON, headers);

		
		return new HttpResponse(client.execute(httpPut));
	}

	/**
	 * This method creates an Request entity
	 * 
	 * @param payload
	 *            The payload as a String.
	 * @return The Input Stream Entity
	 * @throws UnsupportedEncodingException
	 *             If the payload type is unsupported.
	 */
	private InputStreamEntity createRequestEntity(String payload) {
		
		InputStreamEntity requestEntity = null;

		try {
			requestEntity = new InputStreamEntity(new ByteArrayInputStream(
					payload.getBytes(Common.UTF_8_CHAR_ENCODING)), -1);
		} catch (UnsupportedEncodingException e) {
			throw new InternalException(
					"Error in the method HttpUtility.createRequestEntity ", e);
		}

		requestEntity.setContentType(Common.BINARY_OCTET_STREAM);
		requestEntity.setChunked(true);

		
		return requestEntity;

	}
	/**
	 * This method gets the httpResponse
	 * 
	 * @param request
	 *            This is the type of request
	 * @param url
	 *            This is the url for the request
	 * @param method
	 *            The method as a String.
	 * @return The httpResponse
	 * @throws UnsupportedEncodingException
	 *             If the request and url is unsupported.
	 */
	private HttpResponse getResponse(HttpUriRequest request, String url,
			String method) {

		HttpResponse httpResponse = null;
		try {
			long startTime = System.currentTimeMillis();
			httpResponse = new HttpResponse(client.execute(request));
			long elapsedTime = System.currentTimeMillis() - startTime;
			LOG.info(method + ": " + url + " - Time taken: " + elapsedTime
					+ "ms - " + RequestCorrelation.HEADER + ": "
					+ RequestCorrelation.getId());
		} catch (IOException e) {
			throw new InternalException(
					"Unable to get the response for " + method + "", e);
		}
		return httpResponse;
	}

	/**
	 * Uplaods the file on the give url.
	 * 
	 * @param url
	 *            The url on which the file needs to be uplaoded.
	 * @param fileName
	 *            The name of the file.
	 * @param content
	 *            The content of the file to be uplaoded.
	 * @param fieldName
	 *            Any extra param for file upload.
	 * @return The HTTP response for the upload.
	 * @throws IOException
	 *             In case error uplaoding the file.
	 */
	public HttpResponse uploadFile(String url, String fileName, byte[] content,
			String fieldName) throws IOException {

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);

		entity.addPart(fieldName, new InputStreamBody(
				new ByteArrayInputStream(content), fileName));

		HttpPost httpPost = new HttpPost(url);

		httpPost.setEntity(entity);

		return new HttpResponse(client.execute(httpPost));
	}

	/**
	 * update the give file content which is in the form of byte array to the
	 * given <code>url</code>.
	 * 
	 * @param url,
	 *            url where file need to be update
	 * @param fileName,
	 *            file name of the file which need to be update
	 * @param content,
	 *            byte array form of file
	 * @param fieldName,
	 *            this will be having the value "field"
	 * @param headers,
	 *            header data which will be in the form of map
	 * @return HttpResponse
	 */
	public HttpResponse updateFile(String url, byte[] content,
			Map<String, String> headers) {

		ByteArrayEntity entity = new ByteArrayEntity(content);

		HttpPut httpPut = new HttpPut(url);

		httpPut.setEntity(entity);

		setRequestHeaders(httpPut, ContentType.WILDCARD, headers);

		httpPut.setHeader(Common.CONTENT_TYPE, "image/jpeg");

		try {
			return new HttpResponse(client.execute(httpPut));
		} catch (IOException e) {
			throw new InternalException("Unable to upload image", e);
		}
	}

}
