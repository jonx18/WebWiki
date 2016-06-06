package wikiAnalicis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.UserContributorDAO;
import wikiAnalicis.dao.UserContributorDAO;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.entity.Page;
import wikiAnalicis.service.UserContributorService;
import wikiAnalicis.service.UserContributorService;

@Service
@Transactional
public class UserContributorServiceImpl implements UserContributorService {
	@Autowired
	private UserContributorDAO contributorDAO;

	public UserContributorServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createUserContributor(UserContributor userContributor) {
		return contributorDAO.createUserContributor(userContributor);
	}

	@Override
	public UserContributor mergeUserContributor(UserContributor userContributor) {
		return contributorDAO.mergeUserContributor(userContributor);
	}

	@Override
	public UserContributor updateUserContributor(UserContributor userContributor) {
		return contributorDAO.updateUserContributor(userContributor);
	}

	@Override
	public void deleteUserContributor(long id) {
		contributorDAO.deleteUserContributor(id);
	}

	@Override
	public List<UserContributor> getAllUserContributors() {
		return contributorDAO.getAllUserContributors();
	}

	@Override
	public UserContributor getUserContributor(long id) {
		return contributorDAO.getUserContributor(id);
	}

	@Override
	public UserContributor getUserContributor(String username) {

		return contributorDAO.getUserContributor(username);
	}

}
