package versio;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StagingArea {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("1", "d");
        map.put("2", "a");
        map.put("3", "b");
        map.put("4", "c");
        map.put("5", "d");

        Map<String,String> stagingArea = new HashMap<>();
        stagingArea.put("1", "a");
        stagingArea.put("4", "rm");
        Map<String, String> modified = updateCommitMap(stagingArea, map);
        System.out.println(updateCommitMap(stagingArea,map));
    }


    //FileToBlob means fileMap
    public static void saveStagingArea(Map<String, String> fileToBlob) {
        Repository.STAGING_AREA.mkdir();
        File stagingArea = Utils.join(Repository.STAGING_AREA, "STAGING_AREA");
        Utils.writeObject(stagingArea, (Serializable) fileToBlob);

    }
    //need testing
    //update all the file entry inside
    //if the file is labeled as rm
    //remove the file from the commitMap



    public static Map<String, String> updateCommitMap(Map<String, String> stagingArea, Map<String, String> commitMap) {
        for(String key: stagingArea.keySet()) {


            if (stagingArea.get(key).equals("rm")) {
                commitMap.remove(key);
            } else {
                String value = stagingArea.get(key);
                commitMap.put(key, value);
            }
        }
        return commitMap;
    }

    public static Map<String, String> readStagingArea() {

        File stagingArea = Utils.join(Repository.STAGING_AREA, "STAGING_AREA");
        return Utils.readObject(stagingArea, HashMap.class);
    }
}
