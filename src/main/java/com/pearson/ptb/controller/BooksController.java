package com.pearson.ptb.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.service.BookService;
import com.pearson.ptb.util.URLHelper;
import com.pearson.ptb.util.UserHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import jakarta.servlet.http.HttpServletRequest;

/**
 * To get the books and disciplines for the user
 *
 */
@RestController
@Api(value = "Books", description = "Book APIs")
public class BooksController extends BaseController {

	private static final Logger logger = LogManager
			.getLogger(BooksController.class);

	/**
	 * @Qualifier annotation searched for the value books in
	 *            appServlet-servlet.xml file created instance
	 */
	@Autowired
	@Qualifier("bookService")
	private BookService bookService;

	/**
	 * To get all books of the user
	 * 
	 * @return List<Book>, a list of all books
	 *
	 */
	@ApiOperation(value = "Returns a list of all Books", notes = "Returns a list of all books")
	@GetMapping("/books")
	@ResponseBody
	public List<Book> getAllBooks(HttpServletRequest request) {
		logger.info("Request received to get all books.");
		List<Book> books = null;
		String userId = UserHelper.getUserId(request);
		Map<String, String> searchCriteria = URLHelper
				.getQueryMap(request.getQueryString());

		logger.info("User ID: {}", userId);
		logger.info("Search Criteria: {}", searchCriteria);

		books = bookService.getBooks(searchCriteria, userId);

		logger.info("Number of books retrieved: {}", books.size());

		return books;

	}

	/**
	 * To get book for the given id
	 * 
	 * @param id
	 *            of the required book
	 * @return Book
	 */
	@GetMapping("/books/{id}")
	@ResponseBody
	// done
	public Book getBookById(@PathVariable String id) {
		return bookService.getBookByID(id);
	}

	/**
	 * To get disciplines
	 * 
	 * @return list of disciplines
	 *
	 */
	@GetMapping("/disciplines")
	@ResponseBody
	// done
	public List<String> getDisciplines() {
		List<String> disciplines;
		disciplines = bookService.getDisciplines();
		System.out.println("printing the bookservice object " + disciplines);
		return disciplines;
	}
	
	
	/*
	 * @GetMapping("/redis") public void name() throws IOException {
	 * CacheWrapper instance = CacheWrapper.getInstance();
	 * //instance.initializeJedis(); instance.set("manoj", "manojkvakue");
	 * 
	 * }
	 */
}
