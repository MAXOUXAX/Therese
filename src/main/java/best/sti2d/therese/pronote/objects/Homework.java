package best.sti2d.therese.pronote.objects;

import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Homework {

    private String subject;
    private String description;
    private Color color;
    private Date givenAt;
    private Date dueTo;
    private HashMap<String, String> files = new HashMap<>();

    public Homework(JSONObject element) {
        this.subject = element.getString("subject");
        this.description = element.getString("description");
        this.color = element.isNull("color") ? Color.WHITE : Color.decode(element.getString("color"));
        this.givenAt = new Date(element.getLong("givenAt"));
        this.dueTo = new Date(element.getLong("for"));
        JSONArray filesArray = element.getJSONArray("files");
        filesArray.forEach(o -> {
            JSONObject jsonObject = (JSONObject) o;
            files.put(jsonObject.getString("name"), jsonObject.getString("url"));
        });
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Date getGivenAt() {
        return givenAt;
    }

    public void setGivenAt(Date givenAt) {
        this.givenAt = givenAt;
    }

    public Date getDueTo() {
        return dueTo;
    }

    public void setDueTo(Date dueTo) {
        this.dueTo = dueTo;
    }

    public HashMap<String, String> getFiles() {
        return files;
    }

    public void setFiles(HashMap<String, String> files) {
        this.files = files;
    }

    public MessageEmbed toEmbed(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");

        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(getSubject()+" - Pour le "+formatter.format(getDueTo()))
                .setDescription(getDescription()+"\n" +
                        "**Donné le**: "+formatter.format(getGivenAt()))
                .setColor(getColor());
        return embedCrafter.build();
    }

}
