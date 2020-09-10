package best.sti2d.therese.database;

public class DatabaseCredentials {

    private String host;
    private String user;
    private String password;
    private String databaseName;
    private int port;

    public DatabaseCredentials(String host, String user, String password, String databaseName, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
        this.port = port;
    }

    public String toURI(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);
        return stringBuilder.toString();
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getPort() {
        return port;
    }
}
