package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.commands.SimpleCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.entities.UserImpl;

public class HelpCommand {

    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Command(name="help",type= Command.ExecutorType.USER,description="Affiche l'entièreté des commandes disponibles", help = ".help", example = ".help")
    private void help(User user, MessageChannel channel, Guild guild){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Aide » Liste des commandes");
        builder.setColor(3447003);

        for(SimpleCommand command : commandMap.getDiscordCommands()){
            if(command.getExecutorType() == Command.ExecutorType.CONSOLE) continue;

            if(guild != null && command.getPower() > commandMap.getPowerUser(guild, user)) continue;

            builder.addField(command.getName(), command.getDescription(), true);
        }

        if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        ((UserImpl)user).getPrivateChannel().sendMessage(builder.build()).queue();

        channel.sendMessage(user.getAsMention()+", veuillez regarder vos message privés.").queue();

    }

}
