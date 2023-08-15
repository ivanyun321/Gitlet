package gitlet;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Ivan and Ines
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    //Current working directory

    public static void main(String[] args) throws IOException {
        Repository r = new Repository();
        if ((r.gitletRepository).exists()) {
            r = Utils.readObject(r.gitletRepository, Repository.class);
        }
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        } else {
            String firstArg = args[0];
            switch (firstArg) {
                case "":
                    System.out.print("Please enter a command.");
                    break;
                case "init":
                    r.init();
                    break;
                case "add":
                    r.add(args);
                    break;
                case "commit":
                    r.makeCommit(args);
                    break;
                case "rm":
                    r.rm(args[1]);
                    break;
                case "restore":
                    // add restore method
                    r.restore(args);
                    break;
                case "log":
                    r.log();
                    break;
                case "status":
                    repoCheck();
                    r.status();
                    break;
                case "global-log":
                    r.globalLog();
                    break;
                case "branch":
                    r.branch(args);
                    break;
                case "switch":
                    r.switchBranch(args);
                    break;
                case "find":
                    r.find(args);
                    break;
                case "reset":
                    r.reset(args);
                    break;
                case "rm-branch":
                    r.rmBranch(args);
                    break;
                default:
                    System.out.println("No command with that name exists.");
            }
            Utils.writeObject(r.gitletRepository, r);
        }
    }

    public static void repoCheck() {
        Repository r = new Repository();
        if (!r.gitletRepository.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}



