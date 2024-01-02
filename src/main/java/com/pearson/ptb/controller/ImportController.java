package com.pearson.ptb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.bean.Books;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.ConfigException;
import com.pearson.ptb.framework.exception.InternalException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.service.ImportService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * importing additional books
 */
@RestController
@Api(value = "Books", description = "Import Book APIs")
public class ImportController extends BaseController {

	@Autowired
	@Qualifier("importService")
	private ImportService importService;

	/**
	 * Save the question to repository
	 * 
	 * @param question,
	 *            JSON data having meta data and qti2.1 xml
	 * @param bookid,
	 *            Book id under which question has to be created.
	 * @param nodeId,
	 *            Node id under which question has to be created.
	 * @return response message from repository
	 * @throws NotFoundException
	 * @throws InternalException
	 * @throws ConfigException
	 * @throws BadDataException
	 */
	// @ApiOperation(value = "import books", notes = Swagger.SAVE_QUESTION)
	@RequestMapping(value = "/books/import", method = RequestMethod.POST)
	@ResponseBody
	// done
	public void importBooks(@ApiParam(name = "body") @RequestBody Books books) {

		importService.importBooks(books);
		System.out.println("Created sssssss");
	}

}
