package com.pearson.ptb.download;

import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.pearson.ptb.bean.DownloadFormat;
/**
 * This <code>BlackboardFillInBlank</code> is responsible for support black
 * board format for fill in the blank questions.
 */
public class BlackboardFillInBlank extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_FillInBlankQuestion.xml";
	private static final String QUESTIONTYPE = "Fill in the Blank";
	private static final String RESPCONDITION_MAXVALUE = "/item/resprocessing/outcomes/decvar";
	private static final String ITEMFEEDBACK = "//itemfeedback[@ident=\"\"]";
	private static final String TITLE = "title";
	private static final String RESPONSE = "RESPONSE_";
	private static final String RESPROCESSING_RESPCONDITION = "//resprocessing/respcondition[@title=\"\"]";
	private static final String RESPROCESSING_RESPCONDITION_INCORRECTTITLE = "//resprocessing/respcondition[@title=\"incorrect\"]";

	/**
	 * Constructor which takes the download format, sequence, ptbquestionXML as
	 * parameters and sets the format.
	 * 
	 * @param bbFormat
	 * @param sequence
	 * @param ptbQuestionXML
	 */
	public BlackboardFillInBlank(DownloadFormat bbFormat, int sequence,
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

		Node resprocessingMaxvalue = getXMLNode(RESPCONDITION_MAXVALUE, true);
		resprocessingMaxvalue.getAttributes().getNamedItem("maxvalue")
				.setNodeValue(this.getSumScore());

		Node renderChoice = getXMLNode("resprocessing", false);
		Node respcondition = getXMLNode(RESPROCESSING_RESPCONDITION, true);
		Node respconditionIncorrect = getXMLNode(
				RESPROCESSING_RESPCONDITION_INCORRECTTITLE, true);

		int i = 1;
		for (Entry<String, Double> entry : answerKeysWithScore.entries()) {
			if (i == 1) {
				respcondition.getAttributes().getNamedItem(TITLE)
						.setNodeValue(RESPONSE + i);
				Node varequal = ((Element) respcondition)
						.getElementsByTagName("varequal").item(0);
				varequal.getAttributes().getNamedItem("respident")
						.setNodeValue(Integer.toString(i));
				varequal.setTextContent(entry.getKey());
			} else {
				Node newRespCondition = respcondition.cloneNode(true);

				newRespCondition.getAttributes().getNamedItem(TITLE)
						.setNodeValue(RESPONSE + i);
				Node varequal = ((Element) newRespCondition)
						.getElementsByTagName("varequal").item(0);
				varequal.getAttributes().getNamedItem("respident")
						.setNodeValue(Integer.toString(i));
				varequal.setTextContent(entry.getKey());
				renderChoice.insertBefore(newRespCondition,
						respconditionIncorrect);
			}
			i = i + 1;
		}

	}

	
	private void updateItemfeedback() {
		Node itemfeedback = getXMLNode(ITEMFEEDBACK, true);
		Node itemNode = getXMLNode("item", false);

		for (int i = 0; i < getAnswerChoiceLength(); i++) {
			if (i == 0) {
				itemfeedback.getAttributes().getNamedItem("ident")
						.setNodeValue(RESPONSE + (i + 1));
			} else {
				Node newItemfeedback = itemfeedback.cloneNode(true);
				newItemfeedback.getAttributes().getNamedItem("ident")
						.setNodeValue(RESPONSE + (i + 1));
				itemNode.appendChild(newItemfeedback);
			}
		}

	}
}
