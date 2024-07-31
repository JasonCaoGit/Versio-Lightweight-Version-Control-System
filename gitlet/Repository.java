package gitlet;

import java.io.File;
import static gitlet.Utils.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */


    public static void main(String[] args) {

            merge("Master");
        status();
        Commit commit = Commit.findCurrentCommit();
        System.out.println(commit.getFilesMap());
        merge("Master");
        ArrayList<String> list = new ArrayList<>();

        list.add("e");
        list.add("c");
        list.add("a");
        list.add("e");
        list.add("a");
        list.add("b");
        list.add("g");
        list.add("d");
        list.add("b");

        ArrayList<String> sorted = mergeSort(list);
        System.out.println(sorted.toString());

//        Head head = Head.findCurrentHeadObject();
//        System.out.println(head.currentHead);
//        System.out.println(head.currentHead);
//        Branch branch = Branch.findCurrentBranch();
//        System.out.println(branch.getCurrentCommit());
//        System.out.println(branch.getBranchName());
//
//
//
//
//
//        Map<String, String> stagingArea = StagingArea.readStagingArea();
//        System.out.println(stagingArea);


        //first manual commit
        //;


    }


    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(GITLET_DIR, "Commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "Blobs");
    public static final File STAGING_AREA = join(GITLET_DIR, "StagingArea");
    public static final File Head_DIR = join(GITLET_DIR, "HEAD");
    public static final File Branches_DIR = join(GITLET_DIR, "Branches");


    public static String readByteObjectAsString(File blobToRead) {
        byte[] byteObject = Utils.readObject(blobToRead, byte[].class);
        return new String(byteObject, StandardCharsets.UTF_8);
    }




    /*
    * Merge files in the given branch to the current
    * The split point is the earliest common parent commits of two commits
    * Several scenarios regarding the split point
    * 1 if the split point is the same commit as the given branch
    * do nothing. Display  Given branch is an ancestor of the current branch.
    * 2 if the split point is the current branch
    * then we checkout the given branch and displays Current branch fast-forwarded
    *
    * If not the scenatrios above do the following
    *1 If a  file that is modified btween given branch and the split point
    * but is not modified in the current branch, change it to the version in the given branch
    * The files will be staged(overwrite and stage)
    *
    * 2 if a file that is modified in the current branch but not in the given branch
    * stay the same
    * 3 If a file is modified in the same way, same content or both removed,
    * are left unchanged. If a file was removed from both, and it is still in the CWD
    * do not track it or stage it
    * 4 If a file is not in the split point and the given branch
    *  but is in the current branch only, remain the same
    * 5 If a file not in the split point and the current branch
    * , only in the given branch, checkout that file and stage it
    * 6 If a file is in the split point, unchanged in the current branch, and not in the given branch
    * remove it and do not track it in the merge commit
    * 7 If a file is at the split point, unchanged in  the given branch, and not in the current branch,
    * it will not be tracked in the merge commit
    * 8 Any files that are modified differently are in conflict
    *They could be modified differently or one is changed and another is delteld
    * If a conflict happens replace the conflict file to this:
    * <<<<<<< HEAD
contents of file in current branch
=======
contents of file in given branch
>>>>>>>
* fill blank if it is deleted in one branch
* and stage this file to addition.
*
*
* Once already commit automatically with the commit message Merged [given branch name] into
[current branch name].
* Print  Encountered a
merge conflict, if encounter a conflict
* Merged commits have the current branch as the first parent
* and the given branch as the second parent
*
*
*
*
* Exceptions:
* 1 if staging area has files , error You have
uncommitted changes.
* 2 If the branch does not exist, A branch with that name does not exist.
    *

3    if the branch is the current branch, error  Cannot merge a branch with itself
*
* 4 if the merge commit will overwrite a untracked file, error   There is an untracked file in the way; delete it, or add and commit it
first.
    *
     */

    public static void merge(String branchName) throws GitletException {
        Map<String, String> stagingArea = StagingArea.readStagingArea();
        if (stagingArea.size() > 0) {
            throw new GitletException(" You have uncommitted changes.");
        }
        boolean doesExist = Branch.isDuplicated(branchName);
        if (!doesExist) {
            throw new GitletException("A branch with that name does not exist.");



        }

        Branch currentBranch = Branch.findCurrentBranch();
        if(currentBranch.getBranchName().equals(branchName)) {
            throw new GitletException("Not supposed to merge a branch with itself");
        }

/*
         Merge files in the given branch to the current
    * The split point is the earliest common parent commits of two commits
    * Several scenarios regarding the split point
    * 1 if the split point is the same commit as the given branch
    * do nothing. Display  Given branch is an ancestor of the current branch.
    * 2 if the split point is the current branch
    * then we checkout the given branch and displays Current branch fast-forwarded
    **/
        /*
        * We have to find the split point of two branch commits.
        * */

        //first we reverse two lists of ancestors, these two lists must have some common ancesstors, and they must have the same index after the list is reversed
        String currentBranchCommitID = currentBranch.getCurrentCommit();
        Commit currentBranchCommit = Commit.findCommitByUID(currentBranchCommitID);
        Branch givenBranch = Branch.getBranchByName(branchName);
        Commit givenBranchCommit = Commit.findCommitByUID(givenBranch.getCurrentCommit());
        //now we have the two branch commits, get their ancestor list
        ArrayList<String> currentAncestorList = currentBranchCommit.getParentCommit();
        currentAncestorList.add(currentBranchCommitID);
        ArrayList<String> givenAncestorList = givenBranchCommit.getParentCommit();
        givenAncestorList.add(givenBranch.getCurrentCommit());

        //find the right list to iterate
        ArrayList<String> listToIterate = null;
        ArrayList<String> listToCompare = null;
        if (currentAncestorList.size() < givenAncestorList.size()) {
            listToIterate = currentAncestorList;
            listToCompare = givenAncestorList;
        } else {
            listToIterate = givenAncestorList;
            listToCompare = currentAncestorList;
        }
        int theFirstAncestorNotEqual = -1;
        //Iterate the list of ancestors and find the leftmost ancestor that is not the same
        for (int i =0 ; i < listToIterate.size(); i++) {
            if(!listToIterate.get(i).equals(listToCompare.get(i))) {
                theFirstAncestorNotEqual = i;
                break;
            }

        }
        if(theFirstAncestorNotEqual == -1) {
            theFirstAncestorNotEqual = listToIterate.size();
        }
        String splitPoint = listToIterate.get(theFirstAncestorNotEqual - 1);
        System.out.println(splitPoint);
        // now we can find the split point





//            * Merge files in the given branch to the current
//    * The split point is the earliest common parent commits of two commits
//    * Several scenarios regarding the split point
//    * 1 if the split point is the same commit as the given branch
//    * do nothing. Display  Given branch is an ancestor of the current branch.
//    * 2 if the split point is the current branch
//    * then we checkout the given branch and displays Current branch fast-forwarded
//    *
    System.out.println(givenBranch.getCurrentCommit());
        if(splitPoint.equals(givenBranch.getCurrentCommit())) {

            System.out.println("Given branch is an ancestor of the current branch.");
        }
        if (splitPoint.equals(currentBranchCommit.getUID())) {
            checkoutBranch(givenBranch.getBranchName());
            System.out.println("Current branch fast-forwarded");
        }

/*            * If not the scenarios above do the following
    *1 If a  file that is modified btween given branch and the split point
    * but is not modified in the current branch, change it to the version in the given branch
    * The files will be staged(overwrite and stage)
    *
    * 2 if a file that is modified in the current branch but not in the given branch
    * stay the same
    * 3 If a file is modified in the same way, same content or both removed,
    * are left unchanged. If a file was removed from both, and it is still in the CWD
    * do not track it or stage it*/
        //If a file's blob is different between given branch and split point, same between current branch and ...,
        /*
        * Checkout that file in the given branch, stage the file
        * */
        //Have to find all files in all three commits
        Map<String, String> filesInSplitPoint = Commit.findCommitByUID(splitPoint).getFilesMap();
        Map<String, String> filesInCurrentBranch = currentBranchCommit.getFilesMap();
        Map<String, String> filesInGivenBranch = givenBranchCommit.getFilesMap();
        ArrayList<String> filesToIterate = new ArrayList<>();
        for(String file: filesInSplitPoint.keySet()) {
            filesToIterate.add(file);
        }

        //For everything in current branch commit map
        for(String file: filesInCurrentBranch.keySet()) {
            //if my list does not includ that file name
            if (!filesToIterate.contains(file)) {
                filesToIterate.add(file);
            }
        }

        for(String file: filesInGivenBranch.keySet()) {
            if (!filesToIterate.contains(file)) {
                filesToIterate.add(file);
            }
        }

//        *1 If a  file that is modified between given branch and the split point
//    * but is not modified in the current branch, change it to the version in the given branch
//    * The files will be staged(overwrite and stage)
//    *

        //Iterate through all the files in the three commits
        for(String file: filesToIterate) {
            String currentBranchBlob = filesInCurrentBranch.get(file);
            String givenBranchBlob = filesInGivenBranch.get(file);
            String splitPointBlob = filesInSplitPoint.get(file);
            //First case
            if(currentBranchBlob!= null && givenBranchBlob!= null && splitPointBlob != null) {
                if(!splitPointBlob.equals(givenBranchBlob) && splitPointBlob.equals(currentBranchBlob)) {
                //checkout the file in the given branch and add it
                checkoutFile(givenBranchCommit.getUID(), file);
                add(file);
            }
                 if(!splitPointBlob.equals(currentBranchBlob) && splitPointBlob.equals(givenBranchBlob)) {
                continue;
            }

            }

   /*           * 2 if a file that is modified in the current branch but not in the given branch
    * stay the same*/

//            * 3 If a file is modified in the same way, same content or both removed,
//    * are left unchanged. If a file was removed from both, and it is still in the CWD
//    * do not track it or stage it*/
            //if it is not contained in both
            boolean isRemovedInBoth = false;
            if (splitPointBlob != null) {
                  if(currentBranchBlob == null && givenBranchBlob == null) {
                isRemovedInBoth = true;
            }
            }



            ArrayList<String> CWDFiles = listAllFiles(CWD);
            if (currentBranchBlob != null) {
                   if(currentBranchBlob.equals(givenBranchBlob) || isRemovedInBoth)  {

                if (CWDFiles.contains(file)) {
                    if(stagingArea.containsKey(file)) {
                        stagingArea.remove(file);
                    StagingArea.saveStagingArea(stagingArea);

                    }

                }
            }
            }


//                * 4 If a file is not in the split point and the given branch
//    *  but is in the current branch only, remain the same
            if(splitPointBlob==null && givenBranchBlob == null &&currentBranchBlob != null) {
                continue;
            }
//
//               * 5 If a file not in the split point and the current branch
//    * , only in the given branch, checkout that file and stage it
            if(splitPointBlob==null && currentBranchBlob == null && givenBranchBlob != null) {
                checkoutFile(givenBranchCommit.getUID(), file);
                stagingArea.put(file, givenBranchCommit.getFilesMap().get(file));
                StagingArea.saveStagingArea(stagingArea);
            }










//    * 6 If a file is in the split point, unchanged in the current branch, and not in the given branch
//    * remove it and do not track it in the merge commit
            if (splitPointBlob != null && currentBranchBlob.equals(splitPointBlob) && givenBranchBlob == null) {
                if(stagingArea.containsKey(file)) {
                    stagingArea.remove(file);
                    StagingArea.saveStagingArea(stagingArea);
                }

                if(CWDFiles.contains(file)) {
                    File fileToDelete = Utils.join(CWD, file);
                    Utils.restrictedDelete(fileToDelete);

                }
            }

//    * 7 If a file is at the split point, unchanged in  the given branch, and not in the current branch,
//    * it will not be tracked in the merge commit
            if (givenBranchBlob != null) {
                 if (splitPointBlob!= null && givenBranchBlob.equals(splitPointBlob) && currentBranchBlob == null) {
                if(stagingArea.containsKey(file)) {
                    stagingArea.remove(file);
                    StagingArea.saveStagingArea(stagingArea);
                }
            }
            }


//    * 8 Any files that are modified differently are in conflict
//    *They could be modified differently or one is changed and another is delteld
//               * If a conflict happens replace the conflict file to this:
//    * <<<<<<< HEAD
//contents of file in current branch
//=======
//contents of file in given branch
//>>>>>>>
//* fill blank if it is deleted in one branch
//* and stage this file to addition.

            //determine if both modified in a different way
            boolean mergeConflict = false;
            //if two branches all contain the files and they are not equal to each other
            if(currentBranchBlob != null && givenBranchBlob != null) {
                if(!currentBranchBlob.equals(givenBranchBlob)) {
                    mergeConflict = true;
                }

            }
            //if is deleted in one branch and changed in another
            //if split point has the file
            if (splitPointBlob != null) {
                //also given branch has the file and changed the content
                if (givenBranchBlob != null) {
                    //current branch delete the file
                       if(currentBranchBlob == null && !splitPoint.equals(givenBranchBlob)) {
                           mergeConflict = true;

                }
                }

                //vice versa for the opposite
                 if (currentBranchBlob != null) {
                    //current branch delete the file
                       if(givenBranchBlob == null && !splitPoint.equals(currentBranchBlob)) {
                           mergeConflict = true;

                }
                }



            }
            //tested
            if(mergeConflict) {
                String conflictContent = "<<<<<<< HEAD" + "\n";
                if (currentBranchBlob != null) {
                    File currentFileContent = Utils.join(BLOBS_DIR, currentBranchBlob);
                    //Write the content into a file first and do it
                    conflictContent += readByteObjectAsString(currentFileContent);

                } else {
                    conflictContent += "";
                }
                conflictContent += "=======" + "\n";
                if (givenBranchBlob != null) {
                    File givenFileContent = Utils.join(BLOBS_DIR, givenBranchBlob);

                    conflictContent += readByteObjectAsString(givenFileContent);
                } else {
                    conflictContent += "";
                }
                conflictContent += ">>>>>>>";

                 File fileInCWD = Utils.join(CWD, file);
            if (fileInCWD.exists()) {
                Utils.restrictedDelete(fileInCWD);
            }
            Utils.writeContents(fileInCWD, conflictContent);

                add(file);

            }

            mergeCommit("Merged " + givenBranch.getBranchName() + " into " + currentBranch.getBranchName(), givenBranchCommit.getUID());
            if (mergeConflict) {
                System.out.println("Encountered a merge conflict.");
            }









            //tested ok




        }







    }
















    /*
     * Get all blobs from the given commit,
     * Remove all files that are in the current commit but is not in that commit
     * Move the current branch to that commit
     * Move the current head to that commit
     * Staging area is cleared
     *
     * Exception:
     * If no commit with that ID exists error  No commit with that id exists.
     *2 if a CWD file is untracked and would be overwritten by that given commit,
     * error There is
an untracked file in the way; delete it, or add and commit it first.
     * */

    public static void reset(String commitID) throws GitletException {

        Commit commitToReset = Commit.findCommitByUID(commitID);
        if (commitToReset == null) {
            throw new GitletException("No commit with that id exists.");
        }




//        Branch branchOfInterest = Branch.getBranchByName(branchToCheckout);
//        Commit branchCommit = Commit.findCommitByUID(branchOfInterest.getCurrentCommit());
        System.out.println(commitToReset);
        Map<String, String> commitToResetFilesMap = commitToReset.getFilesMap();
        //delte all the files that are in current commit
        Commit currentCommit = Commit.findCurrentCommit();
        for (String file : currentCommit.getFilesMap().keySet()) {
            File filesTrackInCurrentCommit = Utils.join(CWD, file);
            Utils.restrictedDelete(filesTrackInCurrentCommit);
        }

        //Get all untracked file names
        ArrayList<String> untrackedFiles = findUntrackedFiles();
        //Check if for each file the branch commit has them already
        for (String untracked : untrackedFiles) {
            if (commitToReset.getFilesMap().containsKey(untracked)) {
                //If it has them compare their blobid, if not the same, error. It means the untracked file will be rewritten
                String blobIDInBranch = commitToResetFilesMap.get(untracked);
                File untrackedFile = Utils.join(CWD, untracked);
                if (!compareFileContentByBlob(untrackedFile, blobIDInBranch)) {
                    throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first");

                }
            }
        }
        //Got the commit file map in the branch commit, now iterate through all files
        for (String file : commitToResetFilesMap.keySet()) {


            checkoutCommitFile(commitToReset.getUID(), file);

        }

//        Move the current branch to that commit
//     * Move the current head to that commit
//     * Staging area is cleared

        Branch currentBranch = Branch.findCurrentBranch();
        //current branch points to that commit
        currentBranch.setCurrentCommit(commitID);
        Head.setHead(commitID);
        Map<String, String> stagingArea = new HashMap<>();
        StagingArea.saveStagingArea(stagingArea);
    }


    /*
     * Deletes the branch with the given name
     *
     * Exceptions:
     * If the name does not exist, error   A
branch with that name does not exist
*
* 2 if the branch is the current branch, error Cannot remove the current branch.
     * */
    public static void rmBranch(String branch) throws GitletException {
        Branch branchToRemove = Branch.getBranchByName(branch);
        if (branchToRemove == null) {
            throw new GitletException("A branch with that name does not exist");
        }
        Branch currentBranch = Branch.findCurrentBranch();
        if (currentBranch.getBranchName().equals(branch)) {
            throw new GitletException("Not supposed to remove the current branch.");
        }

        File filesTrackInCurrentCommit = Utils.join(Branches_DIR, branch);
        if (filesTrackInCurrentCommit.delete()) {
            System.out.println("Deleted");

        }
    }


    public static boolean compareFileContentByBlob(File fileToCompare, String blobID) {
        String blobIDToCompare = makeBlobWithoutSaving(fileToCompare);
        return blobIDToCompare.equals(blobID);

    }


    /*
    * Takes all blobs from a commit at the head of the given branch
    * Update the files in CWD
    * Given branch is now the new head branch,
    * means the hasHead of the current branch is set to false
    * and the given branch hashead is set to true
    * the head is also set to the branch commit
    * Any file tracked in the current branch will be deleted
    * if it not in the checkedout branch commit
    *
    * Exceptions:
    * 1 if no branch with that name exists error   No such branch exists.
    * 2 is the given branch is the same as the current branch, error No need to checkout the current branch
    * 3 if a file is untracked and will be overwritten by the check out error There is an untracked file
    in the way; delete it, or add and commit it first. Untracked means it is not staged for addition or is in the current commit
    *
    * Do this to every file in the branch commit
    * Get a blob id, update the files in CWD(check the error condition before)
    * Set the given branch to has head
    * Set the current branch to not has head
    * Set the head value to the given branch commit
    *
    *
    *
    *
    * */
    public static void checkoutBranch(String branchToCheckout) throws GitletException {
        ArrayList<String> branches = Branch.getCurrentBranches();
        boolean branchFound = false;
        for (String branch : branches) {
            if (branch.equals(branchToCheckout)) {
                branchFound = true;
            }
        }
        if (branchFound == false) {
            throw new GitletException("No such branch exists.");
        }

        //if the current branch equals the branch to check out
        Branch currentBranch = Branch.findCurrentBranch();

        if (currentBranch.getBranchName().equals(branchToCheckout)) {
            System.out.println(currentBranch.getBranchName());
            System.out.println(branchToCheckout);
            ;
            throw new GitletException("No need to checkout the current branch");
        }


        /*        * Do this to every file in the branch commit
         * Get a blob id, update the files in CWD(check the error condition before)
         * Set the given branch to has head
         * Set the current branch to not has head
         * Set the head value to the given branch commit*/
        /*
        * if a file is untracked and will be overwritten by the check out error There is an untracked file
in the way; delete it, or add and commit it first. Untracked means it is not staged for addition or is in the current commit
        *
        * */
        Branch branchOfInterest = Branch.getBranchByName(branchToCheckout);
        Commit branchCommit = Commit.findCommitByUID(branchOfInterest.getCurrentCommit());
        System.out.println(branchCommit);
        Map<String, String> branchCommitFileMap = branchCommit.getFilesMap();
        //delte all the files that are in current commit
        Commit currentCommit = Commit.findCurrentCommit();
        for (String file : currentCommit.getFilesMap().keySet()) {
            File filesTrackInCurrentCommit = Utils.join(CWD, file);
            Utils.restrictedDelete(filesTrackInCurrentCommit);
        }

        //Get all untracked file names
        ArrayList<String> untrackedFiles = findUntrackedFiles();
        //Check if for each file the branch commit has them already
        for (String untracked : untrackedFiles) {
            if (branchCommit.getFilesMap().containsKey(untracked)) {
                //If it has them compare their blobid, if not the same, error. It means the untracked file will be rewritten
                String blobIDInBranch = branchCommitFileMap.get(untracked);
                File untrackedFile = Utils.join(CWD, untracked);
                if (!compareFileContentByBlob(untrackedFile, blobIDInBranch)) {
                    throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first");

                }
            }
        }
        //Got the commit file map in the branch commit, now iterate through all files
        for (String file : branchCommitFileMap.keySet()) {


            checkoutCommitFile(branchCommit.getUID(), file);

        }

//        Set the given branch to has head
//* Set the current branch to not has head
//* Set the head value to the given branch commit*/
        branchOfInterest.setHasHead(true);
        currentBranch.setHasHead(false);
        System.out.println(branchCommit.getUID());
        Head.setHead(branchCommit.getUID());
        Commit newCurrentCommit = Commit.findCurrentCommit();
        System.out.println(newCurrentCommit.getUID());


    }


    /*
     * Rewrite the file to the version of the given commit
     *
     * Exception:
     * 1 if the commit does not exists error No Commit with that id exists
     *
     *
     * */
    public static void checkoutCommitFile(String commitID, String fileToCheckout) {
        Commit commitToCheckout = Commit.findCommitByUID(commitID);
        if (commitToCheckout == null) {
            System.out.println("No Commit with that id exists");
            System.exit(0);
        }
        checkoutFile(commitID, fileToCheckout);


    }


    /*Take the blob of all the given file from the current commit
    and rewrite the file in the CWD if it exists
    if not, then add that file and write the blob
    the file is not staged

    Exceptions:
    if the file does not exist in the commit
    error File does not exist in that commit.
    *
    The content of any file is byte[] and the byte[] object is stored in files, read the file and write the byte[] back to the file
    * */
    public static void checkoutFile(String fileToCheckout) throws GitletException {
        Commit currentCommit = Commit.findCurrentCommit();
        String blobIDInCommit = null;
        boolean found = false;
        for (String fileInCommit : currentCommit.getFilesMap().keySet()) {
            if (fileInCommit.equals(fileToCheckout)) {
                blobIDInCommit = currentCommit.getFilesMap().get(fileToCheckout);
                System.out.println("searching");
                found = true;
            }
        }
        if (found == false) {

            throw new GitletException("File does not exist in that commit.");
        }
        //Set the file you wanna rewrite
        File fileInCWD = Utils.join(CWD, fileToCheckout);
        if (fileInCWD.exists()) {
            Utils.restrictedDelete(fileInCWD);
        }


        //if we found the blob id in the current commit, read that blob and write it to the file
        if (blobIDInCommit != null) {
            File blobInCommit = Utils.join(BLOBS_DIR, blobIDInCommit);
            byte[] blobToOverwrite = Utils.readObject(blobInCommit, byte[].class);
            Utils.writeContents(fileInCWD, blobToOverwrite);

        }


    }


    public static void checkoutFile(String commitID, String fileToCheckout) throws GitletException {
        Commit currentCommit = Commit.findCommitByUID(commitID);
        String blobIDInCommit = null;
        boolean found = false;
        for (String fileInCommit : currentCommit.getFilesMap().keySet()) {
            if (fileInCommit.equals(fileToCheckout)) {
                blobIDInCommit = currentCommit.getFilesMap().get(fileToCheckout);
                System.out.println("searching");
                found = true;
            }
        }
        if (found == false) {

            throw new GitletException("File does not exist in that commit.");
        }
        //Set the file you wanna rewrite
        File fileInCWD = Utils.join(CWD, fileToCheckout);
        if (fileInCWD.exists()) {
            Utils.restrictedDelete(fileInCWD);
        }


        //if we found the blob id in the current commit, read that blob and write it to the file
        if (blobIDInCommit != null) {
            File blobInCommit = Utils.join(BLOBS_DIR, blobIDInCommit);
            byte[] blobToOverwrite = Utils.readObject(blobInCommit, byte[].class);
            Utils.writeContents(fileInCWD, blobToOverwrite);

        }


    }










    /* * Display all the modified but not staged file







     * In parenthesis write deleted if a file is staged for addtion
     * but is not found in the working dir
     * OR
     * if a file is in the current commit's fileMap but is deleted in the working dir
     * and it is not in the staging area with 'rm'
     *
     * In parenthesis write modified if a file is in the current commit's fileMap
     * and has different blob than the current commit, and the file is not in the staging area
     * OR
     * a file is in the staging area, but the blob in the staging area is different than what is in the WD
     *
     * Above is for the modified but not staged
     *
     *
     * Several kinds of files
     * Deleted;
     * 1 if a file is staged for addition but is not found in working directory
     * 2 if a file is in the current commit's fileMap but is not in the working directory and not staged for removal
     *
     * Modified:
     * 1 if a file is tracked by the current commit, but the file in cwd has a different blob than the commit's
     * and is not in the staging area
     * 2 if a file is staged for addition but the file in CWD has different blob in the staging area
     * */

    public static void displayModifiedNotStaged() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        //Group all modified files together
        ArrayList<String> modified = new ArrayList<>();
        Map<String, String> stagingArea = StagingArea.readStagingArea();
        Commit currentCommit = Commit.findCurrentCommit();
        //iterate through all the files that are staged for addtion
        for (String fileInStagingArea : stagingArea.keySet()) {
            //if the file is not staged for removal
            if (!stagingArea.get(fileInStagingArea).equals("rm")) {
                //Find the file in CWD
                File fileInCWD = Utils.join(CWD, fileInStagingArea);
                String blobIDCWD = makeBlobWithoutSaving(fileInCWD);

                if (!blobIDCWD.equals(stagingArea.get(fileInStagingArea))) {
                    modified.add(fileInStagingArea);
                }


            }
        }


        //iterate through all the files in current commit


        for (String fileInCommit : currentCommit.getFilesMap().keySet()) {
            File fileInCWD = Utils.join(CWD, fileInCommit);
            //if the file exists in the cwd
            if (fileInCWD.exists()) {
                //compare the blob ids
                String CWDBlobID = makeBlobWithoutSaving(fileInCWD);
                String commitBlobID = currentCommit.getFilesMap().get(fileInCommit);
                if (!commitBlobID.equals(CWDBlobID)) {
                    //check if it is in the staging area
                    if (!stagingArea.containsKey(fileInCommit)) {
                        modified.add(fileInCommit);
                    }

                }
            }
        }


        modified = mergeSort(modified);
        for (String file : modified) {
            System.out.println(file + " (modified)");
        }


        //Group all deleted files together
        ArrayList<String> deletedFiles = new ArrayList<>();
        ArrayList<String> CWDFiles = listAllFiles(CWD);

        //a file is not staged for removal, is in the current commit, not in the current dir

        //Iterate through all the files in current commit
        for (String file : currentCommit.getFilesMap().keySet()) {
            File fileInCWD = Utils.join(CWD, file);


            //if we found a file that is not the directory
            if (!fileInCWD.exists()) {

                //check if the file is staged for removal
                //if it is contained, then its value equals rm?
                //not contained add it to delete
                if (stagingArea.containsKey(file)) {

                    if (!stagingArea.get(file).equals("rm")) {
                        deletedFiles.add(file);

                    }

                } else {
                    deletedFiles.add(file);
                }

            }
        }


        //first I check for the first kind of files
        for (String file : stagingArea.keySet()) {
            //check if it is staged for addtion
            if (!stagingArea.get(file).equals("rm")) {
                //check if the file is in the CWD

                File fileToCheck = Utils.join(CWD, file);
                if (!fileToCheck.exists()) {
                    deletedFiles.add(file);
                }

            }
        }
        //Edited // needs testing
        deletedFiles = mergeSort(deletedFiles);


        for (String file : deletedFiles) {
            System.out.println(file + " (deleted)");
        }
        System.out.println();


    }


    public static void displayStagingArea() {
        System.out.println("=== Staged Files ===");

        Map<String, String> stagingArea = StagingArea.readStagingArea();
        ArrayList<String> addition = new ArrayList<>();
        ArrayList<String> removal = new ArrayList<>();
        for (String key : stagingArea.keySet()) {
            if (stagingArea.get(key).equals("rm")) {
                removal.add(key);
            } else {
                addition.add(key);
            }
        }
        removal = mergeSort(removal);
        addition = mergeSort(addition);
        for (String key : addition) {
            System.out.println(key);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String key : removal) {
            System.out.println(key);
        }
        System.out.println();
    }


    public static ArrayList<String> mergeH(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> merged = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i).compareTo(list2.get(j)) < 0) {
                merged.add(list1.get(i));
                i++;
            } else {
                merged.add(list2.get(j));
                j++;
            }
        }

        while (i < list1.size()) {
            merged.add(list1.get(i));
            i++;
        }

        while (j < list2.size()) {
            merged.add(list2.get(j));
            j++;
        }
        return merged;
    }


    public static ArrayList<String> mergeSort(ArrayList<String> list) {
        if (list.size() < 2) {
            return list;
        } else {
            int n = list.size() / 2;
            ArrayList<String> leftHalf = new ArrayList<>(list.subList(0, n));
            ArrayList<String> rightHalf = new ArrayList<>(list.subList(n, list.size()));
            return mergeH(mergeSort(leftHalf), mergeSort(rightHalf));


        }
    }


    public static void displayCurrentBranches(ArrayList<String> branches) {
        ArrayList<String> sortedBranches = mergeSort(branches);
        System.out.println("=== Branches ===");
        for (String branch : sortedBranches) {
            if (branch.equals(Branch.findCurrentBranch().getBranchName())) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
    }


    public static ArrayList<String> findUntrackedFiles() {

        ArrayList<String> untrackedFiles = new ArrayList<>();
        // Iterate through all files in CWD
        ArrayList<String> CWDFiles = listAllFiles(CWD);
        Map<String, String> stagingArea = StagingArea.readStagingArea();
        Commit currentCommit = Commit.findCurrentCommit();
        for (String file : CWDFiles) {
            //if the staging area contains the file and the value is not rm
            //or say if it is staged for addtion, break
            if (stagingArea.containsKey(file)) {
                if (!stagingArea.get(file).equals("rm")) {
                    continue;

                } else {
                    untrackedFiles.add(file);
                }
            }
            //if the commit does not track the file
            if (currentCommit.getFilesMap().containsKey(file)) {
                continue;
            }
            untrackedFiles.add(file);

        }

        return untrackedFiles;

    }


    /*
     * Display all files that are not staged for addtion or tracked in the current commit
     * Besides, includes any file that is staged for removal but still in the directory
     * */
    public static void displayUntracked() {
        System.out.println("=== Untracked Files ===");
        ArrayList<String> untrackedFiles = new ArrayList<>();
        // Iterate through all files in CWD
        ArrayList<String> CWDFiles = listAllFiles(CWD);

        Map<String, String> stagingArea = StagingArea.readStagingArea();
        Commit currentCommit = Commit.findCurrentCommit();

        for (String file : CWDFiles) {
            //if the staging area contains the file and the value is not rm
            //or say if it is staged for addtion, break
            if (stagingArea.containsKey(file)) {
                if (!stagingArea.get(file).equals("rm")) {
                    continue;

                } else {
                    untrackedFiles.add(file);
                }
            }
            //if the commit does not track the file
            if (currentCommit.getFilesMap().containsKey(file)) {
                continue;
            }

            untrackedFiles.add(file);

        }

        untrackedFiles = mergeSort(untrackedFiles);
        for (String file : untrackedFiles) {
            System.out.println(file);
        }
        System.out.println();


    }




    /*
     * Display what branches there is
     * The current branch will have an asterick if it has head
     * display all the file in the staging area
     * Must be displayed in lexicographic order
     *
     * Addtional stuff
     * Display all the modified but not staged file
     * In parenthesis write deleted if a file is staged for addtion
     * but is not found in the working dir
     * OR
     * if a file is in the current commit's fileMap but is deleted in the working dir
     * and it is not in the staging area with 'rm'
     *
     * In parenthesis write modified if a file is in the current commit's fileMap
     * and has different blob than the current commit, and the file is not in the staging area
     * OR
     * a file is in the staging area, but the blob in the staging area is different than what is in the WD
     *
     * Above is for the modified but not staged
     *
     * Display untracked files
     * A file is untracked if it is in the WD, but it is not in the staging area or the current commit
     * Also include the files that have 'rm' in the staging area, but it
     *
     *
     *Display the current branches
     *Display all the files in the staging area
     *
     * */

    public static void status() {

        ArrayList<String> branches = Branch.getCurrentBranches();
        displayCurrentBranches(branches);
        displayStagingArea();
        displayModifiedNotStaged();
        displayUntracked();

    }


    /*
    * Creates a new branch object and save it
    * It will point at the current commit and does not have head for now
    *
    *
    *Exception: If there is already a branch with this name error A branch
with that name already exists.
*
*
    * */
    public static void branch(String branchName) throws GitletException {
        if (Branch.isDuplicated(branchName)) {
            throw new GitletException("A branch with that name already exists.");
        }
        Commit currentCommit = Commit.findCurrentCommit();
        Branch newBranch = new Branch(branchName, currentCommit.getUID(), false);
        //Now we have two branches

    }


    /*
     * Prints out the ids of all commits that have the message
     *
     *
     *
     * */
    public static void find(String message) throws GitletException {
        ArrayList<String> commitFiles = listAllFiles(COMMITS_DIR);
        boolean isFound = false;
        for (String commitFile : commitFiles) {
            Commit commit = Commit.findCommitByUID(commitFile);
            if (commit.getMessage().equals(message)) {
                System.out.println(commit.getUID());
                isFound = true;

            }
        }

        if (!isFound) {
            throw new GitletException("Found no commit with that message.");
        }
    }


    /*
     * Display information about all commits, random orders
     *
     *
     *
     * */
    public static void logGlobally() {
        ArrayList<String> commitFiles = listAllFiles(COMMITS_DIR);
        for (String commitFile : commitFiles) {
            printCommitInfo(commitFile);

        }
    }


    /*
     * Display information of all the commits
     * Starts from the current to the initial commit
     * If there is merged commit that has two parents,
     * Only follow the first parent commit chain
     * First parent means the commit that the user is on
     * when calling the merge method
     * Display every commit's commit ID, time, and message
     * //for merged commit add a  Merge line, do this later
     * //The first is the first parent's first seven digit id
     * //so is the second
     *
     *
     * */
    public static void log() {
        Commit currentCommit = Commit.findCurrentCommit();
        ArrayList<String> allParentCommits = currentCommit.getParentCommit();
        //
        printCommitInfo(currentCommit.getUID());
        for (int i = allParentCommits.size() - 1; i >= 0; i--) {
            printCommitInfo(allParentCommits.get(i));
        }


    }

    public static void printCommitInfo(String commitID) {
        System.out.println("===");
        Commit commit = Commit.findCommitByUID(commitID);
        System.out.println("commit " + commit.getUID());
        System.out.println("Date: " + commit.getTimeStamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }


    /*
     * Unstage the file if it is currently in the staging area
     * Check if the file is being tracked in the current commit
     * Remove it from the commit only when user says commit
     * Remove it from the working directory when the user says commit too
     * // so you need change the code for commit method
     *
     * Exception: If the file is not in the staging area and not in
     * the current commit, error  No reason to remove the file.
     *
     *
     *
     * if the file is in the staging area, remove the entry
     * if it is also in the current commit, add this file to staging area and mark it as "rm" //and remove it
     * in the commit function if a file points to "rm" in the staging area, the file will not exist in the new commit
     * and the file will be removed(no remove it in the remove function this needs modifying)

     *
     * */
    public static void rm(String file) throws GitletException {
        Map<String, String> stagingArea = StagingArea.readStagingArea();
        boolean containsInStagingArea = stagingArea.containsKey(file);
        if (containsInStagingArea) {
            stagingArea.remove(file);
        }
        Commit currentCommit = Commit.findCurrentCommit();
        //if current commit contains the key, mark it for removal
        boolean containsInCurrentCommit = currentCommit.getFilesMap().containsKey(file);
        //if the file is tracked in the current commit
        //mark the file as rm so next commit will not have the file tracked
        //and delete the file in the wd
        if (containsInCurrentCommit) {
            stagingArea.put(file, "rm");
            File fileToRemove = Utils.join(CWD, file);
            Utils.restrictedDelete(fileToRemove);


        }
        if (containsInStagingArea == false && containsInCurrentCommit == false) {
            throw new GitletException("No reason to remove the file");
        }
        StagingArea.saveStagingArea(stagingArea);


    }


    /*
    *Create a new commit that tracks the saved files in the staging area
    * By default, the new commit's filemap is the same as its parents'
    *
    * The file in the original map will point to the new version in the staging area
    * //addition: A file tracked in the parent commit if some used rm command,do this later
    * The date of the commit will be different
    * The method will clear the staging area after a commit
    *
    * The commit's parents class will be the current head(and include the current head's parent class)
    * The head pointer will point to the new commit just made
    * Exceptions: if staging area is empty, print error No changes added to the
commit.
*If no message print Please enter a commit message.
*
*
* Make and save a new commit that is the same as its parents, update the time stamp
* update the parent class of the new commit
* update the fileMap
* update the head
* if the a file points to "rm" in staging area, remove the file in the CWD
* and update the commit to not include the file anymore
*
*
    * */
    public static void commit(String message) throws GitletException {

        Map<String, String> stagingArea = StagingArea.readStagingArea();
        if (stagingArea.size() == 0) {
            throw new GitletException("No changes added to the commit.");

        }
        //Get the current commit
        Commit currentCommit = Commit.findCurrentCommit();
        //String message, ArrayList<String> parentCommit, Map<String,String> filesMap
        ArrayList<String> newParentCommit = currentCommit.getParentCommit();
        //Parent commit is an alist of commits, the left most commits or the earliest commit on the index 0
        //We will add the current commit to the new commit's parent commit
        //[commit 0, commit 1,..... current commit] <-- newCommit
        newParentCommit.add(currentCommit.getUID());

        Map<String, String> currentFileMap = currentCommit.getFilesMap();
        Map<String, String> newFileMap = StagingArea.updateCommitMap(stagingArea, currentFileMap);
        System.out.println(newFileMap);

        //We update the message, new parent commit, and new file map

        Commit newCommit = new Commit(message, newParentCommit, newFileMap);
        //if staging area has 'rm' for a file, it will be deleted in WD when commiting// no you do not need to, delete it in rm method

        //restore the staging area
        Map<String, String> clearedStagingArea = new HashMap<String, String>();
        System.out.println("The current commit mapping is " + newCommit.getFilesMap());
        StagingArea.saveStagingArea(clearedStagingArea);


    }
    //added a feature of a second parent
       public static void mergeCommit(String message, String secondParentCommitID) throws GitletException {

        Map<String, String> stagingArea = StagingArea.readStagingArea();
        if (stagingArea.size() == 0) {
            throw new GitletException("No changes added to the commit.");

        }
        //Get the current commit
        Commit currentCommit = Commit.findCurrentCommit();
        //String message, ArrayList<String> parentCommit, Map<String,String> filesMap
        ArrayList<String> newParentCommit = currentCommit.getParentCommit();
        //Parent commit is an alist of commits, the left most commits or the earliest commit on the index 0
        //We will add the current commit to the new commit's parent commit
        //[commit 0, commit 1,..... current commit] <-- newCommit
        newParentCommit.add(currentCommit.getUID());

        Map<String, String> currentFileMap = currentCommit.getFilesMap();
        Map<String, String> newFileMap = StagingArea.updateCommitMap(stagingArea, currentFileMap);
        System.out.println(newFileMap);

        //get the parent commits of the given branch commit and add that branch commit to the parent commits of the new commit
           ArrayList<String> secondParentCommit = Commit.findCommitByUID(secondParentCommitID).getParentCommit();
           secondParentCommit.add(secondParentCommitID);
          //We update the message, new parent commit, and new file map
        Commit newCommit = new Commit(message, newParentCommit,secondParentCommit, newFileMap);
        //if staging area has 'rm' for a file, it will be deleted in WD when commiting// no you do not need to, delete it in rm method

        //restore the staging area
        Map<String, String> clearedStagingArea = new HashMap<String, String>();
        System.out.println("The current commit mapping is " + newCommit.getFilesMap());
        StagingArea.saveStagingArea(clearedStagingArea);


    }


    /* TODO: fill in the rest of this class. */
    //Creates a new .gitlet file in the current directory
    /*
     * Have the initial commit, no files tracked stored in .gitlet
     * Commit msg = "initial commit"
     * Create a new branch: master, points to the above commit
     * Set the commit metadeta, its time is 1970...
     * And its UID
     * Exits if error occured, A Gitlet version-control system already exists in the current directory
     * puts a blank map in staging area
     * */
    public static void initRepository() throws GitletException {

        File gitletDir = GITLET_DIR;
        if (gitletDir.exists()) {
            throw new GitletException("A Gitlet version-control system already exists in the current directory");
        }
        gitletDir.mkdir();
        File stagingArea = STAGING_AREA;
        stagingArea.mkdir();
        Commit initialCommit = new Commit();

        Map<String, String> blankStagingArea = new HashMap<>();
        StagingArea.saveStagingArea(blankStagingArea);


    }

    public static ArrayList<String> listAllFiles(File dir) throws GitletException {
        if (Utils.plainFilenamesIn(dir) == null) {
            return new ArrayList<String>();
        }
        return new ArrayList<String>(Utils.plainFilenamesIn(dir));
    }

    public static String makeBlobAndSaveFrom(File fileToAdd) {
        byte[] blob = Utils.readContents(fileToAdd);
        String blobID = Utils.sha1(blob);
        Blob.saveBlob(blob, blobID);
        return blobID;
    }

    public static String makeBlobWithoutSaving(File fileToAdd) {
        byte[] blob = Utils.readContents(fileToAdd);
        String blobID = Utils.sha1(blob);
        return blobID;
    }


    //find the commit object provided
    /*
     * get its file map
     * if the map contains the file, check if the blob ID is the same
     * if the same return the FileName of that file
     * */
    public static String compareFilesBlobInCommit(String commit, String fileName, String blobID) {
        Commit commitToCompare = Commit.findCommitByUID(commit);
        Map<String, String> commitFileMap = commitToCompare.getFilesMap();
        if (commitFileMap.containsKey(fileName)) {
            if (commitFileMap.get(fileName).equals(blobID)) {
                return commit;
            } else {
                System.out.println("The commit file version is not equal to the current version");
                return null;
            }
        }
        System.out.println("The commit does not include the file");
        return null;

    }

    /*

    *Add a blob of a file to the staging area
    If the file is already in the staging area, we rewrite the blob
    If the added file is the same as what is the current commit,
    do not add and remove it if it is already inside
    * */
    public static void add(String... addedFile) throws GitletException {
        ArrayList<String> CWDFiles = listAllFiles(CWD);
        System.out.println(CWDFiles);

        ArrayList<String> stagingAreaFiles = listAllFiles(STAGING_AREA);
        File fileToAdd = join(CWD, addedFile[0]);
        String blobID = makeBlobAndSaveFrom(fileToAdd);// create a blob and save it from a file
        // Read the mapping in staging area
        Map<String, String> stagingArea = StagingArea.readStagingArea();

        System.out.println(stagingArea);
        //If the key (filename) already exists, update it
        if (stagingArea.containsKey(addedFile[0])) {
            stagingArea.put(addedFile[0], blobID);
        } else {
            stagingArea.put(addedFile[0], blobID);
        }

        ///check if the current commit's file version equal to the added one
        //if so, remove the key from staging area
        String duplicateVersion = compareFilesBlobInCommit(Commit.findCurrentCommit().getUID(), addedFile[0], blobID);
        if (duplicateVersion != null) {
            if (stagingArea.containsKey(addedFile[0])) {
                stagingArea.remove(addedFile[0]);
            }
        }
        StagingArea.saveStagingArea(stagingArea);
        System.out.println("the staging area currently contains" + stagingArea);


//        fileMapToBlob.put(addedFile[0], blobID);
//
//
//
//        StagingArea.saveStagingArea(fileMapToBlob);


//        boolean isInStagingArea = false;
//        for (String file : addedFile) {
//            if (stagingAreaFiles.contains(file)) {
//                isInStagingArea = true;
//            }
//
//        }


    }


}
