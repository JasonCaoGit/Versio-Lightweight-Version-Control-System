package gitlet;

import java.io.File;
import java.io.Serializable;

public class Head implements Serializable {
    public String currentHead;


    public static void setHead(String commitID) {
        Head head = findCurrentHeadObject();
        head.currentHead = commitID;
        head.save();

    }

    public Head(String currentHead) {
        this.currentHead = currentHead;
        this.save();
    }


    public static Head findCurrentHeadObject() {
        File HEAD = Utils.join(Repository.Head_DIR, "HEAD");
        Head headToRead = Utils.readObject(HEAD, Head.class);
        return headToRead;
    }


    public static String readCurrentHead() {
        File HEAD = Utils.join(Repository.Head_DIR, "HEAD");
        if (HEAD.exists()) {
            Head headToRead = Utils.readObject(HEAD, Head.class);
            return headToRead.currentHead;
        } else {
            System.out.println("There is no head file. Did you initialize the repo?");
            return null;
        }
    }

    public void setCurrentHead(String currentHead) {
        this.currentHead = currentHead;
        this.save();
    }
    //serialize the head object and save it
    public void save() {
        Repository.Head_DIR.mkdir();
        File HEAD = Utils.join(Repository.Head_DIR, "HEAD");
        Utils.writeObject(HEAD, this);

    }

    public static void main(String[] args) {
        Head head = new Head("52318edb2df58fa951f9a32a3f78e879e8c0c597");
        head.save();
        readCurrentHead();
        System.out.println(readCurrentHead());
        System.out.println(readCurrentHead());

    }


}
