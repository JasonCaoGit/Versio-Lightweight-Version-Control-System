# Versio

A version control system implemented in Java.

## Setup

This software requires a command-line interface such as Git or Windows CMD to run. To set up:

1. Navigate to the project directory
2. Compile the Java files:
   ```bash
   javac versio/*.java
   ```

The software uses a `.versio` directory for persistent storage, allowing it to maintain state across computer reboots.

## Commands

### Initialize Repository
```bash
java Versio.Main init
```
Creates a `.versio` directory to store commits, blobs, and other files for persistence.

### Add Files
```bash
java Versio.Main add [fileName]
# Example: java Versio.Main add CS.txt
```
Adds a file to the staging area. When committed, creates a snapshot of the file and stores the relationship between the filename and blob (file content) in a HashMap of strings.

### Create Commit
```bash
java Versio.Main commit [commitMessage]
```
Creates a commit with a unique SHA-1 ID and stores it in the commit directory within `.versio`. The commit contains snapshots of all staged files (filename -> blob mappings) and is serialized on the computer.

### Create Branch
```bash
java Versio.Main branch [branchName]
```
Creates a new branch pointer that points to the current commit. Note: This does not change the head pointer to the new branch.

### View Status
```bash
java Versio.Main status
```
Shows:
- Existing branches
- Files in staging area (both additions and removals)
- Modified but unstaged files
- Untracked files in the working directory

### View Log
```bash
java Versio.Main log
```
Displays all commits in the current commit chain.

### Checkout
Three checkout options are available:

1. Switch to a branch:
   ```bash
   java Versio.Main checkout [branchName]
   ```
   Moves the Head pointer to the specified branch and restores files to that branch's version.

2. Restore a file in current commit:
   ```bash
   java Versio.Main checkout -- [fileName]
   ```

3. Restore a file from specific commit:
   ```bash
   java Versio.Main checkout [commitID] -- [fileName]
   ```

## Technical Details

- All version control objects are stored in the `.versio` directory
- Uses a HashMap to store filename to blob mappings
- Commits are stored with SHA-1 IDs
- Maintains persistence across system reboots

## System Requirements
- Java Development Kit (JDK)
- Command-line interface (Git or CMD)
