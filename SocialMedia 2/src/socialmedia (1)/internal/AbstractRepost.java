package socialmedia.internal;

/**class that collects data from endorsments for further serialization
 * 
 */
abstract public class AbstractRepost extends AbstractPost{
    //transient = to mark member variable not to be serialized 
    //creating instance
    private transient AbstractPost sourcePost;
    private Integer sourcePostId;

    /**
     * sets parameters for original post as handle and id 
     * @param id sourcePostId
     * @param account handle of original creator
     * @param sourcePost original post that got endorsed 
     */
    public AbstractRepost(int id, Account account, AbstractPost sourcePost) {
        super(id, account);
        setSourcePost(sourcePost);
    }

    /**
     * finds the source post of the repost 
     * @return sourcePost original post that got endorsed 
     */
    public AbstractPost getSourcePost() {
        return sourcePost;
    }

    /**
     * sets original post of the endorsment as an abstract repost
     * @param sourcePost original post that got endorsed 
     */
    public void setSourcePost(AbstractPost sourcePost) {
        this.sourcePost = sourcePost;
        this.sourcePostId = (sourcePost == null) ? null : sourcePost.getId();
    }

    /**
     * adds original id as an abstaract id
     * @return sourcePostId original post id that got endorsed 
     */
    public Integer getSourcePostId() {
        return sourcePostId;
    }

}
