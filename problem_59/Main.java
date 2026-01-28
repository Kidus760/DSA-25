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