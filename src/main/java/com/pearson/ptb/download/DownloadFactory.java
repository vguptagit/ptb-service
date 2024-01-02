package com.pearson.ptb.download;

import com.pearson.ptb.bean.DownloadFormat;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This is a Factory pattern for download formats such as pdf, doc, bbpm, bbtm
 * and qti21
 */
public class DownloadFactory {

	private DownloadFactory() {
	}
	/**
	 * factory pattern to load the classes to present the assessment
	 * 
	 * @param format
	 *            , string datatype which represents the format of assessment
	 *            presenter
	 * @throws InternalException
	 */
	public static TestDownload getDownloader(DownloadFormat format) {
		TestDownload testDownload = null;
		try {
			switch (format) {
				case doc :
					testDownload = new Word();
					break;
				case pdf :
					testDownload = new Pdf();
					break;
				case bbpm :
				case bbtm :
					testDownload = new Blackboard(format);
					break;
				case qti21 :
					testDownload = new QTI2p1();
					break;
				default :
					break;
			}
		} catch (Exception e) {
			throw new InternalException(
					"Exception while creating the download class object", e);
		}

		return testDownload;
	}

}
