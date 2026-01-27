# Problem 52: Multi-Class Boarding Queue System

## What it does
Simulates an airline boarding process with:
- **7 priority classes** (0..6)
- **FIFO** inside each class
- Optional **group boarding** (families): when one member boards, the full group boards together
- **Gate capacity** `G`: max passengers per boarding tick

## Priority classes
Lower number = higher priority:
- `0` = Wheelchair / Assistance (before First)
- `1` = First
- `2` = Business
- `3` = Premium Economy
- `4` = Economy
- `5` = Basic Economy
- `6` = Standby (after Basic)

## Data structures used
- **Array of queues**: `LinkedList<Passenger>[7]`
- **Group linked list**: custom singly linked list per `groupId`
- **ArrayList**: stores boarded passengers (history)

## Group vs gate capacity rule (important)
Normally we board up to `G` per `BOARD`.

If the next passenger is in a group that is bigger than the remaining capacity:
- If nobody boarded yet in this tick → board the full group anyway (**avoids deadlock** when group size > G).
- If some already boarded → stop this tick and leave the group for next `BOARD`.

This keeps “group boards together” while still respecting capacity as much as possible.

## CLI Commands
- `SET_GATE_CAPACITY <g>`
- `CHECKIN <name> <class> [group_id]`
- `BOARD`
- `STATUS`

## Example
```text
SET_GATE_CAPACITY 3
CHECKIN Alice 1
CHECKIN Bob 3
CHECKIN Carol 1
CHECKIN Dave 2
CHECKIN Eve 3 G1
CHECKIN Frank 3 G1

BOARD
BOARD
STATUS
```

## How to run
```bash
javac Main.java
java Main
```
