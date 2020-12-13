package best.sti2d.therese.generic;

import best.sti2d.therese.utils.EmbedCrafter;
import best.sti2d.therese.utils.ListUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Lesson {

    private String subject;
    private String title;
    private String description;
    private String[] teachers;
    private Color color;
    private Date from;
    private Date to;
    private HashMap<String, String> files = new HashMap<>();
    private DataSource source;

    public Lesson(JSONObject element) {
        this.subject = element.getString("subject");
        this.title = element.getString("title");
        this.description = element.getString("description");
        this.teachers = element.getJSONArray("teachers").toList().toArray(new String[0]);
        this.color = element.isNull("color") ? Color.WHITE : Color.decode(element.getString("color"));
        this.from = new Date(element.getLong("from"));
        this.to = new Date(element.getLong("to"));
        if(!element.isNull("files")) {
            JSONArray filesArray = element.getJSONArray("files");
            filesArray.forEach(o -> {
                JSONObject jsonObject = (JSONObject) o;
                files.put(jsonObject.getString("name"), jsonObject.getString("url"));
            });
        }
        this.source = DataSource.PRONOTE;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTeachers() {
        return teachers;
    }

    public void setTeachers(String[] teachers) {
        this.teachers = teachers;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public HashMap<String, String> getFiles() {
        return files;
    }

    public void setFiles(HashMap<String, String> files) {
        this.files = files;
    }

    public MessageEmbed toEmbed(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayMonth = new SimpleDateFormat("dd/MM");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        dayMonth.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(getSubject()+" - Cours du "+dayMonth.format(getFrom())+" de "+formatter.format(getFrom())+" à "+formatter.format(getTo())+"\n")
                .setDescription(getTitle()+"\n\n" +
                        "**Contenu**: \n```"+getDescription()+"```\n" +
                        "**Professeur(s)**: "+ ListUtils.listToString(Arrays.stream(getTeachers()).iterator())+"\n")
                .setColor(getColor())
                .setAuthor(source.getName(), source.getURL(), source.getIconURL());
        return embedCrafter.build();
    }

    public DataSource getSource() {
        return source;
    }
}
