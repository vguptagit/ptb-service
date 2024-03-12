package com.pearson.ptb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.pearson.ptb.bean.ArchivedFolder;
import com.pearson.ptb.bean.Metadata;
import com.pearson.ptb.bean.TestBinding;
import com.pearson.ptb.bean.TestMetadata;
import com.pearson.ptb.bean.UserFolder;
import com.pearson.ptb.framework.CacheWrapper;
import com.pearson.ptb.framework.exception.DuplicateTitleException;
import com.pearson.ptb.proxy.ArchiveDelegate;
import com.pearson.ptb.proxy.MyTestDelegate;
import com.pearson.ptb.proxy.UserFoldersDelegate;
import com.pearson.ptb.util.CacheKey;

import org.springframework.stereotype.Service;

/**
 * This <code>ArchiveService</code> is responsible for archiving the folders and
 * tests.
 *
 */
@Service("archiveService")
public class ArchiveService {

	@Autowired
	@Qualifier("archiveRepo")
	private ArchiveDelegate archiveRepo;

	@Autowired
	@Qualifier("userFoldersRepo")
	private UserFoldersDelegate userFoldersRepo;

	@Autowired
	@Qualifier("metadataService")
	private MetadataService metadataService;

	@Autowired
	@Qualifier("myTestService")
	private MyTestService myTestService;

	@Autowired
	@Qualifier("myTestsRepo")
	private MyTestDelegate myTestsRepo;

	private static CacheWrapper CACHE;
	private String loggedInUser;
	private Metadata restoringTestMetadata;
	private RestoreType restoreType;
	private String DuplicateTitleMessage = "A test with this title already exists";
	TestBinding restoringTestBinding;

	/**
	 * This constructor initializes the instance of the cache wrapper object for
	 * caching operation.
	 * 
	 */
	public ArchiveService() {
		
	}

	public void initializeCache() {
		CACHE = CacheWrapper.getInstance();
	}
	/**
	 * This method will archive the folder based on folder id
	 * 
	 * @param folderId
	 *            ,id of the folder to be archived.
	 * @return ArchivedFolder.
	 */
	public ArchivedFolder archiveFolder(String folderId) {

		String archivedSelfId = null;

		List<ArchivedFolder> parentsAndSelf = archiveParentsAndSelf(folderId);

		ArchivedFolder archivedFolder = parentsAndSelf
				.get(parentsAndSelf.size() - 1);
		archivedSelfId = archivedFolder.getGuid();

		archiveChildren(folderId, archivedSelfId);

		return archivedFolder;
	}

	/**
	 * This method will restore the archived folder based on folder id
	 * 
	 * @param folderId
	 *            ,id of the folder to be restored.
	 * @return UserFolder.
	 */
	public UserFolder restoreFolder(String folderId) {
		String userSelfId = null;
		restoreType = RestoreType.FOLDER;
		List<UserFolder> parentsAndSelf = userParentsAndSelf(folderId);

		UserFolder userFolder = parentsAndSelf.get(parentsAndSelf.size() - 1);
		userSelfId = userFolder.getGuid();

		restoreChildren(folderId, userSelfId);

		return userFolder;
	}

	/**
	 * This method will delete the folder based on folder id
	 * 
	 * @param folderId
	 *            ,id of the folder to be deleted.
	 */
	public void deleteFolder(String folderId) {

		List<String> folderIds = new ArrayList<>(Arrays.asList(folderId));
		List<ArchivedFolder> archivedFolders = new ArrayList<ArchivedFolder>();

		archivedFolders.add(archiveRepo.getFolder(folderId));

		fillArchivedChildFolders(folderIds, archivedFolders, folderId);

		for (ArchivedFolder archivedFolder : archivedFolders) {

			for (TestBinding testBinding : archivedFolder.getTestBindings()) {
				
				//myTestsRepo.delete(testBinding.getTestId());
			}
		}

		archiveRepo.deleteFolders(folderIds);
	}

	/**
	 * This method will delete the test from PAF.
	 * 
	 * @param folderId
	 *            ,represents folder of the test.
	 * @param testId
	 *            ,represents the test.
	 * @param userId
	 *            ,represents the user.
	 */
	public void deleteTest(String folderId, String testId, String userId) {
		ArchivedFolder archivedFolder;
		if (folderId == null) {
			archivedFolder = archiveRepo.getArchiveRoot(userId);
		} else {
			archivedFolder = archiveRepo.getFolder(folderId);
		}

		List<TestBinding> testBindings = archivedFolder.getTestBindings();

		int index = 0, indexToRemove = -1;
		for (TestBinding testBinding : testBindings) {

			if (testBinding.getTestId().equals(testId)) {
				indexToRemove = index;
				
				//myTestsRepo.delete(testBinding.getTestId());
				CACHE.delete(String.format(CacheKey.TEST_FORMAT,
						testBinding.getTestId()));
			}
			index++;
		}

		if (indexToRemove > -1) {
			testBindings.remove(indexToRemove);
			archivedFolder.setTestBindings(testBindings);
			archiveRepo.saveFolder(archivedFolder);
		}
	}

	/**
	 * This method will archive the test.
	 * 
	 * @param folderId
	 *            ,represents folder of the test.
	 * @param testId
	 *            ,represents the test.
	 * @param userId
	 *            ,represents the user.
	 * @return ArchivedFolder.
	 */
	public ArchivedFolder archiveTest(String userId, String testId,
			String folderId) {

		ArchivedFolder archivedSelfFolder = new ArchivedFolder();
		UserFolder folder = new UserFolder();

		TestBinding originalTestBinding;
		if (folderId == null) {
			folder = userFoldersRepo.getMyTestRoot(userId);
		} else {
			folder = userFoldersRepo.getFolder(folderId);
		}
		originalTestBinding = folder.removeTestBinding(testId);
		userFoldersRepo.saveFolder(folder);

		if (folderId == null) {
			archivedSelfFolder = archiveRepo.getArchiveRoot(userId);
		} else {
			List<ArchivedFolder> parentsAndSelf = archiveParentsAndSelf(
					folderId);
			archivedSelfFolder = parentsAndSelf.get(parentsAndSelf.size() - 1);
		}

		List<TestBinding> testBindings = archivedSelfFolder.getTestBindings();
		testBindings.add(originalTestBinding);
		archivedSelfFolder.setTestBindings(testBindings);

		archiveRepo.saveFolder(archivedSelfFolder);

		if (archivedSelfFolder.getUserID()
				.equals(archivedSelfFolder.getParentId())) {
			archivedSelfFolder = null;
		}
		return archivedSelfFolder;
	}

	/**
	 * This method will restore the archived test.
	 * 
	 * @param folderId
	 *            , represents folder of the test.
	 * @param testId
	 *            ,represents the test.
	 * @param userId
	 *            , represents the user.
	 * @return UserFolder.
	 */
	public UserFolder restoreTest(String userId, String testId,
			String folderId) {
		loggedInUser = userId;
		restoreType = RestoreType.TEST;
		UserFolder userSelfFolder = new UserFolder();
		ArchivedFolder folder = new ArchivedFolder();

		if (folderId == null) {
			folder = archiveRepo.getArchiveRoot(userId);
		} else {
			folder = archiveRepo.getFolder(folderId);
		}

		restoringTestBinding = folder.removeTestBinding(testId);
		restoringTestMetadata = metadataService.getMetadata(testId);

		if (folderId == null) {
			userSelfFolder = userFoldersRepo.getMyTestRoot(userId);
			List<TestBinding> testBindings = userSelfFolder.getTestBindings();
			testBindings.add(restoringTestBinding);
			userSelfFolder.setTestBindings(testBindings);
			userFoldersRepo.saveFolder(userSelfFolder);
		} else {
			if (folderId != null) {
				List<UserFolder> parentsAndSelf = userParentsAndSelf(folderId);
				userSelfFolder = parentsAndSelf.get(parentsAndSelf.size() - 1);
			}
		}

		archiveRepo.saveFolder(folder);

		if (userSelfFolder.getUserID().equals(userSelfFolder.getParentId())) {
			userSelfFolder = null;
		}
		return userSelfFolder;
	}

	private List<UserFolder> userParentsAndSelf(String folderId) {

		
		List<ArchivedFolder> archivedFolders = new ArrayList<ArchivedFolder>();
		fillArchivedParentFolders(archivedFolders, folderId);

		RestoredAndUnRestored restoredAndUnRestored = generateUserParentFolders(
				archivedFolders, null);

		if (!restoredAndUnRestored.alreadyRestoredFolders.isEmpty()) {
			userFoldersRepo
					.saveFolders(restoredAndUnRestored.alreadyRestoredFolders);
		}

		List<UserFolder> selfAndParents = restoredAndUnRestored.unRestoredFolders;
		if (!selfAndParents.isEmpty()) {
			userFoldersRepo.saveFolders(selfAndParents);
		} else {
			selfAndParents = restoredAndUnRestored.alreadyRestoredFolders;
		}

		return selfAndParents;
	}

	private List<ArchivedFolder> archiveParentsAndSelf(String folderId) {

		
		List<UserFolder> userFolders = new ArrayList<UserFolder>();
		fillParentFolders(userFolders, folderId);

		ArchivedAndUnArchived archivedAndUnArchived = generateArchivedParentFolders(
				userFolders, null);
		List<ArchivedFolder> selfAndParents = archivedAndUnArchived.unArchivedFolders;
		if (!selfAndParents.isEmpty()) {
			archiveRepo.archiveFolders(selfAndParents);
		}

		List<ArchivedFolder> parentsAndSelf = archivedAndUnArchived.unArchivedFolders;

		if (parentsAndSelf.isEmpty()) {
			parentsAndSelf = archivedAndUnArchived.alreadyArchivedFolders;
		}

		return parentsAndSelf;
	}

	private void archiveChildren(String folderId, String archivedFolderId) {

		
		List<String> folderIds = new ArrayList<>(Arrays.asList(folderId));
		List<UserFolder> userFolders = new ArrayList<UserFolder>();

		UserFolder userFolderSelf = userFoldersRepo.getFolder(folderId);
		ArchivedFolder archivedFolderSelf = archiveRepo
				.getFolder(archivedFolderId);

		List<TestBinding> bindings = new ArrayList<TestBinding>();
		bindings.addAll(archivedFolderSelf.getTestBindings());
		bindings.addAll(userFolderSelf.getTestBindings());

		archivedFolderSelf.setTestBindings(bindings);

		archiveRepo.saveFolder(archivedFolderSelf);

		fillChildFolders(folderIds, userFolders, folderId);

		if (!userFolders.isEmpty()) {
			ArchivedAndUnArchived archivedAndUnArchived = generateArchivedChildFolders(
					userFolders, archivedFolderId);
			if (!archivedAndUnArchived.unArchivedFolders.isEmpty()) {
				archiveRepo.archiveFolders(
						archivedAndUnArchived.unArchivedFolders);
			}
			if (!archivedAndUnArchived.alreadyArchivedFolders.isEmpty()) {
				archiveRepo.archiveFolders(
						archivedAndUnArchived.alreadyArchivedFolders);
			}
		}

		
		userFoldersRepo.deleteFolders(folderIds);
	}

	private void restoreChildren(String folderId, String userFolderId) {

		
		List<String> folderIds = new ArrayList<>(Arrays.asList(folderId));
		List<ArchivedFolder> archivedFolders = new ArrayList<ArchivedFolder>();

		fillArchivedChildFolders(folderIds, archivedFolders, folderId);

		if (!archivedFolders.isEmpty()) {
			RestoredAndUnRestored restoredAndUnRestored = generateRestoredChildFolders(
					archivedFolders, userFolderId);
			if (!restoredAndUnRestored.unRestoredFolders.isEmpty()) {
				userFoldersRepo
						.saveFolders(restoredAndUnRestored.unRestoredFolders);
			}
			if (!restoredAndUnRestored.alreadyRestoredFolders.isEmpty()) {
				userFoldersRepo.saveFolders(
						restoredAndUnRestored.alreadyRestoredFolders);
			}
		}

		
		archiveRepo.deleteFolders(folderIds);
	}

	private ArchivedAndUnArchived generateArchivedParentFolders(
			List<UserFolder> userFolders, String intialParentId) {

		ArchivedAndUnArchived archivedAndUnArchived = new ArchivedAndUnArchived();

		List<ArchivedFolder> unArchivedFolders = new ArrayList<ArchivedFolder>();
		List<ArchivedFolder> alreadyArchivedFolders = new ArrayList<ArchivedFolder>();

		ArchivedFolder unArchivedFolder;
		ArchivedFolder alreadyArchivedFolder;

		String parentId = intialParentId;

		for (UserFolder userFolder : userFolders) {

			if (isNotArchived(userFolder.getTitle(), parentId,
					userFolder.getUserID())) {

				unArchivedFolder = new ArchivedFolder();

				BeanUtils.copyProperties(userFolder, unArchivedFolder);

				unArchivedFolder.setTestBindings(new ArrayList<TestBinding>());

				unArchivedFolder.setGuid(UUID.randomUUID().toString());

				unArchivedFolder.setParentId(parentId);

				parentId = unArchivedFolder.getGuid();

				unArchivedFolders.add(unArchivedFolder);
			} else {

				alreadyArchivedFolder = archiveRepo.getFolder(
						userFolder.getTitle(), parentId,
						userFolder.getUserID());
				alreadyArchivedFolders.add(alreadyArchivedFolder);

				parentId = alreadyArchivedFolder.getGuid();
			}
		}

		archivedAndUnArchived.unArchivedFolders = unArchivedFolders;
		archivedAndUnArchived.alreadyArchivedFolders = alreadyArchivedFolders;

		return archivedAndUnArchived;
	}

	private RestoredAndUnRestored generateUserParentFolders(
			List<ArchivedFolder> archivedFolders, String intialParentId) {

		RestoredAndUnRestored restoredAndUnRestored = new RestoredAndUnRestored();

		List<UserFolder> unRestoredFolders = new ArrayList<UserFolder>();
		List<UserFolder> alreadyRestoredFolders = new ArrayList<UserFolder>();

		UserFolder unRestoredFolder;
		UserFolder alreadyRestoredFolder;

		String parentId = intialParentId;
		int folderCount = 1;
		for (ArchivedFolder archivedFolder : archivedFolders) {

			if (isNotRestored(archivedFolder.getTitle(), parentId,
					archivedFolder.getUserID())) {

				unRestoredFolder = new UserFolder();

				BeanUtils.copyProperties(archivedFolder, unRestoredFolder);

				if (folderCount == archivedFolders.size()) {
					if (restoreType == RestoreType.TEST) {
						List<TestBinding> testBindings = new ArrayList<TestBinding>();
						testBindings.add(restoringTestBinding);
						unRestoredFolder.setTestBindings(testBindings);
					} else if (restoreType == RestoreType.FOLDER) {
						unRestoredFolder.setTestBindings(
								archivedFolder.getTestBindings());
					}
				} else {
					List<TestBinding> testBindings = new ArrayList<TestBinding>();
					unRestoredFolder.setTestBindings(testBindings);
				}

				unRestoredFolder.setGuid(UUID.randomUUID().toString());

				unRestoredFolder.setParentId(parentId);

				parentId = unRestoredFolder.getGuid();

				unRestoredFolders.add(unRestoredFolder);
			} else {

				alreadyRestoredFolder = userFoldersRepo.getFolderByTitle(
						archivedFolder.getTitle(), parentId,
						archivedFolder.getUserID());
				alreadyRestoredFolders.add(alreadyRestoredFolder);

				if (folderCount == archivedFolders.size()) {
					checkDuplicateAndAdd(alreadyRestoredFolder, archivedFolder);
				}

				parentId = alreadyRestoredFolder.getGuid();
			}
			folderCount += 1;
		}

		restoredAndUnRestored.unRestoredFolders = unRestoredFolders;
		restoredAndUnRestored.alreadyRestoredFolders = alreadyRestoredFolders;
		return restoredAndUnRestored;
	}

	private void checkDuplicateAndAdd(UserFolder alreadyRestoredFolder,
			ArchivedFolder archivedFolder) {
		if (!alreadyRestoredFolder.getTestBindings().isEmpty()) {
			List<TestMetadata> userTests = myTestService.getMyFolderTests(
					loggedInUser, alreadyRestoredFolder.getGuid(), false);
			List<TestMetadata> archivedTests = getMyFolderTests(archivedFolder);

			if (restoreType == RestoreType.TEST) {
				for (TestMetadata userTest : userTests) {
					if (restoringTestMetadata.getTitle()
							.equals(userTest.getTitle())) {
						throw new DuplicateTitleException(
								DuplicateTitleMessage);
					}
				}
			} else if (restoreType == RestoreType.FOLDER) {
				for (TestMetadata userTest : userTests) {
					for (TestMetadata archivedTest : archivedTests) {
						if (userTest.getTitle()
								.equals(archivedTest.getTitle())) {
							throw new DuplicateTitleException(
									DuplicateTitleMessage);
						}
					}
				}
			}
		}

		if (!archivedFolder.getTestBindings().isEmpty()) {
			if (restoreType == RestoreType.TEST) {
				if (alreadyRestoredFolder.getTestBindings().isEmpty()) {
					List<TestBinding> testBindings = new ArrayList<TestBinding>();
					testBindings.add(restoringTestBinding);
					alreadyRestoredFolder.setTestBindings(testBindings);
				} else {
					alreadyRestoredFolder.getTestBindings()
							.add(restoringTestBinding);
				}
			} else if (restoreType == RestoreType.FOLDER) {
				if (alreadyRestoredFolder.getTestBindings().isEmpty()) {
					alreadyRestoredFolder
							.setTestBindings(archivedFolder.getTestBindings());
				} else {
					alreadyRestoredFolder.getTestBindings()
							.addAll(archivedFolder.getTestBindings());
				}
			}
		}
	}

	private ArchivedAndUnArchived generateArchivedChildFolders(
			List<UserFolder> userFolders, String intialParentId) {

		ArchivedAndUnArchived archivedAndUnArchived = new ArchivedAndUnArchived();

		List<ArchivedFolder> unArchivedFolders = new ArrayList<ArchivedFolder>();
		List<ArchivedFolder> alreadyArchivedFolders = new ArrayList<ArchivedFolder>();

		ArchivedFolder unArchivedFolder;
		ArchivedFolder alreadyArchivedFolder;

		String parentId = intialParentId;

		Map<String, String> parentIdMap = new HashMap<String, String>();
		parentIdMap.put(userFolders.get(0).getParentId(), parentId);

		for (UserFolder userFolder : userFolders) {

			if (isNotArchived(userFolder.getTitle(), parentId,
					userFolder.getUserID())) {

				unArchivedFolder = new ArchivedFolder();

				BeanUtils.copyProperties(userFolder, unArchivedFolder);

				unArchivedFolder.setGuid(UUID.randomUUID().toString());

				parentIdMap.put(userFolder.getGuid(),
						unArchivedFolder.getGuid());

				if (parentIdMap.containsKey(userFolder.getParentId())) {
					parentId = parentIdMap.get(userFolder.getParentId());
				} else {
					parentId = unArchivedFolder.getGuid();
					parentIdMap.put(userFolder.getGuid(), parentId);
				}
				unArchivedFolder.setParentId(parentId);

				unArchivedFolders.add(unArchivedFolder);

				unArchivedFolder.setTestBindings(userFolder.getTestBindings());
			} else {

				alreadyArchivedFolder = archiveRepo.getFolder(
						userFolder.getTitle(), parentId,
						userFolder.getUserID());
				alreadyArchivedFolders.add(alreadyArchivedFolder);

				if (parentIdMap.containsKey(userFolder.getParentId())) {
					parentId = parentIdMap.get(userFolder.getParentId());
				} else {
					parentId = alreadyArchivedFolder.getGuid();
					parentIdMap.put(userFolder.getGuid(), parentId);
				}

				List<TestBinding> bindings = new ArrayList<TestBinding>();
				bindings.addAll(alreadyArchivedFolder.getTestBindings());
				bindings.addAll(userFolder.getTestBindings());
				alreadyArchivedFolder.setTestBindings(bindings);
			}
		}

		archivedAndUnArchived.unArchivedFolders = unArchivedFolders;
		archivedAndUnArchived.alreadyArchivedFolders = alreadyArchivedFolders;
		return archivedAndUnArchived;
	}

	private RestoredAndUnRestored generateRestoredChildFolders(
			List<ArchivedFolder> archivedFolders, String intialParentId) {

		RestoredAndUnRestored restoredAndUnRestored = new RestoredAndUnRestored();

		List<UserFolder> unRestoredFolders = new ArrayList<UserFolder>();
		List<UserFolder> alreadyRestoredFolders = new ArrayList<UserFolder>();

		UserFolder unRestoredFolder;
		UserFolder alreadyRestoredFolder;

		String parentId = intialParentId;
		Map<String, String> parentIdMap = new HashMap<String, String>();

		for (ArchivedFolder archivedFolder : archivedFolders) {
			if (isNotRestored(archivedFolder.getTitle(), parentId,
					archivedFolder.getUserID())) {

				unRestoredFolder = new UserFolder();

				BeanUtils.copyProperties(archivedFolder, unRestoredFolder);

				unRestoredFolder.setGuid(UUID.randomUUID().toString());

				if (parentIdMap.containsKey(archivedFolder.getParentId())) {
					unRestoredFolder.setParentId(
							parentIdMap.get(archivedFolder.getParentId()));
				} else {
					unRestoredFolder.setParentId(parentId);
					parentIdMap.put(archivedFolder.getParentId(), parentId);
				}
				parentId = unRestoredFolder.getGuid();

				unRestoredFolders.add(unRestoredFolder);
				unRestoredFolder
						.setTestBindings(archivedFolder.getTestBindings());
			} else {

				alreadyRestoredFolder = userFoldersRepo.getFolderByTitle(
						archivedFolder.getTitle(), parentId,
						archivedFolder.getUserID());
				alreadyRestoredFolders.add(alreadyRestoredFolder);

				parentId = alreadyRestoredFolder.getGuid();
				if (!parentIdMap.containsKey(archivedFolder.getParentId())) {
					parentIdMap.put(archivedFolder.getParentId(),
							alreadyRestoredFolder.getParentId());
				}

				if (!alreadyRestoredFolder.getTestBindings().isEmpty()) {
					List<TestMetadata> userTests = myTestService
							.getMyFolderTests(loggedInUser,
									alreadyRestoredFolder.getGuid(), false);
					List<TestMetadata> archivedTests = getMyFolderTests(
							archivedFolder);

					for (TestMetadata userTest : userTests) {
						for (TestMetadata archivedTest : archivedTests) {
							if (userTest.getTitle()
									.equals(archivedTest.getTitle())) {
								throw new DuplicateTitleException(
										DuplicateTitleMessage);
							}
						}
					}
				}

				if (!archivedFolder.getTestBindings().isEmpty()) {
					if (alreadyRestoredFolder.getTestBindings().isEmpty()) {
						alreadyRestoredFolder.setTestBindings(
								archivedFolder.getTestBindings());
					} else {
						alreadyRestoredFolder.getTestBindings()
								.addAll(archivedFolder.getTestBindings());
					}
				}

			}
		}

		restoredAndUnRestored.unRestoredFolders = unRestoredFolders;
		restoredAndUnRestored.alreadyRestoredFolders = alreadyRestoredFolders;
		return restoredAndUnRestored;
	}

	private class RestoredAndUnRestored {
		List<UserFolder> unRestoredFolders;
		List<UserFolder> alreadyRestoredFolders;
	}

	private class ArchivedAndUnArchived {
		List<ArchivedFolder> unArchivedFolders;
		List<ArchivedFolder> alreadyArchivedFolders;
	}

	private boolean isNotArchived(String folderTitle, String archivedParentId,
			String userId) {

		ArchivedFolder folder = archiveRepo.getFolder(folderTitle,
				archivedParentId, userId);

		return folder == null;
	}

	private boolean isNotRestored(String folderTitle, String restoredParentId,
			String userId) {

		UserFolder folder = userFoldersRepo.getFolderByTitle(folderTitle,
				restoredParentId, userId);

		return folder == null;
	}

	private void fillParentFolders(List<UserFolder> userFolders,
			String folderId) {
		UserFolder userFolder = userFoldersRepo.getFolder(folderId);

		if (userFolder.getParentId() != null) {
			fillParentFolders(userFolders, userFolder.getParentId());
		}

		userFolders.add(userFolder);
	}

	private void fillArchivedParentFolders(List<ArchivedFolder> archivedFolders,
			String folderId) {
		ArchivedFolder archivedFolder = archiveRepo.getFolder(folderId);

		if (archivedFolder.getParentId() != null) {
			fillArchivedParentFolders(archivedFolders,
					archivedFolder.getParentId());
		}

		archivedFolders.add(archivedFolder);
	}

	private void fillChildFolders(List<String> folderIds,
			List<UserFolder> userFolders, String folderId) {

		List<UserFolder> children = userFoldersRepo.getChildFolders(folderId);

		for (UserFolder child : children) {
			userFolders.add(child);
			folderIds.add(child.getGuid());
			fillChildFolders(folderIds, userFolders, child.getGuid());
		}
	}

	private void fillArchivedChildFolders(List<String> folderIds,
			List<ArchivedFolder> archivedFolders, String folderId) {

		List<ArchivedFolder> children = archiveRepo.getChildFolders(folderId);

		for (ArchivedFolder child : children) {
			archivedFolders.add(child);
			folderIds.add(child.getGuid());
			fillArchivedChildFolders(folderIds, archivedFolders,
					child.getGuid());
		}
	}

	/**
	 * This method will get all the archived folders.
	 * 
	 * @param userId
	 *            ,represents the user.
	 * @param parentFolderId
	 *            ,represents parent folder of folder.
	 * @return list of archived folders.
	 */
	public List<ArchivedFolder> getFolders(String userId,
			String parentFolderId) {

		return archiveRepo.getFolders(userId, parentFolderId);
	}

	/**
	 * This method will get all the tests based on folder.
	 * 
	 * @param userId
	 *            ,represents the user.
	 * @param parentFolderId
	 *            ,represents parent folder of test.
	 * @return list of test meta data.
	 */
	public List<TestMetadata> getTests(String userId, String parentFolderId) {

		ArchivedFolder folder = new ArchivedFolder();

		if (parentFolderId == null || "null".equals(parentFolderId)) {

			folder = archiveRepo.getArchiveRoot(userId);
		} else {

			folder = archiveRepo.getFolder(parentFolderId);
		}

		return getMyFolderTests(folder);
	}

	private List<TestMetadata> getMyFolderTests(UserFolder folder) {

		return myTestService.getMyFolderTests(folder);
	}

	/**
	 * This method will get the folder of the test.
	 * 
	 * @param testId
	 *            ,represents the test.
	 */
	public ArchivedFolder getTestFolder(String testId) {
		return archiveRepo.getTestFolder(testId);
	}

	private enum RestoreType {
		FOLDER, TEST
	}
}
