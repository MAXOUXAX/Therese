package best.sti2d.therese.pronote;

import best.sti2d.therese.Therese;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            String subject = element.getString("subject");
            String room = element.isNull("room") ? "-/-" : element.getString("room");
            String teacher = element.isNull("teacher") ? "-/-" : element.getString("teacher");
            String color = element.isNull("color") ? "-/-" : element.getString("color");
            String status = element.isNull("status") ? "-/-" : element.getString("status");
            boolean isAway = !element.isNull("isAway") && element.getBoolean("isAway");
            boolean isCancelled = !element.isNull("isCancelled") && element.getBoolean("isCancelled");
            Date from = new Date(element.getLong("from"));
            Date to = new Date(element.getLong("to"));

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle(subject+" - "+new SimpleDateFormat("dd/MM").format(from))
                    .setDescription("**Salle:** "+room+"\n" +
                            "**Horaires**: "+formatter.format(from)+" » "+formatter.format(to)+"\n" +
                            "**Professeur**: "+teacher+"\n\n"+
                            "**Statut**: " + status)

                    .setColor(Color.decode(color));
            if(isAway || isCancelled) {
                embedCrafter.setTitle("**" + (isAway ? "ABSENT" : "ANNULÉ") + "** - ~~" + subject + "~~ - " + new SimpleDateFormat("dd/MM").format(from))
                        .setDescription("~~**Salle:** " + room + "~~\n" +
                                "~~**Horaires**: " + formatter.format(from) + " » " + formatter.format(to) + "~~\n" +
                                "~~**Professeur**: " + teacher + "~~\n\n" +
                                "**STATUT**: " + status)
                        .setColor(Color.RED);
            }

            messageEmbedList.add(embedCrafter.build());
        });
        return messageEmbedList;
    }

}
