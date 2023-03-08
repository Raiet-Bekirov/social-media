package socialmedia.internal;

public class Post extends AbstractPost {

    private String message;

    public Post(int id, Account account, String message) {
        super(id, account);
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
