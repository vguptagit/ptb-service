package com.pearson.ptb.proxy.repo;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.proxy.TestVersionDelegate;

@Repository
public class TestVersionRepo implements TestVersionDelegate {

	@Override
	public TestResult createVersionTest(TestEnvelop test, String testId) {
		
		return null;
	}

	@Override
	public String getTestVersions(String testID) {
		
		return null;
	}

}
