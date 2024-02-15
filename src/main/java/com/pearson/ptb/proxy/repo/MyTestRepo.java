package com.pearson.ptb.proxy.repo;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.proxy.MyTestDelegate;

import lombok.RequiredArgsConstructor;

@Repository("myTestsRepo")
@RequiredArgsConstructor
public class MyTestRepo implements MyTestDelegate {
	
	private final GenericMongoRepository<TestEnvelop, String> userTestRepository;

	@Override
	public TestResult create(TestEnvelop test) {
		if(test.getGuid() == null) {
			String uId = UUID.randomUUID().toString();
			test.setGuid(uId);
			test.getmetadata().setGuid(uId);
			test.getBody().setGuid(uId);
		}
		userTestRepository.save(test);
		TestResult testResult = new TestResult();
		testResult.setGuid(test.getGuid());
		return testResult;
	}

	@Override
	public TestResult update(TestEnvelop test) {
		
		return null;
	}

	@Override
	public void delete(String testId) {
		
	}

}
