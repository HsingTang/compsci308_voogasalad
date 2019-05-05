package ui;

import engine.external.Entity;
import engine.external.component.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Carrie Hunner
 * This class reads the XML's from the filepath in the default_types_factory.properties file.
 * It then can create components and then entities from those files.
 * It currently only works for components that take in Strings and any class that has a Class.parseClass(String s)
 * method. E.g Double.parseDouble
 *
 * This requires the files in the folder to have a .xml extension
 * This requires each file to define a category and a name and will not create any that are missing
 * either of these components
 */
public class DefaultTypeXMLReaderFactory {
    private static final ResourceBundle PATH_RESOURCES = ResourceBundle.getBundle("authoring_general");
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle("default_types_factory");
    private static final String FOLDER_PATH = PATH_RESOURCES.getString("xml_folder_filepath");
    private static final String EXTENSIONS = ".xml";
    private Map<String, String> myNameToCategory;
    private Map<String, Map<String, String>> myNameToComponents;    //Name of default -> (map from components names -> values)
    private List<Node> myRootsList;

    /**
     * Creates an instance of DefaultTypeXMLReaderFactory
     */
    public DefaultTypeXMLReaderFactory(){
        myNameToCategory = new HashMap<>();
        myNameToComponents= new HashMap<>();
        myRootsList = new ArrayList<>();
        fillRootsList();
        fillMaps();
    }

    /**
     * Gets the defaultNames/options associated with a category
     * @param category String of a category
     * @return List fo Strings of defaultNames for the category
     */
    public List<String> getDefaultNames(String category){
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, String> entry : myNameToCategory.entrySet()){
            if(entry.getValue().equals(category)){
                result.add(entry.getKey());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * @return List of possible categories
     */
    public List<String> getCategories(){
        Set<String> defaultCategories = new HashSet<>(myNameToCategory.values());
        List<String> result = new ArrayList<>(defaultCategories);
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    /**
     * @param name of the default type
     * @return String of its category
     */
    public String getCategory(String name){
        return myNameToCategory.get(name);
    }

    /**
     * This method uses the XML information read and compiled at the construction of an instance
     * and reflection to create each component listed in the XML and then creates and returns
     * an entity
     * @param name Name of a default entity desired. This name will come off of the list of
     *             names from the getDefaultNames() method
     * @return Entity for the desired default name passed in
     */
    public Entity createEntity(String name){
        Entity resultEntity = new Entity();
        if(myNameToComponents.containsKey(name)){
            Map<String, String> componentMap = myNameToComponents.get(name);
            for(Map.Entry<String, String> entry : componentMap.entrySet()){
                makeAndAddComponent(resultEntity, entry);
            }
        }
        else{
            makeAndDisplayError("NoXMLFile");
        }
        return resultEntity;
    }

    private void makeAndAddComponent(Entity resultEntity, Map.Entry<String, String> entry) {
        try {
            Class componentClass = Class.forName(PATH_RESOURCES.getString("component_folder_filepath") + entry.getKey());
            Constructor[] constructors = componentClass.getConstructors();
            Class constructorParamClassType = constructors[0].getParameterTypes()[0];
            Constructor constructor = componentClass.getConstructor(constructorParamClassType);
            Component component;
            //Tries assuming the component parameter is a String
            try {
                component = (Component) constructor.newInstance(entry.getValue());
            }
            //Tries assuming the component parameter can use Class.parseClass(String s) eg. Double.parseDouble(String s)
            catch (IllegalArgumentException e) {
                String[] brokenUpClass = constructorParamClassType.toString().split("\\.");
                String className = brokenUpClass[brokenUpClass.length-1];
                Class parseClass = Class.forName(constructorParamClassType.toString().split(" ")[1]);
                Method method = parseClass.getMethod((RESOURCES.getString("methodSuffix") + className), String.class);
                component = (Component) constructor.newInstance(method.invoke(this, entry.getValue()));
            }
            resultEntity.addComponent(component);
        } catch (Exception e) {
            makeAndDisplayError("ReflectionError");

        }
    }


    private void fillMaps() {
        for (int k = 0; k < myRootsList.size(); k++) {
            Element currentElement = (Element) myRootsList.get(k);
            Map<String, String> componentsMap = fillComponentsMap(currentElement);
            if (hasRequiredInformation(componentsMap, currentElement)) {
                String name = componentsMap.get("NameComponent");
                myNameToComponents.put(name, componentsMap);
                NodeList categoryList = currentElement.getElementsByTagName("Category");
                String category = categoryList.item(categoryList.getLength() - 1).getTextContent();
                myNameToCategory.put(name, category);
            }
        }
    }
    private void fillRootsList(){
        File assetFolder = new File(FOLDER_PATH);
        File[] files = assetFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(EXTENSIONS));
        for(File temp : files) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder;
                documentBuilder= documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(temp);
                document.getDocumentElement().normalize();
                Node root = document.getDocumentElement();
                myRootsList.add(root);
            } catch (Exception e) {
                makeAndDisplayError("XMLReadingError");
            }
        }
    }

    private boolean hasRequiredInformation(Map<String, String> componentsMap, Element root) {
        if(!componentsMap.containsKey("NameComponent")){
            makeAndDisplayError("NoName");
            return false;
        }
        else if(root.getElementsByTagName("Category").getLength() == 0){
            makeAndDisplayError("NoCategory");
            return false;
        }
        return true;
    }

    private Map<String, String> fillComponentsMap(Element root) {
        NodeList components = root.getElementsByTagName("Components");
        Map<String, String> componentsMap = new HashMap<>();
        for(int k = 0; k < components.getLength(); k++) {
            Node currentComponentList = components.item(k);
            NodeList subComponentsList = currentComponentList.getChildNodes();
            for (int i = 0; i < subComponentsList.getLength(); i++) {
                Node node = subComponentsList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    String componentName = node.getNodeName();
                    String componentValue = node.getTextContent();
                    componentsMap.put(componentName, componentValue);
                }
            }
        }
        return componentsMap;
    }

    private void makeAndDisplayError(String resourceKey){
        String[] info = RESOURCES.getString(resourceKey).split(",");
        ErrorBox errorBox = new ErrorBox(info[0], info[1]);
        errorBox.display();
    }
}
