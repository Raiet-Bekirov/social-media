package socialmedia.internal;

public class Account {
    private int id;
    private String handle;
    private String description;

    public Account(int id, String handle, String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }

    public Account(int id, String handle) {
        this(id, handle, null);
    }

    public int getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StringBuilder showAccount(StringBuilder builder) {
        return builder.append("ID: [").append(getId()).append("]\n")
                .append("Handle: [").append(getHandle()).append("]\n")
                .append("Description: [").append(getDescription()).append("]\n");
    }
}
