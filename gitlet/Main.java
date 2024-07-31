package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zhuoyuan Cao
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws GitletException {
        // TODO: what if args is empty?
        if (args.length < 1) {
            System.out.println("Please enter your command!");
            System.exit(0);

        }
        String firstArg = args[0];
        //Creates a new .gitlet file in the current directory
        /*
        * Have the initial commit, no files tracked stored in .gitlet
        * Commit msg = "initial commit"
        * Create a new branch: master, points to the above commit
        * Set the commit metadeta, its time is 1970...
        * And its UID
        * Exits if error occured, A Gitlet version-control system already exists in the current directory
        * */
        switch(firstArg) {
            case "init":
                try {
                    Repository.initRepository();
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }

                // TODO: handle the `init` command
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                //First write the code to add 1 file

                    Repository.add(args[1]);





                break;

            case "commit":
                try {
                       if (args.length == 1) {
                        throw new GitletException("Please enter a commit message");

                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }

                String message = args[1];
                try {
                    Repository.commit(message);
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                break;

            case "rm":
                try {
                       if (args.length == 1) {
                           throw new GitletException("Please enter the file to remove");

                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                String file = args[1];
                try {
                       Repository.rm(file);
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                break;

            case "log":
                Repository.log();
                break;

            case "global-log":
                Repository.logGlobally();
                break;
            case "find":
                try {
                       if (args.length == 1) {
                           throw new GitletException("Please enter the file commit message to find!");

                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }

                String commitMessage = args[1];
                try {
                    Repository.find(commitMessage);
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case "branch":
                try {
                    if (args.length == 1) {
                        throw new GitletException("Please enter the branch name");

                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                String branchName = args[1];
                try {
                    Repository.branch(branchName);
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                break;

            case "checkout":
                try {
                    if (args.length == 1) {
                        throw new GitletException("Please enter the branchName or -- file or commitID -- file!");


                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                //You should edit here to prevent user to enter different stuff

                if (args.length == 3 && args[1].equals("--")) {
                    try {
                        String fileName = args[2];
                        Repository.checkoutFile(fileName);
                    } catch (GitletException e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    };


                }


                if (args.length == 4 && args[2].equals("--")) {
                    try {
                        String commitID = args[1];
                        String fileName = args[3];
                        Repository.checkoutCommitFile(commitID, fileName);
                    } catch (GitletException e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    };


                }


                if (args.length == 2 ) {
                    try {
                        String branchToCheckOut = args[1];
                        Repository.checkoutBranch(branchToCheckOut);
                    } catch (GitletException e) {
                        System.out.println(e.getMessage());
                        System.exit(0);
                    }
                    ;


                }
                break;

            case "status":
                Repository.status();
                break;

            case "rm-branch":
                try {
                    if (args.length == 1) {
                        throw new GitletException("Please enter the branchName!");


                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                try {
                    String branch = args[1];
                Repository.rmBranch(branch);
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                break;


            case "reset":
                try {
                    if (args.length == 1) {
                        throw new GitletException("Please enter the branchName!");


                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                try {
                    String commitID = args[1];
                    Repository.reset(commitID);
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }

                break;
            case "merge":
                try {
                    if (args.length == 1) {
                        throw new GitletException("Please enter the branchName!");


                }
                }catch(GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
                try {
                    String branchToMerge = args[1];

                    Repository.merge(branchToMerge);
                } catch (GitletException e) {
                    System.out.println(e.getMessage());
                    System.exit(0);
                }















            // TODO: FILL THE REST IN
        }
    }
}
