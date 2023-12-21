package com.pearson.ptb.download;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Multimap;
import com.pearson.ptb.bean.DownloadFormat;
/**
 * This <code>BlackboardMatching</code> is responsible for support black board
 * format for matching questions.
 */
public class BlackboardMatching extends BlackboardQTIConvert {

	private static final String TEMPLATEPATH = "/BlackboardTemplates/Template_MatchingQuestion.xml";
	private static final String QUESTIONTYPE = "Matching";
	private static final String FLOW_RESPONSE_BLOCK = "//flow[@class=\"RESPONSE_BLOCK\"]";
	private static final String FLOW_RIGHT_MATCH_BLOCK = "//flow[@class=\"RIGHT_MATCH_BLOCK\"]";
	private static final String RESPCONDITION_MAXVALUE = "//item/resprocessing/outcomes/decvar";
	private static final String MAT_FORMATTEDTEXT = "mat_formattedtext";
	private static final String RESPONSE_LID = "response_lid";
	private static final Integer ASCII_CHAR = 65;
	private static final String RESPROCESSING_SCOREMODEL_SUMOFSCORES = "//resprocessing[@scoremodel=\"SumOfScores\"]";
	private static final String RESPROCESSING_RESPCONDITION_INCORRECTTITLE = "//resprocessing/respcondition[@title=\"incorrect\"]";

	/**
	 * Constructor which takes the download format, sequence, ptbquestionXML as
	 * parameters and sets the format.
	 * 
	 * @param bbFormat
	 * @param sequence
	 * @param ptbQuestionXML
	 */
	public BlackboardMatching(DownloadFormat bbFormat, int sequence,
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
		Multimap<String, String> answerChoicesShuffled = ptbQTIParser
				.getAnswerChoicesForMatching(true);
		updateMetadata();
		updateQuestionText();
		updateAnswerChoice(answerChoicesShuffled);
		updateResprocessing(answerChoicesShuffled);
		return bbQuestionXML;
	}

	/*
	 * Updating answer choice i.e answer choice and matching answers
	 */
	private void updateAnswerChoice(Multimap<String, String> answerChoices) {
		createResponseBlock(answerChoices);
		createRightMatchBlock(answerChoices);
	}

	private void createResponseBlock(Multimap<String, String> answerChoices) {
		Node flowResponseBlock = getXMLNode(FLOW_RESPONSE_BLOCK, true);
		Node flowBlock = ((Element) flowResponseBlock)
				.getElementsByTagName("flow").item(0);

		int i = 1;
		for (Map.Entry<String, String> entry : answerChoices.entries()) {
			if (i == 1) {
				((Element) flowBlock).getElementsByTagName(MAT_FORMATTEDTEXT)
						.item(0).appendChild(bbQuestionXML
								.createCDATASection(entry.getKey()));
				createResponseLabel(((Element) flowBlock)
						.getElementsByTagName(RESPONSE_LID).item(0), i,
						answerChoices.size());

			} else {
				Node newFlowBlock = flowBlock.cloneNode(true);
				Node matTestNode = ((Element) newFlowBlock)
						.getElementsByTagName(MAT_FORMATTEDTEXT).item(0);
				removeAllChild(matTestNode);
				matTestNode.appendChild(
						bbQuestionXML.createCDATASection(entry.getKey()));
				createResponseLabel(
						((Element) newFlowBlock)
								.getElementsByTagName(RESPONSE_LID).item(0),
						i, answerChoices.size());
				flowResponseBlock.appendChild(newFlowBlock);
			}
			i = i + 1;
		}
	}

	private void createResponseLabel(Node responseLid, int index,
			int answerChoiceLength) {
		responseLid.getAttributes().getNamedItem("ident")
				.setNodeValue(Integer.toString(index));
		Node responseLabel = ((Element) responseLid)
				.getElementsByTagName("response_label").item(0);
		Node flowLabel = ((Element) responseLid)
				.getElementsByTagName("flow_label").item(0);
		removeAllChild(flowLabel);
		String chart = Character.toString((char) (ASCII_CHAR + index - 1));
		for (int i = 0; i < answerChoiceLength; i++) {
			Node newResponseLabel = responseLabel.cloneNode(true);
			newResponseLabel.getAttributes().getNamedItem("ident")
					.setNodeValue(chart + Integer.toString(i + 1));
			flowLabel.appendChild(newResponseLabel);
		}
	}

	private void createRightMatchBlock(Multimap<String, String> answerChoices) {
		Node flowRightMatchBlock = getXMLNode(FLOW_RIGHT_MATCH_BLOCK, true);
		Node flowBlock = ((Element) flowRightMatchBlock)
				.getElementsByTagName("flow").item(0);

		int i = 1;
		for (Map.Entry<String, String> entry : answerChoices.entries()) {
			if (i == 1) {
				((Element) flowBlock).getElementsByTagName(MAT_FORMATTEDTEXT)
						.item(0).appendChild(bbQuestionXML
								.createCDATASection(entry.getValue()));
			} else {
				Node newFlowBlock = flowBlock.cloneNode(true);
				Node matTestNode = ((Element) newFlowBlock)
						.getElementsByTagName(MAT_FORMATTEDTEXT).item(0);
				removeAllChild(matTestNode);
				matTestNode.appendChild(
						bbQuestionXML.createCDATASection(entry.getValue()));
				flowRightMatchBlock.appendChild(newFlowBlock);
			}
			i = i + 1;
		}
	}

	/*
	 * Updating response processing
	 */
	private void updateResprocessing(Multimap<String, String> answerChoices) {
		Node resprocessing = getXMLNode(RESPROCESSING_SCOREMODEL_SUMOFSCORES,
				true);
		Node resprocessingMaxvalue = getXMLNode(RESPCONDITION_MAXVALUE, true);
		resprocessingMaxvalue.getAttributes().getNamedItem("maxvalue")
				.setNodeValue(this.getSumScore());
		Node respCondition = getXMLNode("respcondition", false);
		Node respIncorrectCondition = getXMLNode(
				RESPROCESSING_RESPCONDITION_INCORRECTTITLE, true);
		int i = 0;
		for (Map.Entry<String, String> entryShuffled : answerChoices
				.entries()) {
			String chart = Character.toString((char) (ASCII_CHAR + i))
					+ getCorrectAnswerIndex(entryShuffled.getKey(),
							answerChoices);
			if (i == 0) {
				Node varequal = ((Element) respCondition)
						.getElementsByTagName("varequal").item(0);
				varequal.getAttributes().getNamedItem("respident")
						.setNodeValue(Integer.toString(i + 1));
				varequal.setTextContent(chart);
			} else {
				Node newRespCondition = respCondition.cloneNode(true);
				Node varequal = ((Element) newRespCondition)
						.getElementsByTagName("varequal").item(0);
				varequal.getAttributes().getNamedItem("respident")
						.setNodeValue(Integer.toString(i + 1));
				varequal.setTextContent(chart);
				resprocessing.insertBefore(newRespCondition,
						respIncorrectCondition);
			}
			i++;
		}
	}

	private String getCorrectAnswerIndex(String leftAnswerChoice,
			Multimap<String, String> answerChoices) {
		Multimap<String, String> answerChoicesUnShuffled = ptbQTIParser
				.getAnswerChoicesForMatching(false);
		String rightAnswerChoice = "";

		for (Map.Entry<String, String> entryUnShuffled : answerChoicesUnShuffled
				.entries()) {
			if (entryUnShuffled.getKey().equals(leftAnswerChoice)) {
				rightAnswerChoice = entryUnShuffled.getValue();
				break;
			}

		}
		int i = 1;
		for (Map.Entry<String, String> entryShuffled : answerChoices
				.entries()) {
			if (entryShuffled.getValue().equals(rightAnswerChoice)) {
				break;
			}
			i++;
		}
		return Integer.toString(i);
	}

}
