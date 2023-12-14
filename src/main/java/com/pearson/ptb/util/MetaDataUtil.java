package com.pearson.ptb.util;

import com.pearson.ptb.bean.Activity;
import com.pearson.ptb.bean.Book;
import com.pearson.ptb.bean.Container;

public class MetaDataUtil {
	
	private MetaDataUtil(){
		
	}

	/**
	 * setting the Node specific properties in QuestionEnvelop of the PAF package
	 * @param chapter, Container bean
	 * @param pafQuestion, paf question to be updated with container data
	 */
	public static void setNodeMetadata(Container chapter,
			Activity metadata) {
		metadata.setChapterTitle(chapter.getTitle());
	}
	
	/**
	 * setting book specific properties in QuestionEnvelop of the PAF package
	 * @param book, Book bean with data
	 * @param pafQuestion, paf questin to be updated with book data
	 */
	public static void setBookMetadata(Book book,
			Activity metadata) {
		metadata.setBookAuthor(book.getAuthors().get(0));
		metadata.setBookISBN(book.getIsbn());
		metadata.setBookTitle(book.getTitle());
		metadata.setEditionNumber(book.getEditionNumber());
	}
	
}
