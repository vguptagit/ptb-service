package com.pearson.ptb.bean;

import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;

/**
 * The <code>TestVersionInfo</code> class responsible to hold version info like
 * scramble type and no of versions
 * 
 * @author nithinjain
 *
 */
public class TestVersionInfo {

	/**
	 * Indicates the scramble type
	 */
	private TestScrambleType scrambleType;

	/**
	 * Indicates the no of versions
	 */
	private int noOfVersions;

	/** Get {@see #scrambleType}. @return {@link #scrambleType}. */
	public TestScrambleType getScrambleType() {
		return scrambleType;
	}

	/** Set {@see #scrambleType}. @param {@link #scrambleType}. */
	public void setScrambleType(TestScrambleType scrambleType) {
		this.scrambleType = scrambleType;
	}

	/** Get {@see #noOfVersions}. @return {@link #noOfVersions}. */
	public int getNoOfVersions() {
		return noOfVersions;
	}

	/** Set {@see #noOfVersions}. @param {@link #noOfVersions}. */
	public void setNoOfVersions(int noOfVersions) {
		this.noOfVersions = noOfVersions;
	}

	/**
	 * validates the test version info status
	 * 
	 * @param messages
	 *            The validation messages
	 * @return All validation messages
	 * @throws ConfigException
	 */
	public void validateState(){
		ConfigurationManager config = ConfigurationManager.getInstance();
		// Validate noOfVersions field and this field should be grater than zero
		if (this.noOfVersions <= 0
				|| this.noOfVersions >= config.getMaxScrambledVersions()) {
			throw new BadDataException("VersionInfo.noOfVersions should be grater than zero and less than "
					+ config.getMaxScrambledVersions());
		}
		
	}

}
