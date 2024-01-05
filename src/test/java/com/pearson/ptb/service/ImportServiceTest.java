package com.pearson.ptb.service;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.bean.Books;
import com.pearson.ptb.bean.Container;
import com.pearson.ptb.bean.Containers;
import com.pearson.ptb.proxy.repo.BookRepo;
import com.pearson.ptb.proxy.repo.ContainerRepo;

@ExtendWith(MockitoExtension.class)
class ImportServiceTest {

	@Mock
	private BookRepo bookRepo;

	@Mock
	private ContainerRepo containerRepo;

	@InjectMocks
	ImportService importService;

	// to hold Fake objects
	Book bookFake;
	Books booksFake;
	List<Container> containersFake;

	@BeforeEach
	public void setUp() throws Exception {
		// prepare fake objects
		bookFake = generateFakeBook();
		containersFake = generateFakeContainers();
		booksFake = generateFakeBooks();
	}

	@Test()
	void testImportBooks() throws Exception {
//		Mockito.doThrow(new RuntimeException("bookRepo")).when(bookRepo)
//				.save(Mockito.any(Book.class));
//		Mockito.doThrow(new RuntimeException("containerRepo"))
//				.when(containerRepo).save(Mockito.anyList());
		doNothing().when(bookRepo).save(any());
		doNothing().when(containerRepo).save(anyList());
		importService.importBooks(booksFake);
		
		//verify(bookRepo, times(1)).save(bookFake);
		//vrify(containerRepo, times(1)).save(containersFake);
	}

	private Books generateFakeBooks() {
		Books booksFake = new Books();

		List<String> author = new ArrayList<>();
		author.add("author");
		author.add("Author2");

		Containers containers = new Containers();
		containers.setGuid("8350785119");
		containers.setTitle("Chapter 1: The Science of Psychology_8350785116");

		List<Containers> containersList = new ArrayList<>();
		containersList.add(containers);

		booksFake.setGuid("Book_ID9121");
		booksFake.setTitle("Psychology, 3e_No_Chapters & Book9_Book_ID9116");

		String guid = java.util.UUID.randomUUID().toString();
		booksFake.setGuid(guid);
		booksFake.setTitle("title");
		booksFake.setIsbn10("");
		booksFake.setIsbn13("");
		booksFake.setEditionNumber("1");
		booksFake.setDiscipline("Law");
		booksFake.setIsbn("9780205832888");
		booksFake.setType("MyTest");
		booksFake.setAuthors(author);
		booksFake.setPublisher("Allyn & Bacon");
		booksFake.setContainers(containersList);

		return booksFake;
	}

	private List<Container> generateFakeContainers() {
		List<Container> containersFake = new ArrayList<>();
		Container container = new Container();
		container.setParentId("parentId");
		container.setBookid("bookId");
		container.setTitle("title");
		containersFake.add(container);

		return containersFake;
	}

	private Book generateFakeBook() {
		Book bookFake = new Book();

		String guid = java.util.UUID.randomUUID().toString();
		bookFake.setGuid(guid);
		bookFake.setTitle("title");

		return bookFake;
	}
}
