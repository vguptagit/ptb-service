/**
 * 
 */
package com.pearson.ptb.proxy.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.dataaccess.BookDataAccessHelper;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.util.BookHelper;

import lombok.RequiredArgsConstructor;

/**
 * Implementation class which got implemented from interface BookDelegate
 * 
 * @see com.pearson.ptb.proxy.BookDelegate
 *
 */
@Repository("book")
@RequiredArgsConstructor
public class BookRepo implements BookDelegate {

	private final  GenericMongoRepository<Book, String> genericMongoRepository;
	
	@Autowired
	private BookDataAccessHelper accessHelper;


	@Override
	public List<Book> getBooks(Map<String, String> criteria) {
		Map<String, String> filterCriteriaValues = validateFilterCriteria(
				criteria);

		List<Book> books = filterCriteriaValues.containsKey("s")
				? accessHelper.getSearchedBooks(filterCriteriaValues.get("s"))
				: genericMongoRepository.getByFilter(filterCriteriaValues);

		return Optional.of(books).filter(list -> !list.isEmpty())
				.orElseThrow(() -> new NotFoundException("Books not found"));
	}

	@Override
	public List<Book> getBooksByDisciplines(List<String> disciplines) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Retrieves a book by its unique identifier.
	 *
	 * @param bookID
	 *            The unique identifier of the book to retrieve.
	 * @return The book with the specified ID.
	 * @throws NotFoundException
	 *             If the book with the given ID is not found.
	 */
	@Override
	public Book getBookByID(String bookID) {

		Book book = null;
		book = genericMongoRepository.findById(bookID);
		if (book == null) {
			throw new NotFoundException("Book not found");
		}
		return book;
	}

	@Override
	public String getTitle(String bookID) {
		// TODO Auto-generated method stub
		
		return null;
	}

	/**
	 * Retrieves a list of distinct disciplines from the available books.
	 *
	 * @return A list of unique disciplines associated with the available books.
	 */
	@Override
	public List<String> getDisciplines() {

		List<Book> books = genericMongoRepository.findAll();
		return books.stream().map(Book::getDiscipline).distinct()
				.collect(Collectors.toList());
	}

	/**
	 * @see com.pearson.mytest.proxy.BookDelegate#save(Book)
	 */
	@Override
	public void save(Book book) {
		genericMongoRepository.save(book);

	}

	/**
	 * Check for "s" request param, if system finds the value for "s" then
	 * system will do search with "or" operation on (id,title,authors, isbn,
	 * publisher,discipline) Hence system will ignore other request param values
	 * if user has provided
	 * 
	 * @param filterCriteria
	 * @return
	 */
	private Map<String, String> validateFilterCriteria(
			Map<String, String> filterCriteria) {
		Map<String, String> filterCriteriaValues = new HashMap<String, String>();

		if (filterCriteria.containsKey("s")
				&& (filterCriteria.get("s") != null)) {
			filterCriteriaValues.put("s", filterCriteria.get("s"));
		} else {
			for (Map.Entry<String, String> entry : filterCriteria.entrySet()) {
				if (BookHelper.getFilterCriteriaMap()
						.containsKey(entry.getKey())) {

					filterCriteriaValues.put(BookHelper.getFilterCriteriaMap()
							.get(entry.getKey()), entry.getValue());
				}

			}
		}

		return filterCriteriaValues;

	}

}
