package com.pearson.ptb.proxy.repo;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.TestEnvelop;
import com.pearson.ptb.bean.TestResult;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.framework.exception.ResourceNotFoundException;
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
		TestEnvelop testToUpdate = this.getTest(test.getmetadata().getGuid());
		if (testToUpdate != null) {
			testToUpdate.setTitle(test.getTitle());
			testToUpdate.setBody(test.getBody());
			testToUpdate.setmetadata(test.getmetadata());
			TestEnvelop savedItem = userTestRepository.save(testToUpdate);
			TestResult testResult = new TestResult();
			testResult.setGuid(savedItem.getGuid());
			return testResult;
		}
		return new TestResult();
	}

	@Override
	public void delete(String folderId, String testId) {

		UserFolder folder = userTestsFolderRepository.findById(folderId);
		if (folder != null) {
			List<TestBinding> testBindings = folder.getTestBindings();
			boolean removed = testBindings.removeIf(binding -> binding.getTestId().equals(testId));
			if (!removed) {
				throw new ResourceNotFoundException("Test not found for the given testId");
			}
			userTestsFolderRepository.save(folder);
		} else {
			throw new ResourceAccessException("Folder not found for the given folderId");
		}

		userTestRepository.deleteById(testId);

	}

	@Override
	public void deleteLinkedDataRecursively(String id) {
		UserFolder dataToDelete = userTestsFolderRepository.findById(id);

		if (dataToDelete != null) {
			deleteLinkedData(dataToDelete);
		} else {
			throw new ResourceNotFoundException("Folder not found for the given Id");
		}
	}

	private void deleteLinkedData(UserFolder data) {
		List<TestBinding> testBindings = data.getTestBindings();
		for (TestBinding binding : testBindings) {
			userTestRepository.deleteById(binding.getTestId());
		}
		userTestsFolderRepository.delete(data);
		List<UserFolder> childData = userTestsFolderRepository.findByParentId(data.getGuid());
		// Recursively delete linked data
		for (UserFolder child : childData) {
			deleteLinkedData(child);
		}
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

	@Override
	public TestEnvelop getTest(String testId) {
		Query query = userTestRepository.createDataQuery();
		return userTestRepository.findOne(query.addCriteria(Criteria.where(QueryFields.GUID).is(testId)),
				TestEnvelop.class);
	}

}
