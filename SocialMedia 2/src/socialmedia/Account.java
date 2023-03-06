package socialmedia;

//can be used to get the id, handle , description and show account easily
public class Account {
    private int id;
    private String handle;
    private String description;
    
    //for given id, handle and description
    public Account(int id, String handle, String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }
    //description is not given
    public Account(int id, String handle) {
        this(id, handle, null);
    }
    //get the id
    public int getId() {
        return id;
    }
    //get the handle
    public String getHandle() {
        return handle;
    }
    //get the description
    public String getDescription() {
        return description;
    }
    //show the account ; id, handler and description
    public String showAccount() {
        String acc = String.format("ID: %s\nHandle: %s\nDescription: %s\n", getId(), getHandle(), getDescription());
        return acc;
    }
    //testing -> works
    public static void main (String[]args){
        Account m = new Account(1, "Hello", "Im Alya");
        System.out.println(m.getId());
        System.out.println(m.getHandle());
        System.out.println(m.getDescription());
        System.out.println(m.showAccount());
    }
}
