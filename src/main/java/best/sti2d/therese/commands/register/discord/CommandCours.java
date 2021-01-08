package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.generic.Lesson;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
            ArrayList<Lesson> lessons = therese.getGenericHelper().getLessons(formattedDate);
            if (!lessons.isEmpty()) {
                lessons.forEach(lesson -> {
                    textChannel.sendMessage(lesson.toEmbed()).queue();
                    if (!lesson.getFiles().isEmpty()) {
                        lesson.getFiles().forEach((name, url) -> {
                            try {
                                InputStream fileUrl = new URL(name).openStream();
                                textChannel.sendFile(fileUrl, url).queue();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
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
