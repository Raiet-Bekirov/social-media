package socialmedia;

import java.util.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


//implementing SocialMediaPlatform interface
public class SocialMedia1 implements SocialMediaPlatform { //after you're done, type implements SocialMediaPlatform
    private int lastAccountId = 0; //initialize an id -> all accounts are unique
    private static HashMap<String, Account> accountsByHandle = new HashMap<String, Account>();
    private static HashMap<Integer, Account> accountsById = new HashMap<Integer, Account>();

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
        return account.getId(); //the id of created account
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException{
        Account account = accountsByHandle.get(handle); 

        if(account == null){ //if the handle does not exist, account will be null
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        //remove the account 
        accountsByHandle.remove(account.getHandle());
        accountsById.remove(account.getId());

    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException{
        Account account = accountsById.get(id);

        if(account == null){
            throw new AccountIDNotRecognisedException("account Id does not match to any account in the system");
        }

        accountsByHandle.remove(account.getHandle());
        accountsById.remove(account.getId());

    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        Account account = accountsByHandle.get(handle);

        if(account == null){
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        //setter method from Account class -> it will update straight away
        account.setDescription(description);

    }

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
        //setter method to update the handler
        account.setHandle(newHandle);

        accountsByHandle.remove(oldHandle);
        accountsByHandle.put(newHandle, account);
        
        accountsById.remove(account.getId());
        accountsById.put(account.getId(), account);
    }

    @Override
    public int getNumberOfAccounts() {
        return accountsByHandle.size();
    }

    /*
    ID: [account ID]
    Handle: [account handle]
    Description: [account description]
    Post count: [total number of posts, including endorsements and replies]
    Endorse count: [sum of endorsements received by each post of this account] */
    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        Account account = accountsByHandle.get(handle); 

        if(account == null){ //if the handle does not exist, account will be null
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }
        //need to add post count and description
        //Post count: [total number of posts, including endorsements and replies]
        //Endorse count: [sum of endorsements received by each post of this account]
        return account.showAccount();
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
    //Abstract post so it can contain from Post, Comment and Endorsement class
    private static Map<Integer, Integer> endoAndOriId = new HashMap<>();
    private static Map<Integer, Integer> comAndOriId= new HashMap<>();
    private static List<Integer> oriPostId = new ArrayList<>();


    //checking InvalidPostException -> if message is empty or has more than 100 characters
    protected static void checkMessage(String message) throws InvalidPostException {
        if ((message == null) || (message.isEmpty())){
            throw new InvalidPostException("message is empty");
        }
        if(message.length() > 100){
            throw new InvalidPostException("limit is 100 characters");
        }
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        Account account = accountsByHandle.get(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }

        checkMessage(message);

        lastPostId += 1;
        Post post = new Post(lastPostId, account, message);
        postsById.put(lastPostId, post);
        oriPostId.add(lastPostId);
        return lastPostId;
    }

    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException{
        Account account = accountsByHandle.get(handle);
        if (account == null) {
            throw new HandleNotRecognisedException("Handle does not match to any account in the system");
        }
        
        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }
        //if endorsements post -> it is not actionable. So this basically means if the post is an endorsement post
        if (post instanceof Endorsement) {
            throw new NotActionablePostException("Not an actionable post");
        }

        lastPostId += 1; //all original, comments and endorse are considerred as posts and they all have unique numerical identifier
        Endorsement endorsement = new Endorsement(lastPostId, account , post);
        post.addEndorsement(endorsement); //adding to endorsement list
        postsById.put(lastPostId, endorsement);
        endoAndOriId.put(lastPostId, id);
        return lastPostId;
    }

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
        comAndOriId.put(lastPostId, id);
        return lastPostId;
    }

    public int getOriId(int id){ //get ori id from endo and com id
        AbstractPost post = postsById.get(id);
        if (post instanceof Endorsement){
            for (int i: endoAndOriId.keySet()){
                if(i == id){
                    int oriId = endoAndOriId.get(i);
                    return oriId;
                }
            }
        }

        if (post instanceof Comment){
            for (int i: comAndOriId.keySet()){
                if(i == id){
                    int oriId = comAndOriId.get(i);
                    return oriId;
                }
            }
        }
        return 0;
    }

    public List<Integer> getEndoId(int id){ //get endo id from ori id
        List<Integer> endoList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> set : endoAndOriId.entrySet()) {
            int endoId = set.getKey();
            int oriId = set.getValue();

            //if the ori post are being endorsed -> delete all the endorsed and ori post
            if (oriId == id){
                endoList.add(endoId);
            }
        }
        return endoList;
    }

    public List<Integer> getComId(int id){ //get endo id from ori id
        List<Integer> comList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> set : comAndOriId.entrySet()) {
            int comId = set.getKey();
            int oriId = set.getValue();

            //if the ori post are being endorsed -> delete all the endorsed and ori post
            if (oriId == id){
                comList.add(comId);
            }
        }
        return comList;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        AbstractPost post = postsById.get(id);
        if (post == null) {
            throw new PostIDNotRecognisedException("Post ID does not exist in the system");
        }

        //original post
        if(post instanceof Post){
            for (int endoId: getEndoId(id)){
                AbstractPost endoPost = postsById.get(endoId);
                Endorsement endorsement = (Endorsement)endoPost;

                //remove all endorsement post
                postsById.remove(endoId);
                post.removeEndorsement(endorsement);
                endoAndOriId.remove(endoId);
            }
            //remove the original post
            oriPostId.remove(id);
            postsById.remove(id);
            //postsById.replace(id, post, null); //It should replace the ori post as null, and if the post is null, print the mssage that this post is no longer available
        }

        //if the endorsement are being remove -> remove count from ori post 
        if (post instanceof Endorsement){
            AbstractPost oriPost = postsById.get(getOriId(id));
            Endorsement endorsement = (Endorsement)post;
            oriPost.removeEndorsement(endorsement);
            postsById.remove(id);
            endoAndOriId.remove(id);
        }
        
        if (post instanceof Comment){
            //same with ori post above
            //just remove the comAndOriId hashmap
            //also remove count from Abstract post
        }
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
        HashMap<Integer, Integer> length= new HashMap<Integer, Integer>();
        int mostPost=0;
        for (int id: postsById.keySet()){
            length.put(id, getEndoId(id).size());
        }
        int maxLength = (Collections.max(length.values()));
        for(Entry<Integer, Integer> entry : length.entrySet()){
            if(entry.getValue()==maxLength){
               mostPost= entry.getKey();
            }
        }
        return mostPost;
    }

    @Override
    public int getMostEndorsedAccount() {
        Map<Integer, Integer> length = new HashMap<Integer, Integer>();
        int mostPost=0;
        for (Map.Entry<Integer, AbstractPost> set : postsById.entrySet()) {
            int postId = set.getKey();
            AbstractPost post = set.getValue();

            int accId = post.getAccount().getId();
            int totalEndo = length.containsKey(accId)? length.get(accId):0;
            totalEndo += getEndoId(postId).size();
            
            length.put(accId, totalEndo);
        }
        int maxLength = (Collections.max(length.values()));
        for(Entry<Integer, Integer> entry : length.entrySet()){
            if(entry.getValue()==maxLength){
               mostPost= entry.getKey();
            }
        }
        return mostPost;
    }

    @Override
    public void erasePlatform() {
        accountsByHandle.clear();
        accountsById.clear();
        postsById.clear();
        endoAndOriId.clear();
        comAndOriId.clear();
        oriPostId.clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        // TODO
        //soemthing abour serialized -> go back to this week lecture
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO
    }
    
    @Override
    public int getTotalOriginalPosts() {
        return oriPostId.size();
    }

    @Override
    public int getTotalEndorsmentPosts() {
        return endoAndOriId.size();
    }

    @Override
    public int getTotalCommentPosts() {
        return comAndOriId.size();
    }

    //no need to submit this one. Just for testing -> might be useful for the SocialMediaPlatformTest though
    public static void main (String[]args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException{
        SocialMedia1 a = new SocialMedia1();
        System.out.println("####### ACCOUNTS REGISTERRED ######");
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
            System.out.println(accountsByHandle.get(handler).showAccount());
            System.out.println();
        }*/

        //testing removing account and show to see if it is removed -> work
        a.removeAccount("Siti");
        //a.removeAccount("Lolly");
        
        //uncomment to see
        /*for (String handler: accountsByHandle.keySet()){
            System.out.println(accountsByHandle.get(handler).showAccount());
            System.out.println();
        }*/

        //works
        a.updateAccountDescription("Alya", "May the patience be with you, RIP java");
        //uncomment to see
        /*for (String handler: accountsByHandle.keySet()){
            System.out.println(accountsByHandle.get(handler).showAccount());
            System.out.println();
        }*/

        //works
        a.changeAccountHandle("John", "Legend");
        //a.changeAccountHandle("Lolly", "Patrick");
        /*for (String handler: accountsByHandle.keySet()){
            System.out.println(a.showAccount(handler));
            System.out.println();
        }

        System.out.println("Total Number of accounts in the system: "+ a.getNumberOfAccounts());
        System.out.println();

        System.out.println("####### POST INFORMATION ######"); */

        //Works
        a.createPost("Alya", "Life be hitting so hard these days");
        a.createPost("Alya", "I love Jollibee");
        a.createPost("Raiet", "surviving I guess");
        a.createPost("Legend", "When life gives you lemon, eat it");
        //a.createPost("Lolly", "My account does not exist");


        try {
            a.endorsePost("Raiet", 4);
            a.endorsePost("Alya", 3);
            a.endorsePost("Alya", 3);
            a.endorsePost("Raiet", 3);
            
            
        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (NotActionablePostException e) {
            throw new RuntimeException(e);
        }catch (HandleNotRecognisedException e) {
            throw new RuntimeException(e);
        }

        /*for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }*/

        try {
            a.commentPost("Raiet", 4, "I only have limes!");
            a.commentPost("Alya", 4, "Lol");
            a.commentPost("Legend", 2, "KFC for me");
            a.commentPost("Raiet", 9, "Right");
            a.commentPost("Raiet", 9, "Actually no");
            

        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (NotActionablePostException e) {
            throw new RuntimeException(e);
        } catch (HandleNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (InvalidPostException e) {
            throw new RuntimeException(e);
        }

        /*for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }*/

        //checking if it will change SocialMediaSystem platform 
        //a.changeAccountHandle("Raiet", "BOSS");
        /*or (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }  */

        /*try {
            System.out.println(a.showIndividualPost(4));
        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } */

        /*try {
            System.out.println(a.showPostChildrenDetails(4));
        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } catch (NotActionablePostException e) {
            throw new RuntimeException(e);
        }*/

        try {
            a.deletePost(3);

        } catch (PostIDNotRecognisedException e) {
            throw new RuntimeException(e);
        } 

        for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }

        /*System.out.println("Most endorsed post ID: "+ a.getMostEndorsedPost());
        System.out.println("Most endorsed Account ID: "+ a.getMostEndorsedAccount());
        System.out.println("Total original post: "+ a.getTotalOriginalPosts());
        System.out.println("Total endorsement post: "+ a.getTotalEndorsmentPosts());
        System.out.println("Total comment post: "+ a.getTotalCommentPosts());*/
        
        //a.erasePlatform();
        /*a.createAccount("Syafiq", "Hello");
        a.createAccount("Naqiyyah", "Bye");
        a.createAccount("Mommy", "A genius");
        a.createAccount("John");
        
        for (String handler: accountsByHandle.keySet()){
            System.out.println(a.showAccount(handler));
            System.out.println();
        } 

        System.out.println("Total Number of accounts in the system: "+ a.getNumberOfAccounts());
        System.out.println();

        System.out.println("####### POST INFORMATION ######");

        a.createPost("Naqiyyah", "Life be hitting so hard these days");
        a.createPost("Mommy", "I love Jollibee");
        a.createPost("Mommy", "surviving I guess");
        a.createPost("Syafiq", "When life gives you lemon, eat it");
        for (AbstractPost post : postsById.values()) {
            System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
        }*/
        
    }
}