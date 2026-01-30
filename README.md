# BST Visualizer

An interactive binary search tree (BST) visualizer built with JavaFX to practice
data structures and UI programming.

## Features
- Insert, delete, and search nodes
- Visual layout of nodes and edges
- In-order, pre-order, and post-order traversal output
- Keyboard shortcuts for quick actions

## Tech Stack
- Java 23
- JavaFX 23.0.2
- Maven

## Requirements
- JDK 23 installed and on your `PATH`
- Maven installed

## Run
```bash
mvn javafx:run -Djavafx.mainClass=com.hectora15.binarytree.Main
```

If you use IntelliJ, run the `com.hectora15.binarytree.Main` class directly.

## Controls
- **Switch Mode**: cycles Add, Delete, Search
- **Mode**: executes the current action
- **Clear**: clears the tree
- **Keyboard**:
  - `Enter`: execute current mode
  - `Tab`: switch mode
  - `Esc`: exit
