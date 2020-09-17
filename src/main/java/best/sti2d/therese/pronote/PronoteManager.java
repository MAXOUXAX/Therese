package best.sti2d.therese.pronote;

import best.sti2d.therese.Therese;
import best.sti2d.therese.utils.ConfigurationManager;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PronoteManager {

    private final Therese therese;
    private final String pronoteServerUrl;
    private String token;

    public PronoteManager() {
        therese = Therese.getInstance();
        pronoteServerUrl = Therese.getInstance().getConfigurationManager().getStringValue("pronote_server_url");
    }

    public void connect() throws IOException {
        ConfigurationManager configurationManager = therese.getConfigurationManager();
        URL url = new URL(pronoteServerUrl+"/auth/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", configurationManager.getStringValue("pronote_url"));
        jsonObject.put("username", configurationManager.getStringValue("pronote_username"));
        jsonObject.put("password", configurationManager.getStringValue("pronote_password"));
        jsonObject.put("cas", configurationManager.getStringValue("pronote_cas"));

        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream()))
        {
            out.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = in.readLine()) != null)
            {
                content.append(line).append(System.lineSeparator());
            }
        }

        connection.disconnect();
        JSONObject finalObject = new JSONObject(content.toString());
        System.out.println("finalObject.toString() = " + finalObject.toString());

        token = finalObject.getString("token");
    }

    public ArrayList<MessageEmbed> testRequest() throws IOException {
        URL url = new URL(pronoteServerUrl+"/graphql");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Token", token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "{timetable(from: \"2020-09-18\") {from to subject room}}");

        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream()))
        {
            out.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = in.readLine()) != null)
            {
                content.append(line).append(System.lineSeparator());
            }
        }

        connection.disconnect();
        JSONObject finalObject = new JSONObject(content.toString());
        System.out.println("finalObject.toString() = " + finalObject.toString());
        JSONArray jsonArray = finalObject.getJSONObject("data").getJSONArray("timetable");
        ArrayList<MessageEmbed> messageEmbedList = new ArrayList<>();
        jsonArray.forEach(o -> {
            JSONObject element = (JSONObject) o;
            System.out.println("element.toString() = " + element.toString());
            String subject = element.getString("subject");
            String room = element.isNull("room") ? "-/-" : element.getString("room");
            Date from = new Date(element.getLong("from"));
            Date to = new Date(element.getLong("to"));

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

            EmbedCrafter embedCrafter = new EmbedCrafter();
            embedCrafter.setTitle(subject+" - "+new SimpleDateFormat("dd/MM").format(from))
                    .setDescription("**Salle:** "+room+"\n"+formatter.format(from)+" » "+formatter.format(to));
            messageEmbedList.add(embedCrafter.build());
        });
        return messageEmbedList;
    }

}
