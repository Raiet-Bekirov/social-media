package socialmedia;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractPost {
    private int id;
    private Account account;
    private Post oriPost;

    private List<Comment> comments;
    private List<Endorsement> endorsements;

    public AbstractPost(int id, Account account) {
        this.id = id;
        this.account = account;

        comments = new ArrayList<>(); // lists of comments
        endorsements = new ArrayList<>(); //lists of endorsements
    }

    public int getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getHandle() {
        return account == null ? null : account.getHandle();
    }

    public String getMessage() {
        return oriPost.getMessage();
    }


    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public int getCommentsCount() {
        return comments.size();
    }
    public void addEndorsement(Endorsement endorsement) {
        endorsements.add(endorsement);
    }

    public void removeEndorsement(Endorsement endorsement) {
        endorsements.remove(endorsement);
    }

    public int getEndorsementsCount() {
        return endorsements.size(); //total endorsements
    }

    public StringBuilder showPostDetails(StringBuilder sb, boolean withChildren) {
        return showPostInternal(sb, "", withChildren);
    }

    protected StringBuilder showPostInternal(StringBuilder sb, String margin, boolean withChildren) {
        /*if(oriPost == null){
            StringBuilder res = showMessage(sb, margin);
            if (withChildren && (getCommentsCount() > 0)) {
                res = showTreeRootLine(res, margin);
                String new_margin = getTreeMargin(margin);
                for (Comment comment : comments) {
                    res = showTreeArrow(res, margin);
                    res = comment.showPostInternal(res, new_margin, withChildren);
                }
            }
            return res;

        }*/
        StringBuilder res = showPostHeader(sb, margin); //The main post that is being commented on
        if (withChildren && (getCommentsCount() > 0)) {
            res = showTreeRootLine(res, margin);
            String new_margin = getTreeMargin(margin);
            for (Comment comment : comments) { //show all the comments on that post
                res = showTreeArrow(res, margin); //>
                res = comment.showPostInternal(res, new_margin, withChildren); //the comment
            }
        }
        return res;
        
    }

    protected StringBuilder showMessage(StringBuilder sb, String margin){
        return sb.append(margin).append("The original content was removed from the system and is no longer available.").append("\n");
    
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
        /*if (oriPost == null){
            return sb.append(margin).append("The original content was removed from the system and is no longer available.");
        }
        else{
            return sb.append("ID: ").append(id).append("\n")
                .append(margin).append("Account: ").append(getHandle()).append("\n")
                .append(margin).append("No. endorsements: ").append(getEndorsementsCount())
                .append(" | No. comments: ").append(getCommentsCount()).append("\n");
        }*/
        
    }
    //change here somewhere if post is null, print the message "post is deleted"

}