package best.sti2d.therese.database;

import best.sti2d.therese.Therese;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseManager {

    public static DatabaseAccess databaseAccess;

    public static void initDatabaseConnection() throws Exception {
        DatabaseCredentials databaseCredentials;
        try(final Reader reader = Files.newBufferedReader(Paths.get("database.yml"), StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ClassLoader.getSystemClassLoader()));
            yaml.setBeanAccess(BeanAccess.FIELD);

            databaseCredentials = yaml.loadAs(reader, DatabaseCredentials.class);
            if(databaseCredentials.getHost().equalsIgnoreCase("none")){
                throw new Exception("Database is not configured in config.yml");
            }
            databaseAccess = new DatabaseAccess(databaseCredentials);
            databaseAccess.initPool();
        }catch (IOException e){
            Therese.getInstance().getErrorHandler().handleException(e);
        }

    }

    public static void closeDatabaseConnection(){
        databaseAccess.closePool();
    }

    public static DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }
}