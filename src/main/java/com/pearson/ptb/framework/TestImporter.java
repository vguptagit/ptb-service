package com.pearson.ptb.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pearson.ptb.bean.ExtMetadata;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.util.QuestionTypes;

public class TestImporter {
	private Map<String, ZipEntry> zipMap;
	private Map<String, String> imageUrlMap;
	private Map<String, MultipartFile> imageFileMap;
	private ZipFile zipfile;
	private NodeList resources;
	File tempFile;
	private static final Logger LOG = LogWrapper
			.getInstance(TestImporter.class);

	public TestImporter(MultipartFile file) {
		zipMap = new HashMap<String, ZipEntry>();
		imageUrlMap = new HashMap<String, String>();
		imageFileMap = new HashMap<String, MultipartFile>();
		initialize(file);
	}
	/**
	 * Getting the map consists of filename and multipart file object
	 */
	public Map<String, MultipartFile> getImageMap() {
		return imageFileMap;
	}
	/**
	 * Adding the map item with key as filename and value as eps url.
	 */
	public void addImage(String key, String value) {
		imageUrlMap.put(key, value);
	}
	/**
	 * Validate the package by checking the existence of files by parsing the
	 * imsmanifest file
	 */
	public void validatePackage() {
		ZipEntry entry = zipMap.get("imsmanifest.xml");
		InputStream read = null;
		try {
			if (entry == null) {
				throw new BadDataException("Invalid Package");
			}
			read = zipfile.getInputStream(entry);

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc;

			doc = factory.newDocumentBuilder().parse(read);
			resources = doc.getElementsByTagName("resource");

			NodeList fields;
			String href, type;
			for (int i = 0; i < resources.getLength(); i++) {
				type = ((Element) resources.item(i)).getAttribute("type");
				if (!type.equals("imsqti_item_xmlv2p1")
						&& !type.equals("imsqti_test_xmlv2p1")) {
					throw new BadDataException("Invalid Package");
				}
				fields = ((Element) resources.item(i))
						.getElementsByTagName("file");
				for (int j = 0; j < fields.getLength(); j++) {
					href = ((Element) fields.item(j)).getAttribute("href");
					if (!zipMap.containsKey(href)
							&& !imageFileMap.containsKey(href)) {
						throw new BadDataException("Invalid Package");
					}

				}
			}
		} catch (SAXException | IOException | ParserConfigurationException
				| BadDataException e) {

			throw new BadDataException("Invalid Package", e);
		} finally {
			try {
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
				LOG.error("Unable to close the stream.", e);
			}
		}

	}
	/**
	 * Get the list of question envelops.
	 */
	public java.util.List<QuestionEnvelop> getQuestions() {
		List<QuestionEnvelop> questions = new ArrayList<QuestionEnvelop>();
		try {
			String body, href, qti;
			for (int i = 0; i < resources.getLength(); i++) {
				NodeList fields = ((Element) resources.item(i))
						.getElementsByTagName("file");
				body = null;
				QTIParser qtiParser = null;
				for (int j = 0; j < fields.getLength(); j++) {
					href = ((Element) fields.item(j)).getAttribute("href");
					if (!href.equals("imsasssessment.xml")) {
						if (href.contains("media/")) {
							body = addImageToBody(body, href);
						} else {
							qti = extractQTI(href);
							qtiParser = new QTIParser();
							qtiParser.setXMLDocument(qti);
							body = qtiParser.appendCData();
						}
					}
				}
				if (body != null) {
					QuestionEnvelop envelop = buildQuestionEnvelop(body,
							qtiParser);
					questions.add(envelop);
				}
			}

		} catch (IOException e) {
			throw new InternalException("Error in building question envelope ",
					e);
		}
		return questions;
	}
	/**
	 * Get the test title from imsassessment.xml
	 */
	public String getTestTitle() {
		ZipEntry entry = zipMap.get("imsasssessment.xml");
		String fileName = null;
		InputStream read = null;
		try {
			read = zipfile.getInputStream(entry);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc = factory.newDocumentBuilder().parse(read);
			fileName = ((Node) doc.getElementsByTagName("assessmentTest")
					.item(0)).getAttributes().getNamedItem("title")
					.getNodeValue();
			read.close();
		} catch (IOException | SAXException | ParserConfigurationException e) {
			throw new InternalException("Error in extracting test ", e);
		} finally {
			if (read != null) {
				try {
					read.close();
				} catch (IOException e) {
					LOG.error("Unable to close the stream.", e);
				}
			}
		}
		return fileName;
	}

	public void clearData() {
		zipMap = null;
		imageUrlMap = null;
		imageFileMap = null;
		try {
			zipfile.close();
			tempFile.delete();
		} catch (IOException e) {
			throw new InternalException("Unable to close the file ", e);
		}
	}

	/**
	 * Initialise the zip file
	 */
	private void initialize(MultipartFile file) {
		zipMap = new HashMap<String, ZipEntry>();
		imageUrlMap = new HashMap<String, String>();
		imageFileMap = new HashMap<String, MultipartFile>();
		InputStream question = null;
		try {
			ZipInputStream zis = new ZipInputStream(file.getInputStream());
			ZipEntry ze;
			tempFile = File.createTempFile("import", null);
			file.transferTo(tempFile);
			zipfile = new ZipFile(tempFile);

			for (ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
				if (ze.getName().contains("media/")) {
					question = zipfile.getInputStream(ze);
					// MultipartFile image = new
					// MockMultipartFile("file",ze.getName().substring("media/".length()),"*/*",
					// question);
					// imageFileMap.put(ze.getName(), image);
					question.close();
				} else {
					zipMap.put(ze.getName(), ze);
				}
			}

		} catch (IOException e) {
			throw new InternalException("Error in extracting file ", e);
		} finally {
			if (question != null) {
				try {
					question.close();
				} catch (IOException e) {
					LOG.error("Unable to close the stream.", e);
				}
			}
		}
	}
	/**
	 * Building the question envelop for saving the questions
	 * 
	 * @param body
	 * @param qtiParser
	 * @return
	 */
	private QuestionEnvelop buildQuestionEnvelop(String body,
			QTIParser qtiParser) {
		QuestionEnvelop envelop = new QuestionEnvelop();
		Metadata metadata = new Metadata();
		List<ExtMetadata> extMetaData = new ArrayList<ExtMetadata>();
		metadata.setQuizType(getQuizType(qtiParser.getQuestionType()));
		metadata.setExtendedMetadata(extMetaData);
		envelop.setmetadata(metadata);
		envelop.setBody(body);
		return envelop;
	}

	/**
	 * Extracting the qti from zip file
	 * 
	 * @param href
	 * @return
	 * @throws IOException
	 */
	private String extractQTI(String href) throws IOException {
		InputStream question = zipfile.getInputStream(zipMap.get(href));
		String qti = IOUtils.toString(question);
		question.close();
		if (qti.indexOf("<![CDATA[") > 0)
			return qti;
		qti = qti.replaceAll("<br>", "<br/>");
		qti = qti.trim().replaceFirst("^([\\W]+)<", "<");

		int index = qti.indexOf("<img", 0);
		String temp, temp1;
		while (index != -1) {
			temp = qti.substring(index);
			int anotherIndex = temp.indexOf(">");
			temp1 = temp.substring(0, anotherIndex + 1);
			if (temp1.indexOf("/>") == -1)
				qti = qti.replaceFirst(temp1,
						temp.substring(0, anotherIndex) + "/>");
			index = qti.indexOf("<img", index + temp1.length());
		}

		qti = qti.replaceAll("&nbsp;", " ");

		qti = qti.replaceAll("w:st", "wst");
		return qti;
	}

	/**
	 * Replace image in the qti with eps url
	 * 
	 * @param body
	 * @param href
	 * @return
	 */
	private String addImageToBody(String body, String href) {
		if (body != null) {
			body = body.replaceAll(href, imageUrlMap.get(href));
		}
		return body;
	}

	/**
	 * Getting the quiz type for the question type
	 * 
	 * @param type
	 * @return
	 */
	private String getQuizType(QuestionTypes type) {
		switch (type) {
			case ESSAY :
				return "Essay";
			case FILLINBLANKS :
				return "FillInBlanks";
			case MATCHING :
				return "Matching";
			case MULTIPLECHOICE :
				return "MultipleChoice";
			case MULTIPLERESPONSE :
				return "MultipleResponse";
			default :
				return "TrueFalse";

		}

	}

}
