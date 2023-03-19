package socialmedia;
//can be used to get the id, handle , description and show account easily
public class Account {
    //create instance
    private int id;
    private String handle;
    private String description;

    //constructors
    public Account(int id, String handle, String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }
    
    public Account(int id, String handle) {
        this(id, handle, null);
    }

    //getters and setters
    public int getId() {
        return id;
    }
    //get the handle
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
    //get the description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //show the account ; id, handler and description
    public String showAccount() {
        return "ID: "+ getId()+"\nHandle: "+ getHandle()+"\nDescription : "+getDescription();
    }

}