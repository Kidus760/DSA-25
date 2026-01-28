# DSA Problem Combination 25 – Java Implementation

## Overview
This repository contains the implementation of **Problem Combination #25** for the Data Structures and Algorithms (DSA) course.  
All problems are implemented using **Java**, following efficient algorithmic and data structure principles.

Each problem is organized into its **own package**, with a dedicated implementation and documentation.

---

## Problem Combination Details

**Problem Combination #25 includes:**
- **Problem #19** – The Live Gaming Leaderboard 
- **Problem #52** – Multi-Class Boarding Queue System 
- **Problem #59** – The VIP Spender Tracker

---

## Technologies Used
- **Programming Language:** Java  
- **Core Data Structures:**
  - Queues
  - Linked Lists
  - Hash Maps
  - Min-Heaps (Priority Queues)
- **Development Tools:**
  - JDK 8 or later
  - Any Java-supported IDE (IntelliJ IDEA, Eclipse, VS Code)

---

## Project Structure

Each problem folder contains:
- Source code files
- A `README.md` explaining the problem, logic, and execution steps

---

## Team Members & Contributions

### **Yabsega Demis**
- **Problem #19 – The Live Gaming Leaderboard**
- Designed a real-time leaderboard using a Min-Heap of size K.
- Optimized score updates with **O(log K)** complexity.
- Implemented `INIT`, `SCORE`, and `SHOW_TOP` commands.

---

### **Kidus Michael**
- **Problem #52 – Multi-Class Boarding Queue System**
- Implemented boarding logic using an array of queues for seven priority levels.
- Added group boarding support using linked lists.
- Managed gate capacity constraints.
- Implemented `BOARD` and `STATUS` commands.

---

### **Dibora Mulatu**
- **Problem #59 – The VIP Spender Tracker**
- Built customer spend tracking using a HashMap.
- Maintained a live Top 3 VIP list using a Min-Heap.
- Implemented `PURCHASE` and `SHOW_VIP` operations.

---

## How to Run the Code

1. **Clone the repository**
   ```bash
   git clone <https://github.com/Kidus760/DSA-25.git>

2. **Follow each problem's readme.md file to run**
