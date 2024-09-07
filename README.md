My current version of git is capable of these commands

Setting up:

It needs a command-line software to run, such as Git or CMD from Windows. The user has to go to the directory and javac Versio/*.java to compile .java into .class files. My Git software will read the user's command line arguments. The format is as such: java Gitlet.Main init, java Gitlet.Main merge Master, java Gitlet.Main commit 'message' .... 
 
My software stores different objects as files in the .Versio directory, so the software has persistence. Even if the user reboots the computer, my program can start from the point where the user was at.
 
java Gitlet.Main init:

The software creates .Versio software to achieve persistence and store commits, blobs and other files.
 
java Gitlet.Main add [fileName](eg. java Gitlet.Main add CS.txt):

The file is added to the staging area for addition. Once the user hits commit, the software will take the snapshot of the file and store the relation between the file and blob(blob means the actual content of the file) into a Hashmap of strings.

java Gitlet.Main commit [commitMessage]: 

The user inputs a message and adds the commit object(with a unique SHA-1 ID) to the commit directory in .Versio. The snapshots of the files(fileName ---> Blob) are stored in that commit and the commit is serialized in the computer. User can checkout the commit and restore their file as tracked.

java Gitlet.Main branch [branchName]:
  
The user can create a new branch pointer that points to the current commit, the head pointer will not be changed to the new branch for now.
 
java Gitlet.Main log/ java Gitlet.Main status:

The user can use log to see all commits in the current chain of commits. They can use status to check out these statuses:
Branches that exist
Files in the staging area, including files staged for addition and files staged for removal
Modified files that have not been staged
Files that are untracked in the CWD

java Gitlet.Main checkout [branchName]/ java Gitlet.Main checkout -- [fileName]/ java Gitlet.Main checkout [commitID] -- [fileName]

Users can use checkout to move the Head pointer to a different branch, and restore the files back to the version in that branch commit.
  
Or with different command arguments, the user can also choose to checkout a file in the current commit. They can also checkout a file in a commit by providing the commit ID and the file name.

Above are the software features that have already been realized.
