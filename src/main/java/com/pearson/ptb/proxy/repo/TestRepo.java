package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.Test;
import com.pearson.ptb.proxy.TestDelegate;

@Repository("tests")
public class TestRepo implements TestDelegate {

	@Override
	public Test getTestByID(String testID) {
		
		return null;
	}

}
