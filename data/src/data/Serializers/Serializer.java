package data.Serializers;

/**
 * The Serializer interface has two methods, serialize and deserialize that should work as inverses of on another.
 * Classes that implement the serializer interface will likely serve as wrappers for a serialization tool such as
 * xstream or gson, but this interface provides the ability to change implementation decisions of what serializer to
 * use without affecting any of the surrounding code.
 */
public interface Serializer {

    /**
     * Serializes the object that is passed to it and returns a string of the object in serialized form
     * @param objectToSerialize the object that should be serialized
     * @return String representation of the serialized object
     */
    String serialize(Object objectToSerialize);

    /**
     * Deserializes the string representation of a serialized object and returns the object that needs to be cast
     * @param serializedObject string representation of the serialized object
     * @return a Java object that has been deserialized and needs to be cast
     */
    Object deserialize(String serializedObject);
}
