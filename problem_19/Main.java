package problem_19;
import java.io.*;
import java.util.*;


public class Main {

    static class ScoreEntry {
        final String playerId;
        final long score;

        ScoreEntry(String playerId, long score) {
            this.playerId = playerId;
            this.score = score;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        int k = 0;

        PriorityQueue<ScoreEntry> minHeap = new PriorityQueue<>(
                (a, b) -> {
                    int cmp = Long.compare(a.score, b.score);
                    if (cmp != 0) return cmp;
                    return a.playerId.compareTo(b.playerId);
                }
        );

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0].toUpperCase(Locale.ROOT);

            switch (cmd) {
                case "INIT": {
                    if (parts.length != 2) {
                        System.out.println("ERROR: Usage: INIT <k>");
                        break;
                    }
                    k = Integer.parseInt(parts[1]);
                    if (k <= 0) {
                        System.out.println("ERROR: k must be > 0");
                        k = 0;
                        minHeap.clear();
                        break;
                    }
                    minHeap.clear();
                    System.out.println("OK: Leaderboard initialized with K=" + k);
                    break;
                }

                case "SCORE": {
                    if (k <= 0) {
                        System.out.println("ERROR: Call INIT <k> first");
                        break;
                    }
                    if (parts.length != 3) {
                        System.out.println("ERROR: Usage: SCORE <player> <val>");
                        break;
                    }
                    String player = parts[1];
                    long val = Long.parseLong(parts[2]);

                    ScoreEntry entry = new ScoreEntry(player, val);

                    if (minHeap.size() < k) {
                        minHeap.add(entry);
                        if (minHeap.size() == k) {
                            System.out.println("ACCEPTED: inserted (heap is now FULL)");
                        } else {
                            System.out.println("ACCEPTED: inserted (heap not full yet)");
                        }
                    } else {
                        ScoreEntry gatekeeper = minHeap.peek();
                        if (gatekeeper != null && val > gatekeeper.score) {
                            minHeap.poll();        // evict current K-th best
                            minHeap.add(entry);    // insert new score
                            System.out.println("ACCEPTED: evicted (" + gatekeeper.playerId + "," + gatekeeper.score + "), inserted (" + player + "," + val + ")");
                        } else {
                            System.out.println("IGNORED: (" + player + "," + val + ") <= gatekeeper (" + gatekeeper.playerId + "," + gatekeeper.score + ")");
                        }
                    }
                    break;
                }

                case "SHOW_TOP": {
                    if (k <= 0) {
                        System.out.println("ERROR: Call INIT <k> first");
                        break;
                    }
                    List<ScoreEntry> list = new ArrayList<>(minHeap);
                    list.sort((a, b) -> {
                        int cmp = Long.compare(a.score, b.score);
                        if (cmp != 0) return cmp;
                        return a.playerId.compareTo(b.playerId);
                    });

                    System.out.println("TOP " + k + " (ascending):");
                    for (ScoreEntry e : list) {
                        System.out.println(e.playerId + " " + e.score);
                    }
                    break;
                }

                default:
                    System.out.println("ERROR: Unknown command: " + cmd);
            }
        }
    }
}
