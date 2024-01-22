package com.pearson.ptb.proxy.repo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StreamUtils;

import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.dataaccess.QuestionEnvelopRepoHelper;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.mapper.ModelMapperProvider;
import com.pearson.ptb.proxy.AmazonS3Service;
import com.pearson.ptb.proxy.QuestionDelegate;
import com.pearson.ptb.proxy.aws.util.Converter;

@Repository("questions")
public class QuestionRepo implements QuestionDelegate {
	
	@Autowired
	private AmazonS3Service amazonS3Service;
	
	@Autowired
	private QuestionEnvelopRepoHelper questionEnvelopRepoHelper;
	
	@Value("${aws.s3.bucket.name}")
	private String bucketName;
	
	private static final LogWrapper LOG = LogWrapper
			.getInstance(QuestionRepo.class);

	@Override
	public List<QuestionMetadata> getQuestions(String bookID) {
		// PAF related

		return null;
	}

	@Override
	public String getQuestionXmlById(String questionId) {
		S3Object s3Object = amazonS3Service.download(bucketName, questionId);
		try {
			return StreamUtils.copyToString(s3Object.getObjectContent(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.error("Error Converting S3 Stream to String",e);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String saveQuestion(QuestionEnvelop question, String bookTitle,
			String chapterTitle) {
		com.pearson.ptb.proxy.aws.bean.QuestionEnvelop awsQuestion = ModelMapperProvider.getDestinationBean(question, com.pearson.ptb.proxy.aws.bean.QuestionEnvelop.class);
		awsQuestion.getMetadata().setBookTitle(bookTitle);
		awsQuestion.getMetadata().setChapterTitle(chapterTitle);
		awsQuestion.setGuid(question.getmetadata().getGuid());
		
		// Add metadata to be stored along with body xml
		Map<String, String> awsMetadata = new HashMap<>();
		String awsFileName = awsQuestion.getMetadata().getGuid();
		
		amazonS3Service.upload(bucketName, awsFileName, Optional.of(awsMetadata), IOUtils.toInputStream(awsQuestion.getBody()));
			
		// Convert body filed to s3 filename & store in mongodb
		Converter.updateBodyWithAwsRefId(awsQuestion, awsFileName);
		questionEnvelopRepoHelper.save(awsQuestion);
		
		Gson gson = new Gson();
		String payload = gson.toJson(awsQuestion);
		return payload;
	}

}
