package com.pearson.ptb.download;

import java.io.OutputStream;

import com.aspose.words.SaveFormat;
import com.pearson.ptb.bean.AnswerKeys;
import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;
import com.pearson.ptb.framework.exception.InternalException;
/**
 * This <code>Pdf</code> is responsible to hold the information of download
 * settings for pdf format.
 */
public class Pdf extends Word {

	/**
	 * this method downloads the document based on the content type and file
	 * name
	 * 
	 * @return downloaded document
	 */
	@Override
	public DownloadOutput download(OutputStream stream,
			DownloadInfo downloadInfo) {

		try {
			download(stream, downloadInfo, SaveFormat.PDF);

			DownloadOutput output = new DownloadOutput();
			String fileName;
			output.setContentType("pdf");
			if (downloadInfo.getPrintSettings().getIncludeAnswerKeyIn()
					.equals(AnswerKeys.SEPARATEFILE)) {
				fileName = downloadInfo.getTestTitle() + "-AnswerKeys";
			} else {
				fileName = downloadInfo.getTestTitle();
			}

			output.setFileName(fileName + this.extension());
			return output;

		} catch (Exception e) {
			throw new InternalException(
					"Exception while rendering the PDF document", e);
		}
	}

	/***
	 * Returns the extension of the file type downloaded by this downloader
	 */
	@Override
	public String extension() {
		return ".pdf";
	}
}
