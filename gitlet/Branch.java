package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Branch implements Serializable {
    private String branchName;
    private String currentCommit;
    private boolean hasHead;





    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
        this.save();
    }


    public static Branch getBranchByName(String branchName) {
        File branchFile = Utils.join(Repository.Branches_DIR, branchName);
        if (branchFile.exists()) {
            Branch branch = convertFileToBranch(branchFile);
            return branch;


        } else {
            System.out.println("The given branch does not exist");
            return null;
        }
    }




    //List all files in the branch path
    public static ArrayList<String> getCurrentBranches() {
        ArrayList<String> branches = Repository.listAllFiles(Repository.Branches_DIR);
        return branches;
    }


    public static boolean isDuplicated(String branchName) {
        ArrayList<String> branches = Repository.listAllFiles(Repository.Branches_DIR);
        for (String branch : branches) {
            if (branch.equals(branchName)) {
                return true;
            }
        }
        return false;

    }


    public static void setNewCommit(String commitID) {
        Branch currentBranch = findCurrentBranch();
        currentBranch.setCurrentCommit(commitID);
        currentBranch.save();


    }








    public Branch(String branchName, String currentCommit, boolean hasHead) {
        this.branchName = branchName;
        this.currentCommit = currentCommit;
        this.hasHead = hasHead;
        save();
    }
    /*
    * Find the branch that has hasHead as true in the branch folder
    * return the string
    *
    * */
    public static Branch findCurrentBranch() {
        ArrayList<String> branches = Repository.listAllFiles(Repository.Branches_DIR);
        for( String branch : branches ) {
            File f = Utils.join(Repository.Branches_DIR, branch);
            Branch b = convertFileToBranch(f);
            if (b.hasHead == true) {
                return b;
            }
        }
        System.out.println("No branch has head");
        return null;

    }


    public static Branch convertFileToBranch(File file) {
        return Utils.readObject(file, Branch.class);
    }

    public void save() {
        Repository.Branches_DIR.mkdir();
        File Branch = Utils.join(Repository.Branches_DIR, getBranchName());
        Utils.writeObject(Branch, this);

    }

    public String getBranchName() {
        return branchName;
    }
    public String getCurrentCommit() {
        return currentCommit;
    }

    public void setCurrentCommit(String currentCommit) {
        this.currentCommit = currentCommit;
    }

    public static void main(String[] args) {
        Branch branch = new Branch("master", "52318edb2df58fa951f9a32a3f78e879e8c0c597", true);

        branch.save();
    }



}
