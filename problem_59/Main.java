package problem_59;
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

        case "PURCHASE": {
          if (parts.length != 3) {
            System.out.println("ERROR: Usage: PURCHASE <user> <amount>");
            break;
          }
          String user = parts[1];
          long amount = Long.parseLong(parts[2]);
          if (amount < 0) {
            System.out.println("ERROR: amount must be >= 0");
            break;
          }

          long newTotal = totals.getOrDefault(user, 0L) + amount;
          totals.put(user, newTotal);

          // If already in Top-K, we "update" by pushing a fresh entry and updating
          // heapBest
          if (heapBest.containsKey(user)) {
            heapBest.put(user, newTotal);
            heap.add(new Entry(user, newTotal));
            cleanup(heap, heapBest);
            System.out.println("OK: " + user + " total updated to " + newTotal + " (in Top-" + K + ")");
            break;
          }

          if (heapBest.size() < K) {
            heapBest.put(user, newTotal);
            heap.add(new Entry(user, newTotal));
            cleanup(heap, heapBest);
            System.out.println("OK: " + user + " entered Top-" + K + " with total " + newTotal);
            break;
          }

          cleanup(heap, heapBest);
          if (heap.isEmpty()) {
            heapBest.put(user, newTotal);
            heap.add(new Entry(user, newTotal));
            System.out.println("OK: " + user + " entered Top-" + K + " with total " + newTotal);
            break;
          }

          Entry gate = heap.peek();
          if (newTotal > gate.total) {
            Entry evicted = heap.poll();
            heapBest.remove(evicted.user);

            heapBest.put(user, newTotal);
            heap.add(new Entry(user, newTotal));

            cleanup(heap, heapBest);
            System.out.println("OK: " + user + " entered Top-" + K + " with total " + newTotal +
                " (evicted " + evicted.user + " with " + evicted.total + ")");
          } else {
            System.out.println("IGNORED: " + user + " total " + newTotal + " <= gatekeeper " + gate.user
                + " " + gate.total);
          }
          break;
        }

        case "SHOW_VIP": {
          List<Entry> vip = new ArrayList<>();
          for (Map.Entry<String, Long> e : heapBest.entrySet()) {
            vip.add(new Entry(e.getKey(), e.getValue()));
          }
          vip.sort((a, b) -> {
            int cmp = Long.compare(b.total, a.total);
            if (cmp != 0)
              return cmp;
            return a.user.compareTo(b.user);
          });

          System.out.println("TOP " + K + " VIP (descending):");
          for (Entry e : vip) {
            System.out.println(e.user + " " + e.total);
          }
          break;
        }

        default:
          System.out.println("ERROR: Unknown command: " + cmd);
      }
    }
  }

  static void cleanup(PriorityQueue<Entry> heap, Map<String, Long> heapBest) {
    while (!heap.isEmpty()) {
      Entry top = heap.peek();
      Long cur = heapBest.get(top.user);
      if (cur == null || cur != top.total) {
        heap.poll();
      } else {
        break;
      }
    }
  }
}
