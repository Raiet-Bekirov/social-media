package socialmedia.internal;

/**
 * A class that contain information about the original post.
 * It extends an abstract class called AbstractPost. 
 */
public class Post extends AbstractPost {
    //create intance
    private String message;

    /**
     * The constructor; Post(id, account, message)
     * @param id post's id
     * @param account account that post this message
     * @param message the message being posted
     */
    public Post(int id, Account account, String message) {
        super(id, account);
        this.message = message;
    }

    //overriding an abstract method from AbstractPost class
    @Override
    public String getMessage() {
        return message;
    }

    //overriding 
    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin) {
        return super.showPostHeader(sb, margin)
                .append(margin).append(getMessage()).append("\n");
    }
}

