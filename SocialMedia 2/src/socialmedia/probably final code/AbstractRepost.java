package socialmedia.internal;

import java.io.Serializable;

abstract public class AbstractRepost extends AbstractPost implements Serializable {
    private transient AbstractPost sourcePost;
    private Integer sourcePostId;

    public AbstractRepost(int id, Account account, AbstractPost sourcePost) {
        super(id, account);
        setSourcePost(sourcePost);
    }

    public AbstractPost getSourcePost() {
        return sourcePost;
    }

    public void setSourcePost(AbstractPost sourcePost) {
        this.sourcePost = sourcePost;
        this.sourcePostId = (sourcePost == null) ? null : sourcePost.getId();
    }

    public Integer getSourcePostId() {
        return sourcePostId;
    }

}
