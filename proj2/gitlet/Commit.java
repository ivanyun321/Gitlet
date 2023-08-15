package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Ivan & Ines
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timeStamp;
    private Commit parent;
    private TreeMap<String, Blobs> contents = new TreeMap<>();
    private String ID;

    public Commit(String message, Commit parent) {
        this.message = message;
        this.parent = parent;
        byte[] commitID = Utils.serialize(this);
        this.ID = Utils.sha1(commitID);
        if (parent == null) {
            timeStamp = new Date(0);
        } else {
            timeStamp = new Date();
        }
    }
    public String getID() {
        return this.ID;
    }
    public String getMessage() {
        return this.message;
    }

    public String gettimeStamp() {
        SimpleDateFormat proj2format = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        proj2format.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        return proj2format.format(timeStamp);
    }

    public Commit getParent() {
        return this.parent;
    }

    public void addContents(TreeMap<String, Blobs> lst) {
        Set keys;
        keys = lst.keySet();
        for (Object x : keys) {
            contents.put((String) x, lst.get(x));
        }
    }

    public TreeMap<String, Blobs> getContents() {
        return contents;
    }

}



