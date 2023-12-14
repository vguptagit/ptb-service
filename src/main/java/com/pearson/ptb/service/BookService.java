package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.UserSettingsDelegate;

/**
 * 
 * Serves the book and discipline related information.
 *
 */
@Service("bookService")
public class BookService {

	/**
	 * 
	 * 
	 */

	@Autowired
	@Qualifier("book")
	private BookDelegate bookDelegate;

	@Autowired
	@Qualifier("usersettings")
	private UserSettingsDelegate userSettingService;

	/**
	 * This will get the list of books depending on the search criteria
	 * 
	 * @param searchCriteria to search the book
	 * @return List of books
	 * @throws NotFoundException
	 */
	public List<Book> getBooks(Map<String, String> searchCriteria, String userid) {
		List<Book> books = null;
		List<Book> userSelectedbooks = new ArrayList<Book>();

		if (searchCriteria.containsKey("userBooks") && Boolean.valueOf(searchCriteria.get("userBooks"))) {
			List<String> userbooks = null;
			books = bookDelegate.getBooks(searchCriteria);
			userbooks = userSettingService.getUserBooks(userid);

			for (String bookid : userbooks) {
				for (Book book : books) {
					if (bookid.equals(book.getGuid())) {
						userSelectedbooks.add(book);
					}
				}
			}

			return userSelectedbooks;
		} else {
			return bookDelegate.getBooks(searchCriteria);
		}
	}

	/**
	 * Gets the list of disciplines/areas Like "Law","Nursing" ect
	 * 
	 * @return list of disciplines
	 * @throws NotFoundException
	 */

	public List<String> getDisciplines() {
		return bookDelegate.getDisciplines();
	}

	/**
	 * Gets the book detail by book id
	 * 
	 * @param bookID
	 * @return book details
	 * @throws NotFoundException
	 */
	public Book getBookByID(String bookID) {
		return bookDelegate.getBookByID(bookID);
	}
}
