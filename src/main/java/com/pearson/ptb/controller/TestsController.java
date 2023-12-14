package com.pearson.ptb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.QuestionOutput;
import com.pearson.ptb.bean.Test;
import com.pearson.ptb.service.MetadataService;
import com.pearson.ptb.service.TestService;
//import com.pearson.mytest.util.Swagger;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * To list all Tests for given book and Questions in the given test
 *
 */

@Controller

@Api(value = "tests", description = "tests")
public class TestsController extends BaseController {

	@Autowired

	@Qualifier("testService")
	private TestService testService;

	@Autowired

	@Qualifier("metadataService")
	private MetadataService metadataService;

	/**
	 * To fetch the details of a specific test
	 * 
	 * @param id
	 * @return Test
	 */

	//@ApiOperation(value = Swagger.GET_TESTS_VALUE, notes = Swagger.GET_TESTS_NOTE)

	@RequestMapping(value = "/tests/{id}", method = RequestMethod.GET)

	@ResponseBody
	public Test getTestbyId(@PathVariable String id) {
		return testService.getTestByID(id);
	}

	/**
	 * To fetch all publisher tests of given book id
	 * 
	 * @param id
	 * @return List<Test> of test
	 */

//	@ApiOperation(value = Swagger.GET_TESTS_VALUE, notes = Swagger.GET_TESTS_NOTE)

	@RequestMapping(value = "/books/{id}/tests", method = RequestMethod.GET)

	@ResponseBody
	public List<Test> getPublisherTestsByBookId(@PathVariable String id) {
		return testService.getPublisherTestsByBookId(id);
	}

	/**
	 * To fetch all Questions of given test id
	 * 
	 * @param id
	 * @return list of questions
	 */

//	@ApiOperation(value = Swagger.GET_TESTQUESTIONS_VALUE, notes = Swagger.GET_TESTQUESTIONS_NOTE)

	@RequestMapping(value = "/test/{id}/questions", method = RequestMethod.GET)

	@ResponseBody
	public List<QuestionOutput> getTestQuestions(@PathVariable String id) {

		List<QuestionOutput> questions = null;
		questions = testService.getTestQuestions(id);

		return questions;

	}

	/**
	 * To fetch Metadata of the test
	 * 
	 * @param testId
	 * @return Metadata
	 */
//	@ApiOperation(value = Swagger.GET_TESTS_METADATA_VALUE, notes = Swagger.GET_TESTS_METADATA_NOTE)

	@RequestMapping(value = "test/{testId}/metadata", method = RequestMethod.GET)

	@ResponseBody
	public Metadata getTestMetadata(@PathVariable String testId) {

		return metadataService.getMetadata(testId);

	}

}
