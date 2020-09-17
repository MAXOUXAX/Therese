package best.sti2d.therese;

import best.sti2d.therese.commands.CommandMap;
import best.sti2d.therese.database.DatabaseManager;
import best.sti2d.therese.listeners.DiscordListener;
import best.sti2d.therese.pronote.PronoteManager;
import best.sti2d.therese.utils.ConfigurationManager;
import best.sti2d.therese.utils.ErrorHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Therese implements Runnable{

    private static Therese instance;
    private static JDA jda;
    private final CommandMap commandMap;
    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger;
    private final ErrorHandler errorHandler;
    private PronoteManager pronoteManager;
    private final ConfigurationManager configurationManager;

    private boolean running;
    private final String version;

    public Therese() throws LoginException, IllegalArgumentException, NullPointerException, IOException, InterruptedException, SQLException {
        instance = this;
        this.logger = org.slf4j.LoggerFactory.getLogger(Therese.class);
        this.errorHandler = new ErrorHandler();

        DatabaseManager.initDatabaseConnection();

        String string = new File(Therese.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        string = string.replaceAll("Therese-", "")
                .replaceAll(".jar", "");
        this.version = string;

        this.configurationManager = new ConfigurationManager();

        logger.info("--------------- STARTING ---------------");

        logger.info("> Generated new BOT instance");
        logger.info("> BOT thread started, loading libraries...");
        this.commandMap = new CommandMap();
        logger.info("> Libraries loaded! Loading JDA...");

        loadDiscord();
        logger.info("> JDA loaded! Loading Pronote...");

        loadPronote();
        logger.info("> Pronote loaded!");

        logger.info("> The BOT is now good to go !");
        logger.info("--------------- STARTING ---------------");
    }

    private void loadPronote() {
        this.pronoteManager = new PronoteManager();
        try {
            pronoteManager.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        jda.getPresence().setActivity(Activity.playing(configurationManager.getStringValue("gameName")));
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
        logger.info("--------------- STOPPING ---------------");
        logger.info("> Shutdowning...");
        scanner.close();
        logger.info("> Scanner closed!");
        jda.shutdown();
        logger.info("> JDA shutdowned!");
        DatabaseManager.closeDatabaseConnection();
        logger.info("> Closed database connection!");
        logger.info("--------------- STOPPING ---------------");
        logger.info("Arrêt du BOT réussi");
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            Therese therese = new Therese();
            new Thread(therese, "therese").start();
        } catch (LoginException | IllegalArgumentException | NullPointerException | IOException | InterruptedException | SQLException e) {
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

    public PronoteManager getPronoteManager() {
        return pronoteManager;
    }
}
