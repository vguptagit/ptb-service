package com.pearson.ptb.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pearson.ptb.bean.Book;
import com.pearson.ptb.proxy.BookDelegate;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookDelegate bookDelegate;

    @InjectMocks
    private BookService bookService;

    @Test
    void testGetDisciplines() {
        List<String> mockDisciplines = Arrays.asList("Mathematics", "History");
        when(bookDelegate.getDisciplines()).thenReturn(mockDisciplines);

        List<String> result = bookService.getDisciplines();

        assertEquals(mockDisciplines, result);
    }
    

    @Test
    public void testGetBookByID_BookFound() {
        String bookId = "testBookId";
        Book expectedBook = new Book();
        expectedBook.setGuid(bookId);
        when(bookDelegate.getBookByID(bookId)).thenReturn(expectedBook);
        Book result = bookService.getBookByID(bookId);
        verify(bookDelegate, times(1)).getBookByID(bookId);
        assertNotNull(result);
        assertEquals(bookId, result.getGuid());
    }


    

    
}
