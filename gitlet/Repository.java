package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */
           //Creates a new .gitlet file in the current directory
        /*
        * Have the initial commit, no files tracked stored in .gitlet
        * Commit msg = "initial commit"
        * Create a new branch: master, points to the above commit
        * Set the commit metadeta, its time is 1970...
        * And its UID
        * Exits if error occured, A Gitlet version-control system already exists in the current directory
        * */
    public static void initRepository() throws GitletException {

        File gitletDir = GITLET_DIR;
        if (gitletDir.exists()) {
            throw new GitletException("A Gitlet version-control system already exists in the current directory");
        }
        gitletDir.mkdir();
        Commit initialCommit = new Commit();
        initialCommit.save();


    }


    public static void main(String[] args) {
        initRepository();
    }





}
