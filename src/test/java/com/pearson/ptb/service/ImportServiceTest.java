/*
 * package com.pearson.ptb.service; import static org.mockito.Mockito.*;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
 * import org.mockito.InjectMocks; import org.mockito.Mock; import
 * org.mockito.MockitoAnnotations; import
 * org.springframework.beans.factory.annotation.Qualifier;
 * 
 * import com.pearson.ptb.bean.Book; import com.pearson.ptb.bean.Books; import
 * com.pearson.ptb.bean.Container; import com.pearson.ptb.bean.Containers;
 * import com.pearson.ptb.proxy.BookDelegate; import
 * com.pearson.ptb.proxy.ContainerDelegate; import
 * com.pearson.ptb.service.ImportService;
 * 
 * @RunWith(MockitoJUnitRunner.class) class ImportServiceTest {
 * 
 * @InjectMocks private ImportService importService;
 * 
 * @Mock
 * 
 * @Qualifier("book") private BookDelegate bookDelegate;
 * 
 * @Mock
 * 
 * @Qualifier("container") private ContainerDelegate containerDelegate;
 * 
 * @BeforeEach public void setup() { MockitoAnnotations.openMocks(this); }
 * 
 * @Before public void setUp() {
 * when(bookDelegate.save(any(Book.class))).thenReturn(new Book("Test Title",
 * "Test Author")); when(containerDelegate.save(anyList())).thenReturn(new
 * ArrayList<Container>()); }
 * 
 * 
 * @Test public void testImportBooks() { // Given Books books = new Books();
 * books.setGuid("test-guid");
 * 
 * // When importService.importBooks(books);
 * 
 * // Then verify(bookDelegate, times(1)).save(any(Book.class));
 * verify(containerDelegate, times(1)).save(anyList()); } }
 * 
 * 
 */