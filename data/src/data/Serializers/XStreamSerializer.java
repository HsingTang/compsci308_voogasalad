package data.Serializers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The XStreamSerializer class is a sample implementation of the Serializer interface and serves as a wrapper of the
 * XStream class, providing the ability to serialize and deserialize objects to and from XML. The XStreamSerializer
 * is what the GameDataManager currently uses as its Serializer
 */
public class XStreamSerializer implements Serializer {

    private XStream mySerializer;

    /**
     * XStreamSerializer constructor initializes XStream instance variable with new DomDriver
     */
    public XStreamSerializer() {
        mySerializer = new XStream(new DomDriver());
    }

    /**
     * Serializes an object to XML and returns the string of raw XML
     * @param objectToSerialize the object that should be serialized
     * @return the raw XML of the serialized object
     */
    @Override
    public String serialize(Object objectToSerialize) {
        return mySerializer.toXML(objectToSerialize);
    }

    /**
     * Deserializes an object that is currently in raw XML
     * @param serializedObject string representation of the serialized object
     * @return a deserialized object from raw XML
     */
    @Override
    public Object deserialize(String serializedObject) {
        return mySerializer.fromXML(serializedObject);
    }
}
