package socialmedia.internal;

/**
 * A class that contain information about the endorsement post.
 * It extends an abstract class called AbstractRepost. 
 */
public class Endorsement extends AbstractRepost {
    
    //creating an instance
    private String message;

    /**
     * The constructor; Endorsement(id, account, sourcePost, message)
     * 
     * @param id post's id
     * @param account account that post this endorsement
     * @param sourcePost the reference post that is being endorsed
     * @param message the message generated when endorsing a post: "EP@" + [endorsed account handle] + ": " + [endorsed message]
     */
    public Endorsement(int id, Account account, AbstractPost sourcePost, String message) {
        super(id, account, sourcePost);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;

    }

    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin){
        return super.showPostHeader(sb, margin).append(getMessage()).append("\n");
    }
}
