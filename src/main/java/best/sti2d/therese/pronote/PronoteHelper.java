package best.sti2d.therese.pronote;

import best.sti2d.therese.Therese;
import best.sti2d.therese.pronote.objects.Class;
import best.sti2d.therese.pronote.objects.Homework;
import best.sti2d.therese.pronote.objects.Lesson;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PronoteHelper {

    private final Therese therese;
    private final PronoteManager pronoteManager;

    public PronoteHelper(PronoteManager pronoteManager) {
        therese = Therese.getInstance();
        this.pronoteManager = pronoteManager;
    }

    public ArrayList<MessageEmbed> getClassesEmbeds(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{timetable(from: \""+date+"\") {from to subject room teacher color status isAway isCancelled}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("timetable");
        ArrayList<MessageEmbed> messageEmbedList = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Class currentClass = new Class(element);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

            String time = formatter.format(currentClass.getFrom())+" - "+formatter.format(currentClass.getTo());
            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle(time+" - "+currentClass.getSubject()+" - "+new SimpleDateFormat("dd/MM").format(currentClass.getFrom()))
                    .setDescription("**Salle:** "+currentClass.getRoom()+"\n" +
                            "**Horaires**: "+formatter.format(currentClass.getFrom())+" » "+formatter.format(currentClass.getTo())+"\n" +
                            "**Professeur**: "+currentClass.getTeacher()+"\n\n"+
                            "**Statut**: " + currentClass.getStatus())

                    .setColor(currentClass.getColor());
            if(currentClass.isAway() || currentClass.isCancelled()) {
                embedCrafter.setTitle("**" + (currentClass.isAway() ? "ABSENT" : "ANNULÉ") + "** - ~~" + currentClass.getSubject() + "~~ - " + new SimpleDateFormat("dd/MM").format(currentClass.getFrom()))
                        .setDescription("~~**Salle:** " + currentClass.getRoom() + "~~\n" +
                                "~~**Horaires**: " + formatter.format(currentClass.getFrom()) + " » " + formatter.format(currentClass.getTo()) + "~~\n" +
                                "~~**Professeur**: " + currentClass.getTeacher() + "~~\n\n" +
                                "**STATUT**: " + currentClass.getStatus())
                        .setColor(Color.RED);
            }

            messageEmbedList.add(embedCrafter.build());
        });
        return messageEmbedList;
    }

    public ArrayList<MessageEmbed> getHomeworksEmbeds(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{homeworks(from: \""+date+"\") {description subject givenAt for color}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("homeworks");
        ArrayList<MessageEmbed> messageEmbedList = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Homework currentHomework = new Homework(element);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");

            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle(currentHomework.getSubject()+" - Pour le "+formatter.format(currentHomework.getDueTo()))
                    .setDescription(currentHomework.getDescription()+"\n" +
                            "**Donné le**: "+formatter.format(currentHomework.getGivenAt()))
                    .setColor(currentHomework.getColor());
            messageEmbedList.add(embedCrafter.build());
        });
        return messageEmbedList;
    }

    public ArrayList<MessageEmbed> getLessonsEmbeds(String date) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{contents(from: \""+date+"\") {from to subject teachers color title description}}");

        JSONObject finalObject = pronoteManager.makeRequest("/graphql", jsonObject);

        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("contents");
        ArrayList<MessageEmbed> messageEmbedList = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            Lesson currentLesson = new Lesson(element);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle(currentLesson.getSubject()+" - Cours du "+new SimpleDateFormat("dd/MM").format(currentLesson.getFrom()))
                    .setDescription(currentLesson.getTitle()+"\n\n" +
                            "**Contenu**: "+currentLesson.getDescription()+"\n" +
                            "**Professeur(s)**: "+ listToString(Arrays.stream(currentLesson.getTeachers()).iterator())+"\n" +
                            "**Horaires**: "+formatter.format(currentLesson.getFrom())+" » "+formatter.format(currentLesson.getTo())+"\n")
                    .setColor(currentLesson.getColor());
                    messageEmbedList.add(embedCrafter.build());
        });
        return messageEmbedList;
    }

    public String listToString(Iterator<String> iterator){
        StringBuilder stringBuilder = new StringBuilder();
        while(iterator.hasNext()){
            String teacher = iterator.next();
            stringBuilder.append(teacher);
            if (iterator.hasNext())stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }


}
