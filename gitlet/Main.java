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
                break;
            // TODO: FILL THE REST IN
        }
    }
}
