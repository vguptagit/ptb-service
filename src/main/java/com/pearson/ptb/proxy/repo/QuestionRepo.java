package com.pearson.ptb.proxy.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.proxy.QuestionDelegate;

@Repository("questions")
public class QuestionRepo implements QuestionDelegate {

	@Override
	public List<QuestionMetadata> getQuestions(String bookID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuestionXmlById(String questionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveQuestion(QuestionEnvelop question, String bookTitle, String chapterTitle) {
		// TODO Auto-generated method stub
		return null;
	}

}
