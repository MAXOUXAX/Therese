package best.sti2d.therese.pronote;

import best.sti2d.therese.Therese;
import best.sti2d.therese.utils.ConfigurationManager;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PronoteManager {

    private final Therese therese;
    private final String pronoteServerUrl;
    private final PronoteHelper pronoteHelper;
    private String token;

    public PronoteManager() {
        therese = Therese.getInstance();
        pronoteServerUrl = Therese.getInstance().getConfigurationManager().getStringValue("pronote_server_url");
        pronoteHelper = new PronoteHelper(this);
    }

    public void connect() throws IOException {
        ConfigurationManager configurationManager = therese.getConfigurationManager();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", configurationManager.getStringValue("pronote_url"));
        jsonObject.put("username", configurationManager.getStringValue("pronote_username"));
        jsonObject.put("password", configurationManager.getStringValue("pronote_password"));
        jsonObject.put("cas", configurationManager.getStringValue("pronote_cas"));

        JSONObject finalObject = makeRequest("/auth/login", jsonObject);
        System.out.println("finalObject.toString() = " + finalObject.toString());

        token = finalObject.getString("token");
        keepAlive();
    }

    private void keepAlive() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", "mutation { setKeepAlive(enabled: true) }");

        JSONObject finalObject = makeRequest("/graphql", jsonObject);
        System.out.println("finalObject.toString() = " + finalObject.toString());
    }

    public JSONObject makeRequest(String endpoint, JSONObject query) throws IOException {
        URL url = new URL(pronoteServerUrl+endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if(token != null)connection.setRequestProperty("Token", token);

        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream()))
        {
            out.write(query.toString().getBytes(StandardCharsets.UTF_8));
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
        return new JSONObject(content.toString());
    }

    public PronoteHelper getHelper() {
        return pronoteHelper;
    }
}
