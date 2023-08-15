package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.io.Serializable;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Ivan & Ines
 */
public class Repository implements Serializable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    private final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    final File GITLET_DIR = join(CWD, ".gitlet");
    final File gitletRepository = gitlet.Utils.join(GITLET_DIR, "repo");
    final File stagingAreaFile = gitlet.Utils.join(gitletRepository, "staging area");
    TreeMap<String, Blobs> stagingArea = new TreeMap<>();
    TreeMap<String, Blobs> stagingAreaAdd = new TreeMap<>();
    TreeMap<String, Blobs> stagingAreaRemove = new TreeMap<>();
    final TreeMap<String, String> blobIDtoContents = new TreeMap<>();
    final TreeMap<String, Commit> commitIDs = new TreeMap<>();
    final TreeMap<String, Branch> treeBranch = new TreeMap<>();
    Commit head;
    Branch currBranch;

    public void init() throws IOException {
        if (!commitIDs.isEmpty()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        } else {
            if (!GITLET_DIR.exists()) {
                GITLET_DIR.mkdir();
            }
            if (!gitletRepository.exists()) {
                Utils.writeContents(gitletRepository, "");
            }
            Commit commit0 = new Commit("initial commit", null);
            head = commit0;
            commitIDs.put(head.getID(), head);
            currBranch = new Branch("main", head);
            treeBranch.put("main", currBranch);
        }
    }

    public void add(String[] args) {
        File cwdPath = Utils.join(CWD, args[1]);
        // checks if file exists
        if (!cwdPath.exists()) {
            System.out.println("File does not exist.");
        } else {
            // check if file exists in current commit and if contents are the same
            if (stagingAreaRemove.containsKey(args[1])) {
                stagingAreaRemove.remove(args[1]);
            }
            if (head.getContents().containsKey(args[1])
                    && head.getContents().get(args[1]).getContents().equals(Utils.readContentsAsString(cwdPath))) {
                stagingArea.remove(args[1]); // removes it from stagingArea if so
            } else {
                // checks if file exists in staging area
                if (Utils.join(stagingAreaFile, args[1]).exists()) {
                    // change the file in staging area to match the file in CWD
                    Utils.writeContents(Utils.join(stagingAreaFile, args[1]), Utils.readContentsAsString(cwdPath));
                }
                // remove from current TreeMap to add updated one
                if (stagingAreaAdd.containsKey(args[1])) {
                    stagingAreaAdd.remove(args[1]);
                }
                // Making blob pertaining to the file
                Blobs newBlob = new Blobs(cwdPath);
                newBlob.makeID(newBlob);
                blobIDtoContents.put(newBlob.getID(), newBlob.getContents());
                stagingAreaAdd.put(args[1], newBlob);
                Utils.join(stagingAreaFile, args[1]);
                stagingArea.put(args[1], newBlob);
            }
        }
    }

    public void rm(String name) {
        // checks if file exists in stagingAreaFile or current commit
        if (stagingArea.containsKey(name) || head.getContents().containsKey(name)) {
            // maps the file to the blob in the TreeMap stagingAreaRemove
            if (head.getContents().containsKey(name)) {
                stagingAreaRemove.put(name, stagingAreaAdd.get(name));
                Utils.restrictedDelete(name);
            }
            // Remove from stagingAreaAdd
            stagingAreaAdd.remove(name);
            stagingArea.remove(name);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }
    public void makeCommit(String[] args) {
        if (stagingArea.isEmpty()
                && stagingAreaAdd.isEmpty()
                && stagingAreaRemove.isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else if (args[1].equals("")) {
            System.out.println("Please enter a commit message.");
        } else {
            // makes a new commit with message args[1] and parent head
            Commit curr = new Commit(args[1], head);
        /* copies over the TreeMap stagingAreaAdd which contains the names
        of files mapped to blobs */
            curr.addContents(head.getContents());
            Set keyRemove = stagingAreaRemove.keySet();
            for (Object x: keyRemove) {
                if (curr.getContents().containsKey(x)) {
                    curr.getContents().remove(x);
                }
            }
            curr.addContents(stagingAreaAdd);
            stagingAreaAdd = new TreeMap<>();
            stagingAreaRemove = new TreeMap<>();
            stagingArea = new TreeMap<>();
            commitIDs.put(curr.getID(), curr); // TreeMap mapping sha1 ID to commits
            head = curr; // update head to current commit
            currBranch.updateCommit(head);
        }
    }

    public void restore(String[] args) {
        if (!args[2].equals("--") && !args[1].equals("--")) {
            System.out.println("Incorrect operands.");
        }
        if (args.length == 3) { // restoring from current commit
            restoreFile(args[2]);
        } else if (args.length == 4) {
            restoreFromCommit(args[1], args[3]); // restoring from prev commit
        }
    }

    public void restoreFile(String name) {
        if (!head.getContents().containsKey(name)) {
            System.out.println("File does not exist in that commit.");
        } else {
            Blobs oldBlob = head.getContents().get(name); // gets blob from current version
            String contents = oldBlob.getContents(); // gets contents of that blob
            // rewrites our current file's contents to contents saved
            Utils.writeContents(Utils.join(CWD, name), contents);
        }
    }

    public void restoreFromCommit(String commitID, String name) {
        Commit oldCommit;
        Set comIDs = commitIDs.keySet();
        for (Object x: comIDs) {
            if (x.toString().contains(commitID)) {
                oldCommit = commitIDs.get(x);
                if (oldCommit.getContents().containsKey(name)) {
                    Blobs oldBlob = oldCommit.getContents().get(name); // pulls up blob within that commit
                    // rewrites our current file's contents to contents saved
                    Utils.writeContents(Utils.join(CWD, name), oldBlob.getContents());
                } else {
                    System.out.println("File does not exist in that commit.");
                }
                return ;
            }
        }
        System.out.println("No commit with that id exists.");
    }

    public void log() {
        Commit temp = head;
        while (temp != null) {
            System.out.println("=== \n"
                    + "commit "
                    + temp.getID()
                    + "\n"
                    + "Date: "
                    + temp.gettimeStamp()
                    + "\n"
                    + temp.getMessage()
                    + "\n"
            );
            temp = temp.getParent();
        }
    }

    public void globalLog() {
        Set allCommits = commitIDs.keySet();
        for (Object x: allCommits) {
            System.out.println("=== \n"
                    + "commit "
                    + commitIDs.get(x).getID()
                    + "\n"
                    + "Date: "
                    + commitIDs.get(x).gettimeStamp()
                    + "\n"
                    + commitIDs.get(x).getMessage()
                    + "\n"
            );
        }
    }

    public void status() {
        System.out.println("=== Branches ===");
        Set branches = treeBranch.keySet();
        for (Object x: branches) {
            if (currBranch.getName().equals(x)) {
                System.out.println("*" + x);
            } else {
                System.out.println(x);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Set stagingKeys = stagingArea.keySet();
        for (Object x: stagingKeys) {
            System.out.println(x);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        Set removeKeys = stagingAreaRemove.keySet();
        for (Object x: removeKeys) {
            System.out.println(x);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();

    }

    public void branch(String[] args) {
        if (treeBranch.containsKey(args[1])) {
            System.out.println("A branch with that name already exists.");
        } else {
            Branch newBranch = new Branch(args[1], head);
            treeBranch.put(newBranch.getName(), newBranch);
        }
    }
    //&& !stagingAreaAdd.get(x).equals(Utils.readContentsAsString(Utils.join(CWD, x))))
    public void switchBranch(String[] args) {
        if (!treeBranch.containsKey(args[1])) {
            System.out.println("No such branch exists.");
            return ;
        }
        for (String x: Utils.plainFilenamesIn(CWD)) {
            if (!head.getContents().containsKey(x)
                    && !stagingAreaRemove.containsKey(x)
                    && !stagingAreaAdd.containsKey(x)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return ;
            }
        }
        currBranch = treeBranch.get(args[1]);
        TreeMap currContents = currBranch.getSavedCommit().getContents();
        head = currBranch.savedCommit;
        Set key = currContents.keySet();
        for (String x : Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(x);
        }
        for (Object x: key) {
            Utils.join(CWD, (String) x);
            Blobs contentsBlob = (Blobs) currContents.get(x);
            Utils.writeContents(Utils.join(CWD, (String) x), contentsBlob.getContents());
        }
    }

    public void find(String[] args) {
        Set comms = commitIDs.keySet();
        Boolean containsMessage = false;
        for (Object x: comms) {
            if (commitIDs.get(x).getMessage().equals(args[1])) {
                containsMessage = true;
            }
        }
        if (containsMessage == false) {
            System.out.println("Found no commit with that message.");
        }
        for (Object x: comms) {
            if (commitIDs.get(x).getMessage().equals(args[1])) {
                System.out.println(commitIDs.get(x).getID());
            }
        }
    }

    public void reset(String[] args) {
        Commit oldCommit;
        for (String x: Utils.plainFilenamesIn(CWD)) {
            if (!head.getContents().containsKey(x)
                    && !stagingAreaAdd.containsKey(x)
                    && !stagingAreaRemove.containsKey(x)
            ) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return ;
            }
        }
        if (!commitIDs.containsKey(args[1])) {
            System.out.println("No commit with that id exists.");
        }
        else {
            oldCommit = commitIDs.get(args[1]);
            head = oldCommit;
            for (String x : Utils.plainFilenamesIn(CWD)) {
                if (!oldCommit.getContents().containsKey(x)) {
                    restrictedDelete(x);
                }
            }
            currBranch.savedCommit = head;
            stagingArea = new TreeMap<>();
            stagingAreaRemove = new TreeMap<>();
            stagingAreaAdd = new TreeMap<>();
        }
    }

    public void rmBranch(String[] args) {
        if (!treeBranch.containsKey(args[1])) {
            System.out.println("A branch with that name does not exist.");
        } else if (currBranch.getName().equals(args[1])) {
            System.out.println("Cannot remove the current branch.");
        } else {
            treeBranch.remove(args[1]);
        }
    }
}




