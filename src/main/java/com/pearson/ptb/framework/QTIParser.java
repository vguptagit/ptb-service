package com.pearson.ptb.framework;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.QuestionTypes;


/**
 * Helper class for word, which has methods to render the qti elements based on
 * section
 *
 */
public class QTIParser {

	private static DocumentBuilderFactory factory;
	private Document xmlDoc;
	private static Transformer xmlToString;
	private static XPath xpath;

	private static final String PARAGRAPH_NODE = "p";
	private static final String PARAGRAPH_OPEN_TAG = "<p>";
	private static final String PARAGRAPH_CLOSE_TAG = "</p>";
	private static final String STRING_EMPTY = "";
	private static final String DOCUMENT_BUILDER_EXCEPTION = "Exception on Document builder navigation";
	private static final String RENDER_ASSESSMENTTITLE_EXCEPTION = "Exception while rendering the AssessmentTitle";
	private static final String SIMPLECHOICE_NODE = "simpleChoice";
	private static final String MATCH_NODE = "match";
	private static final String IDENTIFIER_NODE = "identifier";
	private static final String RESPONSE_IDENTIFIER_NODE = "responseIdentifier";
	private static final String MAPENTRY_TAG = "mapEntry";
	private static final String ASSESSMENT_ITEM = "assessmentItem";
	private static final String MAPKEY_ATTRIBUTE = "mapKey";
	private static final String BASEVALUE_NODE = "baseValue";
	private static final String TRANSFORMATION_EXCEPTION = "Exception in creating New transformer";
	private static final String XML_CONVERSION_EXCEPTION = "Exception on converting xml to document";
	private static final String PARSING_QUESTIONTITLE_EXCEPTION = "Exception in parsing the question title";
	private static final String A_MATCHING_EXPRESSION = "/assessmentItem/itemBody/blockquote/p";
	private static final String B_MATCHING_EXPRESSION = "/assessmentItem/itemBody/blockquote/p/inlineChoiceInteraction[1]/inlineChoice";
	private static final String INLINECHOICEINTERACTION_NODE = "/assessmentItem/itemBody/blockquote/p/inlineChoiceInteraction";
	private static final String CHOICE_PRINT_EXCEPTION = "Exception while printing the choices";
	private static final String CHOICES_EXPRESSION = "/assessmentItem/itemBody/choiceInteraction/simpleChoice";
	private static final String CHOICEINTERACTION_NODE = "/assessmentItem/itemBody/choiceInteraction";
	private static final String CHOICE_INTERACTION_EXPRESSION = "/assessmentItem/itemBody/choiceInteraction";
	private static final String INLINE_INTERACTION_EXPRESSION = "/assessmentItem/itemBody/blockquote/p/inlineChoiceInteraction";

	private static final String RECOMMENDED_ANSWER_FOR_ESSAY = "/assessmentItem/responseDeclaration/correctResponse/value";
	private static final String ANSWER_AREA_FOR_ESSAY = "/assessmentItem/itemBody/extendedTextInteraction";

	private static final int ASCII_CHAR_A = 97;
	private static final int PARAGRAPH_LAST_INDEX = 3;
	private static final String TEXTENTRYINTERACTION = "/assessmentItem/itemBody/blockquote/p/textEntryInteraction";
	private static final String EXTENDEDTEXTINTERACTION = "/assessmentItem/itemBody/extendedTextInteraction";
	private static final String RESPONSE_MAP = "/assessmentItem/responseDeclaration/mapping/mapEntry";
	private static final String RESPONSEDECLARATION_MAPPING_MAPENTRY = "/assessmentItem/responseDeclaration/mapping/mapEntry";
	private static final String RESPONSEDECLARATION = "/assessmentItem/responseDeclaration";
	private static final String MAP_CONTENTS = "/assessmentItem/itemBody/blockquote[1]/p/inlineChoiceInteraction/inlineChoice";
	private static final String RECOMMENDED_ANSWER = "/assessmentItem/responseDeclaration/correctResponse/value";
	private static final String BLOCKQUOTE_NODE = "/assessmentItem/itemBody/blockquote";
	private static final String ITEMBODY_NODE = "/assessmentItem/itemBody";
	private static final String OUTCOMEDECLARATION_IDENTIFIER_MAXSCORE = "//outcomeDeclaration[@identifier=\"MAXSCORE\"]";
	
	public QTIParser() {
		initialize();
	}

	/**
	 * Initialize the necessary utilities required for parsing the xml.
	 * 
	 * @param questionXML
	 *            , question xml which will be in qti2.1
	 * @throws InternalException
	 * @throws ConfigException
	 */
	private void initialize() {
		if (factory == null) {
			factory = DocumentBuilderFactory.newInstance();
		}

		if (xpath == null) {
			xpath = XPathFactory.newInstance().newXPath();
		}

		if (xmlToString == null) {
			try {
				xmlToString = TransformerFactory.newInstance().newTransformer();
			} catch (TransformerConfigurationException e) {
				throw new InternalException(TRANSFORMATION_EXCEPTION, e);
			}

		}

	}

	/**
	 * This will allows to set the QTI XML which need to be parsed.
	 * 
	 * @param questionXML
	 * @throws InternalException
	 * @throws ConfigException
	 */
	public void setXMLDocument(String questionXML) {
		try {
			xmlDoc = factory.newDocumentBuilder().parse(
					new InputSource(new StringReader(questionXML)));
			
		} catch (SAXException | IOException e) {
			throw new InternalException(XML_CONVERSION_EXCEPTION, e);
		} catch (ParserConfigurationException e) {
			throw new ConfigException(XML_CONVERSION_EXCEPTION, e);
		}
	}

	/**
	 * Parses the XML and gets the question text.
	 * 
	 * @return
	 * @throws InternalException
	 */
	public String getQuestionText() {
		try {
			Node node = getXMLNode(PARAGRAPH_NODE,false);

			Transformer t = TransformerFactory.newInstance().newTransformer();
			StringWriter sw = new StringWriter();
			String str = "";

			if (node.getFirstChild() instanceof CharacterData
					|| node.getFirstChild().getNodeName() == "textEntryInteraction"
					&& node.getFirstChild().getNextSibling() instanceof CharacterData) {
				int length = node.getChildNodes().getLength();
				for (int i = 0; i < length; i++) {
					if (node.getChildNodes().item(i) instanceof CharacterData) {
						sw.write(((CharacterData) node.getChildNodes().item(i))
								.getData());
						str = str + sw.toString();
						sw.getBuffer().setLength(0);
					} else if (node.getChildNodes().item(i).getNodeName() == "textEntryInteraction") {
						str = str + getBlank(node.getChildNodes().item(i));
					}
				}
			} else {
				t.transform(new DOMSource(node), new StreamResult(sw));
				str = sw.toString().replaceFirst(PARAGRAPH_OPEN_TAG,
						STRING_EMPTY);

				str = str.substring(0,
						sw.toString().lastIndexOf(PARAGRAPH_CLOSE_TAG)
								- PARAGRAPH_LAST_INDEX);
			}
			return str;

		} catch (Exception e) {
			throw new InternalException(DOCUMENT_BUILDER_EXCEPTION, e);
		}
	}

	/**
	 * Method to get the blank depending the expectedLength attribute of the
	 * textEntryInteraction node
	 * 
	 * @param item
	 * @return
	 */
	private String getBlank(Node item) {
		String expectedLengthValue = item.getAttributes()
				.getNamedItem("expectedLength").getNodeValue();

		int expectedLength = Integer.parseInt(expectedLengthValue);

		String blank = "";

		for (int i = 0; i < expectedLength; i++) {
			blank = blank + "_";
		}
		return blank;
	}

	/**
	 * This will get the correct answers for the question. It fetches the
	 * correct answers depending on question types.
	 * 
	 * @return
	 * @throws InternalException
	 */
	public String getAnswerKeys() {
		QuestionTypes questionType;
		String answerKeys = "";

		try {
			questionType = getQuestionType();

			if (questionType == QuestionTypes.TRUEFALSE
					|| questionType == QuestionTypes.MULTIPLECHOICE) {
				answerKeys = getAnswerKeysForTrueFalse();
			} else if (questionType == QuestionTypes.MULTIPLERESPONSE) {
				answerKeys = getAnswerKeysForMultipleResponse();
			} else if (questionType == QuestionTypes.FILLINBLANKS) {
				answerKeys = getAnswerKeysForFillInTheBlank();
			} else if (questionType == QuestionTypes.MATCHING) {
				answerKeys = getAnswerKeysForMatch();
			} else if (questionType == QuestionTypes.ESSAY) {
				answerKeys = getAnswerKeysForEssay();
			}

		} catch (InternalException e) {
			throw new InternalException(PARSING_QUESTIONTITLE_EXCEPTION, e);
		}

		return answerKeys;
	}

	/**
	 * Get the correct answers for true false and multiple choice question
	 * 
	 * @return
	 */
	private String getAnswerKeysForTrueFalse() {
		NodeList answerChoiseNodes = xmlDoc
				.getElementsByTagName(SIMPLECHOICE_NODE);
		NodeList responseMatchNodes = xmlDoc
				.getElementsByTagName(MATCH_NODE);

		Multimap<Integer, String> answerKeyList = LinkedListMultimap.create();
		if (answerChoiseNodes != null && answerChoiseNodes.getLength() > 0) {
			for (int i = 0; i < answerChoiseNodes.getLength(); i++) {
				Element elAC = (Element) answerChoiseNodes
						.item(i);
				String identifier = elAC.getAttribute(IDENTIFIER_NODE);

				for (int j = 0; j < responseMatchNodes.getLength(); j++) {
					Element elRM = (Element) responseMatchNodes
							.item(j);
					String matchIdentifiew = elRM
							.getElementsByTagName(BASEVALUE_NODE).item(0)
							.getFirstChild().getNodeValue();

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR
						Node scoreNode = elRM.getNextSibling();
						String score = scoreNode.getChildNodes().item(0)
								.getFirstChild().getNodeValue();

						if (Double.parseDouble(score) > 0) {
							if (elAC.getFirstChild() instanceof CharacterData) {
								answerKeyList.put(i, ((CharacterData) elAC
										.getFirstChild()).getData());
							} else {
								answerKeyList.put(i, elAC.getFirstChild()
										.getNodeValue());
							}
						}

					}
				}

			}
		}

		return buildAnswerKeys(answerKeyList);
	}

	private Boolean isIdentifierMatches(String identifier,
			String matchIdentifiew) {
		return identifier.equals(matchIdentifiew);
	}

	/**
	 * Get the correct answers for true false and multiple response question
	 * 
	 * @return
	 * @throws InternalException
	 */
	private String getAnswerKeysForMultipleResponse() {

		Multimap<Integer, String> answerKeyList = LinkedListMultimap.create();

		try {
			NodeList simpleChoiceNode = getXMLNodes(CHOICES_EXPRESSION,true);

			NodeList responseMap = getXMLNodes(RESPONSE_MAP,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(IDENTIFIER_NODE);

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM
							.getAttribute(MAPKEY_ATTRIBUTE);

					if (isIdentifierMatches(identifier, matchIdentifiew)
							&& Double.parseDouble(elRM
									.getAttribute("mappedValue")) > 0) {// NOSONAR
						if (elAC.getFirstChild() instanceof CharacterData) {
							answerKeyList.put(i, ((CharacterData) elAC
									.getFirstChild()).getData());
						} else {
							answerKeyList.put(i, elAC.getFirstChild()
									.getNodeValue());
						}
					}
				}

			}

		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}

		return buildAnswerKeys(answerKeyList);
	}

	/**
	 * Get the correct answers for true false and fill in the blank question
	 * 
	 * @return
	 * @throws InternalException
	 */

	private String getAnswerKeysForFillInTheBlank() {
		NodeList simpleChoiceNode;
		NodeList responseMap;
		StringBuilder answerKeys = new StringBuilder();

		try {
			simpleChoiceNode = getXMLNodes(TEXTENTRYINTERACTION,true); 
			responseMap = getXMLNodes(RESPONSEDECLARATION,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(RESPONSE_IDENTIFIER_NODE)
						.trim();

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM.getAttribute(IDENTIFIER_NODE)
							.trim();

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR

						String answerKey = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute(MAPKEY_ATTRIBUTE);

						if (answerKeys.toString().isEmpty()) {
							answerKeys.append(answerKey);
						} else {
							answerKeys.append(", ").append(answerKey);
						}

					}
				}

			}
		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}

		return answerKeys.toString();
	}

	/**
	 * Get the correct answers for true false and matching question
	 * 
	 * @return
	 * @throws InternalException
	 */
	private String getAnswerKeysForMatch() {

		Multimap<Integer, String> answerKeyList = LinkedListMultimap.create();
		NodeList simpleChoiceNode, responseMap, rightContents;
		try {
			simpleChoiceNode = getXMLNodes(INLINE_INTERACTION_EXPRESSION,true);

			responseMap =  getXMLNodes(RESPONSEDECLARATION,true);

			rightContents = getXMLNodes(MAP_CONTENTS,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(RESPONSE_IDENTIFIER_NODE);

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM.getAttribute(IDENTIFIER_NODE);

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR

						String answerKey = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute(MAPKEY_ATTRIBUTE);

						for (int k = 0; k < rightContents.getLength(); k++) {

							Element elRC = (Element) rightContents
									.item(k);

							String respidentifier = elRC
									.getAttribute(IDENTIFIER_NODE);

							if (respidentifier.equals(answerKey)) {
								if (elRC.getFirstChild() instanceof CharacterData) {
									answerKeyList.put(k, ((CharacterData) elRC
											.getFirstChild()).getData());
								} else {
									answerKeyList.put(k, elRC.getFirstChild()
											.getNodeValue());
								}

							}
						}

					}
				}

			}
		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}

		return buildAnswerKeys(answerKeyList);

	}

	/*
	 * Getting the recommended answer for answer key.
	 */
	private String getAnswerKeysForEssay() {
		String recommendedAnswer = "";
		try {
			Node recommendedAnswerForEssay = getXMLNode(RECOMMENDED_ANSWER_FOR_ESSAY,true);

			StringWriter sw = new StringWriter();

			if (recommendedAnswerForEssay.getFirstChild() instanceof CharacterData) {
				recommendedAnswer = ((CharacterData) recommendedAnswerForEssay
						.getFirstChild()).getData();
			} else {
				Transformer t = TransformerFactory.newInstance()
						.newTransformer();
				t.transform(new DOMSource(recommendedAnswerForEssay),
						new StreamResult(sw));
				recommendedAnswer = sw.toString();
			}

		} catch (Exception e) {
			throw new InternalException(
					"Exception while reading recommended answer for essay", e);
		}

		return recommendedAnswer;
	}

	/**
	 * Appends the correct answer keys to string builder with answer index
	 * 
	 * @param answerKeyList
	 * @return
	 */
	private String buildAnswerKeys(Multimap<Integer, String> answerKeyList) {
		StringBuilder answerKeys = new StringBuilder();
		if (answerKeyList.size() > 1) {
			for (Map.Entry<Integer, String> answerKey : answerKeyList.entries()) {
				if (answerKeys.toString().isEmpty()) {
					answerKeys.append(convertToLetter(answerKey.getKey()));
					answerKeys.append(answerKey.getValue());
				} else {
					answerKeys.append(", ");
					answerKeys.append(convertToLetter(answerKey.getKey()));
					answerKeys.append(answerKey.getValue());
				}
			}
		} else {
			for (Map.Entry<Integer, String> answerKey : answerKeyList.entries()) {
				answerKeys.append(answerKey.getValue());
			}
		}

		return answerKeys.toString();
	}

	/**
	 * Gets the answer choices for matching question.
	 * 
	 * @return
	 * @throws InternalException
	 */
	public Multimap<String, String> getAnswerChoicesForMatching(boolean shuffleAnswer) {
			if(shuffleAnswer){
				return getAnswerChoicesForMatchingWithShuffle();
			}else{
				return getAnswerChoicesForMatchingWithoutShuffle();
			}
	}
	
	private Multimap<String, String> getAnswerChoicesForMatchingWithShuffle(){
		Multimap<String, String> twoColumnAnswerChoices = LinkedListMultimap.create();
		NodeList rightContents = null;
		NodeList leftContents = null;
				
		try {
			shuffleMatchingRightSideAnswers();	
			
			leftContents = getXMLNodes(A_MATCHING_EXPRESSION,true);
			rightContents = getXMLNodes(B_MATCHING_EXPRESSION,true);
			
			for (int i = 0; i < leftContents.getLength(); i++) {
				getMatchingAnswerShuffled(twoColumnAnswerChoices,
						rightContents, leftContents, i);

			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting answer choices For matching Question with shuffle", e);
		}
		return twoColumnAnswerChoices;
	}

	private void getMatchingAnswerShuffled(
			Multimap<String, String> twoColumnAnswerChoices,
			NodeList rightContents, NodeList leftContents, int index) {
		String leftContent;
		String rightContent;
		try {

			StreamResult xmlOutputL = new StreamResult(new StringWriter());
			xmlToString.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			xmlToString.transform(new DOMSource(leftContents.item(index)),
					xmlOutputL);

			if (leftContents.item(index).getFirstChild() instanceof CharacterData) {
				leftContent = ((CharacterData) leftContents.item(index)
						.getFirstChild()).getData();
			} else {
				leftContent = xmlOutputL.getWriter().toString();
				leftContent = leftContent.replaceFirst(PARAGRAPH_OPEN_TAG,
						STRING_EMPTY);

				if (leftContent.indexOf("<inlineChoiceInteraction") > -1) {
					leftContent = leftContent
							.substring(0, leftContent
									.indexOf("<inlineChoiceInteraction"));
				}
			}

			StreamResult xmlOutputR = new StreamResult(new StringWriter());
			xmlToString.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			xmlToString.transform(new DOMSource(rightContents.item(index)),
					xmlOutputR);
			if (rightContents.item(index).getFirstChild() instanceof CharacterData) {
				rightContent = ((CharacterData) rightContents.item(index)
						.getFirstChild()).getData();
			} else {
				rightContent = xmlOutputR.getWriter().toString();
			}
			twoColumnAnswerChoices.put(leftContent, rightContent);

		} catch (Exception e) {
			throw new InternalException(CHOICE_PRINT_EXCEPTION, e);
		}
	}
	
	private Multimap<String, String> getAnswerChoicesForMatchingWithoutShuffle(){
		
		Multimap<String, String> twoColumnAnswerChoices = LinkedListMultimap.create();
		NodeList rightContents = null;
		NodeList leftContents = null;
		NodeList responseMap=null;
		String leftContent = null;
		
		try{
			leftContents = getXMLNodes(A_MATCHING_EXPRESSION,true);
			responseMap =  getXMLNodes(RESPONSEDECLARATION,true);
			rightContents = getXMLNodes(MAP_CONTENTS,true);
		
			for (int i = 0; i < leftContents.getLength(); i++) {
				leftContent=getNodeContent(leftContents.item(i));
				String responseIdentifier=((Element)leftContents.item(i)).getElementsByTagName("inlineChoiceInteraction").item(0).getAttributes().getNamedItem("responseIdentifier").getNodeValue();
				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM.getAttribute(IDENTIFIER_NODE);
					if (isIdentifierMatches(responseIdentifier, matchIdentifiew)) { // NOSONAR
						String answerKey = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute(MAPKEY_ATTRIBUTE);
						for (int k = 0; k < rightContents.getLength(); k++) {
							Element elRC = (Element) rightContents
									.item(k);
							String respidentifier = elRC
									.getAttribute(IDENTIFIER_NODE);
							if (respidentifier.equals(answerKey)) {
								if (elRC.getFirstChild() instanceof CharacterData) {
									twoColumnAnswerChoices.put(leftContent,((CharacterData) elRC
											.getFirstChild()).getData());
								} else {
									twoColumnAnswerChoices.put(leftContent,elRC.getFirstChild()
											.getNodeValue());
								}
	
							}
						}
	
					}
				}
			}
			
		}catch (Exception e) {
			throw new InternalException("Exception while getting answer choices For matching Question without shuffle", e);
		}
		
		return twoColumnAnswerChoices;
	}
	
	
	private String getNodeContent(Node node){
		String content="";
		try {
			StreamResult xmlOutputL = new StreamResult(new StringWriter());
			xmlToString.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			xmlToString.transform(new DOMSource(node),xmlOutputL);

			if (node.getFirstChild() instanceof CharacterData) {
				content = ((CharacterData) node
						.getFirstChild()).getData();
			} else {
				content = xmlOutputL.getWriter().toString();
				content = content.replaceFirst(PARAGRAPH_OPEN_TAG,
						STRING_EMPTY);

				if (content.indexOf("<inlineChoiceInteraction") > -1) {
					content = content
							.substring(0, content
									.indexOf("<inlineChoiceInteraction"));
				}
			}
		}catch (Exception e) {
			throw new InternalException(CHOICE_PRINT_EXCEPTION, e);
		}
		
		
		return content;
	}

	/**
	 * Gets the answer choices for questions other than matching questions.
	 * 
	 * @return
	 * @throws InternalException
	 */
	public List<String> getAnswerChoices() {
		List<String> answerChoiceList = new ArrayList<String>();
		NodeList nodelist = null;
		nodelist = getXMLNodes(CHOICES_EXPRESSION,true);

		for (int i = 0; i < nodelist.getLength(); i++) {
			try {
				StringWriter sw = new StringWriter();
				if (nodelist.item(i).getFirstChild() instanceof CharacterData) {
					sw.write(((CharacterData) nodelist.item(i).getFirstChild())
							.getData());
				} else {

					xmlToString.transform(new DOMSource(nodelist.item(i)),
							new StreamResult(sw));
				}
				answerChoiceList.add(sw.toString());

			} catch (Exception e) {
				throw new InternalException(CHOICE_PRINT_EXCEPTION, e);
			}

		}

		return answerChoiceList;
	}

	/**
	 * method to convert the integer index to alphabet index
	 * 
	 * @param index
	 * @return
	 */
	private String convertToLetter(Integer index) {
		return (char) (ASCII_CHAR_A + index) + ".";
	}

	/**
	 * Gets the question type. i.e true false, multiple choice, multiple
	 * response, Matching, bill in the blank, essay
	 * 
	 * @return
	 * @throws InternalException
	 */
	public QuestionTypes getQuestionType() {
		NodeList choiceInteraction;
		NodeList simpleChoiceNode;
		NodeList textEntryInteraction;
		NodeList inlineChoiceInteraction;
		NodeList extendedTextInteraction;
		NodeList multipleResponseAnswerKeys;

		Integer maxChoices;
		QuestionTypes questionType = null;

		try {
			choiceInteraction = getXMLNodes(CHOICE_INTERACTION_EXPRESSION,true);
			simpleChoiceNode = getXMLNodes(CHOICES_EXPRESSION,true);
			textEntryInteraction = getXMLNodes(TEXTENTRYINTERACTION,true);
			inlineChoiceInteraction = getXMLNodes(B_MATCHING_EXPRESSION,true);

			extendedTextInteraction = getXMLNodes(EXTENDEDTEXTINTERACTION,true);

			if (choiceInteraction.getLength() > 0) {
				maxChoices = Integer
						.parseInt(((Element) choiceInteraction
								.item(0)).getAttribute("maxChoices"));

				multipleResponseAnswerKeys = getXMLNodes(RESPONSEDECLARATION_MAPPING_MAPENTRY,true);
				int nodeLength = 2;
				if (maxChoices == 1
						&& multipleResponseAnswerKeys.getLength() == 0) {
					if(simpleChoiceNode.getLength() > nodeLength) {
						questionType = QuestionTypes.MULTIPLECHOICE;
					}else {
						questionType = QuestionTypes.TRUEFALSE;
					}
				}else {
					questionType = QuestionTypes.MULTIPLERESPONSE;
				}

			} else if (textEntryInteraction.getLength() > 0) {
				questionType = QuestionTypes.FILLINBLANKS;
			} else if (inlineChoiceInteraction.getLength() > 0) {
				questionType = QuestionTypes.MATCHING;
			} else if (extendedTextInteraction.getLength() > 0) {
				questionType = QuestionTypes.ESSAY;
			}

		} catch (Exception e) {
			throw new InternalException(PARSING_QUESTIONTITLE_EXCEPTION, e);
		}

		return questionType;

	}

	/**
	 * This will initiate the answer scrambling process.based on the question
	 * type, different method will be called for shuffling answer choices. This
	 * will also rewrite the identity attribute of the QTI XML as this need to
	 * be saved as separate question.
	 * 
	 * @return QTI XML as string
	 */
	public String shuffleAnswerChoices() {
		StringWriter sw = new StringWriter();
		try {
			if (getQuestionType() == QuestionTypes.MULTIPLECHOICE
					|| getQuestionType() == QuestionTypes.TRUEFALSE
					|| getQuestionType() == QuestionTypes.MULTIPLERESPONSE) {
				shuffleMultipleChoiceAnswer();
			} else if (getQuestionType() == QuestionTypes.MATCHING) {
				shuffleMatchingQuestionAnswerChoices();
			}

			Node assessmentItem = getXMLNode(ASSESSMENT_ITEM,false);
			NamedNodeMap attr = assessmentItem.getAttributes();
			Node nodeAttr = attr.getNamedItem(IDENTIFIER_NODE);

			UUID uuid = UUID.randomUUID();
			nodeAttr.setTextContent(uuid.toString());

			xmlToString.transform(new DOMSource(xmlDoc), new StreamResult(sw));
		} catch (Exception e) {
			throw new InternalException(
					"Error while shuffleing Answer Choices", e);
		}

		return sw.toString();

	}

	/**
	 * This will pick answer choices as list and shuffle it and rewrite it.
	 */
	private void shuffleMultipleChoiceAnswer() {

		try {
			NodeList simpleChoiceNode = getXMLNodes(CHOICES_EXPRESSION,true);
			Node choiceinteractionNode = getXMLNode(CHOICEINTERACTION_NODE,true);

			List<Node> answerNodes = new ArrayList<Node>();

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				answerNodes.add(simpleChoiceNode.item(i));
				choiceinteractionNode.removeChild(simpleChoiceNode.item(i));
			}

			Collections.shuffle(answerNodes);

			for (Node node : answerNodes) {

				choiceinteractionNode.appendChild(node);
			}

		} catch (Exception e) {
			throw new InternalException(
					"Error while shuffleing Multiple Choice Answer", e);
		}

	}

	/**
	 * This will shuffle the matching answer choices.List of answer choices
	 * (right side) will be repeated for each option (left side) in XML. So
	 * answer choices will be picked and shuffled and re written to each option.
	 */
	private void shuffleMatchingRightSideAnswers() {

		try {
			NodeList inlineChoiceInteraction = getXMLNodes(INLINECHOICEINTERACTION_NODE,true);

			NodeList rightContents = null;
			List<Node> answerNodes = new ArrayList<Node>();

			for (int i = 0; i < inlineChoiceInteraction.getLength(); i++) {

				rightContents = inlineChoiceInteraction.item(i).getChildNodes();

				if (i == 0) {
					for (int j = 0; j < rightContents.getLength(); j++) {
						answerNodes.add(rightContents.item(j));
					}	
				}
				
				while (inlineChoiceInteraction.item(i).hasChildNodes()){
					inlineChoiceInteraction.item(i).removeChild(inlineChoiceInteraction.item(i).getFirstChild());
				}
				
				if (i == 0) {
					Collections.shuffle(answerNodes);
				}

				for (Node node : answerNodes) {
					inlineChoiceInteraction.item(i).appendChild(
							node.cloneNode(true));
				}
			}

		} catch (Exception e) {
			throw new InternalException(
					"Error while shuffleing matching answer choice", e);
		}

	}
	
	private void shuffleMatchingQuestionAnswerChoices() {
		try {
			NodeList blockquoteNode = getXMLNodes(BLOCKQUOTE_NODE,true);
			Node itembodyNode = getXMLNode(ITEMBODY_NODE,true);

			List<Node> answerNodes = new ArrayList<Node>();

			for (int i = 0; i < blockquoteNode.getLength(); i++) {
				answerNodes.add(blockquoteNode.item(i));
				itembodyNode.removeChild(blockquoteNode.item(i));
			}

			Collections.shuffle(answerNodes);

			for (Node node : answerNodes) {

				itembodyNode.appendChild(node);
			}

		} catch (Exception e) {
			throw new InternalException(
					"Error while shuffleing Multiple Choice Answer", e);
		}
	}

	/*
	 * Getting answer area space for essay question.
	 */
	public int getAnswerAreaForEssay() {

		int lineBreakLength = 0;
		try {
			Node answerAreaForEssay = getXMLNode(ANSWER_AREA_FOR_ESSAY,true);

			lineBreakLength = Integer
					.parseInt(((Element) answerAreaForEssay)
							.getAttribute("expectedLines"));

		} catch (Exception e) {
			throw new InternalException(
					"Error while getting answer area for essay question", e);
		}

		return lineBreakLength;
	}

	/*
	 * Getting the answer choice orientation i.e horizontal or vertical
	 */
	public String getAnswerChoiceOrientation() {
		String answerChoiceOrientation = "";
		try {
			QuestionTypes questionType = getQuestionType();
			if (questionType == QuestionTypes.TRUEFALSE
					|| questionType == QuestionTypes.MULTIPLECHOICE
					|| questionType == QuestionTypes.MULTIPLERESPONSE) {

				Node choiceinteractionNode = getXMLNode(CHOICEINTERACTION_NODE,true);
				answerChoiceOrientation = ((Element) choiceinteractionNode)
						.getAttribute("orientation");

			}

		} catch (Exception e) {
			throw new InternalException(
					"Error while getting answer choice orientation", e);
		}

		return answerChoiceOrientation;
	}
	
	/**
	 * Get the identifier of the assessmentItem tag
	 * @return identifier of type string
	 */
	public String getQTIIdentifier(){
		return getXMLNode(ASSESSMENT_ITEM,false).getAttributes().getNamedItem(IDENTIFIER_NODE).getNodeValue();
	}
	
	/**
	 * Get the tile of the 
	 * @return
	 */
	public String getQTITitle(){
		return getXMLNode(ASSESSMENT_ITEM,false).getAttributes().getNamedItem("title").getNodeValue();
	}
	
	/**
	 * Gets the maximum score.
	 * @return maximum score as staring 
	 */
	public String getMaximumScore(){
		String maximumScore="";
		try {
			Node maximumScoreNode=getXMLNode(OUTCOMEDECLARATION_IDENTIFIER_MAXSCORE,true);
			maximumScore=((Element)maximumScoreNode).getElementsByTagName("value").item(0).getTextContent();
		} catch (Exception e) {
			throw new InternalException(
					"Error while getting maximum score", e);
		}
		return maximumScore;
	}
	
	/**
	 * Gets the score for each answer choice.
	 * @return Map<String, Integer> Answer choice will be key and score will be value.
	 */
	public Multimap<String,Double> getAnswerKeysWithScore(){
		
		QuestionTypes questionType = getQuestionType();
		if (questionType == QuestionTypes.FILLINBLANKS) {
			return getAnswerKeysWithScoreForFIB();
		}else if (questionType == QuestionTypes.MULTIPLERESPONSE){
			return getAnswerKeysWithScoreForMultipleResponse();
		}else if(questionType == QuestionTypes.MATCHING) {
			return getAnswerKeysWithScoreForMatching();
		}else{
			return getAnswerKeysWithScoreForAQuestion();
		}
	}
	
	private Multimap<String,Double> getAnswerKeysWithScoreForAQuestion(){
		NodeList answerChoiseNodes = xmlDoc
				.getElementsByTagName(SIMPLECHOICE_NODE);
		NodeList responseMatchNodes = xmlDoc
				.getElementsByTagName(MATCH_NODE);

		Multimap<String,Double> answerKeyWithScore = LinkedListMultimap.create();
		if (answerChoiseNodes != null && answerChoiseNodes.getLength() > 0) {
			for (int i = 0; i < answerChoiseNodes.getLength(); i++) {
				Element elAC = (Element) answerChoiseNodes
						.item(i);
				String identifier = elAC.getAttribute(IDENTIFIER_NODE);

				for (int j = 0; j < responseMatchNodes.getLength(); j++) {
					Element elRM = (Element) responseMatchNodes
							.item(j);
					String matchIdentifiew = elRM
							.getElementsByTagName(BASEVALUE_NODE).item(0)
							.getFirstChild().getNodeValue();

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR
						Node scoreNode = elRM.getNextSibling();
						String score = scoreNode.getChildNodes().item(0)
								.getFirstChild().getNodeValue();

						if (elAC.getFirstChild() instanceof CharacterData) {
							answerKeyWithScore.put(((CharacterData) elAC
									.getFirstChild()).getData(),Double.parseDouble(score));
						} else {
							answerKeyWithScore.put(elAC.getFirstChild()
									.getNodeValue(),Double.parseDouble(score));
						}
						

					}
				}

			}
		}
		
		return answerKeyWithScore;
	}
	private Multimap<String,Double> getAnswerKeysWithScoreForFIB(){
		NodeList simpleChoiceNode;
		NodeList responseMap;
		Multimap<String,Double> answerKeyWithScore = LinkedListMultimap.create();

		try {
			simpleChoiceNode = getXMLNodes(TEXTENTRYINTERACTION,true);
			responseMap = getXMLNodes(RESPONSEDECLARATION,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(RESPONSE_IDENTIFIER_NODE)
						.trim();

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM.getAttribute(IDENTIFIER_NODE)
							.trim();

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR

						String answerKey = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute(MAPKEY_ATTRIBUTE);
						String answerScore = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute("mappedValue");
						
						answerKeyWithScore.put(answerKey, Double.parseDouble(answerScore));
					}
				}

			}
		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}

		return answerKeyWithScore;
	}
	
	private Multimap<String,Double> getAnswerKeysWithScoreForMultipleResponse(){
		Multimap<String,Double> answerKeyWithScore = LinkedListMultimap.create();

		try {
			NodeList simpleChoiceNode = getXMLNodes(CHOICES_EXPRESSION,true);
			NodeList responseMap = getXMLNodes(RESPONSE_MAP,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(IDENTIFIER_NODE);

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM
							.getAttribute(MAPKEY_ATTRIBUTE);

					if (isIdentifierMatches(identifier, matchIdentifiew)) {// NOSONAR
						if (elAC.getFirstChild() instanceof CharacterData) {
							answerKeyWithScore.put(((CharacterData) elAC.getFirstChild()).getData(),Double.parseDouble(elRM.getAttribute("mappedValue")));
						} else {
							answerKeyWithScore.put(elAC.getFirstChild().getNodeValue(),Double.parseDouble(elRM.getAttribute("mappedValue")));
						}
					}
				}

			}

		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}
		
		return answerKeyWithScore;
	}
	
	private Multimap<String,Double> getAnswerKeysWithScoreForMatching(){
		Multimap<String,Double> answerKeyWithScore = LinkedListMultimap.create();
		
		NodeList simpleChoiceNode, responseMap, rightContents;
		try {
			simpleChoiceNode = getXMLNodes(INLINE_INTERACTION_EXPRESSION,true);
			responseMap = getXMLNodes(RESPONSEDECLARATION,true);
			rightContents = getXMLNodes(MAP_CONTENTS,true);

			for (int i = 0; i < simpleChoiceNode.getLength(); i++) {
				Element elAC = (Element) simpleChoiceNode
						.item(i);
				String identifier = elAC.getAttribute(RESPONSE_IDENTIFIER_NODE);

				for (int j = 0; j < responseMap.getLength(); j++) {
					Element elRM = (Element) responseMap
							.item(j);
					String matchIdentifiew = elRM.getAttribute(IDENTIFIER_NODE);

					if (isIdentifierMatches(identifier, matchIdentifiew)) { // NOSONAR

						String answerKey = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute(MAPKEY_ATTRIBUTE);
						
						String answerValue = ((Element) elRM
								.getElementsByTagName(MAPENTRY_TAG).item(0))
								.getAttribute("mappedValue");

						for (int k = 0; k < rightContents.getLength(); k++) {

							Element elRC = (Element) rightContents
									.item(k);

							String respidentifier = elRC
									.getAttribute(IDENTIFIER_NODE);

							if (respidentifier.equals(answerKey)) {
								if (elRC.getFirstChild() instanceof CharacterData) {
									answerKeyWithScore.put(((CharacterData) elRC
											.getFirstChild()).getData(),Double.parseDouble(answerValue));
								} else {
									answerKeyWithScore.put(elRC.getFirstChild()
											.getNodeValue(),Double.parseDouble(answerValue));
								}

							}
						}

					}
				}

			}
		} catch (Exception e) {
			throw new InternalException(RENDER_ASSESSMENTTITLE_EXCEPTION, e);
		}

		return answerKeyWithScore;
	}
	
	
	
	/**
	 * Gets the answer choices and its feedback
	 * @return Map<String, String> Answer choice will be key and feedback will be value.
	 */
	public Map<String,String> getAnswerChoicesWithFeedback(){
		Map<String, String> answerChoiceWithFeedback = new HashMap<String, String>();
		
		try {
		NodeList answerChoiseNodes = xmlDoc
				.getElementsByTagName(SIMPLECHOICE_NODE);
		NodeList responseMatchNodes = xmlDoc
				.getElementsByTagName(MATCH_NODE);
		if (answerChoiseNodes != null && answerChoiseNodes.getLength() > 0) {
			for (int i = 0; i < answerChoiseNodes.getLength(); i++) {
				Element elAC = (Element) answerChoiseNodes
						.item(i);
				String identifier = elAC.getAttribute(IDENTIFIER_NODE);

				for (int j = 0; j < responseMatchNodes.getLength(); j++) {
					Element elRM = (Element) responseMatchNodes
							.item(j);
					String matchIdentifiew = elRM
							.getElementsByTagName(BASEVALUE_NODE).item(0)
							.getFirstChild().getNodeValue();

					if (isIdentifierMatches(identifier, matchIdentifiew)) {
						String feedbackIdentifier=((Element)elRM.getNextSibling().getNextSibling()).getFirstChild().getTextContent();
						Node feedback=getXMLNode("//modalFeedback[@identifier=\""+feedbackIdentifier+"\"]",true);
						if(feedback!=null){
							answerChoiceWithFeedback.put(elAC.getFirstChild().getNodeValue(),feedback.getFirstChild().getTextContent());	
						}
					}
								
				}
			}
		}
		
		} catch (Exception e) {
			throw new InternalException(
					"Error while getting feedback for answer choices", e);
		}
		
		return answerChoiceWithFeedback;
	}
	
	/**
	 * Gets the Recommended Answer for essay question
	 * @return Recommended Answer staring.
	 */
	public String getRecommendedAnswer(){
		String recommendedAnswer="";
		try {
			Node recommendedanswer = getXMLNode(RECOMMENDED_ANSWER,true);
			if (recommendedanswer.getFirstChild() instanceof CharacterData) {
			      CharacterData cd = (CharacterData) recommendedanswer.getFirstChild();
			      recommendedAnswer=cd.getData();
			}
			
		} catch (Exception e) {
			throw new InternalException(
					"Error while getting recommended answer", e);
		}
		
		return recommendedAnswer;
	}
	
	/*
	 * Gets the single node from the question XML. If node
	 * need to be fetched by element tag name, the tag name need to be passed as
	 * identifier and byXPath should be false If node need to be fetched by
	 * XPath, node path need to be passed as identifier and byXPath should be
	 * true
	 */
	private Node getXMLNode(String identifier, boolean byXPath) {
		try {
			if (byXPath) {
				return (Node) xpath.compile(identifier).evaluate(xmlDoc,XPathConstants.NODE);
			} else {
				return xmlDoc.getElementsByTagName(identifier).item(0);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml node", e);
		}
	}

	/*
	 * Gets the list of nodes from question XML. If nodes
	 * need to be fetched by element tag name, the tag name need to be passed as
	 * identifier and byXPath should be false If nodes need to be fetched by
	 * XPath, node path need to be passed as identifier and byXPath should be
	 * true
	 */
	private NodeList getXMLNodes(String identifier, boolean byXPath) {
		try {
			if (byXPath) {
				return (NodeList) xpath.compile(identifier).evaluate(xmlDoc,XPathConstants.NODESET);
			} else {
				return xmlDoc.getElementsByTagName(identifier);
			}
		} catch (Exception e) {
			throw new InternalException("Exception while getting xml nodes", e);
		}
	}
	/**
	 * append the given qti with CDATA tag for user entered text.
	 * @return
	 */
	public String appendCData(){
		Node node = getXMLNode("/assessmentItem/itemBody/p",true);
				
		if(node == null) {
			node = getXMLNode(A_MATCHING_EXPRESSION,true);
		}
		
		NodeList textentries = ((Element) node).getElementsByTagName("textEntryInteraction");
		
		String qtiAppendedWithCDATA = null;
		try {
			if(node.getFirstChild().getNodeType() == 4){
				((Element)xmlDoc.getElementsByTagName("assessmentItem").item(0)).setAttribute("identifier", "QUESTION-X");
				return getNodeOuterXML(xmlDoc);
			}
			
			String questionText = getNodeOuterXML(node);
			String paragraph = questionText.substring(3,questionText.length()-4);
			String content = String.format("<![CDATA[%s]]>", paragraph);
			
			for (int i = 0; i < textentries.getLength(); i++) {
				
				String textEntry = getNodeOuterXML(textentries.item(i));
				
				((Element)textentries.item(i)).setAttribute("expectedLength", "50");
				
				String updatedTextEntry = getNodeOuterXML(textentries.item(i));
				
				content = content.replaceFirst(textEntry, "]]>"+updatedTextEntry+"<![CDATA[");
				
			}
			
			content = "<p>" + content + "</p>";
			
			Node fragment = null;
			fragment = factory.newDocumentBuilder().parse(
						new InputSource(new StringReader(content))).getDocumentElement();
			
			fragment = xmlDoc.importNode(fragment, true);
			node.getParentNode().replaceChild(fragment, node);
	
			setCDataToNode("/assessmentItem/modalFeedback","modalFeedback");
			
			setCDataToNode("/assessmentItem/itemBody/infoControl","infoControl");
			
			setCDataToNode("/assessmentItem/itemBody/choiceInteraction/simpleChoice","simpleChoice");
			
			
			setCDataToNodeForMatching();
			
			Node extendedTextInteraction = getXMLNode("/assessmentItem/itemBody/extendedTextInteraction",true);
			
			if(extendedTextInteraction != null) {
				((Element)extendedTextInteraction).setAttribute("expectedLines", "10");
			}
			
			
			((Element)xmlDoc.getElementsByTagName("assessmentItem").item(0)).setAttribute("identifier", "QUESTION-X");
			
			qtiAppendedWithCDATA = getNodeOuterXML(xmlDoc);
			
		} catch (TransformerException | TransformerFactoryConfigurationError | SAXException | IOException | ParserConfigurationException e3) {
			throw new InternalException("Error while parsing qti");
		}
		return qtiAppendedWithCDATA;
	}

	private void setCDataToNodeForMatching() throws TransformerException {
		NodeList nodes = getXMLNodes("/assessmentItem/itemBody/blockquote/p/inlineChoiceInteraction", true);
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node parentNode = nodes.item(i).getParentNode();
			String inlineChoices = getNodeOuterXML(parentNode);
			String matchA = inlineChoices.substring("<p>".length(), inlineChoices.indexOf("<inlineChoiceInteraction"));
			parentNode.setTextContent("");
			parentNode.appendChild(xmlDoc.createCDATASection(matchA));
			parentNode.appendChild(nodes.item(i));
		}
	}
	/**
	 * getting the outerxml in string for the given node
	 * @param node
	 * @return
	 * @throws TransformerException
	 */
	private String getNodeOuterXML(Node node)
			throws TransformerException {
		StringWriter sw = new StringWriter();
		xmlToString.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
				"yes");
		xmlToString.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}
	/**
	 * Wrapping the text with CDATA for the given node
	 * @param xpath
	 * @param tagName
	 */
	private void setCDataToNode(String xpath, String tagName){
		NodeList nodes = getXMLNodes(xpath, true);
		if(nodes != null){
			String nodeContent;
			for (int j = 0; j < nodes.getLength(); j++) {
				Node node = nodes.item(j);
				nodeContent = "";
				while(node.getFirstChild() != null){
					try {
						nodeContent = nodeContent + getNodeOuterXML(node.getFirstChild());
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						throw new InternalException("Error while Setting the CData to Node ",e);
					}
					node.removeChild(node.getFirstChild());
				}
				node.appendChild(xmlDoc.createCDATASection(nodeContent));
			}
		}
	}
}
