package com.pearson.ptb.proxy.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.proxy.MyTestDelegate;

import lombok.RequiredArgsConstructor;

@Repository("myTestsRepo")
@RequiredArgsConstructor
public class MyTestRepo implements MyTestDelegate {

	private final GenericMongoRepository<TestEnvelop, String> userTestRepository;
	private final GenericMongoRepository<UserFolder, String> userTestsFolderRepository;

	@Override
	public TestResult create(TestEnvelop test) {
		if (test.getGuid() == null) {
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

	@Override
	public void deleteTestBindings(String testId) {
		UserFolder userFolder = userTestsFolderRepository.findById(testId);

		if (userFolder != null && userFolder.getTestBindings() != null) {
			List<TestBinding> testBindings = userFolder.getTestBindings();

			for (TestBinding testBinding : testBindings) {
				TestEnvelop testEnvelop = userTestRepository.findById(testBinding.getTestId());
				if (testEnvelop != null && testEnvelop.getGuid().equals(testBinding.getTestId())) {
					userTestRepository.deleteById(testBinding.getTestId());
					userTestsFolderRepository.deleteById(testId);
				}
			}
		} else {
			userTestsFolderRepository.deleteById(testId);
		}
	}

}
