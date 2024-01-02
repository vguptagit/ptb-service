package com.pearson.ptb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.bean.Container;
import com.pearson.ptb.service.ContainerService;
import com.pearson.ptb.util.SearchHelper;
import com.pearson.ptb.util.URLHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import jakarta.servlet.http.HttpServletRequest;

/**
 * To List the containers
 *
 */
@RestController
@Api(value = "Containers", description = "Containers")
public class ContainersController extends BaseController {

	/**
	 * @Qualifier annotation searched for the value books in
	 *            appServlet-servlet.xml file created instance
	 */

	@Autowired
	@Qualifier("containerService")
	private ContainerService containerService;

	/**
	 * To List all containers for the given Book
	 * 
	 * @param bookid
	 * @param quizTypes
	 *            optional
	 * @param flat
	 * @return List<Container> of the container
	 */

	@ApiOperation(value = "Returns a list of all containers for the given Book", notes = "Returns the list of "
			+ "all containers for the given book identified through the {bookid}")
	@RequestMapping(value = "/books/{bookid}/nodes", method = RequestMethod.GET)
	@ResponseBody
	public List<Container> getContainersByBook(@PathVariable String bookid,
			HttpServletRequest request,
			// @ApiParam(value = Swagger.GET_ALL_CONTAINERS, required = false)
			@RequestParam(required = false, defaultValue = "0") boolean flat) {

		Map<String, String> filterCriteria = URLHelper.getQueryMap(
				request.getQueryString(),
				SearchHelper.getQuestionFilterCriteriaMap());
		SearchHelper.updateSearchValues(filterCriteria);
		return containerService.getContainers(bookid, flat, filterCriteria);
	}

	/**
	 * To List container for the given bookId and containerId
	 * 
	 * @param bookid
	 * @param id
	 * @param quizTypes
	 * @param includeSelf
	 * @return List<Container> of the container
	 */
	@ApiOperation(value = "Return container childern for the given id", notes = "Return the list of container childrens identified by {id} for the given book "
			+ "identified through {bookid}")
	@RequestMapping(value = "/books/{bookid}/nodes/{id}/nodes", method = RequestMethod.GET)
	@ResponseBody
	public List<Container> getContainerChildrenById(@PathVariable String bookid,
			@PathVariable String id, HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") boolean includeSelf) {

		Map<String, String> filterCriteria = URLHelper.getQueryMap(
				request.getQueryString(),
				SearchHelper.getQuestionFilterCriteriaMap());
		SearchHelper.updateSearchValues(filterCriteria);
		return containerService.getContainers(bookid, id, filterCriteria,
				includeSelf);
	}

}
