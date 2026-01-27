import java.io.*;
import java.util.*;

public class Main {

    static class Passenger {
        final String name;
        final int cls;          
        final String groupId;  
        final long seq;        

        Passenger(String name, int cls, String groupId, long seq) {
            this.name = name;
            this.cls = cls;
            this.groupId = groupId;
            this.seq = seq;
        }
    }


    static class GroupNode {
        Passenger p;
        GroupNode next;
        GroupNode(Passenger p) { this.p = p; }
    }

    static class GroupList {
        GroupNode head, tail;
        int size = 0;

        void add(Passenger p) {
            GroupNode node = new GroupNode(p);
            if (head == null) head = tail = node;
            else { tail.next = node; tail = node; }
            size++;
        }

        Iterable<Passenger> members() {
            return () -> new Iterator<Passenger>() {
                GroupNode cur = head;
                public boolean hasNext() { return cur != null; }
                public Passenger next() {
                    if (cur == null) throw new NoSuchElementException();
                    Passenger out = cur.p;
                    cur = cur.next;
                    return out;
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        LinkedList<Passenger>[] queues = new LinkedList[7];
        for (int i = 0; i < 7; i++) queues[i] = new LinkedList<>();

        Map<String, GroupList> groups = new HashMap<>();

        List<Passenger> boarded = new ArrayList<>();

        int gateCapacity = 1;
        long seq = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0].toUpperCase(Locale.ROOT);

            switch (cmd) {
                case "SET_GATE_CAPACITY": {
                    if (parts.length != 2) {
                        System.out.println("ERROR: Usage: SET_GATE_CAPACITY <g>");
                        break;
                    }
                    gateCapacity = Integer.parseInt(parts[1]);
                    if (gateCapacity <= 0) {
                        gateCapacity = 1;
                        System.out.println("ERROR: gate capacity must be > 0, defaulting to 1");
                    } else {
                        System.out.println("OK: Gate capacity set to " + gateCapacity);
                    }
                    break;
                }

                case "CHECKIN": {
                    if (parts.length != 3 && parts.length != 4) {
                        System.out.println("ERROR: Usage: CHECKIN <name> <class> [group_id]");
                        break;
                    }
                    String name = parts[1];
                    int cls = Integer.parseInt(parts[2]);
                    if (cls < 0 || cls > 6) {
                        System.out.println("ERROR: class must be between 0 and 6");
                        break;
                    }
                    String groupId = (parts.length == 4) ? parts[3] : null;

                    Passenger p = new Passenger(name, cls, groupId, seq++);
                    queues[cls].addLast(p);

                    if (groupId != null) {
                        GroupList gl = groups.computeIfAbsent(groupId, k -> new GroupList());
                        gl.add(p);
                    }

                    System.out.println("OK: Checked in " + name + " (Class " + cls + (groupId != null ? ", Group " + groupId : "") + ")");
                    break;
                }

                case "BOARD": {
                    int remaining = gateCapacity;
                    List<Passenger> boardedThisTick = new ArrayList<>();

                    while (remaining > 0) {
                        Passenger next = peekHighestPriority(queues);
                        if (next == null) break;

                        if (next.groupId == null) {
                            // board single
                            pollSpecificPassenger(queues[next.cls], next);
                            boarded.add(next);
                            boardedThisTick.add(next);
                            remaining--;
                        } else {
                            GroupList gl = groups.get(next.groupId);
                            int groupSize = (gl == null) ? 1 : gl.size;

                           
                            if (groupSize > remaining && !boardedThisTick.isEmpty()) {
                                break;
                            }

                            
                            Set<Passenger> groupMembers = new LinkedHashSet<>();
                            if (gl != null) {
                                for (Passenger gp : gl.members()) groupMembers.add(gp);
                            } else {
                                groupMembers.add(next);
                            }

                            for (Passenger gp : groupMembers) {
                                boolean removed = removeFromAllQueues(queues, gp);
                                if (removed) {
                                    boarded.add(gp);
                                    boardedThisTick.add(gp);
                                }
                            }

                            
                            remaining = Math.max(0, remaining - groupSize);
                        }
                    }

                    System.out.println("Boarding (capacity " + gateCapacity + "):");
                    if (boardedThisTick.isEmpty()) {
                        System.out.println("- (no one boarded)");
                    } else {
                        for (Passenger p : boardedThisTick) {
                            if (p.groupId == null) {
                                System.out.println("- " + p.name + " (Class " + p.cls + ")");
                            } else {
                                System.out.println("- " + p.name + " (Class " + p.cls + ", Group " + p.groupId + ")");
                            }
                        }
                    }
                    break;
                }

                case "STATUS": {
                    int totalWaiting = 0;
                    for (int i = 0; i < 7; i++) totalWaiting += queues[i].size();

                    System.out.println("STATUS:");
                    for (int i = 0; i < 7; i++) {
                        System.out.println("Queue Class " + i + ": " + queues[i].size());
                    }
                    System.out.println("Boarded: " + boarded.size());
                    System.out.println("Remaining (all queues): " + totalWaiting);
                    break;
                }

                default:
                    System.out.println("ERROR: Unknown command: " + cmd);
            }
        }
    }

    static Passenger peekHighestPriority(LinkedList<Passenger>[] queues) {
        for (int cls = 0; cls <= 6; cls++) {
            if (!queues[cls].isEmpty()) return queues[cls].peekFirst();
        }
        return null;
    }

    static void pollSpecificPassenger(LinkedList<Passenger> queue, Passenger p) {
        Passenger first = queue.peekFirst();
        if (first == p) {
            queue.pollFirst();
        } else {
            queue.remove(p);
        }
    }

    static boolean removeFromAllQueues(LinkedList<Passenger>[] queues, Passenger target) {
        for (int cls = 0; cls <= 6; cls++) {
            Iterator<Passenger> it = queues[cls].iterator();
            while (it.hasNext()) {
                Passenger p = it.next();
                if (p == target) { // reference equality (same object)
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }
}
