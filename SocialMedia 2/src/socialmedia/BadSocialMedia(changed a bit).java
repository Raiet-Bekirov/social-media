package socialmedia;

import socialmedia.internal.Account;
import socialmedia.internal.AccountManager;
import socialmedia.internal.PostManager;

import java.io.IOException;

/**
 * BadSocialMedia is a minimally compiling, but non-functioning implementor of
 * the SocialMediaPlatform interface.
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class BadSocialMedia implements SocialMediaPlatform {

	private AccountManager accountManager = new AccountManager();
	private PostManager postManager = new PostManager();

	@Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		return createAccount(handle, null);
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		return accountManager.createAccount(handle, description);
	}

	private Account findAccount(int id) throws AccountIDNotRecognisedException {
		Account account = accountManager.getAccount(id);
		if (account == null) {
			throw new AccountIDNotRecognisedException("the ID does not match to any account in the system");
		}

		return account;
	}

	private Account findAccountByHandle(String handle) throws HandleNotRecognisedException {
		Account account = accountManager.getAccountByHandle(handle);
		if (account == null) {
			throw new HandleNotRecognisedException("handle does not match to any account in the system");
		}

		return account;
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		Account account = findAccount(id);
		removeAccountInternal(account);
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		Account account = findAccountByHandle(handle);
		removeAccountInternal(account);
	}

	private void removeAccountInternal(Account account) {
		postManager.removeAccount(account);
		accountManager.removeAccount(account);
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		accountManager.changeAccountHandle(oldHandle, newHandle);
	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		accountManager.updateAccountDescription(handle, description);
	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		Account account = findAccountByHandle(handle);

		return account.showAccount(new StringBuilder("<pre>\n"))
				.append("</pre>\n").toString();

		// TODO
		//	 * Post count: [total number of posts, including endorsements and replies]
		//	 * Endorse count: [sum of endorsements received by each post of this account]
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAccounts() {
		return accountManager.getNumberOfAccounts();
	}

	@Override
	public int getTotalOriginalPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCommentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedPost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedAccount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void erasePlatform() {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePlatform(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

}
