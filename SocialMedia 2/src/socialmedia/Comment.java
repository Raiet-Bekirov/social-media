package socialmedia.internal;

public class Comment extends AbstractPost {

    private AbstractPost post;
    private String message;

    public Comment(int id, Account account, AbstractPost post, String message) {
        super(id, account);
        this.post = post;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin) {
        return super.showPostHeader(sb, margin)
                .append(margin).append(getMessage()).append("\n");
    }
}
