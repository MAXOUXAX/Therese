package best.sti2d.therese;

import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.listeners.DiscordListener;
import best.sti2d.therese.utils.ConfigurationManager;
import best.sti2d.therese.utils.ErrorHandler;
import best.sti2d.therese.utils.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

public class Therese implements Runnable{

    private static Therese instance;
    private static JDA jda;
    private final CommandMap commandMap;
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger;
    private final ErrorHandler errorHandler;

    private ConfigurationManager configurationManager;

    private boolean running;
    private final String version;

    public Therese() throws LoginException, IllegalArgumentException, NullPointerException, IOException, InterruptedException {
        instance = this;
        this.logger = new Logger();

        this.errorHandler = new ErrorHandler();

        String string = new File(Therese.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        string = string.replaceAll("BouloBOT-", "")
                .replaceAll("-jar-with-dependencies", "")
                .replaceAll(".jar", "");
        this.version = string;

        this.configurationManager = new ConfigurationManager();

        logger.log(Level.INFO, "--------------- STARTING ---------------");

        logger.log(Level.INFO, "> Generated new BOT instance");
        logger.log(Level.INFO, "> BOT thread started, loading libraries...");
        this.commandMap = new CommandMap();
        logger.log(Level.INFO, "> Libraries loaded! Loading JDA...");

        loadDiscord();
        logger.log(Level.INFO, "> JDA loaded!");

        logger.log(Level.INFO, "> The BOT is now good to go !");
        logger.log(Level.INFO, "--------------- STARTING ---------------");
    }

    private void loadDiscord() throws LoginException, InterruptedException {
        jda = JDABuilder.create(configurationManager.getStringValue("botToken"), GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES)
                .build();
        jda.addEventListener(new DiscordListener(commandMap));
        jda.getPresence().setActivity(Activity.playing("Amazingly powerful"));
        jda.awaitReady();
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public JDA getJda() {
        return jda;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                commandMap.discordCommandConsole(nextLine);
            }
        }

        jda.getPresence().setActivity(Activity.playing("Arrêt en cours..."));
        logger.log(Level.INFO, "--------------- STOPPING ---------------");
        logger.log(Level.INFO, "> Shutdowning...");
        scanner.close();
        logger.log(Level.INFO, "> Scanner closed!");
        jda.shutdown();
        logger.log(Level.INFO, "> JDA shutdowned!");
        logger.save();
        logger.log(Level.INFO, "> Logger saved");
        logger.log(Level.INFO, "--------------- STOPPING ---------------");
        logger.log(Level.INFO, "Arrêt du BOT réussi");
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            Therese therese = new Therese();
            new Thread(therese, "bot").start();
        } catch (LoginException | IllegalArgumentException | NullPointerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public String getVersion() {
        return version;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public static Therese getInstance(){
        return instance;
    }

}
