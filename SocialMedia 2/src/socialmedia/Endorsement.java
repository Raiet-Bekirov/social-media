package socialmedia.internal;

public class Endorsement extends AbstractPost {
    private AbstractPost post;

    public Endorsement(int id, Account account, AbstractPost post) {
        super(id, account);
        this.post = post;
    }

}
