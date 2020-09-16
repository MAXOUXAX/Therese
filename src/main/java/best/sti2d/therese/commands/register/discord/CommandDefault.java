package best.sti2d.therese.commands.register.discord;

import best.sti2d.therese.Therese;
import best.sti2d.therese.commands.Command;
import best.sti2d.therese.commands.CommandMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class CommandDefault {

    private final Therese therese;
    private final CommandMap commandMap;

    public CommandDefault(CommandMap commandMap){
        this.commandMap = commandMap;
        this.therese = Therese.getInstance();
    }

    @Command(name="stop",type= Command.ExecutorType.CONSOLE)
    private void stop(){
        therese.setRunning(false);
    }

    @Command(name="power",power=150, description = "Permet de définir le power d'un utilisateur", example = ".power 150 @MAXOUXAX", help = ".power <power> <@user>")
    private void power(User user, MessageChannel channel, Message message, String[] args){
        MessageEmbed helperEmbed = commandMap.getHelpEmbed("power");
        if(args.length == 0 || message.getMentionedUsers().size() == 0){
            channel.sendMessage(helperEmbed).queue();
        }
        int power = 0;
        try{
            power = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            channel.sendMessage(helperEmbed).queue();
        }

        User target = message.getMentionedUsers().get(0);
        commandMap.setUserPower(target, power);
        channel.sendMessage("Le power de "+target.getAsMention()+" est maintenant de "+power).queue();
    }

    @Command(name="game",power=100,description = "Permet de modifier le jeu du BOT.", help = ".game <jeu>", example = ".game planter des tomates")
    private void game(TextChannel textChannel, JDA jda, String[] args){
        MessageEmbed helperEmbed = commandMap.getHelpEmbed("game");
        if(args.length == 0){
            textChannel.sendMessage(helperEmbed).queue();
        }else {
            StringBuilder builder = new StringBuilder();
            for (String str : args) {
                if (builder.length() > 0) builder.append(" ");
                builder.append(str);
            }

            jda.getPresence().setActivity(Activity.playing(builder.toString()));
        }
    }

    @Command(name="delete",power=50,description = "Permet de nettoyer un nombre x de message du salon", example = ".delete 50", help = ".delete <nombre de message>")
    private void delete(TextChannel textChannel, JDA jda, String[] args){
        if (getInt(args[0]) <= 100) {
            List<Message> msgs;
            MessageHistory history = new MessageHistory(textChannel);
            msgs = history.retrievePast(getInt(args[0])).complete();
            textChannel.deleteMessages(msgs).queue();
            textChannel.sendMessage("Suppression de " + args[0] + " messages terminée !").queue();
        }
    }

    private int getInt(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (Exception e) {
            return 0;
        }

    }

    @Command(name = "info",description = "Permet d'obtenir des informations sur un membre",type = Command.ExecutorType.USER, help = ".info <@user>", example = ".info @Maxx_#2233")
    private void info(User user, Guild guild, TextChannel textChannel, String[] args, Message message){
        Member member;

        if (args.length > 0) {
            member = guild.getMember(message.getMentionedUsers().get(0));
        } else {
            member = message.getMember();
        }

        String name = member.getEffectiveName();
        String tag = member.getUser().getName() + "#" + member.getUser().getDiscriminator();
        String guildJoinDate = member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String discordJoinDate = member.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String id = member.getUser().getId();
        String status = member.getOnlineStatus().getKey();
        String roles = "";
        String game;
        String avatar = member.getUser().getAvatarUrl();

        try {
            game = member.getActivities().get(0).getName();
        } catch (Exception e) {
            game = "-/-";
        }

        for ( Role r : member.getRoles() ) {
            roles += r.getName() + ", ";
        }
        if (roles.length() > 0)
            roles = roles.substring(0, roles.length()-2);
        else
            roles = "Aucun rôle.";

        if (avatar == null) {
            avatar = "Pas d'avatar";
        }

        EmbedBuilder em = new EmbedBuilder().setColor(Color.GREEN);
        em.setDescription(":spy:   **Informations sur " + member.getUser().getName() + ":**")
                .addField("Nom", name, true)
                .addField("Tag", tag, true)
                .addField("ID", id, true)
                .addField("Statut", status, true)
                .addField("Joue à", game, true)
                .addField("Rôles", roles, true)
                .addField("A rejoint le serveur le", guildJoinDate, true)
                .addField("A rejoint discord le", discordJoinDate, true)
                .addField("URL de l'avatar", avatar, true);
        em.setFooter(therese.getConfigurationManager().getStringValue("embedFooter"), therese.getConfigurationManager().getStringValue("embedIconUrl"));
        if (!avatar.equals("Pas d'avatar")) {
            em.setThumbnail(avatar);
        }

        textChannel.sendMessage(
                em.build()
        ).queue();
    }

    @Command(name = "ping", description = "Permet de récupérer le ping du bot", type = Command.ExecutorType.USER, example = ".ping", help = ".ping")
    private void ping(TextChannel textChannel, User user, Guild guild){
        long ping = guild.getJDA().getGatewayPing();
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("DiscordAPI ping", therese.getConfigurationManager().getStringValue("websiteUrl"))
                .setThumbnail(user.getAvatarUrl()+"?size=256")
                .addField(new MessageEmbed.Field("Ping", ping+"ms", true))
                .setFooter(therese.getConfigurationManager().getStringValue("embedFooter"), therese.getConfigurationManager().getStringValue("embedIconUrl"));
        if(ping > 300) {
            builder.setColor(Color.RED);
            builder.setDescription("Mauvais ping");
        }else{
            builder.setColor(Color.GREEN);
            builder.setDescription("Bon ping");
        }
        textChannel.sendMessage(builder.build()).queue();
    }

    @Command(name = "sendrules", description = "Permet d'envoyer les règles dans le salon destiné.", type = Command.ExecutorType.CONSOLE)
    private void sendRules(){
        EmbedBuilder embedBuilder1 = new EmbedBuilder()
                .setImage(therese.getConfigurationManager().getStringValue("rulesBanner")+"?size=1000")
                .setColor(2895667);
        EmbedBuilder embedBuilder2 = new EmbedBuilder()
                .setTitle("Règles")
                .setColor(15105570)
                .setThumbnail(therese.getConfigurationManager().getStringValue("rulesEmbedThumbnail"))
                .setDescription(":eight_pointed_black_star:️ 1 - Pas d'insulte\n:eight_pointed_black_star:️ 2 - Pas de majuscules abusives ou de spam/flood\n:eight_pointed_black_star:️ 3 - Pas de propos racistes, sexistes ou homophobes\n:eight_pointed_black_star:️ 4 - Aucune image à caractère raciste ou de type sexuelle (que ce soit en photo de profil ou en message)\n:eight_pointed_black_star:️ 5 - Pas d'appellation de jeux à caractère sexuel, violent ou raciste\n:eight_pointed_black_star:️ 6 - Pas de pseudos incorrects ou remplis d'émoticônes ou de caractères spéciaux empêchant de vous mentionner");
        EmbedBuilder embedBuilder3 = new EmbedBuilder()
                .setTitle("Modération")
                .setThumbnail(therese.getConfigurationManager().getStringValue("rulesEmbedThumbnailModeration"))
                .setColor(3066993)
                .setDescription("Avant de pouvoir accèder à la totalité des fonctionnalités du Discord, vous devrez valider nos règles. Une fois celle-ci validées, vous aurez accès à l'ensemble du serveur et acceptez que notre équipe de modération peut vous sanctionner à tout moment si vous les enfreinez.");
        EmbedBuilder embedBuilder4 = new EmbedBuilder()
                .setTitle("Attention !")
                .setColor(15158332)
                .setThumbnail(therese.getConfigurationManager().getStringValue("rulesAttentionThumbnailUrl"))
                .setDescription("Avant de pouvoir accèder au Discord, nous souhaitons nous assurer que vous acceptiez nos règles.\n\nÊtes-vous sûr d'accepter notre règlement ?\n\nSi vous ne respectez pas une règle, vous recevrez un avertissement.\n\nAu bout de 3 avertissements, vous serez banni définitivement du serveur.\n\n**DE PLUS**, si vous commetez une sanction très grave, l'équipe de modération se réserve le droit de vous bannir directement, sans avertissement.")
                .setFooter("Ajoutez une réaction afin de rejoindre le serveur", therese.getConfigurationManager().getStringValue("rulesAttentionFooterIconUrl")+"?size=256");
        TextChannel textChannel = Objects.requireNonNull(therese.getJda()
                .getGuildById(therese.getConfigurationManager().getStringValue("guildId")))
                .getTextChannelById(therese.getConfigurationManager().getStringValue("rulesTextChannelId"));
        if(textChannel == null){
            therese.getErrorHandler().handleException(new Exception("textChannel == null (the textchannel id or the guildid (or both) may not have been set in the config file)"));
        }else {
            textChannel.sendMessage(embedBuilder1.build()).queue();
            textChannel.sendMessage(embedBuilder2.build()).queue();
            textChannel.sendMessage(embedBuilder3.build()).queue();
            textChannel.sendMessage(embedBuilder4.build()).queue(message -> message
                    .addReaction(Objects.requireNonNull(textChannel.getGuild().getEmoteById(therese.getConfigurationManager().getStringValue("rulesAcceptEmoteId")))).queue());
        }
    }
}