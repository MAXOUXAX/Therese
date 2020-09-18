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

public class CommandEmploiDuTemps {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandEmploiDuTemps(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="edt",type = Command.ExecutorType.ALL,power = 50,help = "edt <date>",example = "edt 29/09/2020")
    public void emploiDuTemps(TextChannel textChannel, String[] args) throws IOException {
        if (args.length == 0) {
            textChannel.sendMessage(commandMap.getHelpEmbed("edt")).queue();
        } else {
            String[] dateParts = args[0].split("/");
            String formattedDate = dateParts[2] +"-"+ dateParts[1] +"-"+ dateParts[0];
            ArrayList<MessageEmbed> embeds = therese.getPronoteManager().getHelper().getClassesEmbeds(formattedDate);
            if (!embeds.isEmpty()) {
                embeds.forEach(messageEmbed -> {
                    textChannel.sendMessage(messageEmbed).queue();
                });
            } else {
                textChannel.sendMessage(new EmbedCrafter()
                        .setTitle("Aucun cours pour le " + args[0])
                        .setDescription("Aucun cours n'a été trouvé pour cette journée!")
                        .setColor(Color.RED).build()).queue();
            }
        }
    }

}
