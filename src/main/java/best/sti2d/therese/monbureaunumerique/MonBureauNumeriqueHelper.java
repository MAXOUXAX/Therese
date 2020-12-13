package best.sti2d.therese.monbureaunumerique;

import best.sti2d.therese.Therese;
import best.sti2d.therese.generic.Homework;
import me.vinceh121.jkdecole.entities.homework.HWDay;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class MonBureauNumeriqueHelper {

    private final Therese therese;
    private final MonBureauNumeriqueManager monBureauNumeriqueManager;

    public MonBureauNumeriqueHelper(MonBureauNumeriqueManager monBureauNumeriqueManager) {
        therese = Therese.getInstance();
        this.monBureauNumeriqueManager = monBureauNumeriqueManager;
    }

    public ArrayList<Homework> getHomeworks(String date) throws ParseException, IOException {
        ArrayList<Homework> homeworks = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = simpleDateFormat.parse(date);
        Optional<HWDay> hwDayOptional = monBureauNumeriqueManager.getMbn().getAgenda().getDays().stream().filter(hwDay -> isSameDate(dateObj, hwDay.getDate())).findFirst();
        if(hwDayOptional.isPresent()){
            HWDay hwDay = hwDayOptional.get();
            Arrays.asList(hwDay.getHomeworks()).forEach(homework -> homeworks.add(new Homework(homework)));
        }
        return homeworks;
    }

    public boolean isSameDate(Date first, Date second){
        return (first.getDay() == second.getDay() && first.getMonth() == second.getMonth() && first.getYear() == second.getYear());
    }

}
