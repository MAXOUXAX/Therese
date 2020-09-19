package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CommandCours {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandCours(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="cours",type = Command.ExecutorType.ALL,power = 50,help = "cours <date>",example = "cours 29/09/2020")
    public void cours(TextChannel textChannel, String[] args) throws IOException {
        if (args.length == 0) {
            textChannel.sendMessage(commandMap.getHelpEmbed("cours")).queue();
        } else {
            String[] dateParts = args[0].split("/");
            String formattedDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            ArrayList<MessageEmbed> embeds = therese.getPronoteManager().getHelper().getLessonsEmbeds(formattedDate);
            if (!embeds.isEmpty()) {
                embeds.forEach(messageEmbed -> {
                    textChannel.sendMessage(messageEmbed).queue();
                });
            } else {
                textChannel.sendMessage(new EmbedCrafter()
                        .setTitle("Aucun contenu de cours du " + args[0])
                        .setDescription("Aucun contenu de cours n'a été trouvé pour cette journée!")
                        .setColor(Color.RED).build()).queue();
            }
        }
    }

}
