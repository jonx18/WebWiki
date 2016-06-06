package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.UserContributor;


public interface UserContributorDAO {
	public long createUserContributor(UserContributor userContributor);
	public UserContributor mergeUserContributor(UserContributor userContributor);
	public UserContributor updateUserContributor(UserContributor userContributor);
	public void deleteUserContributor(long id);
	public List<UserContributor> getAllUserContributors();
	public UserContributor getUserContributor(long id); 
	public UserContributor getUserContributor(String username); 
}
