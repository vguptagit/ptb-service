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
 * This <code>BlackboardMultipleResponse</code> is responsible for support black board format for multiple response questions.
 */
public class BlackboardMultipleResponse extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_MultipleResponseQuestion.xml";
	private static final String QUESTIONTYPE = "Multiple Answer";
	private static final String FORMATTEDTEXT_TAG = "mat_formattedtext";
	private static final String RENDER_CHOICE = "/item/presentation/flow/flow/response_lid/render_choice";
	private static final String FLOW_LABEL = "/item/presentation/flow/flow/response_lid/render_choice/flow_label";
	private static final String RESPCONDITION_MAXVALUE = "/item/resprocessing/outcomes/decvar";
	private static final String RESPCONDITION = "/item/resprocessing/respcondition";

	/**
	 * Constructor which takes the download format, sequence, ptbquestionXML as parameters and sets the
	 * format.
	 * 
	 * @param bbFormat
	 * @param sequence
	 * @param ptbQuestionXML
	 */
	public BlackboardMultipleResponse(DownloadFormat bbFormat, int sequence,
			String ptbQuestionXML) {
		super(bbFormat, TEMPLATEPATH, sequence, QUESTIONTYPE, ptbQuestionXML);
	}

	@Override
	Document getBlackboardQuestionXML() {
		updateMetadata();
		updateQuestionText();
		updateAnswerChoices();
		updateResprocessing();
		return bbQuestionXML;
	}

	/*
	 * update Multiple-Response question AnswerChoices
	 */
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
								.getAttributes().getNamedItem("ident")
								.setNodeValue(Integer.toString(i + 1));
						Node child = (Node) ((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0).getFirstChild();
						((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0).removeChild(child);
						((Element) renderChoice)
								.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0)
								.appendChild(
										bbQuestionXML
												.createCDATASection(answerChoicesList
														.get(i).toString()));
					} else {
						newElement.getElementsByTagName("response_label")
								.item(0).getAttributes().getNamedItem("ident")
								.setNodeValue(Integer.toString(i + 1));
						NodeList feedback = newElement
								.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0).getChildNodes();
						if (feedback.item(0) instanceof CharacterData) {
							CharacterData child = (CharacterData) feedback
									.item(0);
							newElement.getElementsByTagName(FORMATTEDTEXT_TAG)
									.item(0).removeChild(child);
						}
						newElement
								.getElementsByTagName(FORMATTEDTEXT_TAG)
								.item(0)
								.appendChild(
										bbQuestionXML
												.createCDATASection(answerChoicesList
														.get(i).toString()));
						renderChoice.appendChild(newElement);
					}
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating Answer Choices", e);
		}
	}

	// update Resprocessing element
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
				if (key.equals("correct")) {// NOSONAR
					int j = 1;
					for (Entry<String, Double> entry : answerKeysWithScore
							.entries()) {
						Element varequal = bbQuestionXML
								.createElement("varequal");
						varequal.setAttribute("case", "No");
						varequal.setAttribute("respident", "response");
						varequal.setTextContent(Integer.toString(j));

						if (entry.getValue() == 0) {
							Element not = bbQuestionXML.createElement("not");
							((Element) currentItem).getElementsByTagName("and")
									.item(0).appendChild(not)
									.appendChild(varequal);
						} else {
							((Element) currentItem).getElementsByTagName("and")
									.item(0).appendChild(varequal);
						}
						j++;
					}
				}
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while updating response processing", e);
		}
	}

}
