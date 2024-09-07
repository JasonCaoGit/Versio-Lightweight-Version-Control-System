package versio;

import java.io.File;

public class Blob {
    public static void saveBlob(byte[] blob, String blobID) {
        Repository.BLOBS_DIR.mkdir();
        File blobFile = Utils.join(Repository.BLOBS_DIR, blobID);
        Utils.writeObject(blobFile, blob);
    }

    //ReadBlob function is also needed
}
