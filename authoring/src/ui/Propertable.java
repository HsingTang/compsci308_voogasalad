package ui;

import javafx.collections.ObservableMap;

/**
 * Interface to be applied to classes in the ByteMe Authoring Environment UI
 * that contain observable properties and are associated with an Enum class
 * @author Harry Ross
 */
public interface Propertable {

    /**
     * Returns ObservableMap of Propertable properties
     * @return Observable properties Map
     */
    ObservableMap<Enum, String> getPropertyMap();

    /**
     * Returns Enum class corresponding with a Propertable class
     * @return Enum class associated with Propertable
     */
    Class<? extends Enum> getEnumClass();

}
