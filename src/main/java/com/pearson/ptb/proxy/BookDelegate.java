/**
 * 
 */
package com.pearson.ptb.proxy;

import java.util.List;
import java.util.Map;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.framework.exception.NotFoundException;

/**
 * This interface defines the contract for accessing the books from repository
 *
 */

public interface BookDelegate {

	/**
	 * Get all books from the repository
	 * 
	 * @param criteria
	 *            The HashMap search criteria,if this is empty then system will
	 *            give all books. Return <code>null</code> if there is no book
	 *            exist.
	 * @return the list of books, if any
	 * @throws NotFoundException
	 *             Books not found custom exception
	 */
	List<Book> getBooks(Map<String, String> criteria);
	
	List<Book> getBooksByDisciplines(List<String> disciplines);

	/**
	 * Get the book for the given book id number <code>bookID</code> from the
	 * repository. Return <code>null</code> if there is no book exist.
	 * 
	 * @param bookID
	 *            Book id number to be search.
	 * @throws NotFoundException
	 *             The book not found custom exception
	 */
	Book getBookByID(String bookID);

	/**
	 * Get Title of the book
	 * @param bookID, Book id of the book bean
	 * @return title of the book
	 * @throws NotFoundException
	 */
	String getTitle(String bookID);

	/**
	 * Get distinct disciplines from Books collection
	 * @return
	 */
	List<String> getDisciplines();

	/**
	 * Saving the imported book
	 * @param book
	 */
	void save(Book book);

 
}
