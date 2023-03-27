package socialmedia.internal;

import socialmedia.AccountIDNotRecognisedException;
import socialmedia.HandleNotRecognisedException;
import socialmedia.IllegalHandleException;
import socialmedia.InvalidHandleException;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private int lastAccountId = 0;
    private Map<String, Account> accountsByHandle = new HashMap<>();
    private Map<Integer, Account> accountsById = new HashMap<>();

    /**
     * The method checks handle for errors
     *
     * @param handle      account's handle.
     * @throws InvalidHandleException if the new handle is empty, has more than 30
     *                                characters, or has white spaces.
     */
    public void checkHandle(String handle) throws InvalidHandleException {
        if ((handle == null) || (handle.isEmpty())) {
            throw new InvalidHandleException("handle is empty");
        }
        if (handle.length() > 30) {
            throw new InvalidHandleException("handle has more than 30 characters");
        }
        if (handle.matches(".*\\s.*")) {
            throw new InvalidHandleException("handle has white spaces");
        }
    }

    /**
     * The method creates an account in the platform with the given handle and
     * description.
     * <p>
     * The state of this SocialMediaPlatform must be be unchanged if any exceptions
     * are thrown.
     *
     * @param handle      account's handle.
     * @param description account's description.
     * @throws IllegalHandleException if the handle already exists in the platform.
     * @throws InvalidHandleException if the new handle is empty, has more than 30
     *                                characters, or has white spaces.
     * @return the ID of the created account.
     */
    public synchronized int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        checkHandle(handle);

        if (accountsByHandle.containsKey(handle)) {
            throw new IllegalHandleException("the handle already exists in the platform");
        }

        lastAccountId += 1;
        Account account = new Account(lastAccountId, handle, description);
        accountsByHandle.put(handle, account);
        accountsById.put(account.getId(), account);
        return account.getId();
    }

    /**
     * The method removes the account with the corresponding ID from the platform.
     * When an account is removed, all of their posts and likes should also be
     * removed.
     * <p>
     * The state of this SocialMediaPlatform must be be unchanged if any exceptions
     * are thrown.
     *
     * @param account the account to remove.
     */
    public synchronized void removeAccount(Account account) {
        if (account != null) {
            accountsByHandle.remove(account.getHandle());
            accountsById.remove(account.getId());
        }
    }

    public Account getAccount(int id) {
        return accountsById.get(id);
    }

    public Account getAccountByHandle(String handle) {
        return accountsByHandle.get(handle);
    }

    public int getNumberOfAccounts() {
        return accountsById.size();
    }

    /**
     * The method replaces the oldHandle of an account by the newHandle.
     * <p>
     * The state of this SocialMediaPlatform must be be unchanged if any exceptions
     * are thrown.
     *
     * @param oldHandle account's old handle.
     * @param newHandle account's new handle.
     * @throws HandleNotRecognisedException if the old handle does not match to any
     *                                      account in the system.
     * @throws IllegalHandleException       if the new handle already exists in the
     *                                      platform.
     * @throws InvalidHandleException       if the new handle is empty, has more
     *                                      than 30 characters, or has white spaces.
     */
    public synchronized void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        checkHandle(newHandle);

        if (accountsByHandle.containsKey(newHandle)) {
            throw new InvalidHandleException("new handle already exists in the platform");
        }

        Account account = getAccountByHandle(oldHandle);
        if (account == null) {
            throw new HandleNotRecognisedException("old handle does not match to any account in the system");
        }

        accountsByHandle.remove(oldHandle);

        Account newAccount = new Account(account.getId(), newHandle, account.getDescription());
        accountsByHandle.put(newHandle, newAccount);
        accountsById.put(newAccount.getId(), newAccount);
    }

    /**
     * The method updates the description of the account with the respective handle.
     * <p>
     * The state of this SocialMediaPlatform must be be unchanged if any exceptions
     * are thrown.
     *
     * @param handle      handle to identify the account.
     * @param description new text for description.
     * @throws HandleNotRecognisedException if the handle does not match to any
     *                                      account in the system.
     */
    public synchronized void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        Account account = getAccountByHandle(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("handle does not match to any account in the system");
        }

        Account newAccount = new Account(account.getId(), account.getHandle(), description);
        accountsByHandle.put(handle, newAccount);
        accountsById.put(newAccount.getId(), newAccount);
    }
}
