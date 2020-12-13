package best.sti2d.therese.generic;

import best.sti2d.therese.Therese;
import best.sti2d.therese.monbureaunumerique.MonBureauNumeriqueHelper;
import best.sti2d.therese.pronote.PronoteHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class GenericHelper {

    private final Therese therese;
    private final PronoteHelper pronoteHelper;
    private final MonBureauNumeriqueHelper monBureauNumeriqueHelper;

    public GenericHelper(PronoteHelper pronoteHelper, MonBureauNumeriqueHelper monBureauNumeriqueHelper) {
        therese = Therese.getInstance();
        this.pronoteHelper = pronoteHelper;
        this.monBureauNumeriqueHelper = monBureauNumeriqueHelper;
    }

    public ArrayList<Class> getClasses(String date) throws IOException {
        return pronoteHelper.getClasses(date);
    }

    public ArrayList<Homework> getHomeworks(String date) throws IOException, ParseException {
        ArrayList<Homework> homeworks = new ArrayList<>();
        homeworks.addAll(pronoteHelper.getHomeworks(date));
        homeworks.addAll(monBureauNumeriqueHelper.getHomeworks(date));
        return homeworks;
    }

    public ArrayList<Lesson> getLessons(String date) throws IOException {
        return pronoteHelper.getLessons(date);
    }


}
