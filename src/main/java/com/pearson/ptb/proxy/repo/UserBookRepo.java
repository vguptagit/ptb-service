package com.pearson.ptb.proxy.repo;

import java.util.List;

import com.pearson.ptb.bean.UserBook;
import com.pearson.ptb.proxy.UserBookDelegate;

import org.springframework.stereotype.Repository;

@Repository("userBookRepo")
public class UserBookRepo implements UserBookDelegate {

	@Override
	public List<UserBook> getUserBooks(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserBooks(List<UserBook> userBooks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserBook getUserBook(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserBook(UserBook userBook) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * private DataAccessHelper<UserBook> accessor;
	 * 
	 *//**
		 * Constructor to access DataAccessHelper to perform user book operation.
		 * 
		 */
	/*
	 * public UserBookRepo() { //accessor = new
	 * DataAccessHelper<UserBook>(UserBook.class); }
	 * 
	 *//**
		 * This method will get the user books from database.
		 * 
		 * @param userId , represents the user.
		 * @return list of user books.
		 */
	/*
	 * @Override public List<UserBook> getUserBooks(String userId) {
	 * 
	 * Query<UserBook> query = accessor.getDataQuery(); return query
	 * .filter(QueryFields.USERID, userId).asList();
	 * 
	 * }
	 * 
	 *//**
		 * This method will get the user book from database.
		 * 
		 * @param userId , represents the user.
		 * @return UserBook.
		 */
	/*
	 * @Override public UserBook getUserBook(String bookId) {
	 * 
	 * Query<UserBook> query = accessor.getDataQuery();
	 * 
	 * return query.filter(QueryFields.GUID, bookId).get(); }
	 * 
	 *//**
		 * This method will save the user books to database.
		 * 
		 * @param userBooks , represents the books which belongs to user.
		 */
	/*
	 * @Override public void saveUserBooks(List<UserBook> userBooks) {
	 * accessor.save(userBooks); }
	 * 
	 *//**
		 * This method will save the user book to database.
		 * 
		 * @param userBooks , represents the book which belongs to user.
		 *//*
			 * @Override public void saveUserBook(UserBook userBook) {
			 * accessor.save(userBook); }
			 */
}
