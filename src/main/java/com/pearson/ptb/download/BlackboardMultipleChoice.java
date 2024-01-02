package com.pearson.ptb.download;

import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This <code>BlackboardMultipleChoice</code> is responsible for support black
 * board format for multiple choice questions.
 */
public class BlackboardMultipleChoice extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_MultipleChoiceQuestion.xml";
	private static final String QUESTIONTYPE = "Multiple Choice";
	private static final String INDENT = "ident";
	private static final String FORMATTEDTEXT_TAG = "mat_formattedtext";
	private static final String RENDER_CHOICE = "/item/presentation/flow/flow/response_lid/render_choice";
	private static final String FLOW_LABEL = "/item/presentation/flow/flow/response_lid/render_choice/flow_label";
	private static final String ITEMFEEDBACK = "/item/itemfeedback";
	private static final String ITEM = "/item";
	private static final String RESPCONDITION_MAXVALUE = "/item/resprocessing/outcomes/decvar";
	private static final String RESPCONDITION = "/item/resprocessing/respcondition";
	private static final int FEEDBACK_INDEX = 2;

	/**
	 * Constructor which takes the download format, sequence, ptbquestionXML as
	 * parameters and sets the format.
	 * 
	 * @param bbFormat
	 * @param sequence
	 * @param ptbQuestionXML
	 */
	public BlackboardMultipleChoice(DownloadFormat bbFormat, int sequence,
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

	// update Multiple-Choice question AnswerChoices
	private void updateAnswerChoices() {
		try {
			Node renderChoice = getXMLNode(RENDER_CHOICE, true);
			Node responseLabel = getXMLNode(FLOW_LABEL, true);

			List<String> answerChoicesList = ptbQTIParser.getAnswerChoices();
			for (int i = 0; i < answerChoicesList.size(); i++) {
				Element newElement = (Element) responseLabel.cloneNode(true);
				if (renderChoice instanceof Element) {// NOSONAR
					if (i == 0) {
						((Element) renderChoice)
								.getElementsByTagName("response_label").item(0)
								.getAttributes().getNamedItem(INDENT)
								.setNodeValue(Integer.toString(i + 1));
						Node child = (Node) ((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.getFirstChild();
						((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.removeChild(child);
						((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.appendChild(bbQuestionXML.createCDATASection(
										answerChoicesList.get(i).toString()));
					} else {
						newElement.getElementsByTagName("response_label")
								.item(0).getAttributes().getNamedItem(INDENT)
								.setNodeValue(Integer.toString(i + 1));
						NodeList feedback = newElement
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.getChildNodes();
						if (feedback.item(0) instanceof CharacterData) {
							CharacterData child = (CharacterData) feedback
									.item(0);
							newElement.getElementsByTagName(FORMATTEDTEXT_TAG)
									.item(0).removeChild(child);
						}
						newElement.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0)
								.appendChild(bbQuestionXML.createCDATASection(
										answerChoicesList.get(i).toString()));
						renderChoice.appendChild(newElement);
					}
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating Answer Choices", e);
		}
	}

	// update Resproces element
	private void updateResprocessing() {
		try {
			Node resprocessingmaxvalue = getXMLNode(RESPCONDITION_MAXVALUE,
					true);
			resprocessingmaxvalue.getAttributes().getNamedItem("maxvalue")
					.setNodeValue(this.getMaximumScore());

			NodeList respconditions = getXMLNodes(RESPCONDITION, true);
			for (int i = 0; i < respconditions.getLength(); i++) {
				Node currentItem = respconditions.item(i);
				String key = currentItem.getAttributes().getNamedItem("title")
						.getNodeValue();
				if ("correct".equals(key)) {
					((Element) currentItem).getElementsByTagName("varequal")
							.item(0).setTextContent(
									Integer.toString(getMaximumScoreIndex()));
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating response processing", e);
		}
	}

	// To get index of the Answer choice.
	private int getMaximumScoreIndex() {
		String maxScore = getMaximumScore();
		int i = 1;
		for (Entry<String, Double> entry : answerKeysWithScore.entries()) {
			if (Double.toString(entry.getValue()).equals(maxScore)) {
				return i;
			}
			i++;
		}
		return i;
	}

	// update Multiple-Choice Itemfeedback
	private void updateItemfeedback() {
		try {
			Node rootNode = getXMLNode(ITEM, true);

			NodeList itemfeedbacks = getXMLNodes(ITEMFEEDBACK, true);
			Node itemfeedback = itemfeedbacks.item(FEEDBACK_INDEX);

			List<String> answerChoicesList = ptbQTIParser.getAnswerChoices();
			for (int i = 0; i < answerChoicesList.size(); i++) {
				Element newElement = (Element) itemfeedback.cloneNode(true);
				if (rootNode instanceof Element) {
					if (i == 0) {
						((Element) itemfeedback).getAttributes()
								.getNamedItem(INDENT)
								.setNodeValue(Integer.toString(i + 1));
						Node child = (Node) ((Element) itemfeedback)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.getFirstChild();
						((Element) itemfeedback)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.removeChild(child);
						((Element) itemfeedback)
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.appendChild(
										bbQuestionXML.createCDATASection(" "));
					} else {
						newElement.getAttributes().getNamedItem(INDENT)
								.setNodeValue(Integer.toString(i + 1));

						NodeList feedback = newElement
								.getElementsByTagName(FORMATTEDTEXT_TAG).item(0)
								.getChildNodes();
						if (feedback.item(0) instanceof CharacterData) {
							CharacterData child = (CharacterData) feedback
									.item(0);
							newElement.getElementsByTagName(FORMATTEDTEXT_TAG)
									.item(0).removeChild(child);
						}
						newElement.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0).appendChild(
										bbQuestionXML.createCDATASection(""));
						rootNode.appendChild(newElement);
					}
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating Item feedback", e);
		}

	}
}
