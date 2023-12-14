package com.pearson.ptb.framework;

import com.pearson.ptb.framework.exception.ConfigException;

/**
 * This class will is used to read the configuration. 
 */
public final class ConfigurationManager extends BaseConfigurationManager {

	/**
	 * This is the class name for instantiating.
	 * 
	 */
	private static ConfigurationManager configurationManager = null;

	/**
	 * This is the property file path and name
	 * 
	 */
	private static final String PROPERTY_FILE_PATH = "/config.properties";

	/**
	 * <p>
	 * This is the private constructor of the class. It is used to make
	 * ConfigurationManager as a singleton
	 * </p>
	 * 
	 * @throws ConfigException 
	 */
	protected ConfigurationManager(){
		// Calling parent class Constructor to load all properties from config
		// properties file
		super();
	}

	/**
	 * This method resets the config
	 */
	public static void reset() {
		configurationManager = null;
	}

	/**
	 * <p>
	 * To get the configuration property file path
	 * </p>
	 * 
	 * @return Returns file path of the configuration property file
	 */
	protected String getConfigPath() {
		return PROPERTY_FILE_PATH;
	}

	/**
	 * <p>
	 * Instantiating the singleton
	 * </p>
	 * 
	 * @return Configuration Manager returns the instance of
	 *         ConfigurationManager class.
	 * @throws ConfigException 
	 */
	public static ConfigurationManager getInstance(){
		// synchronized block for creating ConfigurationManager singleton
		if (configurationManager == null) {
			// null check in synchronization for race condition
			synchronized (CacheConfigManager.mutex) {
				if (configurationManager == null) {
					// create ConfigurationManager instance as singleton
					configurationManager = new ConfigurationManager();
				}
			}
		}
		// return ConfigurationManager
		return configurationManager;

	}

	/**
	 * <p>
	 * This method returns the DebugEnable boolean.
	 * </p>
	 * 
	 * @return A Boolean which returns the value of IsDebugEnabled.
	 * @author
	 */
	public Boolean isDebugEnabled() {
		// returns the IsDebugEnabled
		return Boolean
				.valueOf(ConfigurationManager.configurationProperties.get(
						"IsDebugEnabled").replaceAll(SPECIAL_CHARACTER_STRING,
						""));
	}
	
	/**
	 * 	 * This method returns the quad consumer key.
	 	 * 
	 * @return A String which returns the value of QUADConsumerKey.
	 */
	public String getQUADConsumerKey() {
	
		return ConfigurationManager.configurationProperties
				.get("QUADConsumerKey");
	}

	/**	
	 * This method returns the quad data secret key.	
	 * 
	 * @return A String which has the value of QUADDataSecretKey.
	 */
	public String getQUADDataSecretKey() {
		
		return ConfigurationManager.configurationProperties
				.get("QUADDataSecretKey");
	}

	/**
	 * This method returns the quad Oauth algorithm.	
	 * 
	 * @return A String which has the value of QUADOauthAlgorithm.
	 */
	public String getQUADOauthAlgorithm() {
		
		return ConfigurationManager.configurationProperties
				.get("QUADOauthAlgorithm");
	}
	
	/**
	 * This method returns the PTB ImportUrl for importing books.	
	 * 
	 * @return A String which has the value of PTBImportUrl.
	 */	
	public String getPTBImportUrl() {
		return String.valueOf(ConfigurationManager.configurationProperties
				.get("PTBImportUrl"));
	}

	/**
	 * <p>
	 * This method returns the paf consumer key.
	 * </p>
	 * 
	 * @return A String which returns the value of PAFConsumerKey.
	 * @author nithinjain
	 */
	public String getPAFConsumerKey() {
		// returns the paf consumer key
		return ConfigurationManager.configurationProperties
				.get("PAFConsumerKey");
	}

	/**
	 * <p>
	 * This method returns the paf data secret key.
	 * </p>
	 * 
	 * @return A String which has the value of PAFDataSecretKey.
	 * @author nithinjain
	 */
	public String getPAFDataSecretKey() {
		// returns the paf DataSecret Key
		return ConfigurationManager.configurationProperties
				.get("PAFDataSecretKey");
	}

	/**
	 * <p>
	 * This method returns the paf Oauth algorithm.
	 * </p>
	 * 
	 * @return A String which has the value of PAFOauthAlgorithm.
	 * @author nithinjain
	 */
	public String getPAFOauthAlgorithm() {
		// returns the paf Oauth Algorithm
		return ConfigurationManager.configurationProperties
				.get("PAFOauthAlgorithm");
	}

	/**
	 * Gets the url to generate PI token.
	 * 
	 * @return Gets the url to generate PI token.
	 */
	public String getSysToSysPITokenUrl() {

		return ConfigurationManager.configurationProperties
				.get("SysToSysPITokenUrl");
	}
	
	/**
	 * <p> This method returns the PI Token From Access Token Url. </p>
	 * 
	 * @return A String which returns the value of SysToSysPITokenFromAccessTokenUrl.
	 */
	public String getSysToSysPITokenFromAccessTokenUrl() {

		return ConfigurationManager.configurationProperties
				.get("SysToSysPITokenFromAccessTokenUrl");
	}
	
	/**
	 * <p> This method returns the User Profile From AccessToken Url. </p>
	 * 
	 * @return A String which returns the value of SysToSysUserProfileFromAccessTokenUrl.
	 */
	public String getSysToSysUserProfileFromAccessTokenUrl() {

		return ConfigurationManager.configurationProperties
				.get("SysToSysUserProfileFromAccessTokenUrl");
	}

	/**
	 * <p> This method returns the Data Secure Rest Url. </p>
	 * 
	 * @return A String which returns the value of DataSecureRestUrl.
	 */
	public String getDataSecureRestUrl() {

		return ConfigurationManager.configurationProperties
				.get("DataSecureRestUrl");
	}
	
	/**
	 * <p> This method returns the Token Url. </p>
	 * 
	 * @return A String which returns the value of TokenUrl.
	 */
	public String getTokenUrl() {

		return ConfigurationManager.configurationProperties
				.get("TokenUrl");
	}

	/**
	 * <p> This method returns the Instructor Authorization Url. </p>
	 * 
	 * @return A String which returns the value of InstructorAuthorizationUrl.
	 */
    public String getInstructorAuthUrl() {

        return ConfigurationManager.configurationProperties
                .get("InstructorAuthorizationUrl");
    }
    
    /**
	 * <p> This method returns the Instructor Group Id. </p>
	 * 
	 * @return A String which returns the value of InstructorGroupId.
	 */
    public String getInstructorGroupId() {

        return ConfigurationManager.configurationProperties
                .get("InstructorGroupId");
    }
    
    /**
	 * <p> This method returns the Socket Timeout. </p>
	 * 
	 * @return A String which returns the value of SocketTimeout.
	 */
	public int getSocketTimeout() {

		return Integer.parseInt(ConfigurationManager.configurationProperties
				.get("SocketTimeout").replaceAll(SPECIAL_CHARACTER_STRING, ""));
	}

	/**
	 * <p> This method returns the Connection Timeout. </p>
	 * 
	 * @return A String which returns the value of ConnectionTimeout.
	 */
	public int getConnectionTimeout() {

		return Integer.parseInt(ConfigurationManager.configurationProperties
				.get("ConnectionTimeout").replaceAll(SPECIAL_CHARACTER_STRING,
						""));
	}

	/**
	 * <p> This method returns the KeyMoniker, used to generate token from PI. </p>
	 * 
	 * @return A String which returns the value of pi.keyMoniker.
	 */
	public String getKeyMoniker() {

		return ConfigurationManager.configurationProperties
				.get("pi.keyMoniker");
	}
	
	/**
	 * <p> This method returns the KeyMap, used to generate token from PI. </p>
	 * 
	 * @return A String which returns the value of pi.keyMap.
	 */
	public String getKeyMap() {

		return ConfigurationManager.configurationProperties.get("pi.keyMap");
	}

	/**
	 * <p> This method returns the Duration for which PI token would be valid. </p>
	 * 
	 * @return A String which returns the value of pi.sessionDuration.
	 */
	public int getPISessionDuration() {

		return Integer.parseInt(ConfigurationManager.configurationProperties
				.get("pi.sessionDuration").replaceAll(SPECIAL_CHARACTER_STRING,
						""));
	}

	/**
	 * <p> This method returns the Maximum Scrambled Versions for the test. </p>
	 * 
	 * @return A String which returns the value of maxScrambledVersions.
	 */
	public int getMaxScrambledVersions() {

		return Integer.parseInt(ConfigurationManager.configurationProperties
				.get("maxScrambledVersions").replaceAll(
						SPECIAL_CHARACTER_STRING, ""));
	}	

	/**
	 * <p> This method returns the Database Name. </p>
	 * 
	 * @return A String which returns the value of db.database.
	 */
	public String getDataBase() {
		return String.valueOf(ConfigurationManager.configurationProperties.get(
				"db.database").replaceAll(SPECIAL_CHARACTER_STRING, ""));
	}

	/**
	 * <p> This method returns the Server Name. </p>
	 * 
	 * @return A String which returns the value of db.server.
	 */
	public String getDataBaseServer() {
		System.out.println(ConfigurationManager.configurationProperties.get(
				"db.server"));
		return String.valueOf(ConfigurationManager.configurationProperties.get(
				"db.server").replaceAll(SPECIAL_CHARACTER_STRING, ""));
	}

	/**
	 * <p> This method returns the Database UserName. </p>
	 * 
	 * @return A String which returns the value of db.userName.
	 */
	public String getDataBaseUserName() {
		return String.valueOf(ConfigurationManager.configurationProperties.get(
				"db.userName").replaceAll(SPECIAL_CHARACTER_STRING, ""));
	}
	
	/**
	 * <p> This method returns the Database Password. </p>
	 * 
	 * @return A String which returns the value of db.password.
	 */
	public String getDataBasePassword() {
		return String.valueOf(ConfigurationManager.configurationProperties.get(
				"db.password").replaceAll(SPECIAL_CHARACTER_STRING, ""));
	}
	
	/**
	 * <p> This method returns the PAF Base Url. </p>
	 * 
	 * @return A String which returns the value of PAFBaseUrl.
	 */
	public String getPAFBaseUrl() {
		return String.valueOf(ConfigurationManager.configurationProperties
				.get("PAFBaseUrl"));
	}

	/**
	 * <p> This method returns the PAF Activities End Point. </p>
	 * 
	 * @return A String which returns the value of PAFActivitiesEndPoint.
	 */
	public String getPAFActivitiesEndPoint() {
		return String.valueOf(ConfigurationManager.configurationProperties
				.get("PAFActivitiesEndPoint"));
	}
	
	/**
	 * <p> This method returns the Size of the record which can be fetched from PAF in single request. </p>
	 * 
	 * @return A String which returns the value of PAFActivities_RecordSize.
	 */
	public String getPAFActivitiesRecordSize(){
		return String.valueOf(configurationProperties.get("PAFActivities_RecordSize"));
	}

	/**
	 * <p> This method returns the name of the environment. </p>
	 * 
	 * @return A String which returns the value of EnvironmentName.
	 */
	public String environmentName() {
		return ConfigurationManager.configurationProperties.get(
				"EnvironmentName").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}
	
	/**
	 * <p> This method returns the name of the application. </p>
	 * 
	 * @return A String which returns the value of ApplicationName.
	 */
	public String applicationName() {
		return ConfigurationManager.configurationProperties.get(
				"ApplicationName").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}

	/**
	 * <p>
	 * This method returns the MyTest base url.
	 * </p>
	 * 
	 * @return A String which has the value of MyTest base url.
	 * @author nithinjain
	 */
	public String getMyTestBaseUrl() {
		return ConfigurationManager.configurationProperties
				.get("MyTestBaseUrl").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}
	
	/**
	 * <p> This method returns the EPS Authorization Token. </p>
	 * 
	 * @return A String which returns the value of eps.authorisationToken.
	 */
	public String getEPSAuthorisationToken(){
		return ConfigurationManager.configurationProperties
				.get("eps.authorisationToken").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}
	
	/**
	 * <p> This method returns the EPS Staging url. </p>
	 * 
	 * @return A String which returns the value of eps.stagingUrl.
	 */
	public String getEPSStagingURL(){
		return ConfigurationManager.configurationProperties
				.get("eps.stagingUrl").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}
	
	/**
	 * <p> This method returns the EPS Item url. </p>
	 * 
	 * @return A String which returns the value of eps.itemUrl.
	 */
	public String getEPSItemURL(){
		return ConfigurationManager.configurationProperties
				.get("eps.itemUrl").replaceAll(SPECIAL_CHARACTER_STRING, "");
	}
	
	/**
	 * <p> This method returns the Cache Expiry In Seconds For Book Questions. </p>
	 * 
	 * @return A String which returns the value of cache.book_questions.cacheExpiryInSeconds.
	 */
	public Integer getCacheExpiryInSecondsForBookQuestions(){
		return Integer.valueOf(configurationProperties.get("cache.book_questions.cacheExpiryInSeconds"));
	}
	
	/**
	 * <p> This method returns the Cache Expiry In Seconds For BookContainer Questions. </p>
	 * 
	 * @return A String which returns the value of cache.book_Container_questions.cacheExpiryInSeconds.
	 */
	public Integer getCacheExpiryInSecondsForBookContainerQuestions(){
		return Integer.valueOf(configurationProperties.get("cache.book_Container_questions.cacheExpiryInSeconds"));
	}
	
	/**
	 * <p> This method returns the Cache Expiry In Seconds For QuestionXML. </p>
	 * 
	 * @return A String which returns the value of cache.question_XML.cacheExpiryInSeconds.
	 */
	public Integer getCacheExpiryInSecondsForQuestionXML(){
		return Integer.valueOf(configurationProperties.get("cache.question_XML.cacheExpiryInSeconds"));
	}	

	/**
	 * <p> This method returns the name of the application for Health details. </p>
	 * 
	 * @return A String which returns the value of HealthApplicationName.
	 */
	public String getHealthApplicationName() {
		return ConfigurationManager.configurationProperties.get(
				"HealthApplicationName").replaceAll(HEALTH_SPECIAL_CHARACTER, " ");
	}

	/**
	 * <p> This method returns the name of the owner for Health details. </p>
	 * 
	 * @return A String which returns the value of HealthApplicationOwnerName.
	 */
	public String getHealthApplicationOwnerName() {
		return ConfigurationManager.configurationProperties.get(
				"HealthApplicationOwnerName").replaceAll(HEALTH_SPECIAL_CHARACTER, " ");
	}
	
	/**
	 * <p> This will get the exception message we get from PAF when we try to delete the parent test </p>
	 * 
	 * @return A String which returns the value of ParentTestDeleteExceptionMsg.
	 */
	public String getParentTestDeleteExceptionMsg() {
		return ConfigurationManager.configurationProperties.get("ParentTestDeleteExceptionMsg");
	}
}
