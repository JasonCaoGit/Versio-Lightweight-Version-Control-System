# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Main

#### Fields

1. Branch branch
2. Commit  //need name specified
3. 


### Repository
It sets up all functions the main needs to 
execute stuff.

#### Fields

1. Field 1
2. Field 2

### Commit
You find the current commit object
#### Fields
1. Branch
2. isHead


### Head
Creates head object that store String current head and can be stored in Head
folder

#### Fields

1. currentHead


### Branch
Creates a branch object and save it as an serialized object

#### Fields

1. branchName
2. currentCommit

### Stagingarea
We use a hashmap to store the connections between files and blobs,
this class is used to save the map as an object and read them back

#### Fields

1. currentHead
## Algorithms

## Persistence
For all commits, save them as objects within a folder that 
has the two first letter of its UID. Save the blob in that 
folder too!
For staging area, we need a hashmap for a staging area
and we save that hash map as a file, next we commit we use the file
