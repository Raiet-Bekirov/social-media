package socialmedia.internal;

import java.io.Serializable;

public class Endorsement extends AbstractRepost implements Serializable {

    public Endorsement(int id, Account account, AbstractPost sourcePost) {
        super(id, account, sourcePost);
    }

}
