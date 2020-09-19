package best.sti2d.therese.pronote;

import best.sti2d.therese.Therese;
import best.sti2d.therese.pronote.objects.Class;
import best.sti2d.therese.pronote.objects.Homework;
import best.sti2d.therese.pronote.objects.Lesson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class PronoteHelper {

    private final Therese therese;
    private final PronoteManager pronoteManager;

    public PronoteHelper(PronoteManager pronoteManager) {
        therese = Therese.getInstance();
        this.pronoteManager = pronoteManager;
    }

    public ArrayList<Class> getClasses(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{timetable(from: \""+date+"\") {from to subject room teacher color status isAway isCancelled}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("timetable");
        ArrayList<Class> classes = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Class currentClass = new Class(element);
            classes.add(currentClass);
        });
        return classes;
    }

    public ArrayList<Homework> getHomeworksEmbeds(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{homeworks(from: \""+date+"\") {description subject givenAt for color}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("homeworks");
        ArrayList<Homework> homeworks = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Homework currentHomework = new Homework(element);
            homeworks.add(currentHomework);
        });
        return homeworks;
    }

    public ArrayList<Lesson> getLessons(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{contents(from: \""+date+"\") {from to subject teachers color title description}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("contents");
        ArrayList<Lesson> lessons = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Lesson currentLesson = new Lesson(element);
            lessons.add(currentLesson);
        });
        return lessons;
    }


}
