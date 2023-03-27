package socialmedia.internal;

import java.io.Serializable;

public class Comment extends AbstractRepost implements Serializable {

    private String message;

    public Comment(int id, Account account, AbstractPost sourcePost, String message) {
        super(id, account, sourcePost);
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
