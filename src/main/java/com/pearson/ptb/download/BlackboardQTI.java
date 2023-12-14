package com.pearson.ptb.download;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.framework.QTIParser;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.QuestionTypes;
/**
 * This <code>BlackboardQTI</code> is responsible for support black board QTI format for all type of questions.
 */
public class BlackboardQTI {

	private static XPath xpath;
	DownloadInfo testInfo;
	DownloadFormat format;
	QTIParser qtiParser;
	
	/**
	 * Constructor which expects blackboard format i.e. Pool manager and Test
	 * manager. downloadInfo which contains information of the test title print settings and questions.
	 */
	public BlackboardQTI(DownloadFormat bbFormat, DownloadInfo downloadInfo) {
		testInfo = downloadInfo;
		format = bbFormat;
		qtiParser = new QTIParser();
		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();
		}
	}
	/**
	 * This method will get the test XML from the test template
	 * 
	 * @return XML document
	 */
	public Document getTestXML() {
		Document testXML = getTemplateXML();
		updateTestXML(testXML);
		addQuestionsToTestXML(testXML);
		return testXML;
	}

	private void updateTestXML(Document testXML) {
		Node assessment = getXMLNode(testXML, "assessment", false);
		((org.w3c.dom.Element) assessment).setAttribute("title",
				testInfo.getTestTitle());
		NodeList assessmentTypeNodes = getXMLNodes(testXML,
				"bbmd_assessmenttype", false);
		for (int i = 0; i < assessmentTypeNodes.getLength(); i++) {
			assessmentTypeNodes.item(i).setTextContent(getExportType());
		}
	}

	private void addQuestionsToTestXML(Document testXML) {

		int i = 1;
		Document questionXML = null;
		for (Object question : testInfo.getQuestions()) {
			qtiParser.setXMLDocument((String) question);
			QuestionTypes questionType = qtiParser.getQuestionType();
			questionXML = getConverter(questionType, i, (String) question)
					.getBlackboardQuestionXML();

			NodeList itemList = questionXML.getElementsByTagName("item");
			Element itemElement = (Element) itemList.item(0);
			Node copiedNode = testXML.importNode(itemElement, true);
			NodeList nList = testXML.getElementsByTagName("section");
			NodeList sectionMetadataNodeList = testXML
					.getElementsByTagName("sectionmetadata");
			Node sectionMetadataNode = sectionMetadataNodeList.item(0);
			Element element1 = (Element) nList.item(0);
			element1.insertBefore(copiedNode, sectionMetadataNode);
			i++;
		}

	}

	private BlackboardQTIConvert getConverter(QuestionTypes questionType,
			int sequence, String ptbQuestionXML) {
		BlackboardQTIConvert blackboardQTIConvert = null;
		switch (questionType) {
		case ESSAY:
			blackboardQTIConvert = new BlackboardEssay(format, sequence,
					ptbQuestionXML);
			break;
		case TRUEFALSE:
			blackboardQTIConvert = new BlackboardTrueFalse(format, sequence,
					ptbQuestionXML);
			break;
		case FILLINBLANKS:
			blackboardQTIConvert = new BlackboardFillInBlank(format, sequence,
					ptbQuestionXML);
			break;
		case MATCHING:
			blackboardQTIConvert = new BlackboardMatching(format, sequence,
					ptbQuestionXML);
			break;
		case MULTIPLECHOICE:
			blackboardQTIConvert = new BlackboardMultipleChoice(format,
					sequence, ptbQuestionXML);
			break;
		case MULTIPLERESPONSE:
			blackboardQTIConvert = new BlackboardMultipleResponse(format,
					sequence, ptbQuestionXML);
			break;
		default:
			break;
		}
		return blackboardQTIConvert;
	}

	private Document getTemplateXML() {
		Document testXML;
		try {
			String templatePath = "/BlackboardTemplates/Blackboard_TestTemplate.xml";
			File inputFile = new File(this.getClass().getResource(templatePath)
					.getPath());
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			testXML = docBuilder.parse(inputFile);
		} catch (Exception e) {
			throw new InternalException(
					"Exception while getting test template", e);
		}
		return testXML;
	}

	private Node getXMLNode(Document testXML, String identifier, boolean byXPath) {
		try {
			if (byXPath) {
				return (Node) xpath.evaluate(identifier, testXML,
						XPathConstants.NODE);
			} else {
				return testXML.getElementsByTagName(identifier).item(0);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml node", e);
		}
	}

	protected NodeList getXMLNodes(Document testXML, String identifier,
			boolean byXPath) {
		try {
			if (byXPath) {
				return (NodeList) xpath.evaluate(identifier, testXML,
						XPathConstants.NODESET);
			} else {
				return testXML.getElementsByTagName(identifier);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml nodes", e);
		}
	}

	private String getExportType() {
		String exportType = "";
		if (format == DownloadFormat.bbpm) {
			exportType = "Pool";
		}else if (format == DownloadFormat.bbtm) {
			exportType = "Test";
		}

		return exportType;
	}

}
