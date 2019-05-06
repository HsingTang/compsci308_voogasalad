package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Carrie Hunner
 * This class was created for the purpose of arranging asset files based on name, as the database has one table.
 * Each node is capable of storing a name as well as list of its children that are files (these are leaf nodes)
 * and its children that are TreeNodes.
 * The String name associated with the Node is used for different display purposes.
 */
public class TreeNode {
    private String myName;
    private Set<File> myFileChildren;
    private Set<TreeNode> myTreeNodeChildren;

    /**
     * @param name String associated with the TreeNode
     */
    public TreeNode(String name){
        myName = name;
        myFileChildren = new HashSet<>();
        myTreeNodeChildren = new HashSet<>();
    }

    /**
     * Adds a TreeNode child
     * @param child TreeNode to be a child of this node
     */
    public void addChild(TreeNode child){
        myTreeNodeChildren.add(child);
    }

    /**
     * Adds a File to be a leaf node child of this node
     * @param child
     */
    public void addChild(File child){
        myFileChildren.add(child);
    }


    /**
     * Goes to the indicated next child treenode, note if the child doesn't exist, it creates a new one and returns that
     * @param nextNodeName name of the next node
     * @return TreeNode of the name entered
     */
    public TreeNode next(String nextNodeName){
        for(TreeNode treeNode : myTreeNodeChildren){
            if (treeNode.getName().equals(nextNodeName)){
                return treeNode;
            }
        }
        TreeNode newTreeNode = new TreeNode(nextNodeName);
        this.addChild(newTreeNode);
        return newTreeNode;
    }

    /**
     * @return String name associated with this node
     */
    public String getName(){
        return  myName;
    }

    /**
     * Gets the File leaf node children associated with this node
     * @return List<File>
     */
    public List<File> getFileChildren(){
        List<File> fileList = new ArrayList<>(myFileChildren);
        return Collections.unmodifiableList(fileList);
    }

    /**
     * Gets the TreeNode children associated with this node
     * @return List<TreeNode>
     */
    public List<TreeNode> getNodeChildren(){
        List<TreeNode> nodeList = new ArrayList<>(myTreeNodeChildren);
        return Collections.unmodifiableList(nodeList);
    }
}
