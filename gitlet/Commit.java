package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
// TODO: any imports you need here


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
    private ArrayList<String> parentCommit;
    private String branch;//If we have head class, we can ignore this attribute

    private String UID;
    //sth for file name
    private Map<String,String> filesMap;
    private ArrayList<String> secondParent;








    //sth that can link file and blob

    //Read the curren head
    //Get the UID of the current commit
    /*
    * Go to commit path and search for that commit file
    * Return the commit object
    * */


    public static Commit findCurrentCommit() {
        String currentCommitID = Head.readCurrentHead();

        File commitFile = Utils.join(Repository.COMMITS_DIR, currentCommitID);
        Commit currentCommit = Utils.readObject(commitFile, Commit.class);

        return currentCommit;
    }

    public static Commit findCommitByUID(String UID) {
        File commitFile = Utils.join(Repository.COMMITS_DIR, UID);
        if (!commitFile.exists()) {
            return null;
        } else {
            return Utils.readObject(commitFile, Commit.class);
        }
    }






    //Find the current commit using the ID
    //New commit has an empty map that stores no file
    public Commit() {
        this.timeStamp = new Date(0).toString();
        parentCommit = new ArrayList<>();
        this.message = "initial commit";

        Map<String, String> map = new HashMap<>();
        filesMap = new HashMap<String,String>();
        setUID(Utils.sha1(timeStamp, message, parentCommit.toString(), filesMap.toString()));
        Head HEAD = new Head(this.UID);
        System.out.println(Head.readCurrentHead());
        Branch master = new Branch("Master", this.UID, true);
        save();

        //For initial commit we only use these metadata. M

        // May need modification

    }

    public Commit(String message, ArrayList<String> parentCommit, Map<String,String> filesMap) {
        this.timeStamp = new Date().toString();
        this.parentCommit = parentCommit;
        this.filesMap = filesMap;
        this.message = message;
        setUID(Utils.sha1(timeStamp, message, parentCommit.toString(), filesMap.toString()));
        Head.setHead(this.UID);

        Branch.setNewCommit(this.UID);
        save();
    }

    public Commit(String message, ArrayList<String> parentCommit, ArrayList<String> secondParent, Map<String,String> filesMap) {
        this.timeStamp = new Date().toString();
        this.parentCommit = parentCommit;
        this.secondParent = secondParent;
        this.filesMap = filesMap;
        this.message = message;
        setUID(Utils.sha1(timeStamp, message, parentCommit.toString(), filesMap.toString()));
        Head.setHead(this.UID);

        Branch.setNewCommit(this.UID);
        save();
    }


    public void save() {
        Repository.COMMITS_DIR.mkdir();
        File commitFile = Utils.join(Repository.COMMITS_DIR, this.getUID());
        Utils.writeObject(commitFile, this);

    }


    /**
     * The message of this Commit.
     */
    private String message;

    /* TODO: fill in the rest of this class. */

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public ArrayList<String> getParentCommit() {
        return this.parentCommit;
    }

    public String getMessage() {
        return this.message;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setParentCommit(ArrayList<String> parentCommit) {
        this.parentCommit = parentCommit;

    }

    public void setMessage(String message) {
        this.message = message;
    }


//    public String getBranch() {
//        return this.branch;
//
//
//    }

//
//    public void setBranch(String branch) {
//        this.branch = branch;
//    }


    public static void main(String[] args) {
        System.out.println(new Date(0));

        Commit c = new Commit();
        System.out.println(findCurrentCommit().getUID());

    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Map<String, String> getFilesMap() {
        return this.filesMap;
    }

    public void setFilesMap(Map<String, String> files) {
        this.filesMap = files;
    }
}
