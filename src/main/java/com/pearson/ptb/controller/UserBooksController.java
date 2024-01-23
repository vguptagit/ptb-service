package com.pearson.ptb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.ptb.bean.UserBook;
import com.pearson.ptb.service.UserBookService;
import com.pearson.ptb.util.UserHelper;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * User Book APIs for managing migrated IC course contents
 *
 */
@Controller
@Tag(name = "User Books", description = "User Book APIs for managing migrated IC course contents")
public class UserBooksController extends BaseController {

	@Autowired
	@Qualifier("userBookService")
	private UserBookService userBookService;

	/**
	 * To List of all Users Books migrated from Pegasus, and yet to be imported to
	 * Evalu8
	 * 
	 * @param request
	 * @return List of UserBook
	 */
	@Operation(summary = "List of all Users Books migrated from Pegasus, and yet to be imported to Evalu8", description = "List all non-imported user books")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success") })

	@RequestMapping(value = "/my/importbooks", method = RequestMethod.GET)
	@ResponseBody
	public List<UserBook> getUserBooks(HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		return userBookService.getUserBooks(userId);
	}

	/**
	 * To import books of given userBookIds
	 * 
	 * @param userBookIds
	 * @param request
	 */
	@Operation(summary = "Users Books to be imported", description = "Import selected user books")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created") })

	@RequestMapping(value = "/my/importbooks", method = RequestMethod.POST)
	@ResponseBody
	public void importUserBooks(@RequestBody List<String> userBookIds, HttpServletRequest request) {

		String userId = UserHelper.getUserId(request);
		userBookService.importUserBooks(userBookIds, userId);
	}
}
