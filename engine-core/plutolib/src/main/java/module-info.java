module org.plutoengine.plutolib {
    requires java.base;
    requires java.desktop;

    requires transitive org.joml;
    requires transitive org.joml.primitives;

    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive com.fasterxml.jackson.annotation;

    requires transitive org.jetbrains.annotations;
    requires org.yaml.snakeyaml;
    requires org.apache.commons.lang3;

    opens org.plutoengine.address;

    exports org.plutoengine.address;
    exports org.plutoengine.util.color;
    exports org.plutoengine.io.property;
    exports org.plutoengine.math;
    exports org.plutoengine.event.lambda;
    exports org.plutoengine.chrono;

}