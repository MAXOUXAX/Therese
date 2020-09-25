package best.sti2d.therese.pronote.objects;

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

    public Class(JSONObject element) {
        this.subject = element.getString("subject");
        this.room = element.isNull("room") ? "-/-" : element.getString("room");
        this.teacher = element.isNull("teacher") ? "-/-" : element.getString("teacher");
        this.color = element.isNull("color") ? Color.WHITE : Color.decode(element.getString("color"));
        this.status = element.isNull("status") ? "-/-" : element.getString("status");
        this.isAway = !element.isNull("isAway") && element.getBoolean("isAway");
        this.isCancelled = !element.isNull("isCancelled") && element.getBoolean("isCancelled");
        this.from = new Date(element.getLong("from"));
        this.to = new Date(element.getLong("to"));
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

        String time = formatter.format(getFrom())+" - "+formatter.format(getTo());
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setTitle(time+" - "+getSubject()+" - "+dayMonth.format(getFrom()))
                .setDescription("**Salle:** "+getRoom()+"\n" +
                        "**Horaires**: "+formatter.format(getFrom())+" » "+formatter.format(getTo())+"\n" +
                        "**Professeur**: "+getTeacher()+"\n\n"+
                        "**Statut**: " + getStatus())

                .setColor(getColor());
        if(isAway() || isCancelled()) {
            embedCrafter.setTitle("**" + (isAway() ? "ABSENT" : "ANNULÉ") + "** - ~~" + getSubject() + "~~ - " + dayMonth.format(  getFrom()))
                    .setDescription("~~**Salle:** " + getRoom() + "~~\n" +
                            "~~**Horaires**: " + formatter.format(getFrom()) + " » " + formatter.format(getTo()) + "~~\n" +
                            "~~**Professeur**: " + getTeacher() + "~~\n\n" +
                            "**STATUT**: " + getStatus())
                    .setColor(Color.RED);
        }
        return embedCrafter.build();
    }

}
