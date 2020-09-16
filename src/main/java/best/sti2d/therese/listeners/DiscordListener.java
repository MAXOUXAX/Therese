package best.sti2d.therese.listeners;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.utils.EmbedCrafter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class DiscordListener implements EventListener {

    private final CommandMap commandMap;
    private final Therese therese;

    public DiscordListener(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof MessageReceivedEvent) onMessage((MessageReceivedEvent)event);
        if(event instanceof PrivateMessageReceivedEvent) onDM((PrivateMessageReceivedEvent)event);
        if(event instanceof MessageReactionAddEvent) onReactionAdd((MessageReactionAddEvent)event);
        if(event instanceof MessageReactionRemoveEvent) onReactionRemove((MessageReactionRemoveEvent)event);
    }

    private void onReactionAdd(MessageReactionAddEvent event) {
    }

    private void onReactionRemove(MessageReactionRemoveEvent event) {
    }


    private void onMessage(MessageReceivedEvent event){
        if(event.getMessage().getAuthor().isBot()
                || event.getAuthor().equals(event.getJDA().getSelfUser())
                || event.getChannelType() == ChannelType.PRIVATE)return;

        String message = event.getMessage().getContentDisplay();
        if (message.startsWith(commandMap.getDiscordTag())) {
            message = message.replaceFirst(commandMap.getDiscordTag(), "");
            if (commandMap.discordCommandUser(event.getAuthor(), message, event.getMessage())) {
                event.getChannel().sendTyping().queue();
                event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }

    private void onDM(PrivateMessageReceivedEvent event){
        if(event.getAuthor().equals(event.getJDA().getSelfUser())) return;
        EmbedCrafter embedCrafter = new EmbedCrafter();
        embedCrafter.setColor(Color.RED.getRGB());
        embedCrafter.setTitle("Private message received of " + event.getAuthor().getName());
        embedCrafter.setThumbnailUrl(therese.getConfigurationManager().getStringValue("cancelIcon"));
        embedCrafter.setDescription("Cette action est **IMPOSSIBLE**");
        event.getChannel().sendMessage(embedCrafter.build()).queue();
    }

}
