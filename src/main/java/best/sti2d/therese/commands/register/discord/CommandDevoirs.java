package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.generic.Homework;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class CommandDevoirs {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandDevoirs(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="devoirs",type = Command.ExecutorType.ALL,power = 50,help = "devoirs <date>",example = "devoirs 29/09/2020")
    public void devoirs(TextChannel textChannel, String[] args) throws IOException, ParseException {
        if (args.length == 0) {
            textChannel.sendMessage(commandMap.getHelpEmbed("devoirs")).queue();
        } else {
            String[] dateParts = args[0].split("/");
            String formattedDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
            ArrayList<Homework> homeworks = therese.getGenericHelper().getHomeworks(formattedDate);
            if (!homeworks.isEmpty()) {
                homeworks.forEach(homework -> {
                    textChannel.sendMessage(homework.toEmbed()).queue();
                    if (!homework.getFiles().isEmpty()) {
                        homework.getFiles().forEach((name, url) -> {
                            try {
                                InputStream fileUrl = new URL(url).openStream();
                                textChannel.sendFile(fileUrl, name).queue();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            } else {
                textChannel.sendMessage(new EmbedCrafter()
                        .setTitle("Aucun devoir pour le " + args[0])
                        .setDescription("Aucun devoir à rendre n'a été trouvé pour cette journée!")
                        .setColor(Color.RED).build()).queue();
            }
        }
    }

}
