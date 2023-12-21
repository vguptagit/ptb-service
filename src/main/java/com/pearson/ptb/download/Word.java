package com.pearson.ptb.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.aspose.words.BreakType;
import com.aspose.words.Cell;
import com.aspose.words.ConvertUtil;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Font;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.License;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeList;
import com.aspose.words.NodeType;
import com.aspose.words.PageSetup;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.RunCollection;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.Table;
import com.google.common.collect.Multimap;
import com.pearson.ptb.bean.AnswerAreas;
import com.pearson.ptb.bean.AnswerKeys;
import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.bean.PageNumberDisplay;
import com.pearson.ptb.bean.PrintSettings;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.framework.QTIParser;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.QuestionTypes;

public class Word implements TestDownload {

	Document printDocument = null;
	QTIParser qtiParser = null;
	Map<String, Table> templateTable = null;
	DocumentBuilder builder = null;

	private static final String SECTION_TAG = "//Section";
	private static final String QUESTIONTEXT_TABLE = "QuestionText";
	private static final String ASSESSMENT_TABLE = "AssessmentText";
	private static final String CHOISE_TABLE = "ChoiceText";
	private static final String MATCH_CHOISE_TABLE = "MatchChoiceText";

	private static final int QUESTION_SPACE = 5;
	private static final int QUESTIONTABLE_INDEX = 0;
	private static final int ASSESSMENTTABLE_INDEX = 1;
	private static final int CHOISETABLE_INDEX = 2;
	private static final int MATCHCHOISETABLE_INDEX = 3;
	private static final int QUESTIONTEXT_WITH_ANSWERBLANKTABLE_INDEX = 4;
	private static final int ANSWERCHOICE_WITH_ANSWERBLANKTABLE_INDEX = 5;
	private static final int ANSWERCHOICE_MATCHING_WITH_ANSWERBLANKTABLE_INDEX = 6;
	private static final int ANSWER_CHOICE_HORIZONTALY = 7;

	private static final String TESTNAME = "Test Name :";
	private static final String TESTNAME_LABEL_MERGEFIELD = "TestNameTitle";
	private static final String TESTNAME_MERGEFIELD = "TestName";
	private static final String PAGENUMBER_MERGEFIELD = "PageNumber";
	private static final String BULLET_MERGEFIELD = "Bullet";
	private static final String CONTENT_MERGEFIELD = "Content";
	private static final String DOT = ".";
	private static final int ASCII_CHAR_A = 97;
	private static final String INDEX_MERGEFIELD = "Index";
	private static final String CHOICE_MERGEFIELD = "ChoiceContent";
	private static final String A_INDEX_MERGEFIELD = "Lft";
	private static final String A_OPTION_MERGEFIELD = "AContent";
	private static final String B_INDEX_MERGEFIELD = "Rt";
	private static final String B_OPTION_MERGEFIELD = "RightContent";
	private static final String ANSWERBLANK = "___________";
	private static final String ANSWERBLANK_MERGEFIELD = "AnswerBlank";
	private static final String ANSWER_CHOICE_SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String PAGE_SMALL = "Page ";
	private static final String PAGE_CAPITAL = "PAGE";
	private static final String OF = " of  ";
	private static final Logger LOG = LogWrapper.getInstance(Word.class);

	/**
	 * 
	 * @throws InternalException
	 */
	public Word() {
		setLicense();
	}

	/**
	 * Setting up license for aspose word in the constructor
	 * 
	 * @throws InternalException
	 */
	private void setLicense() {
		License lic = new License();
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader()
					.getResourceAsStream("Aspose.Words.lic");

			lic.setLicense(inputStream);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while setting up licence for Aspose.Word", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error("Error while Closing The Stream", e);
			}
		}

	}

	/**
	 * Creates object of document where content need to be written, also it
	 * creates object of the template from which table are imported
	 * 
	 * @throws InternalException
	 * 
	 */
	private void setDocumentDetails(DownloadInfo downloadInfo) {
		try {
			printDocument = new Document();
			Document templateDocument = getTemplateDocument();
			builder = new DocumentBuilder(printDocument);
			Font font = builder.getFont();
			font.setName(downloadInfo.getPrintSettings().getFont());
			font.setSize(Double.parseDouble(
					downloadInfo.getPrintSettings().getFontSize()));
			Section section = getSection(templateDocument);
			setTableTemplates(section);
			qtiParser = new QTIParser();
		} catch (Exception e) {
			throw new InternalException(
					"Exception initiating the word document", e);
		}
	}

	/**
	 * Gets the object of template document from which table are imported
	 * 
	 * @return
	 * @throws InternalException
	 */
	private Document getTemplateDocument() {
		try {
			File file = new File(this.getClass()
					.getResource("/myTestTemplate.docx").getPath());
			return new Document(file.getAbsolutePath());

		} catch (Exception e) {
			throw new InternalException(
					"Exception while getting template document", e);
		}
	}

	/**
	 * Getting section from the template document
	 * 
	 * @return
	 * @throws InternalException
	 */
	private Section getSection(Document templateDocument) {
		try {
			NodeList<?> sectionList = templateDocument.selectNodes(SECTION_TAG);
			return (Section) printDocument.importNode(sectionList.get(0), true);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while getting section from template document",
					e);
		}
	}

	/**
	 * reading the tables from section and adding the tables to hash map, so
	 * that table can be picked when ever required from the hash map
	 * 
	 * @param section
	 * @return
	 */
	private void setTableTemplates(Section section) {
		templateTable = new HashMap<String, Table>();
		templateTable.put(QUESTIONTEXT_TABLE,
				section.getBody().getTables().get(QUESTIONTABLE_INDEX));
		templateTable.put(ASSESSMENT_TABLE,
				section.getBody().getTables().get(ASSESSMENTTABLE_INDEX));
		templateTable.put(CHOISE_TABLE,
				section.getBody().getTables().get(CHOISETABLE_INDEX));
		templateTable.put(MATCH_CHOISE_TABLE,
				section.getBody().getTables().get(MATCHCHOISETABLE_INDEX));
		templateTable.put("QuestionText_AnswerBlank", section.getBody()
				.getTables().get(QUESTIONTEXT_WITH_ANSWERBLANKTABLE_INDEX));
		templateTable.put("AnswerChoice_AnswerBlank", section.getBody()
				.getTables().get(ANSWERCHOICE_WITH_ANSWERBLANKTABLE_INDEX));
		templateTable.put("MatchAnswerChoice_AnswerBlank",
				section.getBody().getTables().get(
						ANSWERCHOICE_MATCHING_WITH_ANSWERBLANKTABLE_INDEX));

		templateTable.put("Answer_Choice_Horizontaly",
				section.getBody().getTables().get(ANSWER_CHOICE_HORIZONTALY));

	}

	/**
	 * Implementing the download method which is declared in interface.This is
	 * responsible to generate word file
	 */
	@Override
	public DownloadOutput download(OutputStream stream,
			DownloadInfo downloadInfo) {

		download(stream, downloadInfo, SaveFormat.DOC);

		DownloadOutput output = new DownloadOutput();
		output.setContentType("msword");
		String fileName;
		if (downloadInfo.getPrintSettings().getIncludeAnswerKeyIn()
				.equals(AnswerKeys.SEPARATEFILE)) {
			fileName = downloadInfo.getTestTitle() + "-AnswerKeys";
		} else {
			fileName = downloadInfo.getTestTitle();
		}
		output.setFileName(fileName + this.extension());
		return output;
	}

	/***
	 * Returns the extension of the file type downloaded by this downloader
	 */
	@Override
	public String extension() {
		return ".doc";
	}

	protected void download(OutputStream stream, DownloadInfo downloadInfo,
			int downloadFormat) {
		try {
			setDocumentDetails(downloadInfo);
			initDocument(downloadInfo);

			writeContent(downloadInfo);
			printDocument.save(stream, downloadFormat);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while rendering the word document", e);
		}
	}

	private void writeStudentName() {
		try {

			builder.write(String.valueOf("Name: "));

			builder.insertBreak(BreakType.PARAGRAPH_BREAK);
			builder.insertBreak(BreakType.PARAGRAPH_BREAK);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while rendering the AssessmentTitle", e);
		}
	}

	/**
	 * Setting up the page format like page margin, font ect. Also adding header
	 * and footer for documnet
	 * 
	 * @param downloadInfo
	 * @throws InternalException
	 */
	private void initDocument(DownloadInfo downloadInfo) {
		try {
			pagesetup(printDocument.getFirstSection(),
					downloadInfo.getPrintSettings());
			addHeader(downloadInfo);
			addFooter(downloadInfo);
		} catch (Exception e) {
			throw new InternalException("Exception writing header and footer",
					e);
		}
	}

	/**
	 * Setting up page like margins, header distance.
	 * 
	 * @param settings
	 * @param section
	 */
	private void pagesetup(Section section, PrintSettings printSettings) {
		PageSetup page = section.getPageSetup();
		page.setLeftMargin(ConvertUtil.inchToPoint(
				Double.parseDouble(printSettings.getLeftMargin())));
		page.setRightMargin(ConvertUtil.inchToPoint(
				Double.parseDouble(printSettings.getRightMargin())));
		page.setTopMargin(ConvertUtil
				.inchToPoint(Double.parseDouble(printSettings.getTopMargin())));
		page.setBottomMargin(ConvertUtil.inchToPoint(
				Double.parseDouble(printSettings.getBottomMargin())));

		page.setHeaderDistance(ConvertUtil.inchToPoint(
				Double.parseDouble(printSettings.getHeaderSpace())));
		page.setFooterDistance(ConvertUtil.inchToPoint(
				Double.parseDouble(printSettings.getFooterSpace())));
	}

	/**
	 * Adding header for document
	 * 
	 * @param testTitle
	 */
	private void addHeader(DownloadInfo downloadInfo) {
		try {
			Table headerTable = (Table) templateTable.get(ASSESSMENT_TABLE)
					.deepClone(true);

			HeaderFooter headerFooter = new HeaderFooter(printDocument,
					HeaderFooterType.HEADER_PRIMARY);
			headerFooter.appendChild(headerTable);
			printDocument.getSections().get(0).getHeadersFooters()
					.add(headerFooter);
			builder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);

			builder.moveToMergeField(TESTNAME_LABEL_MERGEFIELD);

			Font font = builder.getFont();
			font.setName(downloadInfo.getPrintSettings().getFont());
			font.setSize(Double.parseDouble(
					downloadInfo.getPrintSettings().getFontSize()));

			builder.write(String.valueOf(TESTNAME));
			builder.moveToMergeField(TESTNAME_MERGEFIELD);

			Font fontTestName = builder.getFont();
			fontTestName.setName(downloadInfo.getPrintSettings().getFont());
			fontTestName.setSize(Double.parseDouble(
					downloadInfo.getPrintSettings().getFontSize()));

			builder.write(downloadInfo.getTestTitle());

			builder.moveToMergeField(PAGENUMBER_MERGEFIELD);

			if (downloadInfo.getPrintSettings().getPageNumberDisplay()
					.equals(PageNumberDisplay.TOPRIGHT)) {
				builder.getParagraphFormat()
						.setAlignment(ParagraphAlignment.RIGHT);
				builder.write(PAGE_SMALL);
				builder.insertField(PAGE_CAPITAL, "");
				builder.write(OF);
				builder.insertField(" NUMPAGES", "");

			} else {
				builder.write("");
			}

			builder.moveToDocumentEnd();

		} catch (Exception e) {
			throw new InternalException(
					"Exception while rendering the AssessmentTitle", e);
		}
	}

	/**
	 * Adding footer for document
	 * 
	 * @throws InternalException
	 * 
	 */
	private void addFooter(DownloadInfo downloadInfo) {
		try {
			HeaderFooter headerFooter = new HeaderFooter(printDocument,
					HeaderFooterType.FOOTER_PRIMARY);
			printDocument.getSections().get(0).getHeadersFooters()
					.add(headerFooter);
			builder.moveToHeaderFooter(HeaderFooterType.FOOTER_PRIMARY);
			if (downloadInfo.getPrintSettings().getPageNumberDisplay()
					.equals(PageNumberDisplay.BOTTOMRIGHT)) {
				builder.getParagraphFormat()
						.setAlignment(ParagraphAlignment.RIGHT);
				builder.write(PAGE_SMALL);
				builder.insertField(PAGE_CAPITAL, "");
				builder.write(OF);
				builder.insertField("NUMPAGES", "");
			} else if (downloadInfo.getPrintSettings().getPageNumberDisplay()
					.equals(PageNumberDisplay.BOTTOMMIDDLE)) {
				builder.getParagraphFormat()
						.setAlignment(ParagraphAlignment.CENTER);
				builder.write(PAGE_SMALL);
				builder.insertField(PAGE_CAPITAL, "");
				builder.write(OF);
				builder.insertField(" NUMPAGES", "");
			}

			builder.moveToDocumentEnd();
			builder.insertBreak(BreakType.PARAGRAPH_BREAK);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while writing footer in the document", e);
		}
	}

	/**
	 * Writing the content like question text, answer choices and answer keys
	 * depending on the option.
	 * 
	 * @param downloadInfo
	 * @throws InternalException
	 * @throws ConfigException
	 */
	private void writeContent(DownloadInfo downloadInfo) {
		PrintSettings printSettings = downloadInfo.getPrintSettings();
		try {

			if (printSettings.getIncludeAnswerKeyIn() == AnswerKeys.NONE) {

				if (printSettings.isIncludeStudentName()) {
					writeStudentName();
				}

				writeQuestions(downloadInfo, printSettings);
				writeAnswerBlankForeachQuestion(downloadInfo, printSettings);
			} else if (printSettings
					.getIncludeAnswerKeyIn() == AnswerKeys.SAMEFILE) {

				if (printSettings.isIncludeStudentName()) {
					writeStudentName();
				}

				writeQuestions(downloadInfo, printSettings);
				writeAnswerBlankForeachQuestion(downloadInfo, printSettings);
				writeAnswerKeyForeachQuestion(downloadInfo, printSettings);
			} else if (printSettings
					.getIncludeAnswerKeyIn() == AnswerKeys.SEPARATEFILE) {

				int i = 0;
				for (Object question : downloadInfo.getQuestions()) { // NOSONAR
					qtiParser.setXMLDocument((String) question);
					printAnswerKeys(i, printSettings);
					i = i + 1;
				}
			}

		} catch (Exception e) {
			throw new InternalException("Exception while writing content", e);
		}
	}

	/**
	 * 
	 * @param downloadInfo
	 * @param printSettings
	 * @throws InternalException
	 * @throws ConfigException
	 * @throws Exception
	 */
	private void writeQuestions(DownloadInfo downloadInfo,
			PrintSettings printSettings) {
		int i = 0;
		for (Object question : downloadInfo.getQuestions()) {
			qtiParser.setXMLDocument((String) question);
			printQuestionAndAnswerChoice(i, printSettings);
			i = i + 1;
		}
	}

	/**
	 * 
	 * @param downloadInfo
	 * @param printSettings
	 * @throws Exception
	 * @throws InternalException
	 * @throws ConfigException
	 */
	private void writeAnswerKeyForeachQuestion(DownloadInfo downloadInfo,
			PrintSettings printSettings) {
		try {

			builder.insertBreak(BreakType.PAGE_BREAK);
			int i = 0;
			for (Object question : downloadInfo.getQuestions()) {
				qtiParser.setXMLDocument((String) question);
				printAnswerKeys(i, printSettings);
				i = i + 1;
			}

		} catch (Exception e) {
			throw new InternalException("Exception while writing questions", e);
		}
	}

	/**
	 * 
	 * @param downloadInfo
	 * @param printSettings
	 * @throws InternalException
	 */
	private void writeAnswerBlankForeachQuestion(DownloadInfo downloadInfo,
			PrintSettings printSettings) {
		try {
			if (printSettings
					.getIncludeAreaForStudentResponse() == AnswerAreas.LASTPAGE) {
				builder.insertBreak(BreakType.PAGE_BREAK);
				int i = 1;
				for (Object question : downloadInfo.getQuestions()) {
					qtiParser.setXMLDocument((String) question);
					printAnswerBlank(i, printSettings);
					i = i + 1;
				}

			}
		} catch (Exception e) {
			throw new InternalException("Exception while answer blanks", e);
		}
	}

	/**
	 * Writing question and answer choices in to document.
	 * 
	 * @param questionIndex
	 * @param printSettings
	 * @throws InternalException
	 */
	private void printQuestionAndAnswerChoice(Integer questionIndex,
			PrintSettings printSettings) {
		try {
			QuestionTypes questionType = qtiParser.getQuestionType();

			if (printSettings
					.getIncludeAreaForStudentResponse() == AnswerAreas.LEFTSIDE) {
				appendTable("QuestionText_AnswerBlank");
				builder.moveToMergeField(ANSWERBLANK_MERGEFIELD);
				if (questionType != QuestionTypes.ESSAY
						&& questionType != QuestionTypes.MATCHING
						&& questionType != QuestionTypes.FILLINBLANKS) {
					bindWrite(ANSWERBLANK, printSettings);
				} else {
					bindWrite("", printSettings);
				}
			} else {
				appendTable(QUESTIONTEXT_TABLE);
			}

			builder.moveToMergeField(BULLET_MERGEFIELD);
			bindWrite(String.valueOf(questionIndex + 1) + DOT, printSettings);

			builder.moveToMergeField(CONTENT_MERGEFIELD);
			bindHTML(qtiParser.getQuestionText(), printSettings);
			builder.moveToDocumentEnd();

			printAnswerChoice(printSettings);

			includeWorkingSpace(printSettings);

			if (questionType == QuestionTypes.ESSAY) {
				includeAnswerAreaForEssay();
			}

			builder.insertBreak(BreakType.PARAGRAPH_BREAK);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while writing question text and answer choices",
					e);
		}
	}

	/**
	 * Writing answer choices in to document.
	 * 
	 * @throws InternalException
	 */
	private void printAnswerChoice(PrintSettings printSettings) {

		QuestionTypes questionTypes = qtiParser.getQuestionType();

		if (questionTypes == QuestionTypes.MATCHING) {
			printTwoColumnChoices(printSettings);
		} else {
			printSingleColumnChoices(printSettings);
		}

	}

	/**
	 * Writing answer choices for question like true false, multiple
	 * choice,multiple response
	 * 
	 * @throws InternalException
	 */
	private void printSingleColumnChoices(PrintSettings printSettings) {

		String answerChoiceOrientation = qtiParser.getAnswerChoiceOrientation();
		if ("Horizontal".equals(answerChoiceOrientation)) {
			printAnswerChoicesHorizontally(printSettings);
		} else {
			printAnswerChoicesVertically(printSettings);
		}

	}

	/**
	 * Printing the answer choices horizontally
	 * 
	 * @param printSettings
	 */
	private void printAnswerChoicesHorizontally(PrintSettings printSettings) {
		List<String> answerChoicesList = qtiParser.getAnswerChoices();
		StringBuilder answers = new StringBuilder();
		try {
			for (int i = 0; i < answerChoicesList.size(); i++) {

				answers.append(convertToLetter(i)).append("&nbsp;");
				answers.append(answerChoicesList.get(i).toString())
						.append(ANSWER_CHOICE_SPACE);

			}

			if (printSettings
					.getIncludeAreaForStudentResponse() == AnswerAreas.LEFTSIDE) {
				appendTable("Answer_Choice_Horizontaly");
				builder.moveToMergeField(ANSWERBLANK_MERGEFIELD);
				bindWrite("", printSettings);
			} else {
				appendTable(QUESTIONTEXT_TABLE);
				builder.moveToMergeField(BULLET_MERGEFIELD);
				bindWrite("", printSettings);
			}

			builder.moveToMergeField(CONTENT_MERGEFIELD);
			bindHTML(answers.toString(), printSettings);
			builder.moveToDocumentEnd();
		} catch (Exception e) {
			throw new InternalException(
					"Exception while writing answer choices horizontaly", e);
		}

	}

	/**
	 * Printing the answer choices vertically
	 * 
	 * @param printSettings
	 */
	private void printAnswerChoicesVertically(PrintSettings printSettings) {
		List<String> answerChoicesList = qtiParser.getAnswerChoices();
		for (int i = 0; i < answerChoicesList.size(); i++) {
			try {
				if (printSettings
						.getIncludeAreaForStudentResponse() == AnswerAreas.LEFTSIDE) {
					appendTable("AnswerChoice_AnswerBlank");
					builder.moveToMergeField(ANSWERBLANK_MERGEFIELD);
					bindWrite("", printSettings);
				} else {
					appendTable(CHOISE_TABLE);
				}

				builder.moveToMergeField(INDEX_MERGEFIELD);
				bindWrite(convertToLetter(i), printSettings);
				builder.moveToMergeField(CHOICE_MERGEFIELD);
				bindHTML(answerChoicesList.get(i).toString(), printSettings);

				builder.moveToDocumentEnd();
			} catch (Exception e) {
				throw new InternalException(
						"Exception while writing answer choices Vertically", e);
			}

		}
	}

	/**
	 * Writing answer choices for question like match
	 * 
	 * @throws InternalException
	 */
	private void printTwoColumnChoices(PrintSettings printSettings) {

		Multimap<String, String> twoColumnAnswerChoices = qtiParser
				.getAnswerChoicesForMatching(true);

		try {
			int i = 0;
			for (Map.Entry<String, String> entry : twoColumnAnswerChoices
					.entries()) {
				if (printSettings
						.getIncludeAreaForStudentResponse() == AnswerAreas.LEFTSIDE) {
					appendTable("MatchAnswerChoice_AnswerBlank");
					builder.moveToMergeField(ANSWERBLANK_MERGEFIELD);
					bindWrite(ANSWERBLANK, printSettings);
				} else {
					appendTable("MatchChoiceText");
				}

				builder.moveToMergeField(A_INDEX_MERGEFIELD);
				bindWrite(convertToLetter(i), printSettings);
				builder.moveToMergeField(A_OPTION_MERGEFIELD);
				bindHTML(entry.getKey().toString(), printSettings);

				builder.moveToMergeField(B_INDEX_MERGEFIELD);
				bindWrite(convertToLetter(i), printSettings);
				builder.moveToMergeField(B_OPTION_MERGEFIELD);
				bindHTML(entry.getValue().toString(), printSettings);

				builder.moveToDocumentEnd();

				i = i + 1;
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while writing answer choices for matching question",
					e);
		}
	}

	/**
	 * Writing answer keys. i.e. Correct answers
	 * 
	 * @param questionIndex
	 * @throws InternalException
	 */
	private void printAnswerKeys(Integer questionIndex,
			PrintSettings printSettings) {
		try {
			String answerKeys = qtiParser.getAnswerKeys();

			appendTable(QUESTIONTEXT_TABLE);
			builder.moveToMergeField(BULLET_MERGEFIELD);

			bindWrite(String.valueOf(questionIndex + 1) + DOT, printSettings);

			builder.moveToMergeField(CONTENT_MERGEFIELD);
			if (answerKeys != null) {
				bindHTML(answerKeys, printSettings);
			} else {
				bindHTML("", printSettings);
			}

			builder.moveToDocumentEnd();
			builder.insertBreak(BreakType.PARAGRAPH_BREAK);
		} catch (Exception e) {
			throw new InternalException("Exception while writing answer keys",
					e);
		}
	}

	/**
	 * 
	 * @param questionIndex
	 * @throws InternalException
	 */
	private void printAnswerBlank(Integer questionIndex,
			PrintSettings printSettings) {
		try {
			appendTable(CHOISE_TABLE);

			builder.moveToMergeField(INDEX_MERGEFIELD);
			bindWrite(questionIndex.toString() + ".", printSettings);
			builder.moveToMergeField(CHOICE_MERGEFIELD);
			QuestionTypes questionType = qtiParser.getQuestionType();
			if (questionType == QuestionTypes.ESSAY) {
				includeAnswerAreaForEssay();
				bindWrite("", printSettings);
			} else {
				bindWrite(ANSWERBLANK, printSettings);
			}

			builder.moveToDocumentEnd();
		} catch (Exception e) {
			throw new InternalException("Exception while answer blank in page",
					e);
		}

	}

	/**
	 * Gets the table from hash map and adds to the document where content need
	 * to be written.
	 * 
	 * @param tableIndex
	 * @throws InternalException
	 */
	private void appendTable(String tableIndex) {
		try {
			Table table = (Table) templateTable.get(tableIndex).deepClone(true);
			printDocument.getFirstSection().getBody().appendChild(table);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while appending the table in the document", e);
		}
	}

	/**
	 * Writes the HTML content to the document and also default fond is applied
	 * if any font is not set for the content.
	 * 
	 * @param content
	 * @throws InternalException
	 */
	private void bindHTML(String content, PrintSettings printSettings) {
		try {
			builder.insertHtml(content);
			RunCollection runCollection = ((Cell) builder.getCurrentParagraph()
					.getParentNode()).getFirstParagraph().getRuns();

			if (runCollection != null) {
				for (int i = 0; i < runCollection.getCount(); i++) {
					Font font = runCollection.get(i).getFont();
					font.setName(printSettings.getFont());
				}
			}

			setImageSize((Cell) builder.getCurrentParagraph().getParentNode());
		} catch (Exception e) {
			throw new InternalException(
					"Exception while inserting the html content to document",
					e);
		}
	}

	/**
	 * Sets the width and height of the image to fit in to the given cell.
	 * 
	 * @param currentcell
	 */
	private void setImageSize(Cell currentcell) {
		double currentCellWidth;
		double margin;
		Shape shape = null;
		double imageWidth;
		double imageHeight;
		double changeRatio;
		double changedWidth;
		double changedHeight;

		currentCellWidth = currentcell.getCellFormat().getWidth();
		margin = builder.getPageSetup().getRightMargin();
		NodeCollection<?> nodes = currentcell.getChildNodes(NodeType.SHAPE,
				true);

		changedWidth = currentCellWidth - margin;
		if (changedWidth < 0) {
			changedWidth = currentCellWidth;
		}

		for (int i = 0; i < nodes.getCount(); i++) {
			shape = (Shape) nodes.get(i);
			imageWidth = shape.getWidth();
			imageHeight = shape.getHeight();

			changeRatio = imageWidth / changedWidth;
			changedHeight = imageHeight / changeRatio;

			if (imageWidth > currentCellWidth) {
				try {
					shape.setWidth(changedWidth);
					shape.setHeight(changedHeight);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * Writes the content and sets default font.
	 * 
	 * @param content
	 * @throws InternalException
	 */
	private void bindWrite(String content, PrintSettings printSettings) {
		try {
			Font font = builder.getFont();
			font.setSize(Double.parseDouble(printSettings.getFontSize()));
			font.setName(printSettings.getFont());
			builder.write(content);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while writing the content to document", e);
		}
	}

	/**
	 * Sets the line space between the questions.
	 * 
	 * @param printSettings
	 * @throws InternalException
	 */
	private void includeWorkingSpace(PrintSettings printSettings) {
		try {
			if (printSettings
					.getIncludeAreaForStudentResponse() == AnswerAreas.BETWEENQUESTIONS) {
				for (int i = 0; i < QUESTION_SPACE; i++) {
					builder.insertBreak(BreakType.LINE_BREAK);
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while providing space betweeb question", e);
		}
	}

	/**
	 * Including the answer area for essay questions.
	 */
	private void includeAnswerAreaForEssay() {
		try {
			int lineBreakLength = qtiParser.getAnswerAreaForEssay();

			for (int i = 0; i < lineBreakLength; i++) {
				builder.insertBreak(BreakType.LINE_BREAK);
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while printing answer area for essay", e);
		}
	}

	/**
	 * Gets the alphabet depending the index passed
	 * 
	 * @param index
	 * @return
	 */
	private String convertToLetter(Integer index) {
		return (char) (ASCII_CHAR_A + index) + ".";
	}
}
