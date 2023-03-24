package socialmedia.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractPost implements Serializable {
    private int id;
    private transient Account account;
    private Integer accountId;

    private transient List<Comment> comments;
    private transient List<Endorsement> endorsements;

    public AbstractPost(int id, Account account) {
        this.id = id;
        setAccount(account);
    }

    public int getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.accountId = (account == null) ? null : account.getId();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getHandle() {
        return account == null ? null : account.getHandle();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        if (comments != null) {
            comments.remove(comment);
        }
    }

    public int getCommentsCount() {
        return (comments == null) ? 0 : comments.size();
    }


    public List<Endorsement> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(List<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    public void addEndorsement(Endorsement endorsement) {
        if (endorsements == null) {
            endorsements = new ArrayList<>();
        }
        endorsements.add(endorsement);
    }

    public void removeEndorsement(Endorsement endorsement) {
        if (endorsements != null) {
            endorsements.remove(endorsement);
        }
    }

    public int getEndorsementsCount() {
        return (endorsements == null) ? 0 : endorsements.size();
    }

    public StringBuilder showPostDetails(StringBuilder sb, boolean withChildren) {
        return showPostInternal(sb, "", withChildren);
    }

    protected StringBuilder showPostInternal(StringBuilder sb, String margin, boolean withChildren) {
        StringBuilder res = showPostHeader(sb, margin);
        if (withChildren && (getCommentsCount() > 0)) {
            res = showTreeRootLine(res, margin);
            String new_margin = getTreeMargin(margin);
            if (comments != null) {
                for (Comment comment : comments) {
                    res = showTreeArrow(res, margin);
                    res = comment.showPostInternal(res, new_margin, withChildren);
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
