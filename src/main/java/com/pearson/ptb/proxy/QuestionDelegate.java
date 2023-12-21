/**
 * 
 */
package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;

/**
 * This interface defines the contract for accessing and saving the Questions
 * for the given book and Container.
 *
 */
public interface QuestionDelegate {

	/**
	 * This method will get all questions of quiz types passed for a given
	 * course.
	 * 
	 * @param bookID
	 * @return list of question meta data
	 */
	List<QuestionMetadata> getQuestions(String bookID);

	/**
	 * Get the question XML of a given question id <code>questionId</code>
	 * 
	 * @param quuestionId
	 *            The question identification number
	 * @return The question XML for the given question id
	 * @throws NotFoundException
	 *             The question not found custom exceptions
	 * @throws InternalException
	 *             The application custom exceptions
	 */
	String getQuestionXmlById(String questionId);

	/**
	 * Save the question
	 * <code>question<code> under Container having id <code>ContainerID</code>
	 * 
	 * @param containerID
	 *            Container's identification number where question is going to
	 *            create
	 * @param question
	 *            Question details which will going to save in repository
	 * @return
	 * @throws NotFoundException
	 * @throws InternalException
	 *             The application custom exceptions
	 */
	String saveQuestion(QuestionEnvelop question, String bookTitle,
			String chapterTitle);
}
