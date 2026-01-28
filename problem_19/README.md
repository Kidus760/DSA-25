# Problem 19: The Live Gaming Leaderboard (Top K via Min-Heap)

## What it does
Maintains a **Top-K leaderboard** of score submissions from a live stream.

- Each incoming `SCORE` is treated as a **unique event**.
- We keep only the **K highest** scores seen so far.
- We do this efficiently with a **Min-Heap of size K**.

## Data structure idea (why Min-Heap?)
A min-heap lets us quickly find the **smallest** score inside the current Top-K (the *gatekeeper*).

- If heap size `< K` → insert directly.
- Else compare new score vs `heap.peek()`:
  - If `newScore > peekScore` → evict peek, insert new.
  - Otherwise → ignore.

Time per score: **O(log K)**.

## CLI Commands
- `INIT <k>`  
  Initialize leaderboard size.
- `SCORE <player> <val>`  
  Submit a score event.
- `SHOW_TOP`  
  Prints the current top K (sorted ascending).

## Example
```text
INIT 3
SCORE P1 100
SCORE P2 500
SCORE P3 300
SHOW_TOP
SCORE P4 50
SCORE P5 200
SHOW_TOP
```

## How to run
```bash
javac Main.java
java Main
```

Then type commands (or pipe from a file).
