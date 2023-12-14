package com.pearson.ptb.download;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.framework.QTIParser;
import com.pearson.ptb.framework.exception.InternalException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class QTI2p1 implements TestDownload {

	private QTIParser parser = null;
	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	
	public QTI2p1(){
		try {
			factory=DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			throw new InternalException("Exception while initializing DocumentBuilderFactory in QTI2p1",e);
		}
	}
	/**
	 * Overridden method from TestDownload interface which downloads the
	 * catridges containing the questions xml in 2.1 format, imsassessment.xml
	 * and imsmanifest.xml.
	 */
	@Override
	public DownloadOutput download(OutputStream stream,
			DownloadInfo downloadInfo) {

		parser = new QTIParser();
		DownloadOutput downloadOutput = new DownloadOutput();
		try {
			ZipOutputStream zipOuputStream = new ZipOutputStream(
					new BufferedOutputStream(stream));

			buildZIPFile(downloadInfo, zipOuputStream);

			zipOuputStream.close();

			downloadOutput.setContentType("zip");
			downloadOutput.setFileName(getPackgeName(downloadInfo));
		} catch (Exception e) {
			throw new InternalException("Exception while exporting to Qti2.1",
					e);
		}
		return downloadOutput;
	}

	@Override
	public String extension() {
		return null;
	}

	/**
	 * This builds the package name using test name,format and date and time
	 * 
	 * @param downloadInfo
	 * @return package name with extension
	 */
	private String getPackgeName(DownloadInfo downloadInfo) {
		String packgeName = "";
		String exportType = "QTImgr";
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		Date date = new Date();
		packgeName = downloadInfo.getTestTitle() + " " + exportType + " "
				+ df.format(date) + ".zip";
		return packgeName;
	}

	/**
	 * Building the zip file containing the list of questions xml file which are
	 * in test, imsmanifest file and imsassessment file
	 * 
	 * @param downloadInfo
	 *            , containing the list of questions in qti2.1
	 * @param zipOuputStream
	 *            , the output memory stream where file will be written in bytes
	 */
	private void buildZIPFile(DownloadInfo downloadInfo,
			ZipOutputStream zipOuputStream) {
		Map<String, Document> questionDocuments = getquestionDocuments(downloadInfo);

		try {
			for (Map.Entry<String, Document> entry : questionDocuments
					.entrySet()) {
				String key = entry.getKey();
				Document value = entry.getValue();
				zipOuputStream.putNextEntry(new ZipEntry(key + ".xml"));
				zipOuputStream.write(documentToByte(value));
			}
			zipOuputStream.putNextEntry(new ZipEntry("imsmanifest.xml"));
			zipOuputStream
					.write(documentToByte(getManifestDocument(downloadInfo)));

			zipOuputStream.putNextEntry(new ZipEntry("imsasssessment.xml"));
			zipOuputStream
					.write(documentToByte(getAssessmentXMLDocument(downloadInfo)));

		} catch (Exception e) {
			throw new InternalException("Exception while writing to ZIP", e);
		}

	}

	/**
	 * Getting the imsassessment file
	 * 
	 * @param downloadInfo
	 *            , contains the list of questions in qti2.1
	 * @return Document form of imsassessment file
	 */
	private Document getAssessmentXMLDocument(DownloadInfo downloadInfo) {
		// TODO : Hard coded xml format will be moved to template file
		StringBuilder assessment = new StringBuilder();
		assessment
				.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><assessmentTest xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" identifier=\"ASSESSMENT-IDENTIFIER\" title=\"" + downloadInfo.getTestTitle() + "\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p1 http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_v2p1.xsd\"><testPart identifier=\"TESTPART\" navigationMode=\"nonlinear\" submissionMode=\"simultaneous\"><assessmentSection identifier=\"SECTION_IDENTIFIER\" title=\"SECTION_TITLE\" visible=\"true\"><ordering shuffle=\"false\" />");
		for (String questionXML : downloadInfo.getQuestions()) {

			parser.setXMLDocument(questionXML);
			String identifier = parser.getQTIIdentifier();
			assessment.append("<assessmentItemRef identifier=\"" + identifier
					+ "\" href=\"" + identifier + ".xml\" fixed=\"false\" />");
		}
		assessment.append("</assessmentSection></testPart></assessmentTest>");
		return stringToDocument(assessment.toString());
	}

	/**
	 * Getting the imsmanifest file
	 * 
	 * @param downloadInfo
	 *            , contains the list of questions in qti2.1
	 * @return Document form of imsmanifest file
	 */
	private Document getManifestDocument(DownloadInfo downloadInfo) {
		// TODO : Hard coded xml format will be moved to template file
		StringBuilder manifest = new StringBuilder();
		manifest.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><manifest xmlns:imsmd=\"http://www.imsglobal.org/xsd/imsmd_v1p2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:imsqti=\"http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_metadata_v2p1\" identifier=\""
				+ UUID.randomUUID().toString()
				+ "\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imscp_v1p1 http://www.imsglobal.org/xsd/imscp_v1p2.xsd http://www.imsglobal.org/xsd/imsmd_v1p2 http://www.imsglobal.org/xsd/imsmd_v1p2p4.xsd http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_metadata_v2p1 http://www.imsglobal.org/xsd/qti/qtiv2p1/imsqti_metadata_v2p1.xsd\" version=\"CP 1.2\" xmlns=\"http://www.imsglobal.org/xsd/imscp_v1p1\">");
		manifest.append("<metadata><imsmd:lom><imsmd:general><imsmd:title><imsmd:langstring xml:lang=\"en\">"
				+ UUID.randomUUID().toString()
				+ "</imsmd:langstring></imsmd:title><imsmd:language>en</imsmd:language></imsmd:general><imsmd:lifecycle><imsmd:version><imsmd:langstring imsmd:lang=\"en\">2.1</imsmd:langstring></imsmd:version><imsmd:status><imsmd:source><imsmd:langstring imsmd:lang=\"x-none\">LOMv1.0</imsmd:langstring></imsmd:source><imsmd:value><imsmd:langstring imsmd:lang=\"x-none\">Final</imsmd:langstring></imsmd:value></imsmd:status></imsmd:lifecycle><imsmd:metametadata><imsmd:metadatascheme>LOMv1.0</imsmd:metadatascheme><imsmd:metadatascheme>QTIv2.1</imsmd:metadatascheme><imsmd:language>en</imsmd:language></imsmd:metametadata></imsmd:lom></metadata><resources>");
		for (String questionXML : downloadInfo.getQuestions()) {
			parser.setXMLDocument(questionXML);
			String identifier = parser.getQTIIdentifier();
			String title = parser.getQTITitle();

			manifest.append("<resource identifier=\""
					+ identifier
					+ "\" type=\"imsqti_item_xmlv2p1\" href=\""
					+ identifier
					+ ".xml\"><metadata><qtiMetadata xmlns=\"http://www.imsglobal.org/xsd/imsqti_metadata_v2p1\"><timeDependent>false</timeDependent><interactionType>choiceInteraction</interactionType><feedbackType>none</feedbackType><solutionAvailable>false</solutionAvailable></qtiMetadata><imsmd:lom><imsmd:general><imsmd:identifier>"
					+ identifier
					+ "</imsmd:identifier><imsmd:title><imsmd:langstring xml:lang=\"en\">"
					+ title
					+ "</imsmd:langstring></imsmd:title></imsmd:general><imsmd:lifecycle><imsmd:version><imsmd:langstring imsmd:lang=\"en\">2.1</imsmd:langstring></imsmd:version><imsmd:status><imsmd:source><imsmd:langstring imsmd:lang=\"x-none\">LOMv1.0</imsmd:langstring></imsmd:source><imsmd:value><imsmd:langstring imsmd:lang=\"x-none\">Final</imsmd:langstring></imsmd:value></imsmd:status></imsmd:lifecycle><imsmd:technical><imsmd:format>text/x-imsqti-item-xml</imsmd:format></imsmd:technical></imsmd:lom></metadata><file href=\""
					+ identifier + ".xml\" /></resource>");
		}
		manifest.append("<resource identifier=\"ASSESSMENT\" type=\"imsqti_test_xmlv2p1\" href=\"imsasssessment.xml\"><file href=\"imsasssessment.xml\" />");
		for (String questionXML : downloadInfo.getQuestions()) {
			parser.setXMLDocument(questionXML);
			String identifier = parser.getQTIIdentifier();
			manifest.append("<dependency identifierref=\"" + identifier
					+ "\" />");
		}
		manifest.append("</resource></resources></manifest>");
		return stringToDocument(manifest.toString());
	}

	/**
	 * Getting the map of question identifier and Document form of question xml
	 * 
	 * @param downloadInfo
	 *            , contains the list of questions in qti2.1
	 * @return
	 */
	private Map<String, Document> getquestionDocuments(DownloadInfo downloadInfo) {
		Map<String, Document> map = new HashMap<String, Document>();

		for (String questionXML : downloadInfo.getQuestions()) {
			parser.setXMLDocument(questionXML);
			String identifier = parser.getQTIIdentifier();
			map.put(identifier, stringToDocument(questionXML));
		}
		return map;
	}

	/**
	 * Converts the document to bytes
	 * 
	 * @param document
	 * @return
	 */
	// TODO : Duplicate method as in Blackboard.java. Need to move this method
	// to a helper or utility class
	public byte[] documentToByte(Document document) {
		byte[] array;
		try {
			Transformer transformer=TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(document);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(bos);
			transformer.transform(source, result);
			array = bos.toByteArray();
		} catch (Exception e) {
			throw new InternalException(
					"Exception while Document to bite array", e);
		}
		return array;
	}

	/**
	 * Converts String to document
	 * 
	 * @param xmlString
	 * @return
	 */
	// TODO : Duplicate method as in Blackboard.java. Need to move this method
	// to a helper or utility class
	private Document stringToDocument(String xmlString) {
		Document doc;
		try {
			doc = builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
			throw new InternalException(
					"Exception while converting string to document", e);
		}
		return doc;
	}

}
