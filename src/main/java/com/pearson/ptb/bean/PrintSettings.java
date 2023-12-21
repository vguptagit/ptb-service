package com.pearson.ptb.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.ptb.framework.ConfigurationManager;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;

/**
 * The <code>PrintSetting</code> Entity gives all print related settings such
 * margins, font, format, etc.
 */
@Document
public class PrintSettings implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * To check if Test has Multiple Versions
	 */
	private boolean multipleVersions;

	/**
	 * Indicates Number Of Versions
	 */
	private int numberOfVersions;

	/**
	 * Indicates Scramble Order
	 */
	private String scrambleOrder;

	/**
	 * Indicates Position of Area For Student Response, by default setting to
	 * none
	 */
	private AnswerAreas includeAreaForStudentResponse = AnswerAreas.NONE;

	/**
	 * Indicates Position of Answer Key, by default setting to none
	 */
	private AnswerKeys includeAnswerKeyIn = AnswerKeys.NONE;

	/**
	 * To check if it includes Answer Feedback
	 */
	private boolean includeAnwserFeedback;

	/**
	 * To check if it includes Question Hints
	 */
	private boolean includeQuestionHints;

	/**
	 * Indicates Top Margin
	 */
	private String topMargin;

	/**
	 * Indicates Bottom Margin
	 */
	private String bottomMargin;

	/**
	 * Indicates Left Margin
	 */
	private String leftMargin;

	/**
	 * Indicates Right Margin
	 */
	private String rightMargin;

	/**
	 * Indicates Header Space
	 */
	private String headerSpace;

	/**
	 * Indicates Footer Space
	 */
	private String footerSpace;

	/**
	 * Indicates Font type
	 */
	private String font;

	/**
	 * Indicates Font size
	 */
	private String fontSize;

	/**
	 * Indicates file format for download document
	 */
	private DownloadFormat exportFileFormat;

	/**
	 * To check if it includes Randomized Tests
	 */
	private boolean includeRandomizedTests;

	/**
	 * To check if to include Student Name, by default its true
	 */
	private boolean includeStudentName = true;

	/**
	 * Position to Display the page Number , by default its BOTTOMRIGHT
	 */
	private PageNumberDisplay pageNumberDisplay = PageNumberDisplay.BOTTOMRIGHT;

	/**
	 * To answer feedback
	 * 
	 * @return includeAnwserFeedback is a boolean value
	 */
	public boolean isIncludeAnwserFeedback() {
		return includeAnwserFeedback;
	}

	/** Get {@see #pageNumberDisplay}. @return {@link #pageNumberDisplay}. */
	public PageNumberDisplay getPageNumberDisplay() {
		return pageNumberDisplay;
	}

	/** Set {@see #pageNumberDisplay}. @param {@link #pageNumberDisplay}. */
	public void setPageNumberDisplay(PageNumberDisplay pageNumberDisplay) {
		this.pageNumberDisplay = pageNumberDisplay;
	}

	/**
	 * Set {@see #includeAnwserFeedback}. @param {@link #includeAnwserFeedback}.
	 */
	public void setIncludeAnwserFeedback(boolean includeAnwserFeedback) {
		this.includeAnwserFeedback = includeAnwserFeedback;
	}

	/**
	 * To Include Question Hints
	 * 
	 * @return includeQuestionHints is a boolean value
	 */
	public boolean isIncludeQuestionHints() {
		return includeQuestionHints;
	}

	/**
	 * Set {@see #includeQuestionHints}. @param {@link #includeQuestionHints}.
	 */
	public void setIncludeQuestionHints(boolean includeQuestionHints) {
		this.includeQuestionHints = includeQuestionHints;
	}

	/** Get {@see #scrambleOrder}. @return {@link #scrambleOrder}. */
	public String getScrambleOrder() {
		return scrambleOrder;
	}

	/** Set {@see #scrambleOrder}. @param {@link #scrambleOrder}. */
	public void setScrambleOrder(String scrambleOrder) {
		this.scrambleOrder = scrambleOrder;
	}

	/**
	 * To Include Multiple Versions
	 * 
	 * @return multipleVersions is a boolean value
	 */
	public boolean isMultipleVersions() {
		return multipleVersions;
	}

	/** Set {@see #multipleVersions}. @param {@link #multipleVersions}. */
	public void setMultipleVersions(boolean multipleVersions) {
		this.multipleVersions = multipleVersions;
	}

	/** Get {@see #numberOfVersions}. @return {@link #numberOfVersions}. */
	public int getNumberOfVersions() {
		return numberOfVersions;
	}

	/** Set {@see #numberOfVersions}. @param {@link #numberOfVersions}. */
	public void setNumberOfVersions(int numberOfVersions) {
		this.numberOfVersions = numberOfVersions;
	}

	/** Get {@see #topMargin}. @return {@link #topMargin}. */
	public String getTopMargin() {
		return topMargin;
	}

	/** Set {@see #topMargin}. @param {@link #topMargin}. */
	public void setTopMargin(String topMargin) {
		this.topMargin = topMargin;
	}

	/** Get {@see #bottomMargin}. @return {@link #bottomMargin}. */
	public String getBottomMargin() {
		return bottomMargin;
	}

	/** Set {@see #bottomMargin}. @param {@link #bottomMargin}. */
	public void setBottomMargin(String bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	/** Get {@see #leftMargin}. @return {@link #leftMargin}. */
	public String getLeftMargin() {
		return leftMargin;
	}

	/** Set {@see #leftMargin}. @param {@link #leftMargin}. */
	public void setLeftMargin(String leftMargin) {
		this.leftMargin = leftMargin;
	}

	/** Get {@see #rightMargin}. @return {@link #rightMargin}. */
	public String getRightMargin() {
		return rightMargin;
	}

	/** Set {@see #rightMargin}. @param {@link #rightMargin}. */
	public void setRightMargin(String rightMargin) {
		this.rightMargin = rightMargin;
	}

	/** Get {@see #headerSpace}. @return {@link #headerSpace}. */
	public String getHeaderSpace() {
		return headerSpace;
	}

	/** Set {@see #headerSpace}. @param {@link #headerSpace}. */
	public void setHeaderSpace(String headerSpace) {
		this.headerSpace = headerSpace;
	}

	/** Get {@see #footerSpace}. @return {@link #footerSpace}. */
	public String getFooterSpace() {
		return footerSpace;
	}

	/** Set {@see #footerSpace}. @param {@link #footerSpace}. */
	public void setFooterSpace(String footerSpace) {
		this.footerSpace = footerSpace;
	}

	/** Get {@see #font}. @return {@link #font}. */
	public String getFont() {
		return font;
	}

	/** Set {@see #font}. @param {@link #font}. */
	public void setFont(String font) {
		this.font = font;
	}

	/** Get {@see #fontSize}. @return {@link #fontSize}. */
	public String getFontSize() {
		return fontSize;
	}

	/** Set {@see #fontSize}. @param {@link #fontSize}. */
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	/** Get {@see #exportFileFormat}. @return {@link #exportFileFormat}. */
	public DownloadFormat getExportFileFormat() {
		return exportFileFormat;
	}

	/** Set {@see #exportFileFormat}. @param {@link #exportFileFormat}. */
	public void setExportFileFormat(DownloadFormat exportFileFormat) {
		this.exportFileFormat = exportFileFormat;
	}

	/**
	 * To validate the number of versions and scramble order
	 *
	 */
	public void validate() {

		validateNumberOfVersionsInRange();

		validateScrambleOrder();
	}

	private void validateNumberOfVersionsInRange() {
		int maxScrambledVersions;

		try {
			maxScrambledVersions = ConfigurationManager.getInstance()
					.getMaxScrambledVersions();
		} catch (ConfigException ex) {
			throw new InternalException("Unable to read configuration", ex);
		}

		if (isMultipleVersions() && (getNumberOfVersions() < 1
				|| getNumberOfVersions() > maxScrambledVersions)) {

			throw new BadDataException(
					"When Multiple Versions is selected number of versions should be between 1 and "
							+ maxScrambledVersions);
		}
	}

	private void validateScrambleOrder() {
		List<String> scambleOrder = Arrays.asList("Scramble question order",
				"Scramble answer order", "Scramble question and answer order");

		if (isMultipleVersions()
				&& (scambleOrder.indexOf(getScrambleOrder()) == -1)) {

			throw new BadDataException(
					"Scramble Order must be one of " + scambleOrder.toString());
		}
	}

	/**
	 * To Include Randomized Tests
	 * 
	 * @return includeRandomizedTests is a boolean value
	 */
	public boolean isIncludeRandomizedTests() {
		return includeRandomizedTests;
	}

	/**
	 * Set {@see #includeRandomizedTests}. @param
	 * {@link #includeRandomizedTests}.
	 */
	public void setIncludeRandomizedTests(boolean includeRandomizedTests) {
		this.includeRandomizedTests = includeRandomizedTests;
	}

	/**
	 * To Include Student Name
	 * 
	 * @return includeStudentName is a boolean value
	 */
	public boolean isIncludeStudentName() {
		return includeStudentName;
	}

	/** Set {@see #includeStudentName}. @param {@link #includeStudentName}. */
	public void setIncludeStudentName(boolean includeStudentName) {
		this.includeStudentName = includeStudentName;
	}

	/** Get {@see #AnswerKeys}. @return {@link #AnswerKeys}. */
	public AnswerKeys getIncludeAnswerKeyIn() {
		return includeAnswerKeyIn;
	}

	/** Set {@see #includeAnswerKeyIn}. @param {@link #includeAnswerKeyIn}. */
	public void setIncludeAnswerKeyIn(AnswerKeys includeAnswerKeyIn) {
		this.includeAnswerKeyIn = includeAnswerKeyIn;
	}

	/** Get {@see #AnswerAreas}. @return {@link #AnswerAreas}. */
	public AnswerAreas getIncludeAreaForStudentResponse() {
		return includeAreaForStudentResponse;
	}

	/**
	 * Set {@see #includeAreaForStudentResponse}. @param
	 * {@link #includeAreaForStudentResponse}.
	 */
	public void setIncludeAreaForStudentResponse(
			AnswerAreas includeAreaForStudentResponse) {
		this.includeAreaForStudentResponse = includeAreaForStudentResponse;
	}

	/**
	 * To get the print settings
	 * 
	 * @return PrintSettings
	 * @throws InternalException
	 */
	@Override
	public PrintSettings clone() {
		try {
			return (PrintSettings) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new InternalException("Exception while cloning PrintSettings",
					e);
		}
	}
}
