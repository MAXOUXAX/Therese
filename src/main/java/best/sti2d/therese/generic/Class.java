package best.sti2d.therese.generic;

import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Class {

    private String subject;
    private String room;
    private String teacher;
    private String status;
    private Color color;
    private boolean isAway;
    private boolean isCancelled;
    private Date from;
    private Date to;
    private DataSource source;

    public Class(JSONObject pronoteData) {
        this.subject = pronoteData.getString("subject");
        this.room = pronoteData.isNull("room") ? "-/-" : pronoteData.getString("room");
        this.teacher = pronoteData.isNull("teacher") ? "-/-" : pronoteData.getString("teacher");
        this.color = pronoteData.isNull("color") ? Color.WHITE : Color.decode(pronoteData.getString("color"));
        this.status = pronoteData.isNull("status") ? "-/-" : pronoteData.getString("status");
        this.isAway = !pronoteData.isNull("isAway") && pronoteData.getBoolean("isAway");
        this.isCancelled = !pronoteData.isNull("isCancelled") && pronoteData.getBoolean("isCancelled");
        this.from = new Date(pronoteData.getLong("from"));
        this.to = new Date(pronoteData.getLong("to"));
        this.source = DataSource.PRONOTE;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isAway() {
        return isAway;
    }

    public void setAway(boolean away) {
        isAway = away;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
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

    public MessageEmbed toEmbed(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dayMonth = new SimpleDateFormat("dd/MM");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        dayMonth.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        String time = formatter.format(getFrom())+" à "+formatter.format(getTo());
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(getSubject()+" - Séance du "+dayMonth.format(getFrom())+" de "+time)
                .setDescription("**Salle:** "+getRoom()+"\n" +
                        "**Professeur**: "+getTeacher()+"\n\n"+
                        "**Statut**: " + getStatus())

                .setColor(getColor())
                .setAuthor(source.getName(), source.getURL(), source.getIconURL());
        if(isAway() || isCancelled()) {
            embedCrafter.setTitle("**" + (isAway() ? "ABSENT" : "ANNULÉ") + "** - ~~" + getSubject() + "~~ - Séance du " + dayMonth.format(getFrom())+" de "+time)
                    .setDescription("~~**Salle:** " + getRoom() + "~~\n" +
                            "~~**Professeur**: " + getTeacher() + "~~\n\n" +
                            "**STATUT**: " + getStatus())
                    .setColor(Color.RED)
                    .setAuthor(source.getName(), source.getURL(), source.getIconURL());
        }
        return embedCrafter.build();
    }

    public DataSource getSource() {
        return source;
    }
}
