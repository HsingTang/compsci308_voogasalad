package data.Serializers;

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
