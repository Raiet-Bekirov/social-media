package socialmedia;

import socialmedia.internal.*;

import java.io.*;
import java.util.*;

/** 
 * This class implements functionalities provided in the SocialMediaPlatform that extends MiniSocialMediaPlatform.
 * 
 * @author 720027829 
 * @author <<Raiet's student ID>>
 * 
 * @version 26/03/2023
 * 
 */
public class SocialMedia implements SocialMediaPlatform { 
  private int lastAccountId = 0; //initialize account id -> all accounts are unique
  private int lastPostId = 0; //initialize posts id -> all posts are unique

  //Where we store all accounts and posts information
  private static HashMap<String, Account> accountsByHandle = new HashMap<String, Account>();
  private static HashMap<Integer, Account> accountsById = new HashMap<Integer, Account>();
  private static HashMap<Integer, AbstractPost> postsById = new HashMap<>();

  /**
   * The method checks handle for InvalidHandleException
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
      checkHandle(handle);//for InvalidHandleException
      if (accountsByHandle.containsKey(handle)) { //if the account has the same name
          throw new IllegalHandleException("the handle already exists in the platform");
      }

      lastAccountId += 1;
      Account account = new Account(lastAccountId, handle, description); 
      accountsByHandle.put(handle, account); //put into the list of handle, account
      accountsById.put(account.getId(), account); //put into the list of id and account
      return account.getId(); //the id of created account
  }

  @Override
  public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException{
      checkHandle(handle);
      if (accountsByHandle.containsKey(handle)) { //if the account has the same name
          throw new IllegalHandleException("the handle already exists in the platform");
      }
      lastAccountId += 1;
      Account account = new Account(lastAccountId, handle); 
      accountsByHandle.put(handle, account); //put into the list of handle, account
      accountsById.put(account.getId(), account); //put into the list of id and account
      return account.getId(); //the id of created account
  }

  /** 
   * This method will delete all post relating to the account being deleted. It will also delete that particular account.
   * This method will be used in removeAccount function overriden from the SocialMediaPlatform interface.
   * 
   * @param account Account(handle, description) that you want to delete
   * 
   */
  private void removeAccountInternal(Account account) {
      //the method getPosts below returns list of posts relating to that account handle
      List<AbstractPost> posts = getPosts(account.getHandle());
      for (AbstractPost post : posts) {
        deletePostInternal(post); //delete all post made by that handle
      }

      //remove the account from the system
      accountsByHandle.remove(account.getHandle());
      accountsById.remove(account.getId());
  }

  @Override
  public void removeAccount(String handle) throws HandleNotRecognisedException{
      Account account = accountsByHandle.get(handle);
      // get the account information i.e Account(handle, description)

      if(account == null){
          throw new HandleNotRecognisedException("Handle does not match to any account in the system");
      }

      //method made above that deletes all post relating to that account as well as that account itself
      removeAccountInternal(account);
  }

  @Override
  public void removeAccount(int id) throws AccountIDNotRecognisedException{
      Account account = accountsById.get(id);
      // get the account information i.e Account(handle, description)

      if(account == null){
          throw new AccountIDNotRecognisedException("account Id does not match to any account in the system");
      }

      //method made above that deletes all post relating to that account as well as that account itself
      removeAccountInternal(account);
  }

  @Override
  public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
      Account account = accountsByHandle.get(handle);

      if(account == null){
          throw new HandleNotRecognisedException("Handle does not match to any account in the system");
      }

      //setter method from Accopunt class -> it will update straight away
      account.setDescription(description);

  }

  @Override
  public void changeAccountHandle(String oldHandle, String newHandle)
          throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
      checkHandle(newHandle); //InvalidHandleException (because you want to create new so have to check the features)

      if (accountsByHandle.containsKey(newHandle)) { 
          throw new IllegalHandleException("Handle already exists in the platform");
      }

      Account account = accountsByHandle.get(oldHandle);
      if(account == null){
          throw new HandleNotRecognisedException("Old Handle does not match to any account in the system");
      }
      
      //setter method to update the handler
      account.setHandle(newHandle);

      //remove the old handle from the system and put in the new handle name
      accountsByHandle.remove(oldHandle);
      accountsByHandle.put(newHandle, account);

      //repeat with id becuase the account have changed
      accountsById.remove(account.getId());
      accountsById.put(account.getId(), account);

  }

  @Override
  public int getNumberOfAccounts() {
      return accountsByHandle.size();
      //basically just the size of the hashmap where we store all information
  }

  /*
   * It has to show something like this:
   * ID: [account ID]
   * Handle: [account handle]
   * Description: [ account description]
   * Post count: [total number of posts, including endorsements and comments]
   * Endorse count: [sum of endorsements received by each post of this account]
   */
  @Override
  public String showAccount(String handle) throws HandleNotRecognisedException {
      Account account = accountsByHandle.get(handle);
      if(account == null){ //if the handle does not exist, account will be null
          throw new HandleNotRecognisedException("Handle does not match to any account in the system");
      }
      
      //getPosts contains list of posts that is being posted by that handle
      List<AbstractPost> posts = getPosts(handle);
      int postCount= posts.size(); 
      //size is the sum of the list so basically sum of posts made by that handle


      int endorseCount=0;
      for (AbstractPost a: posts){
          //calculating the sum of endorsed receive by that handle
          endorseCount += a.getEndorsementsCount();
      }

      return account.showAccount(new StringBuilder()).append("Post count: ")
              .append(postCount).append("\n").append("Endorse count: ")
              .append(endorseCount).append("\n").toString();
  }
  //END OF ACCOUNT MANAGEMENT//

  //CONTINUE WITH POST MANAGEMENT//
  /*AIM:
    * User can post message up to 100 characters /
    * Users can comment on post /
    * can endorse post #but, endorsement post cannot be commented or endorse /
    * !!post , comment and endorse has unique identifier /
    *
    * After you've done all these. Think about deleteing account -> should remove all the post . Change description -> should not change all the posts /
    */

  /**
   * This method returns the post information according to the post ID
   * @param id post ID
   * @return the post information from Post, Endorsement or Comment class 
   */
  protected AbstractPost getPost(int id) {
      return postsById.get(id);
  }

  /**
   * This method returns list of posts made by that particular handle
   * 
   * @param handle account's handle
   * @return list of posts made by that handle
   */
  private List<AbstractPost> getPosts(String handle){
      List<AbstractPost> list = new ArrayList<>();
      if (handle == null) return list;
      for (var post : postsById.values()) {
          if(handle.equals(post.getHandle())){
              list.add(post); //add all the post if the handle is the one mentioned
          }
      }
      return list;
  }

  /**
   * This method check the message for InvalidPostException. 
   * It will be used in createPost() and commentPost() funtions that are overriden from SocialMediaPlatform interface
   * @param message post's message
   * @throws InvalidPostException when attempting to create a post which message is empty or has more characters that the system's limit (100 characters)
   */
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
      if (account == null) { //account is not in the system
          throw new HandleNotRecognisedException("Handle does not match to any account in the system");
      }

      checkMessage(message); //InvalidPostException

      lastPostId += 1;
      //Creating an object from Post class (and extention from and abstract class called AbstractPost)
      //Original Post
      Post post = new Post(lastPostId, account, message);
      postsById.put(post.getId(), post); //postsById is a hashmap of (post id, post information)
      return lastPostId;
  }

  @Override
  public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException{
      Account account = accountsByHandle.get(handle);
      if (account == null) { //if account not in system
          throw new HandleNotRecognisedException("Handle does not match to any account in the system");
      }

      AbstractPost post = postsById.get(id);
      if (post == null) { //if post not in system
          throw new PostIDNotRecognisedException("Post ID does not exist in the system");
      }

      //if the post is an endorsement post
      if (post instanceof Endorsement) {
          throw new NotActionablePostException("Not an actionable post");
      }

      
      lastPostId += 1; //all original, comments and endorse are considerred as posts and they all have unique numerical identifier
      //creating the message to be printed under endosement post
      //"EP@" + [endorsed account handle] + ": " + [endorsed message]
      String message = "EP@"+ post.getHandle() + ": " + post.getMessage();
      //Creating an object from Endorsement class (and extention from and abstract class called AbstractPost)
      //Endorsement Post
      Endorsement endorsement = new Endorsement(lastPostId, account, post, message);
      post.addEndorsement(endorsement);
      postsById.put(lastPostId, endorsement);
      return lastPostId;
  }

  @Override
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

      checkMessage(message); //InvalidPostException

      lastPostId += 1;
      //Creating an object from Comment class (and extention from and abstract class called AbstractPost)
      //Comment Post
      Comment comment = new Comment(lastPostId, account, post, message);
      post.addComment(comment);
      postsById.put(lastPostId, comment);

      return lastPostId;
  }

  /**
   * This method delete the post from the platform. When a post is deleted (original or comment), all it's
   * endorsements will be deleted as well. If the post has comments, the comment will still remain but the deleted post will be replaced
   * with an empty post. 
   * 
   * The empty post message will be "The original content was removed from the system and is no longer available" and this 
   * empty post is not actionable.
   * 
   * This method will be used in deletePost() funtion that is overriden from SocialMediaPlatform interface
   * 
   * @param post post i.e Post, Endorsement or Comment that you want to delete.
   */
  private void deletePostInternal(AbstractPost post) {
    //delete all endorsement related to that post
    //if the total endorsement count is more than 0
    if (post.getEndorsementsCount() > 0) {
      //getEndorsement() is a method from AbstractPost that returns list of endorsement from that particular post
      List<Endorsement> endorsements = new ArrayList<>(post.getEndorsements());
      for (Endorsement endorsement : endorsements) {
        deletePostInternal(endorsement);
      }
    }

    //if the post being deleted is an original post and it has more than 0 comments
    if(post instanceof Post){
      if (post.getCommentsCount() > 0) {
          Post emptyPost = new Post(-1, null, "The original content was removed from the system and is no longer available.");
          //getComments() is a method from AbstractPost that returns list of comments from that particular post
          List<Comment> comments = post.getComments();
          for (Comment comment : comments) {
              //basically changing the post to empty post
              comment.setSourcePost(emptyPost);
          }
      }
    }

    if(post instanceof Comment){
      if (post.getCommentsCount() > 0) {
        Comment emptyPost = new Comment(-1, null, post, "The original content was removed from the system and is no longer available.");
        List<Comment> comments = post.getComments();
        for (Comment comment : comments) {
            //basically changing the post to empty post
            comment.setSourcePost(emptyPost);
        }
      }

    }


    if (post instanceof AbstractRepost) {
        AbstractRepost repost = (AbstractRepost) post;
        //get the id of the post
        Integer sourcePostId = repost.getSourcePostId();
        AbstractPost sourcePost = getPost(sourcePostId); //get the reference post from that id

        if (repost instanceof Endorsement) {
            if(sourcePost == null);
            else sourcePost.removeEndorsement((Endorsement) repost); //remove the endorsement from the list in AbstractPost class
        }
        if (repost instanceof Comment) {
          if(sourcePost == null); //needed when removing account after deleting post to handle NullPointerException (if post is deleted, it will be null. So avoid repetition when deleting)
          else sourcePost.removeComment((Comment) repost); //remove comment from the list in AbstractPost Class
        }
    }

    postsById.remove(post.getId()); //remove the post from the system
  }

  @Override
  public void deletePost(int id) throws PostIDNotRecognisedException {
      AbstractPost post = postsById.get(id);
      if (post == null) {
          throw new PostIDNotRecognisedException("Post ID does not exist in the system");
      }

      //method made above that delete the post 
      deletePostInternal(post);
  }

  @Override
  public String showIndividualPost(int id) throws PostIDNotRecognisedException {
      AbstractPost post = postsById.get(id);
      if (post == null) {
          throw new PostIDNotRecognisedException("Post ID does not exist in the system");
      }

      //creted in AbstractPost class. Basically if withChildren: false means that just return the individual post with no chains
      return post.showPostDetails(new StringBuilder(), false).toString();
  }

  @Override
  public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
      AbstractPost post = postsById.get(id);
      if (post == null) {
          throw new PostIDNotRecognisedException("Post ID does not exist in the system");
      }

      //since withChildren : true, it means that it will return the post id with chains if being commented on
      return post.showPostDetails(new StringBuilder(), true);
  }

  @Override
  public int getMostEndorsedPost() {
      int mostPost = 0;
      int maxEndr = -1; 

      for (AbstractPost post : postsById.values()) {
          if (post.getEndorsementsCount() > maxEndr) {
              //if the total endorsement for that post is greter than the max,
              mostPost = post.getId(); //returns the id of the post
              maxEndr = post.getEndorsementsCount(); //set the maxEndr as the new maximum
          }
      }

      return mostPost; 
  }

  @Override
  public int getMostEndorsedAccount() {
      Map<Integer, Integer> endrCount = new HashMap<Integer, Integer>();
      int mostAccount = 0;
      int maxEndr = -1;
      for (AbstractPost post : postsById.values()) {
          if (post.getEndorsementsCount() > 0) {
              //get the id of the account
              Integer accountId = post.getAccountId();

              Integer total = endrCount.get(accountId);
              if (total == null) {
                  total = 0;
              }
              //total of endorsement for that account id
              total += post.getEndorsementsCount();
              endrCount.put(accountId, total);
              if (total > maxEndr) {
                  mostAccount = accountId;
                  maxEndr = total;
              }
          }
      }

      return mostAccount; //account id of the most endorsed account
  }

  @Override
  public void erasePlatform() {
      //reset account Id count
      lastAccountId = 0;
      //new hashmap
      accountsByHandle = new HashMap<String, Account>();
      accountsById = new HashMap<Integer, Account>();

      //reset post Id count
      lastPostId = 0;
      //new hashmap
      postsById = new HashMap<>();
  }

  //serialization
  @Override
  public void savePlatform(String filename) throws IOException {
      //use try to close it automatically
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
        ArrayList<Account> accountList = new ArrayList<>(accountsById.values());
        ArrayList<AbstractPost> postList = new ArrayList<>(postsById.values());

        out.writeInt(lastAccountId);
        out.writeObject(accountList);
        out.writeInt(lastPostId);
        out.writeObject(postList);
      }
  }

  //deserialization
  @SuppressWarnings("unchecked")
  @Override
  public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
      //use try to close it automatically
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
        int lastAccId = in.readInt();
        Object obj = in.readObject();
        List<Account> accountList = (List<Account>) obj;
        int lastPId = in.readInt();
        obj = in.readObject();
        List<AbstractPost> postList = (List<AbstractPost>) obj;

        erasePlatform();
        lastAccountId = lastAccId;
        lastPostId = lastPId;

        for (Account account : accountList) {
            accountsById.put(account.getId(), account);
            accountsByHandle.put(account.getHandle(), account);
        }

        Map<Integer, List<Comment>> commentMap = new HashMap<>();
        Map<Integer, List<Endorsement>> endorsementMap = new HashMap<>();
        for (AbstractPost post : postList) {
            postsById.put(post.getId(), post);

            if (post instanceof Comment comment) {
                Integer sourcePostId = comment.getSourcePostId();
                List<Comment> commentList = commentMap.get(sourcePostId);
                if (commentList == null) {
                    commentList = new ArrayList<>();
                    commentMap.put(sourcePostId, commentList);
                }
                commentList.add(comment);
            }

            if (post instanceof Endorsement endorsement) {
                Integer sourcePostId = endorsement.getSourcePostId();
                List<Endorsement> endorsementList = endorsementMap.get(sourcePostId);
                if (endorsementList == null) {
                    endorsementList = new ArrayList<>();
                    endorsementMap.put(sourcePostId, endorsementList);
                }
                endorsementList.add(endorsement);
            }
        }

        for (AbstractPost post : postList) {
            Integer accountId = post.getAccountId();
            Account account = accountsById.get(accountId);
            post.setAccount(account);

            post.setComments(commentMap.get(post.getId()));
            post.setEndorsements(endorsementMap.get(post.getId()));

            if (post instanceof AbstractRepost repost) {
                repost.setSourcePost(postsById.get(repost.getSourcePostId()));
            }
        }
      }
  }

  @Override
  public int getTotalOriginalPosts() {
      int total = 0;
      for (AbstractPost post : postsById.values()) {
          //if the post is original post
          if (post instanceof Post) {
              total += 1; 
          }
      }

      return total;
  }

  @Override
  public int getTotalEndorsmentPosts() {
      int total = 0;
      for (AbstractPost post : postsById.values()) {
          //if the post is endorsement post
          if (post instanceof Endorsement) {
              total += 1;
          }
      }

      return total;
  }

  @Override
  public int getTotalCommentPosts() {
      int total = 0;
      for (AbstractPost post : postsById.values()) {
          //if the post is Comment post
          if (post instanceof Comment) {
              total += 1;
          }
      }

      return total;
  }

  public static void main (String[]args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException, AccountIDNotRecognisedException, NotActionablePostException, PostIDNotRecognisedException{
      SocialMedia a = new SocialMedia();
      int accId1= a.createAccount("Alya", "Hello");
      int accId2 =a.createAccount("Siti", "Bye");
      int accId3=a.createAccount("Raiet", "A genius");
      int accId4=a.createAccount("John");
      //give exception -> uncomment one by one to see
      //a.createAccount("No t", "Ay");
      //a.createAccount("Alya", "P");
      //a.createAccount("nwgvbaiubdwigdvvbbjshygafwbdbkauhs", " ");

      //testing each functionality, to test, add comment uncomment each one
      //a.removeAccount("Siti");
      a.removeAccount(accId2);

      a.updateAccountDescription("Alya", "May the patience be with you, RIP java");

      a.changeAccountHandle("John", "Legend");

      //TESTING POST INFORMATION
      int postId1= a.createPost("Alya", "Life be hitting so hard these days");
      int postId2= a.createPost("Alya", "I love Jollibee");
      int postId3= a.createPost("Legend", "Yeeeees!");
      int postId4= a.createPost("Raiet", "surviving I guess");

      int end1, end2, end3, end4;
      try {
          end1 = a.endorsePost("Alya", postId3); //5
          end2 = a.endorsePost("Raiet", postId1); //6
          end3 = a.endorsePost("Raiet", postId3); //7
          end4 = a.endorsePost("Legend", postId2); //8
      } catch (PostIDNotRecognisedException e) {
          throw new RuntimeException(e);
      } catch (NotActionablePostException e) {
          throw new RuntimeException(e);
      }

      int comm1,comm2,comm3, comm4;
      try {
          comm1 = a.commentPost("Raiet", postId2, "Super!"); //9
          comm2 = a.commentPost("Legend", postId3, "NO"); //10
          comm3 = a.commentPost("Alya", comm1, "Hey"); //11
          comm4 = a.commentPost("Alya", postId3, "Whatever"); //12
      } catch (PostIDNotRecognisedException e) {
          throw new RuntimeException(e);
      } catch (NotActionablePostException e) {
          throw new RuntimeException(e);
      }

      a.changeAccountHandle("Raiet", "BOSS");

      int comm5;
      try {
          comm5 = a.commentPost("BOSS", postId2, "lol"); //13
      } catch (PostIDNotRecognisedException e) {
          throw new RuntimeException(e);
      } catch (NotActionablePostException e) {
          throw new RuntimeException(e);
      }

      /*try {
        a.removeAccount(1);
      } catch (AccountIDNotRecognisedException e) {
          e.printStackTrace();
      }*/


      // test save / load platform
      /*String filename = "pl_dump.ser";
      try {
          a.savePlatform(filename);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
      System.out.println(" --- Platform saved");
      System.out.println("Total accounts: " + a.getNumberOfAccounts());
      System.out.println("Total posts: " + a.getTotalOriginalPosts());
      System.out.println("Total endorsements: " + a.getTotalEndorsmentPosts());
      System.out.println("Total comments: " + a.getTotalCommentPosts());*/

      /*a.erasePlatform();
      System.out.println(" --- Platform erased");
      System.out.println("Total accounts: " + a.getNumberOfAccounts());
      System.out.println("Total posts: " + a.getTotalOriginalPosts());
      System.out.println("Total endorsements: " + a.getTotalEndorsmentPosts());
      System.out.println("Total comments: " + a.getTotalCommentPosts());

      try {
          a.loadPlatform(filename);
      } catch (IOException e) {
          throw new RuntimeException(e);
      } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
      }

      System.out.println(" --- Platform loaded");
      System.out.println("Total accounts: " + a.getNumberOfAccounts());
      System.out.println("Total posts: " + a.getTotalOriginalPosts());
      System.out.println("Total endorsements: " + a.getTotalEndorsmentPosts());
      System.out.println("Total comments: " + a.getTotalCommentPosts());*/


      System.out.println("Most endorsed post: " + a.getMostEndorsedPost());
      System.out.println("Most endorsed account: " + a.getMostEndorsedAccount());

      System.out.println("Total accounts: " + a.getNumberOfAccounts());
      System.out.println("Total posts: " + a.getTotalOriginalPosts());
      System.out.println("Total endorsements: " + a.getTotalEndorsmentPosts());
      System.out.println("Total comments: " + a.getTotalCommentPosts());
      System.out.println();

      System.out.println("---------ACCOUNT INFORMATION-----------");
      for (Account account : accountsById.values()) {
        System.out.println(a.showAccount(account.getHandle()));
      }
      System.out.println();

      System.out.println("-----------POST INFORMATION-------------");
      for (AbstractPost post : postsById.values()) {
        System.out.println(post.showPostDetails(new StringBuilder(), true).toString());
      }
      System.out.println();

  }
}