package com.pearson.ptb.dataaccess;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Book;

@Repository
public class BookDataAccessHelper extends GenericMongoRepository<Book, String> {

	 @Autowired
	    public BookDataAccessHelper(MongoOperations mongoOperations) {
	        super(mongoOperations, Book.class);
	    }
	public List<Book> getSearchedBooks(String s) {
		Criteria searchCriteria = new Criteria().orOperator(Criteria.where("guid").regex(s, "i"),
				Criteria.where("title").regex(s, "i"), Criteria.where("isbn").regex(s, "i"),
				Criteria.where("publisher").regex(s, "i"), Criteria.where("authors").regex(s, "i"),
				Criteria.where("discipline").regex(s, "i"));

		Query query = Query.query(searchCriteria);
		return findAll(query);
	}
}
