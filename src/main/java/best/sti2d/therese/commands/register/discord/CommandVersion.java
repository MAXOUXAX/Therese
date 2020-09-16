package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandVersion {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandVersion(CommandMap commandMap) {
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="version",type= Command.ExecutorType.USER,description="Affiche les informations sur la version du BOT", help = "version", example = "version")
    private void version(MessageChannel channel){
        try{
            EmbedCrafter builder = new EmbedCrafter();
            builder.setTitle("Thérèse • by MAXOUXAX • Amazingly powerful", therese.getConfigurationManager().getStringValue("websiteUrl"));
            builder.setColor(3447003);
            builder.addField("Je suis en version", therese.getVersion(), true);
            builder.addField("Je gère", commandMap.getDiscordCommands().size()+" commandes Discord", true);
            channel.sendMessage(builder.build()).queue();
        }catch (Exception e) {
            therese.getErrorHandler().handleException(e);
            channel.sendMessage("An error occured > " + e.getMessage()).queue();
        }
    }

}
