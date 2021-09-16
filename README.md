# Sudoku with AI

---

## Table of Contents
* [General information](#general-information)
* [Motivation](#motivation)
* [Used Technologies](#used-technologies)
* [Application usage](#application-usage)
* [Screenshots](#screenshots)

---

### General information

Sudoku with AI is a desktop application which allows user to:
* Load sample boards;
* Create boards his very own boards;
* Solve boards by himself - with assistance or without;
* Generate boards using algorithm visalisations on 4 different difficulty levels;
* Solve boards using 2 different algorithms.

In project I used following algorithms:
* <a href="https://en.wikipedia.org/wiki/Backtracking">Backtracking Algorithm</a> - depth-first search algorithm which guarantees to find sudoku solution if it exists.
* 1st Heuristic Algorithm - easy solving algorithm which is based on human logic while solving sudoku. It finds solution only when applied to easy boards.

Both solving algorithms listed above (with small modifications) were also used to implement sudoku board generators

#### Preview

<img src="https://github.com/KKofta/Sudoku-AI/blob/master/Preview/Preview.gif">

---

### Motivation

App was created mainly as a hobby. It was useful expand Java OOP, concurrency and JavaFX knowladge. I wanted to learn about Backtracking Algorithm and apply it in a nicely designed visualisations. Suprisingly it turned out that I also learned something about Sudoku puzzles.

---

### Used Technologies
* `Java 8`
* `JavaFX 8.0.241`
* `CSS`
* `Git`
* `Netbeans 12.0`

---

### Application Usage
In order to run application locally you must have installed `JRE 8`. 

1. Download `SudokuAI.jar` file. 

2. Double click on it or run command: 
    ```bash
    java -jar PATH/SudokuAI.jar
    ```

---

### Screenshots

* Unsolved board:
<img src="https://github.com/KKofta/Sudoku-AI/blob/master/Preview/View.png">

* Solved board:
<img src="https://github.com/KKofta/Sudoku-AI/blob/master/Preview/Solved.png">

* Board while generation is running:
<img src="https://github.com/KKofta/Sudoku-AI/blob/master/Preview/Generation.png">

* Board while being solved by a user:
<img src="https://github.com/KKofta/Sudoku-AI/blob/master/Preview/Solving.png">

---
