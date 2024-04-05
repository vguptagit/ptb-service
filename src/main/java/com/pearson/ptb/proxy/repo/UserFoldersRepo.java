package com.pearson.ptb.proxy.repo;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.bean.UserQuestionsFolder;
import com.pearson.ptb.dataaccess.GenericMongoRepository;
import com.pearson.ptb.proxy.UserFoldersDelegate;

import lombok.RequiredArgsConstructor;

/**
 * Implementation class which got implemented from interface UserFoldersDelegate
 * 
 * @see com.pearson.ptb.proxy.UserFoldersDelegate
 *
 */
@Repository("userFoldersRepo")
@RequiredArgsConstructor
public class UserFoldersRepo implements UserFoldersDelegate {
	
	private final GenericMongoRepository<UserFolder, String> genericMongoRepository;
	private final GenericMongoRepository<UserQuestionsFolder, String> userQuestionFolderRepository;

	@Override
	public void saveFolders(List<UserFolder> folders) {
		genericMongoRepository.saveAll(folders);

	}
	
	@Override
	public void updateFolder(UserFolder folder) {
		UserFolder folderToUpdate = getFolder(folder.getGuid());
		if(folder.getParentId() != null) folderToUpdate.setParentId(folder.getParentId());
		folderToUpdate.setSequence(folder.getSequence());
		if(folder.getTitle() != null) folderToUpdate.setTitle(folder.getTitle());
		if(CollectionUtils.isNotEmpty(folder.getTestBindings())) {
			folderToUpdate.setTestBindings(folder.getTestBindings());
		}
		genericMongoRepository.save(folderToUpdate);
	}

	@Override
	public void saveUserQuestionFolders(List<UserQuestionsFolder> questionFolders) {
		if(CollectionUtils.isNotEmpty(questionFolders)) {
			for(UserQuestionsFolder questionFolder : questionFolders) {
				userQuestionFolderRepository.save(questionFolder);
			}
		}
		
	}

	@Override
	public UserFolder getFolder(String folderId) {
		Query query = genericMongoRepository.createDataQuery();
		return genericMongoRepository.findOne(
				query.addCriteria(
						Criteria.where(QueryFields.GUID).is(folderId)),
				UserFolder.class);
	}
	@Override
	public void deleteFolders(List<String> ids) {
		genericMongoRepository.delete(ids);

	}

	@Override
	public void saveFolder(UserFolder folder) {

		genericMongoRepository.save(folder);
	}

	@Override
	public void deleteFolder(String id) {

		genericMongoRepository.deleteById(id);
	}

	@Override
	public UserFolder getMyTestRoot(String userID) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(
				Criteria.where("userId").is(userID).and("parentId").is(userID));

		UserFolder rootFolder = genericMongoRepository.findOne(query,
				UserFolder.class);

		if (rootFolder == null) {
			UserFolder folder = new UserFolder();

			folder.setGuid(UUID.randomUUID().toString());
			folder.setUserID(userID);
			folder.setParentId(userID);
			folder.setSequence(1.0);
			genericMongoRepository.save(folder);

			rootFolder = genericMongoRepository.findOne(query,
					UserFolder.class);
		}

		return rootFolder;

	}

	@Override
	public UserQuestionsFolder getQuestionFoldersRoot(String userID) {
		
        return userQuestionFolderRepository.findOneByUserIdAndParentId(userID, userID);

	}

	@Override
	public UserFolder getTestFolder(String testId) {
		
		return null;
	}

	@Override
	public UserFolder getFolderByTitle(String title, String parentId,
			String userId) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.USERID).is(userId)
				.and(QueryFields.PARENTID).is(parentId).and(QueryFields.TITLE)
				.is(title));

		return genericMongoRepository.findOne(query, UserFolder.class);
	}

	@Override
	public UserQuestionsFolder getQuestionsFolderByTitle(String title,
			String parentId, String userId) {
	
		return null;
	}

	@Override
	public UserQuestionsFolder getMyQuestionRoot(String userID) {
		Query query = userQuestionFolderRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.USERID).is(userID))
		.addCriteria(Criteria.where(QueryFields.PARENTID).is(userID));
		List<UserQuestionsFolder> rootFolders = userQuestionFolderRepository.findAll(query, UserQuestionsFolder.class);
		
		
		if(CollectionUtils.isEmpty(rootFolders)) {
			
			UserQuestionsFolder folder = new UserQuestionsFolder();
			
			folder.setGuid(UUID.randomUUID().toString());
			folder.setUserID(userID);
			folder.setParentId(userID);
			folder.setSequence(1.0);
			saveFolder(folder);

			rootFolders = userQuestionFolderRepository.findAll(query, UserQuestionsFolder.class);
		}
		
		if(CollectionUtils.isNotEmpty(rootFolders)) {
			return rootFolders.get(0);
		}
		
		return null;
	}

	@Override
	public List<UserFolder> getChildFolders(String userID,
			String parentFolderId) {
		Query query = genericMongoRepository.createDataQuery();
		if(null != parentFolderId) {
			query.addCriteria(
					Criteria.where(QueryFields.PARENTID).is(parentFolderId));
		}
		
		query.addCriteria(Criteria.where(QueryFields.USERID).is(userID));
		query.with(Sort.by(QueryFields.SEQUENCE));

		return genericMongoRepository.findAll(query, UserFolder.class);
	}

	@Override
	public List<UserFolder> getChildFolders(String parentFolderId) {
		Query query = genericMongoRepository.createDataQuery();
		query.addCriteria(
				Criteria.where(QueryFields.PARENTID).is(parentFolderId));
		query.with(Sort.by(QueryFields.SEQUENCE));

		return genericMongoRepository.findAll(query, UserFolder.class);
	}

	@Override
	public UserQuestionsFolder getMyQuestionsFolder(String userID,
			String folderid) {
		Query query = userQuestionFolderRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.GUID).is(folderid));
		query.addCriteria(Criteria.where(QueryFields.USERID).is(userID));
		UserQuestionsFolder userFolder = userQuestionFolderRepository.findOne(query, UserQuestionsFolder.class);
		return userFolder;
	}

	@Override
	public List<UserQuestionsFolder> getMyQuestionsFolders(String userID) {
		Query query = userQuestionFolderRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.PARENTID).is(getMyQuestionRoot(userID).getGuid()))
		.with(Sort.by(QueryFields.SEQUENCE));
		List<UserQuestionsFolder> folders = userQuestionFolderRepository.findAll(query, UserQuestionsFolder.class);		
		return folders;
	}

	@Override
	public List<UserQuestionsFolder> getChildQuestionFolders(String folderid) {
		Query query = userQuestionFolderRepository.createDataQuery();
		query.addCriteria(Criteria.where(QueryFields.PARENTID).is(folderid))
		.with(Sort.by(QueryFields.SEQUENCE));;
		List<UserQuestionsFolder> folders = userQuestionFolderRepository.findAll(query, UserQuestionsFolder.class);		
		return folders;
	}

	@Override
	public double getUserFolderMinSeq(String userID) {
		
		return 0;
	}

	@Override
	public double getUserQuestionsFolderMinSeq(String userId) {
		
		return 0;
	}

}
