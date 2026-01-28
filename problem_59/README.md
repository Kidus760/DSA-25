# Problem 59: The VIP Spender Tracker (Top K)

## What it does
Tracks live purchases and keeps the **Top-K highest spending users**.

- Each purchase updates:
  - `TotalSpend[user] += amount`
- Then we check whether that user belongs in the Top-K leaderboard.

## Data structures (required)
- **HashMap**: `totals[user] -> totalSpend`
- **Min-Heap (PriorityQueue)** size K:
  - Root = “poorest of the rich” (smallest total among Top-K)
- Extra helper map:
  - `heapBest[user] -> total` for users currently in Top-K

## Why do we need “lazy updates”?
Java’s `PriorityQueue` doesn’t support updating an existing element in-place.

So when a VIP’s total changes, we:
1. Push a **new (user,total)** entry into the heap.
2. Mark the “current best” in `heapBest`.
3. When we look at heap root, we **discard stale entries** whose totals don’t match `heapBest`.

This is a standard, reliable pattern.

## CLI Commands
- `INIT <k>` (optional; default is 3)
- `PURCHASE <user> <amount>`
- `SHOW_VIP` (prints Top K sorted descending)

## Example
```text
INIT 3
PURCHASE U1 100
PURCHASE U2 200
PURCHASE U3 300
PURCHASE U4 50
PURCHASE U1 250
SHOW_VIP
```

## How to run
```bash
javac Main.java
java Main
```
