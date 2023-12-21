package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.bean.Books;
import com.pearson.ptb.bean.Container;
import com.pearson.ptb.bean.Containers;
import com.pearson.ptb.framework.exception.BadDataException;
import com.pearson.ptb.framework.exception.NotFoundException;
import com.pearson.ptb.proxy.BookDelegate;
import com.pearson.ptb.proxy.ContainerDelegate;
import com.pearson.ptb.util.Converter;

/**
 * Provides imported book details.
 */
@Service("importService")
public class ImportService {

	@Autowired
	@Qualifier("book")
	private BookDelegate bookDelegate;

	@Autowired
	@Qualifier("container")
	private ContainerDelegate containerDelegate;

	/**
	 * Save imported book details.
	 * 
	 * @param books
	 * @throws NotFoundException
	 * @throws BadDataException
	 */
	public void importBooks(Books books) {
		List<Container> containersTemp = new ArrayList<>();

		Book book = Converter.getDestinationBean(books, Book.class,
				Books.class);

		book.validateState();
		book.setReferenceBookid(book.getGuid());
		book.setGuid(UUID.randomUUID().toString());

		convertContainerJsonToList(books.getContainers(), containersTemp, "",
				book.getGuid());

		bookDelegate.save(book);
		containerDelegate.save(containersTemp);
	}

	/**
	 * Converts tree structured Json To List of containers format.
	 * 
	 * @param books
	 * @throws NotFoundException
	 * @throws BadDataException
	 */

	private void convertContainerJsonToList(List<Containers> containerList,
			List<Container> containersTemp, String parentId, String bookId) {
		for (Containers containers : containerList) {
			Container container = Converter.getDestinationBean(containers,
					Container.class, Containers.class);
			container.setParentId(parentId);
			container.setBookid(bookId);

			// Validate the container values
			container.validateState();
			containersTemp.add(container);

			if (containers.getContainers() != null
					&& !containers.getContainers().isEmpty()) {
				convertContainerJsonToList(containers.getContainers(),
						containersTemp, container.getGuid(), bookId);
			}
		}
	}
}
