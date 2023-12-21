package com.pearson.ptb.download;

import java.io.OutputStream;

import com.pearson.ptb.bean.DownloadInfo;
import com.pearson.ptb.bean.DownloadOutput;

/**
 * Presenting the assessment/assignment in various form
 *
 */
public interface TestDownload {
	DownloadOutput download(OutputStream stream, DownloadInfo downloadInfo);
	String extension();
}
