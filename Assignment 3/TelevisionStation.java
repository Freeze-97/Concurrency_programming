package concurrency.ch03_sharing_objects.exercise_3_2;

import java.text.*;
import java.util.*;

public class TelevisionStation extends MediaCompany {
    private final static DateFormat dayFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Collection<String>> headlines = // Make it private. Can be accessed by any thread.sync get and set if needed.
            new HashMap<>();

    public TelevisionStation() { // Should only initialize, not be used in constructor. It can escape here.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (headlines.size() > 100) { // Checks headlines before construction is completed. Should be out outside this scope.
                    System.out.println("Size of headlines too large: " +
                            headlines.size());
                }
            }
        }, 10_000, 10_000);
    }

    public void showTeleprompter() {
        printHeadlines(headlines.values()); // The value is visible to all threads.
    }

    public void addHeadline(Date date, String line) { // Add sync for thread-safety
        String day = dayFormat.format(date); 
        Collection<String> lines = headlines.get(day);
        if (lines == null) {
            lines = new ArrayList<String>();
				headlines.put(day, lines); // Headlines can be changed here. 
        }
        lines.add(line);
    }
}
