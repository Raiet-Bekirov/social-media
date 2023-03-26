package socialmedia.internal;

import java.io.Serializable;

/** 
 * A class that contain information about the account.
 */
public class Account implements Serializable{
    //create instance
    private int id;
    private String handle;
    private String description;

    /**
     * The constructor; Account(id, handle, description)
     * 
     * @param id account ID
     * @param handle account's handle
     * @param description account's description
     */
    public Account(int id, String handle, String description) {
        this.id = id;
        this.handle = handle;
        this.description = description;
    }
    
    /**
     * The constructor; Account(id, handle)
     * ! Note that this constructor has no description, so it will return null if no description specified
     * @param id
     * @param handle
     */
    public Account(int id, String handle) {
        this(id, handle, null);
    }

    //getters and setters
    /**
     * This method returns the Id of the account
     * 
     * @return account's id
     */
    public int getId() {
        return id;
    }
    /**
     * This method returns the handle of the account
     * 
     * @return account's handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * This method will update the handle with any handle specified in the bracket
     * 
     * @param handle new account's handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * This method will return the description of the account
     * 
     * @return account's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method will update the description with description specified in the bracket
     * 
     * @param description new account's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method will show account in this format:
     * ID:[account ID]
     * Handle: [account's handle]
     * Description: [account's description]
     * 
     * @param builder variable declaration to create a StringBuilder object. To use: .showAccount(new StringBuilder())
     * @return the string of account information ; ID, handle and description
     */
    public StringBuilder showAccount(StringBuilder builder) {
        return builder.append("ID: ").append(getId()).append("\n")
                .append("Handle: ").append(getHandle()).append("\n")
                .append("Description: ").append(getDescription()).append("\n");
    }

}