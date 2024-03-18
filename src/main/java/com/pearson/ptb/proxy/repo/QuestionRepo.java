package com.pearson.ptb.proxy.repo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.dataaccess.QuestionEnvelopRepoHelper;
import com.pearson.ptb.framework.LogWrapper;
import com.pearson.ptb.mapper.ModelMapperProvider;
import com.pearson.ptb.proxy.AmazonS3Service;
import com.pearson.ptb.proxy.QuestionDelegate;
import com.pearson.ptb.proxy.aws.bean.QuestionResponse;
import com.pearson.ptb.proxy.aws.util.Converter;

@Repository("questions")
public class QuestionRepo implements QuestionDelegate {
	
	@Autowired
	private AmazonS3Service amazonS3Service;
	
	@Autowired
	private QuestionEnvelopRepoHelper questionEnvelopRepoHelper;
	
	@Value("${aws.s3.bucket.name}")
	private String bucketName;
	
	@Autowired
	private GenericMongoRepository<com.pearson.ptb.proxy.aws.bean.QuestionEnvelop, String> questionRepository;

	
	private static final LogWrapper LOG = LogWrapper
			.getInstance(QuestionRepo.class);

	@Override
	public List<QuestionMetadata> getQuestions(String bookID) {
		

		return null;
	}

	@Override
	public String getQuestionXmlById(String questionId) {
		com.pearson.ptb.proxy.aws.bean.QuestionEnvelop byId = questionRepository.findById(questionId);
		String body = byId.getBody();
		
		S3Object s3Object = amazonS3Service.download(bucketName, body);
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
//		 Convert Question Bean to AWS Question Envelop to add additional Metadata
		com.pearson.ptb.proxy.aws.bean.QuestionEnvelop awsQuestion = ModelMapperProvider.getDestinationBean(question, com.pearson.ptb.proxy.aws.bean.QuestionEnvelop.class);
		awsQuestion.getMetadata().setBookTitle(bookTitle);
		awsQuestion.getMetadata().setChapterTitle(chapterTitle);
		awsQuestion.setGuid(question.getmetadata().getGuid());
	//	System.out.println(question.getBody() + "  body" );
		
		
		// Set File Name
		Map<String, String> awsMetadata = new HashMap<>();
		String awsFileName = UUID.randomUUID().toString();
		
		// Call Amazon service to upload xml
		amazonS3Service.upload(awsFileName, awsFileName , Optional.of(awsMetadata), IOUtils.toInputStream(awsQuestion.getBody()));
			
		//Save record to DB with the AWS FileName reference
		Converter.updateBodyWithAwsRefId(awsQuestion, awsFileName); // Change body xml to AWS file Reference
		questionEnvelopRepoHelper.save(awsQuestion);
		QuestionResponse awsResponse = new QuestionResponse(awsQuestion.getGuid(), awsQuestion.getBody());
		Gson gson = new Gson();
		return gson.toJson(awsResponse);
	}

}
