package com.pearson.ptb.download;

import java.io.File;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Multimap;
import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.framework.QTIParser;
import com.pearson.ptb.framework.exception.InternalException;

public abstract class BlackboardQTIConvert {

	private static final String SEQUENCE = "bbmd_asi_object_id";
	private static final String QUESTIONTYPE = "bbmd_questiontype";
	private static final String ABSOLUTESCORE_MAXSCORE = "qmd_absolutescore_max";
	private static final String ABSOLUTESCORE_MINSCORE = "qmd_absolutescore_min";
	private static final String ABSOLUTESCORE_SCORE = "qmd_absolutescore";
	private static final String MAT_FORMATTEDTEXT = "//flow[@class=\"QUESTION_BLOCK\"]";

	String ptbQuestionTemplatePath;
	int questionSequence;
	DownloadFormat bbDownloadFormat;
	String currentQuestionType;
	String ptbQuestionXML;

	QTIParser ptbQTIParser;
	XPath xpath;
	Document bbQuestionXML;
	Multimap<String, Double> answerKeysWithScore;

	/**
	 * Constructor which expects blackboard format i.e. Pool manager and Test
	 * manager. Sequence of the question which need to be updated in question
	 * meta data. Question type of the question(i.e
	 * TrueFalse/Essay/MultipleChoice/MultipleResponse/FillInBlank) which need
	 * to be updated in question meta data. Question XML which is 2.1 standard
	 * from which blackboard QTI XML need to be generated
	 */
	public BlackboardQTIConvert(DownloadFormat bbFormat, String templatePath,
			int sequence, String questionType, String questionXML) {
		bbDownloadFormat = bbFormat;
		ptbQuestionTemplatePath = templatePath;
		questionSequence = sequence;
		currentQuestionType = questionType;
		ptbQuestionXML = questionXML;

		setQuestionXMLToParser();
		xpath = XPathFactory.newInstance().newXPath();
		setTemplateXML();
	}

	/*
	 * sets the question template by the template path to the variable.
	 */
	private Document setTemplateXML() {
		try {
			File inputFile = new File(this.getClass()
					.getResource(ptbQuestionTemplatePath).getPath());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			bbQuestionXML = docBuilder.parse(inputFile);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while getting test template", e);
		}
		return bbQuestionXML;
	}

	/**
	 * Creates the object of QTIParser and sets the XML which need to be parsed.
	 */
	private void setQuestionXMLToParser() {
		ptbQTIParser = new QTIParser();
		ptbQTIParser.setXMLDocument(ptbQuestionXML);
		answerKeysWithScore = ptbQTIParser.getAnswerKeysWithScore();
	}

	private String getExportType() {
		String exportType = "";
		if (bbDownloadFormat == DownloadFormat.bbpm) {
			exportType = "Pool";
		} else if (bbDownloadFormat == DownloadFormat.bbtm) {
			exportType = "Test";
		}
		return exportType;
	}

	/**
	 * Gets the single node from of the blackboard question template. If node
	 * need to be fetched by element tag name, the tag name need to be passed as
	 * identifier and byXPath should be false If node need to be fetched by
	 * XPath, node path need to be passed as identifier and byXPath should be
	 * true
	 */
	protected Node getXMLNode(String identifier, boolean byXPath) {
		try {
			if (byXPath) {
				return (Node) xpath.compile(identifier).evaluate(bbQuestionXML,XPathConstants.NODE);
			} else {
				return bbQuestionXML.getElementsByTagName(identifier).item(0);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml node", e);
		}
	}

	/**
	 * Gets the list of nodes from of the blackboard question template. If nodes
	 * need to be fetched by element tag name, the tag name need to be passed as
	 * identifier and byXPath should be false If nodes need to be fetched by
	 * XPath, node path need to be passed as identifier and byXPath should be
	 * true
	 */
	protected NodeList getXMLNodes(String identifier, boolean byXPath) {
		try {
			if (byXPath) {
				return (NodeList) xpath.compile(identifier).evaluate(bbQuestionXML,XPathConstants.NODESET);
			} else {
				return bbQuestionXML.getElementsByTagName(identifier);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml nodes", e);
		}
	}

	/**
	 * Gets the maximum score out of all the answer choice score.
	 */
	protected String getMaximumScore() {
		return Double.toString(Collections.max(answerKeysWithScore.values()));
	}

	/**
	 * Gets the sum score of all the answer choice score.
	 */
	protected String getSumScore() {
		int sum = 0;
		for (Double value : answerKeysWithScore.values()) {
			sum += value;
		}
		return Integer.toString(sum);
	}

	protected int getAnswerChoiceLength() {
		return answerKeysWithScore.size();
	}

	protected void removeAllChild(Node node) {
		while (node.hasChildNodes()){
			node.removeChild(node.getFirstChild());
		}
	}

	/**
	 * Updates the blackboard question meta data
	 */
	protected void updateMetadata() {

		String normalMaximumScore = ptbQTIParser.getMaximumScore();

		Node node = getXMLNode("bbmd_assessmenttype", false);
		node.setTextContent(getExportType());

		NodeList sequenceList = getXMLNodes(SEQUENCE, false);
		for (int i = 0; i < sequenceList.getLength(); i++) {
			sequenceList.item(i).setTextContent(
					Integer.toString(questionSequence));
		}

		NodeList questionTypeList = getXMLNodes(QUESTIONTYPE, false);
		for (int i = 0; i < questionTypeList.getLength(); i++) {
			questionTypeList.item(i).setTextContent(currentQuestionType);
		}

		NodeList absolutescoreMaxscoreList = getXMLNodes(
				ABSOLUTESCORE_MAXSCORE, false);
		for (int i = 0; i < absolutescoreMaxscoreList.getLength(); i++) {
			absolutescoreMaxscoreList.item(i)
					.setTextContent(normalMaximumScore);
		}

		NodeList absolutescoreMinscoreList = getXMLNodes(
				ABSOLUTESCORE_MINSCORE, false);
		for (int i = 0; i < absolutescoreMinscoreList.getLength(); i++) {
			absolutescoreMinscoreList.item(i).setTextContent("0");
		}

		NodeList absolutescoreScoreList = getXMLNodes(ABSOLUTESCORE_SCORE,
				false);
		for (int i = 0; i < absolutescoreScoreList.getLength(); i++) {
			absolutescoreScoreList.item(i).setTextContent(
					"0," + normalMaximumScore);
		}
	}

	/**
	 * Updates the question text of blackboard question
	 */
	protected void updateQuestionText() {
		Node matFormattedtext = getXMLNode(MAT_FORMATTEDTEXT,true);
		((Element) matFormattedtext)
				.getElementsByTagName("mat_formattedtext")
				.item(0)
				.appendChild(
						bbQuestionXML.createCDATASection(ptbQTIParser
								.getQuestionText()));
	}

	/**
	 * Abstract method which need to be implemented by derived class
	 */
	abstract Document getBlackboardQuestionXML();

}
