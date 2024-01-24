package com.pearson.ptb.download;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This <code>BlackboardEssay</code> is responsible for support black board
 * format for essay questions.
 */
public class BlackboardEssay extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_EssayQuestion.xml";
	private static final String QUESTIONTYPE = "Essay";
	private static final String RESPCONDITION_MAXVALUE = "/item/resprocessing/outcomes/decvar";

	/**
	 * Constructor which takes the download format, sequence, ptbquestionXML as
	 * parameters and sets the format.
	 * 
	 * @param bbFormat
	 * @param sequence
	 * @param ptbQuestionXML
	 */
	public BlackboardEssay(DownloadFormat bbFormat, int sequence,
			String ptbQuestionXML) {
		super(bbFormat, TEMPLATEPATH, sequence, QUESTIONTYPE, ptbQuestionXML);
	}

	/*
	 * Abstract method of super class is implemented. This will return the
	 * blackboard question XML
	 * 
	 * @see
	 * com.pearson.mytest.download.BlackboardQTIConvert#getBlackboardQuestionXML
	 * ()
	 */
	@Override
	Document getBlackboardQuestionXML() {
		updateMetadata();
		updateQuestionText();
		updateResprocessing();
		updateItemfeedback();
		return bbQuestionXML;
	}

	
	private void updateResprocessing() {
		try {
			Node resprocessingMaxvalue = getXMLNode(RESPCONDITION_MAXVALUE,
					true);
			resprocessingMaxvalue.getAttributes().getNamedItem("maxvalue")
					.setNodeValue(ptbQTIParser.getMaximumScore());

		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating response processing", e);
		}
	}

	
	private void updateItemfeedback() {
		try {
			XPathExpression expr = xpath
					.compile("//itemfeedback[@ident=\"solution\"]");
			Node recommendedAnswer = (Node) expr.evaluate(bbQuestionXML,
					XPathConstants.NODE);
			((Element) recommendedAnswer)
					.getElementsByTagName("mat_formattedtext").item(0)
					.appendChild(bbQuestionXML.createCDATASection(
							ptbQTIParser.getRecommendedAnswer()));
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating Item feedback", e);
		}

	}
}
