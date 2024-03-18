package com.pearson.ptb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pearson.ptb.bean.QuestionEnvelop;
import com.pearson.ptb.bean.QuestionMetadata;
import com.pearson.ptb.bean.QuestionOutput;
import com.pearson.ptb.proxy.aws.bean.QuestionResponse;
import com.pearson.ptb.service.QuestionService;
import com.pearson.ptb.util.SearchHelper;
import com.pearson.ptb.util.Swagger;
import com.pearson.ptb.util.URLHelper;
import com.pearson.ptb.util.UserHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * To list all Questions for given Book and Container
 *
 */

@Controller
@Tag(name = "Questions", description = "questions")
public class QuestionsController extends BaseController {

	/**
	 * @Qualifier annotation searched for the value books in appServlet-servlet.xml
	 *            file created instance
	 */

	@Autowired

	@Qualifier("questionService")
	private QuestionService questionService;

	/**
	 * To get all Questions for given Book id and Container id
	 * 
	 * @param bookid
	 * @param nodeId  id of the Container
	 * @param request
	 * @param flat
	 * @return List of QuestionMetadata
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = "Returns list of all Questions for given Book and Container", description = Swagger.GET_QUESTIONS_BY_CONTAINER_NOTE)
	@RequestMapping(value = "/books/{bookid}/nodes/{nodeId}/questions", method = RequestMethod.GET)

	@ResponseBody
	public List<QuestionMetadata> getQuestionsByContainer(@PathVariable String bookid, @PathVariable String nodeId,
			HttpServletRequest request,

			
			@RequestParam(required = false, defaultValue = "0") boolean flat) {

		Map<String, String> filterCriteria = URLHelper.getQueryMap(request.getQueryString(),
				SearchHelper.getQuestionFilterCriteriaMap());
		SearchHelper.updateSearchValues(filterCriteria);
		return questionService.getQuestions(bookid, nodeId, filterCriteria, flat);
	}

	/**
	 * To get Questions for given id
	 * 
	 * @param id
	 * @return Question
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.GET_QUESTION_BY_ID_VALUE, description = Swagger.GET_QUESTION_BY_ID_NOTE)
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET)

	@ResponseBody
	public String getQuestionXmlById(@PathVariable String id) {

		return questionService.getQuestionXmlById(id);

	}

	/**
	 * To get the questions created by the user
	 * 
	 * @param folderId
	 * @param request
	 * @param response
	 * @return list of QuestionOutput
	 */

	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = Swagger.GET_USERQUESTIONS_VALUE, description = Swagger.GET_USERQUESTIONS_NOTE)
	@RequestMapping(value = "/my/questions", method = { RequestMethod.GET, RequestMethod.HEAD })

	@ResponseBody
	public List<QuestionOutput> getUserQuestions(@RequestParam(required = false) String folderId,
			HttpServletRequest request, HttpServletResponse response,

			@RequestParam(required = false, defaultValue = "0") boolean flat) {

		String userId = UserHelper.getUserId(request);

		int userQuestionsCount = questionService.getUserQuestionsCount(userId);
		response.addHeader("X-Return-Count", String.valueOf(userQuestionsCount));

		List<QuestionOutput> questions = null;
		
		if (request.getMethod().equals(RequestMethod.GET.name()) ) {
			questions = questionService.getUserQuestions(userId, folderId, flat);
		}

		return questions;
	}

	/**
	 * To save the user created questions
	 * 
	 * @param questions
	 * @param request
	 * @param response
	 * @return List
	 */
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })
	@Operation(summary = "Save Questions", description = Swagger.SAVE_QUESTIONS)
	@RequestMapping(value = "/my/questions", method = RequestMethod.POST)

	@ResponseBody
	public List<QuestionResponse> saveQuestions(@Parameter(name = "body") @RequestBody List<QuestionEnvelop> questions,
			HttpServletRequest request, HttpServletResponse response) {

		List<QuestionResponse> responseMessage = questionService.saveQuestions(questions, UserHelper.getUserId(request), null);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return responseMessage;

	}

}
