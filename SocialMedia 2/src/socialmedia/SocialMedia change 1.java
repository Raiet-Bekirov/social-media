package socialmedia;

import socialmedia.internal.*;

import java.io.IOException;
import java.util.HashMap; //to perform operations such as inserting, deleting, locating elements in a map. key-value

//implementing SocialMediaPlatform interface
public class SocialMedia implements SocialMediaPlatform { //after you're done, type implements SocialMediaPlatform
    private int lastAccountId = 0; //initialize an id -> all accounts are unique
    private static HashMap<String, Account> accountsByHandle = new HashMap<String, Account>();
    private static HashMap<Integer, Account> accountsById = new HashMap<Integer, Account>();
//    private static HashMap<String, Integer> HandleAndId = new HashMap<String, Integer>();

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
    /**
     * The method checks handle for errors
     *
     * @param handle      account's handle.
     * @throws InvalidHandleException if the new handle is empty, has more than 30
     *                                characters, or has white spaces.
     */
    protected static void checkHandle(String handle) throws InvalidHandleException {
        //depending on what the handle is, it will throw the messages
        if ((handle == null) || (handle.isEmpty())) { //if handle is empty
            throw new InvalidHandleException("handle is empty");
        }
        if (handle.length() > 30) { //if handle is more than 30 characters
            throw new InvalidHandleException("handle has more than 30 characters");
        }
        if (handle.matches(".*\\s.*")) { //If handle has white spaces
            throw new InvalidHandleException("handle has white spaces");
        }
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        checkHandle(handle);
        if (accountsByHandle.containsKey(handle)) { //if the account has the same name
            throw new IllegalHandleException("the handle already exists in the platform");
        }

        lastAccountId += 1;
        Account account = new Account(lastAccountId, handle, description); //from class Account(id, handle,description) -> to use the methods made in the class
        accountsByHandle.put(handle, account); //put into accountsByHandle -> list of handle, account
        accountsById.put(account.getId(), account); //put into accountsbyId, the id and account
//        HandleAndId.put(handle, account.getId());
        return account.getId(); //the id of created account
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException{
        checkHandle(handle);
        if (accountsByHandle.containsKey(handle)) { //if the account has the same name
            throw new IllegalHandleException("the handle already exists in the platform");
        }
        lastAccountId += 1;
        Account account = new Account(lastAccountId, handle); //from class Account(id, handle,description) -> to use the methods made in the class
        accountsByHandle.put(handle, account); //put into accountsByHandle -> list of handle, account
        accountsById.put(account.getId(), account); //put into accountsbyId, the id and account
//        HandleAndId.put(handle, account.getId());
        return account.getId(); //the id of created account
    }

    /**
     * The method removes the account with the corresponding handle from the
     * platform. When an account is removed, all of their posts and likes should
     * also be removed.
     * <p>
     * The state of this SocialMediaPlatform must be be unchanged if any exceptions
     * are thrown.
     *
     * @param handle account's handle.
     * @throws HandleNotRecognisedException if the handle does not match to any
     *                                      account in the system.
     */
    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException{
        Account account = accountsByHandle.get(handle);

        if(account == null){
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        accountsByHandle.remove(account.getHandle());
        accountsById.remove(account.getId());

/*
        for (String i: HandleAndId.keySet()){
            if(i == handle){
                //removing accounts of specified handler
                accountsByHandle.remove(i);
                accountsById.remove(HandleAndId.get(i)); //HandleAndId.get(i) the Id of the handler
                HandleAndId.remove(i);
            }
        }
*/
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException{
        Account account = accountsById.get(id);

        if(account == null){
            throw new AccountIDNotRecognisedException("account Id does not match to any account in the system");
        }

        accountsByHandle.remove(account.getHandle());
        accountsById.remove(account.getId());

/*
        for (String i: HandleAndId.keySet()){
            if(i == handle){
                //removing accounts of specified handler
                accountsByHandle.remove(i);
                accountsById.remove(HandleAndId.get(i)); //HandleAndId.get(i) the Id of the handler
                HandleAndId.remove(i);
            }
        }
*/
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
    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        //account is the account of the handle
        Account account = accountsByHandle.get(handle);

        if(account == null){
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        account.setDescription(description);

/*
        Account newAccount = new Account(account.getId(), account.getHandle(), description);
        //Id and handle stays the same, but the description change to whatever stated.

        //use replace hashmap [hashmap.replace(K key, V oldValue, V newValue)]
        accountsByHandle.replace(handle, newAccount);
        accountsById.replace(account.getId(), newAccount);
*/

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
    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        checkHandle(newHandle); //InvalidHandleException -> creating new handle

        if (accountsByHandle.containsKey(newHandle)) {
            throw new IllegalHandleException("Handle already exists in the platform");
        }

        Account account = accountsByHandle.get(oldHandle);
        if(account == null){
            throw new HandleNotRecognisedException("Old Handle does not match to any account in the system");
        }

        account.setHandle(newHandle);

//        Account newAccount = new Account(account.getId(), newHandle, account.getDescription());

        accountsByHandle.remove(oldHandle);
        accountsByHandle.put(newHandle, account);
//        accountsById.replace(account.getId(), newAccount);
//        HandleAndId.remove(oldHandle);
//        HandleAndId.put(newHandle, account.getId());

    }

    /**
     * This method returns the current total number of accounts present in the
     * platform. Note, this is NOT the total number of accounts ever created since
     * the current total should discount deletions.
     *
     * @return the total number of accounts in the platform.
     */
    @Override
    public int getNumberOfAccounts() {
        return accountsByHandle.size();
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        return null;
    }

    //END OF ACCOUNT MANAGEMENT



    //CONTINUE WITH POST MANAGEMENT
    /*AIM:
     * User can post message up to 100 characters
     * Users can comment on post
     * can endorse post #but, endorsement post cannot be commented or endorse
     * !!post , comment and endorse has unique identifier
     *
     * After you've done all these. Think about deleteing account -> should remove all the post . Change description -> should not change all the posts
     */
    private int lastPostId = 0;
    private static HashMap<Integer, AbstractPost> postsById = new HashMap<>();

    protected static void checkMessage(String message) throws InvalidPostException {
        if ((message == null) || (message.isEmpty())){
            throw new InvalidPostException("message is empty");
        }
        if(message.length() > 100){
            throw new InvalidPostException("limit is 100 characters");
        }
    }

    /**
     * This method allows user to post text messages up to 100 characters.
     *
     * @param handle      account's handle.
     * @param message user's post text messages
     * @throws AccountIDNotRecognisedException thrown when attempting to use an account ID that does not exit in the system.
     * @throws InvalidPostException thrown when attempting to create a post which the message is empty or has more characters than the system's limit (100 characters).
     * @return post Id
     */
    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        Account account = accountsByHandle.get(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        checkMessage(message);

        lastPostId += 1;
        Post post = new Post(lastAccountId, account, message);
        postsById.put(lastPostId, post);
        return lastPostId;
    }

//    private static HashMap<Integer, Integer> EndoPostIdAndPostId = new HashMap<Integer, Integer>();
    //might wanna create exception methods so that its easy to read??
    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException{
        Account account = accountsByHandle.get(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }

        if (post instanceof Endorsement) {
            throw new NotActionablePostException("Not an actionable post");
        }

        //might change to EndoPostId and the post instead of post id. lets see.
        lastPostId += 1; //all original, comments and endorse are considerred as posts and they all have unique numerical identifier
        Endorsement endorsement = new Endorsement(lastPostId, account, post);
        post.addEndorsement(endorsement);
        postsById.put(lastPostId, endorsement);
        return lastPostId;
    }

//    private static HashMap<Integer, Integer> ComPostIdAndPostId = new HashMap<Integer, Integer>();
//    private static HashMap<Integer, String> ComPostIdAndCom = new HashMap<Integer, String>();

    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException{
        Account account = accountsByHandle.get(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }

        if (post instanceof Endorsement) {
            throw new NotActionablePostException("Not an actionable post");
        }

        checkMessage(message);

        lastPostId += 1;
        Comment comment = new Comment(lastPostId, account, post, message);
        post.addComment(comment);
        postsById.put(lastPostId, comment);
//        ComPostIdAndPostId.put(lastPostId, id);
//        ComPostIdAndCom.put(lastPostId, message);
        return lastPostId;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        // TODO
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }

        return post.showPostDetails(new StringBuilder(), false).toString();
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }

        return post.showPostDetails(new StringBuilder(), true);
    }

    @Override
    public int getMostEndorsedPost() {
        // TODO
        return 0;
    }

    @Override
    public int getMostEndorsedAccount() {
        // TODO
        return 0;
    }

    @Override
    public void erasePlatform() {
        // TODO
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        // TODO
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO
    }

    @Override
    public int getTotalOriginalPosts() {
        // TODO
        return 0;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        // TODO
        return 0;
    }

    @Override
    public int getTotalCommentPosts() {
        // TODO
        return 0;
    }

    //no need to submit this one. Just for testing -> might be useful for the SocialMediaPlatformTest though
    public static void main (String[]args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException{
        SocialMedia a = new SocialMedia();
        //testing creating an account -> work
        a.createAccount("Alya", "Hello");
        a.createAccount("Siti", "Bye");
        a.createAccount("Raiet", "A genius");
        a.createAccount("John");
        //give exception -> uncomment one by one to see
        //a.createAccount("No t", "Ay");
        //a.createAccount("Alya", "P");
        //a.createAccount("nwgvbaiubdwigdvvbbjshygafwbdbkauhs", " ");

        //printing out the Id, handle and description of registerred account -> uncomment to see
    /*for (String handler: accountsByHandle.keySet()){
      System.out.println(handler + " ->\n "+ accountsByHandle.get(handler).showAccount());
    }*/

        //testing removing account and show to see if it is removed -> work
        a.removeAccount("Siti");
        //uncomment to see
    /*for (String handler: accountsByHandle.keySet()){
      System.out.println(handler + " ->\n "+ accountsByHandle.get(handler).showAccount());
    }*/

        //works
        a.updateAccountDescription("Alya", "May the patience be with you, RIP java");
        //uncomment to see
    /*for (String handler: accountsByHandle.keySet()){
      System.out.println(handler + " ->\n"+ accountsByHandle.get(handler).showAccount());
      System.out.println();
    }*/

        a.changeAccountHandle("John", "Legend");

        //works
        //uncomment to test on getting the current number of accounts
    /*int no_of_accounts= a.getNumberOfAccounts();
    System.out.println(no_of_accounts);*/

        //execute but if Alya post twice, it only show one of the post. Need some changes. maybe one handler have records of post?
        a.createPost("Alya", "Life be hitting so hard these days");
        a.createPost("Alya", "I love Jollibee");
        a.createPost("Raiet", "surviving I guess");
        int id4 = a.createPost("Legend", "When life gives you lemon, eat it");

        try {
            a.endorsePost("Alya", id4);
        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (NotActionablePostException e) {
            throw new RuntimeException(e);
        }

        try {
            a.commentPost("Raiet", id4, "Super!");
        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (NotActionablePostException e) {
            throw new RuntimeException(e);
        }

        for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }

        a.changeAccountHandle("Raiet", "BOSS");

        for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }
/*
        a.commentPost("Alya", 2, "I love jollibee too");
        for (Integer b: ComPostIdAndCom.keySet()){
            System.out.println(b + " :\n"+ ComPostIdAndCom.get(b));
            System.out.println();
        }
*/
    }
}
