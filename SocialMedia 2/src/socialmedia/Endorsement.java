package socialmedia;

public class Endorsement extends AbstractPost {
    private AbstractPost post;

    public Endorsement(int id, Account account, AbstractPost post) {
        super(id, account);
        this.post = post;
    }

    @Override
    protected StringBuilder showPostHeader(StringBuilder sb, String margin) {
        return super.showPostHeader(sb, margin).append("EP@")
                .append(margin).append(post.getAccount().getHandle()).append(" : ")
                .append(margin).append(post.getMessage()).append("\n");
    }

}