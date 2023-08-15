package gitlet;
import java.io.File;
import java.io.Serializable;

public class Blobs implements Serializable {
    private String contents;
    private String ID;
    public Blobs(File read) {
        contents = Utils.readContentsAsString(read);
    }
    public String getContents() {
        return contents;
    }

    public void makeID(Blobs x) {
        byte[] hash = Utils.serialize(x);
        this.ID = Utils.sha1(hash);
    }

    public String getID() {
        return this.ID;
    }

    public void updateContents(String newContents) {
        contents = newContents;
    }
}
