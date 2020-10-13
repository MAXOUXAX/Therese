package best.sti2d.therese.schedulers;

import best.sti2d.therese.Therese;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class TaskCheckClasses implements Runnable {

    private final Therese therese;
    private final HashMap<String, Class> cachedClasses = new HashMap<>();

    public TaskCheckClasses(Therese therese) {
        this.therese = therese;
    }

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        LocalDate firstDayOfNextWeek = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        List<LocalDate> remainingDays = new ArrayList<>();
        while (localDate.isBefore(firstDayOfNextWeek)) {
            remainingDays.add(localDate);
            localDate = localDate.plusDays(1);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        remainingDays.forEach(localDate1 -> {
            try {
                therese.getPronoteManager().getHelper().getClasses(simpleDateFormat.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
