package com.pearson.ptb.proxy.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Login;
import com.pearson.ptb.dataaccess.GenericMongoRepository;

import lombok.RequiredArgsConstructor;

@Repository("loginRepo")
@RequiredArgsConstructor
public class LoginRepo {

	private final GenericMongoRepository<Login, String> genericMongoRepository;


	/**
	 * Constructor to access DataAccessHelper to perform login operation.
	 * 
	 */

	/**
	 * This method will get the login count of the user.
	 * 
	 * @param userid
	 *            , represents the user.
	 * @return login count as a number.
	 */
	public int logLogin(String userid) {
		Login login = new Login();
		login.setUserid(userid);
		login.setDatetime(new Date());

		genericMongoRepository.save(login);

		return getLoginCount(userid);
	}

	private int getLoginCount(String userid) {
		Query query = new Query(Criteria.where("userid").is(userid));
		List<Login> loginList = genericMongoRepository.findAll(query);
		return loginList.size();
	}

}
