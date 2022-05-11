package models.browsers;

public enum BrowserType {
    CHROME("chrome"),
    CHROME_PROXY("chromeWithProxy"),
    FIREFOX("firefox");

    private String name;

    BrowserType(String name) {
        this.name = name;
    }

    public static BrowserType byName(String defaultBrowserName) {
        BrowserType found = null;

        for (BrowserType type : values()) {
            if (type.is(defaultBrowserName)) {
                found = type;
                break;
            }
        }

        if (found == null) {
            throw new IllegalArgumentException("Illegal Browser: " + defaultBrowserName);
        }

        return found;
    }

    public boolean is(String defaultBrowserName) {
        String lowerCasedTypeName = name.toLowerCase();
        String lowerCasedDefaultName = defaultBrowserName.toLowerCase();
        return lowerCasedTypeName.equals(lowerCasedDefaultName);
    }
}
