package best.sti2d.therese.monbureaunumerique;

import best.sti2d.therese.Therese;
import best.sti2d.therese.utils.ConfigurationManager;
import me.vinceh121.jkdecole.JKdecole;

import java.io.IOException;

public class MonBureauNumeriqueManager {

    private final Therese therese;
    private final MonBureauNumeriqueHelper monBureauNumeriqueHelper;
    private final JKdecole mbn;

    public MonBureauNumeriqueManager() {
        therese = Therese.getInstance();
        this.monBureauNumeriqueHelper = new MonBureauNumeriqueHelper(this);
        mbn = new JKdecole();
    }

    public void connect() throws IOException {
        ConfigurationManager configurationManager = therese.getConfigurationManager();

        mbn.setEndpoint("http://mobilite.monbureaunumerique.fr/mobilite/");
        mbn.setToken(configurationManager.getStringValue("mbn_token"));
        mbn.sendStarting();
    }

    public JKdecole getMbn() {
        return mbn;
    }

    public MonBureauNumeriqueHelper getHelper() {
        return monBureauNumeriqueHelper;
    }
}
