package com.pearson.ptb.download;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This <code>Blackboard</code> is responsible for support black board format
 * for all type of questions.
 */
public class Blackboard implements TestDownload {

	DownloadFormat testFormat;

	/**
	 * Constructor which takes the blackboard format as parameter and sets the
	 * format.
	 * 
	 * @param format
	 */
	public Blackboard(DownloadFormat format) {
		testFormat = format;
	}

	/**
	 * Implementing the download method of "TestDownload" interface
	 */
	@Override
	public DownloadOutput download(OutputStream stream,
			DownloadInfo downloadInfo) {
		DownloadOutput downloadOutput = new DownloadOutput();
		try {
			ZipOutputStream zipOuputStream = new ZipOutputStream(
					new BufferedOutputStream(stream));

			buildZIPFile(downloadInfo, zipOuputStream);

			zipOuputStream.close();

			downloadOutput.setContentType("zip");
			downloadOutput.setFileName(getPackageName(downloadInfo));
		} catch (Exception e) {
			throw new InternalException(
					"Exception while exporting to blackboard", e);
		}
		return downloadOutput;
	}

	/**
	 * This builds the package name using test name,format and date and time
	 * 
	 * @param downloadInfo
	 * @return package name with extension
	 */
	private String getPackageName(DownloadInfo downloadInfo) {
		String packgeName = "";
		String exportType = "";
		if (testFormat == DownloadFormat.bbpm) {
			exportType = "BBpoolmgr";
		} else if (testFormat == DownloadFormat.bbtm) {
			exportType = "BBtestmgr";
		}
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss");
		Date date = new Date();
		packgeName = downloadInfo.getTestTitle() + " " + exportType + " "
				+ df.format(date) + ".zip";
		return packgeName;
	}

	/**
	 * Adds QTI file, manifest file, blackboard sign and info file to ZIP file.
	 * 
	 * @param downloadInfo
	 * @param zipOuputStream
	 */
	private void buildZIPFile(DownloadInfo downloadInfo,
			ZipOutputStream zipOuputStream) {
		Document testXML = buildTestXML(downloadInfo);
		Document manifestXML = buildManifestXML(downloadInfo);
		String sig = buildSigFile();
		String infoFile = buildInfoFile();
		try {

			zipOuputStream.putNextEntry(
					new ZipEntry(downloadInfo.getTestTitle() + ".dat"));
			zipOuputStream.write(documentToByte(testXML));

			zipOuputStream.putNextEntry(new ZipEntry("imsmanifest.xml"));
			zipOuputStream.write(documentToByte(manifestXML));

			zipOuputStream.putNextEntry(new ZipEntry(".bb-package-sig"));
			zipOuputStream.write(sig.getBytes());

			zipOuputStream.putNextEntry(new ZipEntry(".bb-package-info"));
			zipOuputStream.write(infoFile.getBytes());

		} catch (Exception e) {
			throw new InternalException("Exception while writing to ZIP", e);
		}
	}

	/**
	 * Gets the black board compatible XML.
	 * 
	 * @param downloadInfo
	 * @return document which is black board compatible XML
	 */
	private Document buildTestXML(DownloadInfo downloadInfo) {
		BlackboardQTI blackboardQTI = new BlackboardQTI(testFormat,
				downloadInfo);
		return blackboardQTI.getTestXML();
	}

	/**
	 * Builds the manifest file which is required for blackboard package
	 * 
	 * @param downloadInfo
	 * @return document
	 */
	private Document buildManifestXML(DownloadInfo downloadInfo) {

		String exportType = "";
		if (testFormat == DownloadFormat.bbpm) {
			exportType = "assessment/x-bb-qti-pool";
		} else if (testFormat == DownloadFormat.bbtm) {
			exportType = "assessment/x-bb-qti-test";
		}
		StringBuilder manifestXML = new StringBuilder();
		manifestXML.append(
				"<manifest identifier=\"man00001\"  xmlns:bb=\"http://www.blackboard.com/content-packaging/\">");
		manifestXML.append("<organizations>");
		manifestXML.append(
				"<organization identifier=\"toc00001\"/></organizations>");
		manifestXML.append("<resources>");
		manifestXML.append("<resource bb:file=\"");
		manifestXML.append(downloadInfo.getTestTitle());
		manifestXML.append(".dat\" bb:title=\"");
		manifestXML.append(downloadInfo.getTestTitle());
		manifestXML.append("\" identifier=\"");
		manifestXML.append(downloadInfo.getTestTitle());
		manifestXML.append("\" type=\"");
		manifestXML.append(exportType + "\"");
		manifestXML.append(" xml:base=\"");
		manifestXML.append(downloadInfo.getTestTitle() + "\"");
		manifestXML.append("/>");
		manifestXML.append("</resources></manifest>");

		return stringToDocument(manifestXML.toString());
	}

	/**
	 * Gets the sign file
	 * 
	 * @return string
	 */
	private String buildSigFile() {
		return "EFB1A7222DE132C794B5F8040BB69A35";
	}

	/**
	 * builds the information file required for blackboard package.
	 * 
	 * @return string
	 */
	private String buildInfoFile() {
		StringBuilder infoFile = new StringBuilder();
		infoFile.append("cx.package.info.version=6.0");
		infoFile.append("\r\n");
		infoFile.append(
				"cx.config.logs=[Log{name\\=C\\:\\\\blackboard\\\\logs\\\\content-exchange\\\\ExportFile_42279_20090821084849.txt, verbosity\\=default}, Log{name\\=C\\:\\\\blackboard\\\\logs\\\\content-exchange\\\\ExportFile_42279_20090821084849_detailed.txt, verbosity\\=debug}]");
		return infoFile.toString();
	}

	/**
	 * Converts the document to bytes
	 * 
	 * @param document
	 * @return
	 */
	public byte[] documentToByte(Document document) {
		byte[] array;
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
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
	 * Converts string to document
	 * 
	 * @param xmlString
	 * @return
	 */
	private Document stringToDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xmlString)));
		} catch (Exception e) {
			throw new InternalException(
					"Exception while converting string to document", e);
		}
		return doc;
	}

	@Override
	public String extension() {
		return null;
	}

}
