package com.pearson.ptb.proxy;

import java.util.List;

import com.pearson.ptb.bean.UserBook;
/**
 * This interface defines the contract for accessing the User books from
 * repository
 */
public interface UserBookDelegate {

	List<UserBook> getUserBooks(String userId);
	
	void saveUserBooks(List<UserBook> userBooks);

	UserBook getUserBook(String bookId);

	void saveUserBook(UserBook userBook); 
}
