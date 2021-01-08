package best.sti2d.therese.generic;

import best.sti2d.therese.utils.EmbedCrafter;
import me.vinceh121.jkdecole.entities.homework.HWDay;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Homework {

    private String subject;
    private String description;
    private Color color;
    private Date givenAt;
    private Date dueTo;
    private HashMap<String, String> files = new HashMap<>();
    private DataSource source;

    public Homework(me.vinceh121.jkdecole.entities.homework.Homework monBureauNumeriqueData, HWDay hwDay) {
        this.subject = monBureauNumeriqueData.getSubject()+" | "+monBureauNumeriqueData.getType();
        this.description = monBureauNumeriqueData.getTitle();
        this.color = Color.WHITE;
        this.dueTo = hwDay.getDate();
        this.givenAt = monBureauNumeriqueData.getDate();
        this.source = DataSource.MONBUREAUNUMERIQUE;
    }

    public Homework(JSONObject pronoteData) {
        this.subject = pronoteData.getString("subject");
        this.description = pronoteData.getString("description");
        this.color = pronoteData.isNull("color") ? Color.WHITE : Color.decode(pronoteData.getString("color"));
        this.givenAt = new Date(pronoteData.getLong("givenAt"));
        this.dueTo = new Date(pronoteData.getLong("for"));
        if(!pronoteData.isNull("files")) {
            JSONArray filesArray = pronoteData.getJSONArray("files");
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
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(getSubject()+" - Pour le "+formatter.format(getDueTo()))
                .setDescription("```"+getDescription()+"```\n\n" +
                        "**Donné le**: "+formatter.format(getGivenAt()))
                .setColor(getColor())
                .setAuthor(source.getName(), source.getURL(), source.getIconURL());
        return embedCrafter.build();
    }

    public DataSource getSource() {
        return source;
    }
}
