package socialmedia.internal;

/**
 * A class that contain information about the comment post.
 * It extends an abstract class called AbstractRepost. 
 */
public class Comment extends AbstractRepost {
    //creating an instance
    private String message;

    /**
     * The constructor; Comment(id, account, sourcePost, message)
     * 
     * @param id post's id
     * @param account account that post this comment
     * @param sourcePost the reference post being commented
     * @param message the message being posted
     */
    public Comment(int id, Account account, AbstractPost sourcePost, String message) {
        super(id, account, sourcePost);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin) {
        return super.showPostHeader(sb, margin)
                .append(margin).append(getMessage()).append("\n");
    }
}