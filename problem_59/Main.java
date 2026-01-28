import java.io.*;
import java.util.*;

public class Main {

  static class Entry {
        final String user;
        final long total;
        Entry(String user, long total) {
            this.user = user;
            this.total = total;
        }
    }

  public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        int K = 3;

        Map<String, Long> totals = new HashMap<>();

        PriorityQueue<Entry> heap = new PriorityQueue<>(
                (a, b) -> {
                    int cmp = Long.compare(a.total, b.total);
                    if (cmp != 0)
                        return cmp;
                    return a.user.compareTo(b.user);
                });

        Map<String, Long> heapBest = new HashMap<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0].toUpperCase(Locale.ROOT);

            switch (cmd) {
                case "INIT": {
                    if (parts.length != 2) {
                        System.out.println("ERROR: Usage: INIT <k>");
                        break;
                    }
                    K = Integer.parseInt(parts[1]);
                    if (K <= 0) {
                        System.out.println("ERROR: k must be > 0");
                        K = 3;
                        break;
                    }
                    totals.clear();
                    heap.clear();
                    heapBest.clear();
                    System.out.println("OK: VIP tracker initialized with K=" + K);
                    break;
                }
