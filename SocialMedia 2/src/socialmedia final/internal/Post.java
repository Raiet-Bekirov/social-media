package socialmedia.internal;

import java.io.Serializable;

public class Post extends AbstractPost implements Serializable {

    private String message;

    public Post(int id, Account account, String message) {
        super(id, account);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin, String firstMargin) {
        return super.showPostHeader(sb, margin, firstMargin)
                .append(margin).append(getMessage()).append("\n");
    }
}
