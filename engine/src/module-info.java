module engine {
    requires xstream;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires org.junit.jupiter.api;
    requires voogasalad_util;
    opens engine.external to xstream;
    exports engine.external;
    exports engine.external.component;

}
