package gitlet;

import java.io.Serializable;

public class Branch implements Serializable {
    String name;
    Commit savedCommit;
    String ID;

    public Branch(String name, Commit branchCommit) {
        this.name = name;
        this.savedCommit = branchCommit;
        this.ID = branchCommit.getID();
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.ID;
    }

    public Commit getSavedCommit() {
        return this.savedCommit;
    }

    public void updateCommit(Commit newFile) {
        this.savedCommit = newFile;
    }

    public void changeBranch(String branchName) {
        this.name = branchName;
    }
}

