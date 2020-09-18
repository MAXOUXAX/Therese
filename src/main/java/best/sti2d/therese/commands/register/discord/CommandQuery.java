package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.pronote.PronoteManager;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.io.IOException;

public class CommandQuery {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandQuery(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="query",type = Command.ExecutorType.ALL,power = 1000,help = "query <query>",example = "query {timetable(from: \"2020-09-19\") {from to subject room teacher color status isAway isCancelled}}")
    public void query(TextChannel textChannel, String[] args) throws IOException {
        PronoteManager pronoteManager = new PronoteManager();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        JSONObject query = new JSONObject();
        query.put("query", stringBuilder.toString());
        JSONObject result = pronoteManager.makeRequest("/graphql", query);
        textChannel.sendMessage("Raw response:\n`"+result.toString()+"`").queue();
    }

}
