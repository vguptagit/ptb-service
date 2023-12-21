package com.pearson.ptb.download;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.framework.exception.InternalException;

/**
 * This <code>BlackboardTrueFalse</code> is responsible for support black board
 * format for true false questions.
 */
public class BlackboardTrueFalse extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_TrueFalseQuestion.xml";
	private static final String QUESTIONTYPE = "True/False";
	private static final String RESPCONDITION_MAXVALUE = "/item/resprocessing/outcomes/decvar";
	private static final String RESPCONDITION = "/item/resprocessing/respcondition";
	private static final String ITEMFEEDBACK = "/item/itemfeedback";
	private static final String MATTEXT = "mattext";
	private static final String IDENT_1 = "1";
	private static final String IDENT_2 = "1";

	/**
	 * Constructor which expects blackboard format i.e. Pool manager and Test
	 * manager. Sequence of the question which need to be updated in question
	 * meta data. Question type of the question(i.e
	 * TrueFalse/Essay/MultipleChoice/MultipleResponse/FillInBlank) which need
	 * to be updated in question meta data. Question XML which is 2.1 standard
	 * from which blackboard QTI XML need to be generated
	 */
	public BlackboardTrueFalse(DownloadFormat bbFormat, int sequence,
			String ptbQuestionXML) {
		super(bbFormat, TEMPLATEPATH, sequence, QUESTIONTYPE, ptbQuestionXML);
	}

	@Override
	Document getBlackboardQuestionXML() {
		updateMetadata();
		updateQuestionText();
		updateAnswerChoices();
		updateResprocessing();
		updateItemfeedback();

		return bbQuestionXML;
	}
	/*
	 * Updating answer choice
	 */
	private void updateAnswerChoices() {
		try {
			NodeList responselabels = getXMLNodes("response_label", false);

			List<String> answerChoicesList = ptbQTIParser.getAnswerChoices();
			for (int i = 0; i < responselabels.getLength(); i++) {
				Node currentItem = responselabels.item(i);
				String key = currentItem.getAttributes().getNamedItem("ident")
						.getNodeValue();

				if (IDENT_1.equals(key)) {
					((Element) currentItem).getElementsByTagName(MATTEXT)
							.item(0).setTextContent(
									answerChoicesList.get(i).toString());
				} else if (IDENT_2.equals(key)) {
					((Element) currentItem).getElementsByTagName(MATTEXT)
							.item(0).setTextContent(
									answerChoicesList.get(i).toString());
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating answer choices", e);
		}
	}

	/*
	 * Updating response processing
	 */
	private void updateResprocessing() {
		try {
			Node resprocessingMaxvalue = getXMLNode(RESPCONDITION_MAXVALUE,
					true);
			resprocessingMaxvalue.getAttributes().getNamedItem("maxvalue")
					.setNodeValue(this.getMaximumScore());

			NodeList respconditions = getXMLNodes(RESPCONDITION, true);
			for (int i = 0; i < respconditions.getLength(); i++) {
				Node currentItem = respconditions.item(i);
				String key = currentItem.getAttributes().getNamedItem("title")
						.getNodeValue();

				if ("correct".equals(key)) {
					((Element) currentItem).getElementsByTagName("varequal")
							.item(0)
							.setTextContent(ptbQTIParser.getAnswerKeys());
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating response processing", e);
		}
	}

	/*
	 * Updating feedback
	 */
	private void updateItemfeedback() {
		NodeList itemfeedbacks;
		try {
			NodeList responseLabels = getXMLNodes("response_label", false);
			itemfeedbacks = ((NodeList) xpath.evaluate(ITEMFEEDBACK,
					bbQuestionXML, XPathConstants.NODESET));
			for (int i = 0; i < itemfeedbacks.getLength(); i++) {
				Node currentItem = itemfeedbacks.item(i);
				String answerChoice = ((Element) responseLabels.item(i))
						.getElementsByTagName("mattext").item(0)
						.getTextContent();
				((Element) currentItem)
						.getElementsByTagName("mat_formattedtext").item(0)
						.appendChild(bbQuestionXML.createCDATASection(
								answerChoiceFeedback(answerChoice)));
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating Item feedback", e);
		}
	}

	/*
	 * getting the feedback for a answer choice
	 */
	private String answerChoiceFeedback(String answer) {
		Map<String, String> answerChoicesWithFeedback = ptbQTIParser
				.getAnswerChoicesWithFeedback();
		String feedback = "";
		for (Entry<String, String> entry : answerChoicesWithFeedback
				.entrySet()) {
			if (entry.getKey().equals(answer)) {
				feedback = entry.getValue();
			}
		}
		return feedback;
	}

}
