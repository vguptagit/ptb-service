package com.pearson.ptb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.service.BookService;
import com.pearson.ptb.util.URLHelper;
import com.pearson.ptb.util.UserHelper;
import com.wordnik.swagger.annotations.ApiOperation;

import jakarta.servlet.http.HttpServletRequest;

/**
 * To get the books and disciplines for the user
 *
 */
@RestController
//@Api(value = "Books", description = "Book APIs")
public class BooksController extends BaseController {

	/**
	 * @Qualifier annotation searched for the value books in appServlet-servlet.xml
	 *            file created instance
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

	@RequestMapping(value = "/books", method = RequestMethod.GET)

	@ResponseBody
	public List<Book> getAllBooks(HttpServletRequest request) {

		List<Book> books = null;
		String userid = UserHelper.getUserId(request);

		Map<String, String> searchCriteria = null;
		searchCriteria = URLHelper.getQueryMap(request.getQueryString());
		books = bookService.getBooks(searchCriteria, userid);

		return books;

	}

	/**
	 * To get book for the given id
	 * 
	 * @param id of the required book
	 * @return Book
	 *//*
		 * @ApiOperation(value = "Return Book for given id", notes =
		 * "Return details of the book for the given {id}")
		 * 
		 * @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
		 * 
		 * @ResponseBody public Book getBookById(@PathVariable String id) {
		 * 
		 * return bookService.getBookByID(id);
		 * 
		 * }
		 */

	@GetMapping("/books/{id}")
	public Book getBookById(@PathVariable String id) {
		return bookService.getBookByID(id);
	}

	/**
	 * To get disciplines
	 * 
	 * @return list of disciplines
	 *
	 */

	@RequestMapping(value = "/disciplines", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getDisciplines() {
		List<String> disciplines;

		disciplines = bookService.getDisciplines();
		System.out.println("printing the bookservice object " + disciplines);
		return disciplines;
	}
}
