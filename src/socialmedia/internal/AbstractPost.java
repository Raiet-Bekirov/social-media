package socialmedia.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A parent class for post information. 
 * An abstract class as well as a superclass containing 4 subclasses that extends it ; Post, Endorsement, Comment class 
 * and AbstractRepost abstract class
 * 
 * The subclasses inherit the characteristics of a superclass
 * 
 */
abstract public class AbstractPost implements Serializable {
    //transient = to mark member variable not to be serialized 
    //creating instance
    private int id;
    private transient Account account;
    private Integer accountId;

    private transient List<Comment> comments;
    private transient List<Endorsement> endorsements;

    /**
     * The constructor; AbstractPost(id, account)
     * (Note: account gives information about account's id, account's handler and account's description)
     * 
     * @param id post's id
     * @param account account that post it
     */
    public AbstractPost(int id, Account account) {
        this.id = id;
        setAccount(account);
    }

    /**
     * This method returns the id of a particular post
     * @return post's id
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the account of a particular post
     * Note: Account(account's id, handle, description)
     * 
     * @return account that post it
     */
    public Account getAccount() {
        return account;
    }

    /**
     * This method allows you to update the account, and account Id in the system
     * @param account new account
     */
    public void setAccount(Account account) {
        this.account = account;
        this.accountId = (account == null) ? null : account.getId();
    }

    /**
     * This method returns the account's id that made the post
     * @return account's id 
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * This method returns the account's handle that made the post
     * @return account's id
     */
    public String getHandle() {
        //if account is null, just return null and otherwise, return the handle
        return account == null ? null : account.getHandle();
    }

    /**
     * This is an abstract method from AbstractPost that Post, Endorsement and Comment class will override.
     * It will return the post's message that will be shown under the post information.
     * 
     * @return post's message
     */
    public abstract String getMessage();

    /**
     * This method returns list of comments received by that post
     * @return list of comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * This method update the list comments received by that post with a new list of comments
     * @param comments new list of comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * This method will add comment post object into the list of comments received by that post
     * @param comment comment post ; Comment(post id, account, reference post, message)
     */
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    /**
     * This method will remove comment post object from the list of comments received by that post
     * @param comment comment post; Comment(post id, account, reference post, message)
     */
    public void removeComment(Comment comment) {
        if (comments != null) {
            comments.remove(comment);
        }
    }

    /**
     * This method returns the total number of comments received by that post
     * @return total number of comments received
     */
    public int getCommentsCount() {
        return (comments == null) ? 0 : comments.size();
    }

    /**
     * This method returns list of endorsements received by that post
     * @return list of endorsements
     */
    public List<Endorsement> getEndorsements() {
        return endorsements;
    }

    /**
     * This method update the list endorsements received by that post with a new list of endorsements
     * @param endorsements new list of endorsements
     */
    public void setEndorsements(List<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    /**
     * This method will add endorsement post object into the list of endorsements received by that post
     * @param comment endorsement post ; Endorsement(post id, account, reference post, message)
     */
    public void addEndorsement(Endorsement endorsement) {
        if (endorsements == null) {
            endorsements = new ArrayList<>();
        }
        endorsements.add(endorsement);
    }

    /**
     * This method will remove endorsement post object from the list of endorsements received by that post
     * @param comment endorsement post; Endorsement(post id, account, reference post, message)
     */
    public void removeEndorsement(Endorsement endorsement) {
        if (endorsements != null) {
            endorsements.remove(endorsement);
        }
    }

    /**
     * This method returns the total number of endorsements received by that post
     * @return total number of endorsements received
     */
    public int getEndorsementsCount() {
        return (endorsements == null) ? 0 : endorsements.size();
    }

    /**
     * This method will show the post in this format (this example is for post ID=2):
     * ID: 2
     * Account: Anonymous
     * No. endorsements: 1 | No. comments: 2
     * I love Jollibee
     * |
     * | > ID: 9
     *     Account: BOSS
     *     No. endorsements: 0 | No. comments: 1
     *     me too !
     *      |
     *      | > ID: 11
     *          Account: Anonymous  
     *          No. endorsements: 0 | No. comments: 0
     *          great, we can go together :)
     * | > ID: 13
     *     Account: Legend
     *     No. endorsements: 0 | No. comments: 0
     *     KFC is better, no doubt.
     * 
     * @param sb variable declaration to create a StringBuilder object. To use: .showPostDetails(new StringBuilder(), withChildren: true/false).toString()
     * @param withChildren true -> show all the comment chains || false -> show the individual post only
     * @return post information 
     */
    public StringBuilder showPostDetails(StringBuilder sb, boolean withChildren) {
        return showPostInternal(sb, "", withChildren);
    }

    protected StringBuilder showPostInternal(StringBuilder sb, String margin, boolean withChildren) {
        StringBuilder res = showPostHeader(sb, margin); 
        //if the children is true and the post has more than 0 comments,
        if (withChildren && (getCommentsCount() > 0)) {
            res = showTreeRootLine(res, margin); //|
            String new_margin = getTreeMargin(margin); // |>
            if (comments != null) {  //if the post has comments basically
                for (Comment comment : comments) {
                    res = showTreeArrow(res, margin); //|>
                    res = comment.showPostInternal(res, new_margin, withChildren); //show all the comments on that post
                }
            }
        }

        return res;
    }

    protected StringBuilder showTreeRootLine(StringBuilder sb, String margin) {
        return sb.append(margin).append("|\n");
    }

    protected StringBuilder showTreeArrow(StringBuilder sb, String margin) {
        return sb.append(margin).append("| > ");
    }

    protected String getTreeMargin(String margin) {
        return margin + "    ";
    }

    protected StringBuilder showPostHeader(StringBuilder sb, String margin) {
        return sb.append("ID: ").append(id).append("\n")
                .append(margin).append("Account: ").append(getHandle()).append("\n")
                .append(margin).append("No. endorsements: ").append(getEndorsementsCount())
                .append(" | No. comments: ").append(getCommentsCount()).append("\n");
    }

}