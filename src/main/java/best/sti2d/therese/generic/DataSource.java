package best.sti2d.therese.generic;

import best.sti2d.therese.Therese;

public enum DataSource {

    PRONOTE("Pronote", "pronote_url", "pronote_icon"),
    MONBUREAUNUMERIQUE("Mon bureau numérique", "mbn_url", "mbn_icon")
    ;

    private String name, configIcon, url;

    DataSource(String name, String url, String configIcon) {
        this.name = name;
        this.url = url;
        this.configIcon = configIcon;
    }

    public String getName() {
        return name;
    }

    public String getConfigIcon() {
        return configIcon;
    }

    public String getIconURL(){
        return Therese.getInstance().getConfigurationManager().getStringValue(configIcon);
    }

    public String getURL() {
        return Therese.getInstance().getConfigurationManager().getStringValue(url);
    }

}
