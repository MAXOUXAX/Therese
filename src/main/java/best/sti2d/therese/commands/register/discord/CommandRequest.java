package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;

public class CommandRequest {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandRequest(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="request",type = Command.ExecutorType.ALL,power = 100,help = "request",example = "request")
    public void embed(TextChannel textChannel) throws IOException {
        therese.getPronoteManager().testRequest().forEach(messageEmbed -> {
            textChannel.sendMessage(messageEmbed).queue();
        });
    }

}
