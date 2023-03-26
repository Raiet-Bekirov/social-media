package socialmedia.internal;

/**
 * 
 */
abstract public class AbstractRepost extends AbstractPost{
    //transient = to mark member variable not to be serialized 
    //creating instance
    private transient AbstractPost sourcePost;
    private Integer sourcePostId;

    /**
     * 
     * @param id
     * @param account
     * @param sourcePost
     */
    public AbstractRepost(int id, Account account, AbstractPost sourcePost) {
        super(id, account);
        setSourcePost(sourcePost);
    }

    /**
     * 
     * @return
     */
    public AbstractPost getSourcePost() {
        return sourcePost;
    }

    /**
     * 
     * @param sourcePost
     */
    public void setSourcePost(AbstractPost sourcePost) {
        this.sourcePost = sourcePost;
        this.sourcePostId = (sourcePost == null) ? null : sourcePost.getId();
    }

    /**
     * 
     * @return
     */
    public Integer getSourcePostId() {
        return sourcePostId;
    }

}
