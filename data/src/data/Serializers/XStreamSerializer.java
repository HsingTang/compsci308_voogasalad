package data.Serializers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamSerializer implements Serializer {

    private XStream mySerializer;

    public XStreamSerializer() {
        mySerializer = new XStream(new DomDriver());
    }

    @Override
    public String serialize(Object objectToSerialize) {
        return mySerializer.toXML(objectToSerialize);
    }

    @Override
    public Object deserialize(String serializedObject) {
        return mySerializer.fromXML(serializedObject);
    }
}
