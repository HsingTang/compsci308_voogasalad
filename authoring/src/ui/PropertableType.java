package ui;

/**
 * Enum class to formally define types of Propertable objects for use in ByteMe Authoring UI with corresponding labels for display
 * @author Harry Ross
 */
public enum PropertableType {

    OBJECT("Entity", "object_properties_list"),
    INSTANCE("Instance", "instance_properties_list"),
    LEVEL("Level", "level_properties_list");

    private String myLabel;
    private String myPropFile;

    /**
     * Creates new PropertableType enum with specified display label and properties file path
     * @param label Display label
     * @param propFile Properties file path for PropertiesPane population
     */
    PropertableType(String label, String propFile) {
        myLabel = label;
        myPropFile = propFile;
    }

    /**
     * Returns properties file path for Propertable enum that defines the contents of its display in a PropertiesPane
     * @return Properties file path
     */
    public String getPropFile() {
        return myPropFile;
    }

    @Override
    public String toString() {
        return myLabel;
    }
}
