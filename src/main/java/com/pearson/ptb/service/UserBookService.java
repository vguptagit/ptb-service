package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.QuestionBinding;
import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.UserBook;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.proxy.UserBookDelegate;

import org.springframework.stereotype.Service;

@Service("userBookService")
public class UserBookService {

	@Autowired
	@Qualifier("userBookRepo")
	private UserBookDelegate userBookRepo;

	@Autowired
	@Qualifier("userFolderService")
	private UserFolderService userFolderService;	
	
	public List<UserBook> getUserBooks(String userId) {		
			
		return userBookRepo.getUserBooks(userId);
	}
	
	public void importUserBooks(List<String> userBookIds, String userId) {
		
		double folderSeq = userFolderService.getUserFolderMinSeq(userId);	
		double size = 2;
		double seq = 2.0;
		folderSeq = folderSeq/Math.pow(size, userBookIds.size());
		
		double userQuestionsfolderSeq = userFolderService.getUserQuestionsFolderMinSeq(userId);
		userQuestionsfolderSeq = userQuestionsfolderSeq/Math.pow(size, userBookIds.size());
					
		String myQuestionsFolderId = userFolderService.getMyQuestionsFolder(userId).getGuid() ;
		
		for(String userBookId : userBookIds) {
			UserBook userBook = userBookRepo.getUserBook(userBookId);
			if(!userBook.getIsImported()) {
				userBook.setIsImported(true);
				userBook.setImportedDate(new Date());				
				
				if(!userBook.getTestBindings().isEmpty()) {
					userFolderService.saveFolder(getUserFolder(userBook, folderSeq), userId);	
				}							

				if(!userBook.getQuestionBindings().isEmpty()) {
					userFolderService.saveFolder(getUserQuestionsFolder(userBook, userQuestionsfolderSeq, myQuestionsFolderId), userId);
				}
				
				folderSeq = folderSeq * seq;
				userQuestionsfolderSeq = userQuestionsfolderSeq * seq;
				
				userBookRepo.saveUserBook(userBook);			
			}
		}
	}
	
	private UserFolder getUserFolder(UserBook userBook, double folderSeq) {
		UserFolder folder = new UserFolder();
		
		folder.setGuid(null);			
		folder.setParentId(null);
		folder.setTitle("Import - " + userBook.getTitle());
		folder.setSequence(folderSeq);
	
		double testSeq = 0.0;
		List<TestBinding> testBindings = new ArrayList<TestBinding>();
		for(String testId : userBook.getTestBindings()) {
			TestBinding testBinding = new TestBinding();
			testBinding.setTestId(testId);
			testBinding.setSequence(testSeq + 1);
			
			testBindings.add(testBinding);
		}			
		folder.setTestBindings(testBindings);
		
		return folder;
	}
	
	private UserQuestionsFolder getUserQuestionsFolder(UserBook userBook, double folderSeq, String parentId) {
		UserQuestionsFolder userQuestionsFolder = new UserQuestionsFolder();
		
		userQuestionsFolder.setGuid(null);
		userQuestionsFolder.setParentId(parentId);
		userQuestionsFolder.setTitle("Import - " + userBook.getTitle());
		userQuestionsFolder.setSequence(folderSeq);
		
		double questionSeq = 0.0;
		List<QuestionBinding> questionBindings = new ArrayList<QuestionBinding>();
		for(String questionId : userBook.getQuestionBindings()) {
			QuestionBinding questionBinding = new QuestionBinding();
			questionBinding.setQuestionId(questionId);
			questionBinding.setSequence(questionSeq + 1);
			
			questionBindings.add(questionBinding);
		}
		userQuestionsFolder.setQuestionBindings(questionBindings);
		
		return userQuestionsFolder;
	}
}
