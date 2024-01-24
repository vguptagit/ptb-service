package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.proxy.MyTestDelegate;

@Repository("myTestsRepo")
public class MyTestRepo implements MyTestDelegate {

	@Override
	public TestResult create(TestEnvelop test) {
		
		return null;
	}

	@Override
	public TestResult update(TestEnvelop test) {
		
		return null;
	}

	@Override
	public void delete(String testId) {
		
	}

}
