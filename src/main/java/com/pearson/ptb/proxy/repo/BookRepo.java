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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.util.BookHelper;

/**
 * Implementation class which got implemented from interface BookDelegate
 * 
 * @see com.pearson.ptb.proxy.BookDelegate
 *
 */
@Repository("book")
public class BookRepo implements BookDelegate {

	private final  GenericMongoRepository<Book, String> genericMongoRepository;
	
	@Autowired
	private com.pearson.ptb.dataaccess.BookDataAccessHelper accessHelper;


	@Override
	public List<Book> getBooks(Map<String, String> criteria) {
		Map<String, String> filterCriteriaValues = validateFilterCriteria(criteria);

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

	@Override
	public List<String> getDisciplines() {

		List<Book> books = genericMongoRepository.findAll();
		return books.stream().map(Book::getDiscipline).distinct().collect(Collectors.toList());
	}

	@Override
	public void save(Book book) {
		// TODO Auto-generated method stub
		genericMongoRepository.save(book);

	}
	/*
	 * 
	 * private BookDataAccessHelper accessor;
	 * 
	 *//**
		 * Constructor to access the DataAccessHelper to perform Book operations.
		 * 
		 * @throws ConfigException
		 * @throws UnknownHostException
		 */
	/*
	 * public BookRepo() { //accessor = new BookDataAccessHelper(); }
	 * 
	 *//**
		 * This method will get the books from database.
		 * 
		 * @param criteria
		 * @return list of books.
		 */
	/*
	 * @Override public List<Book> getBooks(Map<String, String> criteria) {
	 * 
	 * Map<String, String> filterCriteriaValues = null; filterCriteriaValues =
	 * this.validateFilterCriteria(criteria);
	 * 
	 * List<Book> books = null; if (filterCriteriaValues.containsKey("s")) { books =
	 * accessor.getSearchedBooks(filterCriteriaValues.get("s")); } else { books =
	 * accessor.getByFilter(filterCriteriaValues); }
	 * 
	 * if (books.isEmpty()) { throw new NotFoundException("Books not found"); }
	 * 
	 * return books; }
	 * 
	 *//**
		 * This method will get the book from database based on book id.
		 * 
		 * @param bookID , represents id of the book.
		 * @return Book object.
		 */
	/*
	 * @Override public Book getBookByID(String bookID) { Book book = null; book =
	 * accessor.getById(bookID);
	 * 
	 * if (book == null) { throw new NotFoundException("Book not found"); }
	 * 
	 * return book; }
	 * 
	 *//**
		 * This method will get the title of the book from database based on book id.
		 * 
		 * @param bookID , represents id of the book.
		 * @return book title as a string.
		 */
	/*
	 * 
	 * @Override public String getTitle(String bookID) { return
	 * accessor.getBaseFieldById(bookID, "title"); }
	 * 
	 *//**
		 * This method will get the disciplines from database.
		 * 
		 */
	/*
	 * 
	 * 
	 * @Override public List<String> getDisciplines() { accessor = new
	 * BookDataAccessHelper(); System.out.println("lllllll"+accessor); return
	 * accessor.getDistinctValuesByField("discipline"); }
	 * 
	 *//**
		 * This method will get the books of the disciplines from database.
		 * 
		 * @param disciplines , represents the list of disciplines.
		 * @return list of books.
		 */
	/*
	 * 
	 * 
	 * @Override public List<Book> getBooksByDisciplines(List<String> disciplines) {
	 * 
	 * List<Book> books = null; books = accessor.getByFieldHavingAnyOf("discipline",
	 * disciplines); return books; }
	 * 
	 *//**
		 * @see com.pearson.mytest.proxy.BookDelegate#save(Book)
		 */

	/*
	 * 
	 * @Override public void save(Book book) { accessor = new
	 * BookDataAccessHelper(); accessor.save(book);
	 * 
	 * }
	 * 
	 * 
	 *//**
		 * Check for "s" request param, if system finds the value for "s" then system
		 * will do search with "or" operation on (id,title,authors, isbn,
		 * publisher,discipline) Hence system will ignore other request param values if
		 * user has provided
		 * 
		 * @param filterCriteria
		 * @return
		 */
	private Map<String, String> validateFilterCriteria(Map<String, String> filterCriteria) {
		Map<String, String> filterCriteriaValues = new HashMap<String, String>();

		if (filterCriteria.containsKey("s") && (filterCriteria.get("s") != null)) {
			filterCriteriaValues.put("s", filterCriteria.get("s"));
		} else {
			for (Map.Entry<String, String> entry : filterCriteria.entrySet()) {
				if (BookHelper.getFilterCriteriaMap().containsKey(entry.getKey())) {

					filterCriteriaValues.put(BookHelper.getFilterCriteriaMap().get(entry.getKey()), entry.getValue());
				}

			}
		}

		return filterCriteriaValues;

	}

}
