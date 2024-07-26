package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * <p>
     * <p>
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String timeStamp;
    private String parentCommit;
    private String branch;
    private boolean isHead;
    private String UID;
    //sth for file name
    private Map<String,String> files;
    //sth that can link file and blob


    public Commit() {
        this.timeStamp = new Date(0).toString();
        parentCommit = null;
        this.message = "initial commit";
        setBranch("master");
        isHead = true;//
        files = new HashMap<String,String>();
        setUID(Utils.sha1(timeStamp, message));//For initial commit we only use these metadata. M

        // May need modification

    }

    public void save() {
        String folderName = String.valueOf(this.getUID().substring(0,2));
        File commitPath = Utils.join(Repository.GITLET_DIR, folderName);
        commitPath.mkdirs();
        File commitFile = Utils.join(commitPath, this.getUID().substring(2));
        Utils.writeObject(commitFile, this);
        System.out.println(this.getUID());
    }


    /**
     * The message of this Commit.
     */
    private String message;

    /* TODO: fill in the rest of this class. */

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String getParentCommit() {
        return this.parentCommit;
    }

    public String getMessage() {
        return this.message;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHead() {
        return this.isHead;
    }

    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    public String getBranch() {
        return this.branch;


    }


    public void setBranch(String branch) {
        this.branch = branch;
    }


    public static void main(String[] args) {
        System.out.println(new Date(0));

        Commit c = new Commit();
        System.out.println(c.getUID());
    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Map<String, String> getFiles() {
        return this.files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
