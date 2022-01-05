/*
 * Name: Availability modelling and analysis
 * Date: 04 Jan 2020
 * Objectives:
 * A program that can visualize the dependency patterns of an application.
 * Add or remove the different types of nodes, and save the structure to a database.
 * Load a structure from the database.
 * Rotate the resulting structure by 180 degrees.
 * Select some or all elements and save them to a text file.
 * Development environment:
 * NetBeans 8.0.2
 * JDK 1.8 (64-bit)
 * JavaFX Scene Builder 8.5.0
 * MySql 8.0.18 community edition

 */
package node;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class to visualize the dependency patterns of an application
 */
public class Node extends Application {
    
    static int  global_dependency_node_counter;
    static int  local_dependency_node_counter;
    private String data = null;
    private String identifier = null;
    private String node_type = null;
    private String link_type = null;
    
    private ArrayList<Node> children = new ArrayList<>();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
//        stage.setMaximized(true);
        stage.getIcons().add(new Image("file:node_icon.png"));
        stage.setTitle("Availability Modelling and Analysis");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public ArrayList<Node> all_children(){
        return this.children;
    
    }
    
    public void get_all_children(String appender){
        
        System.out.println(appender + this.identifier + appender + this.data);
        ArrayList<Node>items =  this.all_children();
        for(int x = 0; x < items.size();x++)
        {
            items.get(x).get_all_children(appender + appender);
        }
    }
    
    
    public Node(String data,String identifier,String node_type,String link_type){
        this.data = data;
        this.identifier = identifier;
        this.node_type = node_type;
        this.link_type = link_type;
    }
    
    public Node(){
     
    }
    
    public Node add_child(Node child){

        this.children.add(child);
        return child;
        
    }
    
    public void remove_specific_node(String node_id_to_remove,boolean flag){
        
        if(flag)
            return;
        ArrayList<Node>items =  this.all_children();
        for(int x = 0; x < items.size();x++)
        {
            if(items.get(x).identifier.equals(node_id_to_remove))
            {
                this.all_children().remove(items.get(x));
                flag = true;
                return;
            }
            else
            {
                items.get(x).remove_specific_node(node_id_to_remove,flag);    
            }
            
        }   
    }
    
    public boolean add_child_at_specific_position(String parent_node_identifier, String data,String new_node_identifier,String node_type,String link_type, boolean flag,boolean same_flag){
        ArrayList<Node> items =  this.all_children();
        if(this.identifier.equals(parent_node_identifier))
        {
//            check if the previous item is selected as child.. 
//            if yes then enter the old as new node identifier
            if(same_flag)
            {
                this.children.add(new Node(data,new_node_identifier,node_type,link_type));
                flag = true;
                    return flag;
            }
            else
            {
//                check if the node is parent with size 2... 
                if(parent_node_identifier.length() ==  2)
                {
                    if(node_type.equals("Dependency node"))
                    {
                        global_dependency_node_counter++;
                        String first_four_characters_of_parent_node = parent_node_identifier.substring(0,2);
                        String last_two_characters_of_parent_node = parent_node_identifier.substring(parent_node_identifier.length()-2, parent_node_identifier.length());
                        new_node_identifier = first_four_characters_of_parent_node + last_two_characters_of_parent_node + "GD" + String.valueOf(global_dependency_node_counter);
                    }
                    else
                    {
                            //                    if yes... then check if it already has member
                        if(this.all_children().size()>0)
                        {
    //                        then check how many resource nodes are there... 
    //                        becuase resource nodes count are maintained separately
                            int count = 0;
                            for(int x= 0; x < this.all_children().size();x++)
                            {
    //                          check if the children nodes has other resource groups
                                if(this.all_children().get(x).node_type.equals("Resource group node"))
                                {
                                    count++;
                                }
                            }
    //                        if resource node... adding G and count of G from previous for loop
    //                        also with A 
                            if(node_type.equals("Resource group node")){
                                new_node_identifier =  parent_node_identifier + "G" + String.valueOf(count + 1 ) + "A" +  String.valueOf(this.all_children().size());
                            }
                            else
                            {
                                new_node_identifier =  parent_node_identifier + "A" +  String.valueOf(this.all_children().size()+1);
                            }

                        }
                        else
                        {
    //                        if the root node does not have children then simply add G1 or A1
                            if(node_type.equals("Resource group node")){
                                new_node_identifier =  parent_node_identifier + "G1" + "A1";
                            }
                            else
                            {
                                new_node_identifier =  parent_node_identifier + "A1";
                            }

                        }
                    }

                }
                else
                {
                    if(node_type.equals("Dependency node"))
                    {
                        if(this.node_type.equals("Application node") || this.node_type.equals("Resource group node"))
                        {
                            global_dependency_node_counter++;
                            String first_four_characters_of_parent_node = parent_node_identifier.substring(0,4);
                            String last_two_characters_of_parent_node = parent_node_identifier.substring(parent_node_identifier.length()-2, parent_node_identifier.length());
                            new_node_identifier = first_four_characters_of_parent_node + last_two_characters_of_parent_node + "GD" + String.valueOf(global_dependency_node_counter);
                        }
                        else if(this.node_type.equals("Simple resource node") || this.node_type.equals("Weak resource node"))
                        {
                            local_dependency_node_counter++;
                            String first_four_characters_of_parent_node = parent_node_identifier.substring(0,4);
                            String last_two_characters_of_parent_node = parent_node_identifier.substring(parent_node_identifier.length()-2, parent_node_identifier.length());
                            new_node_identifier = first_four_characters_of_parent_node + last_two_characters_of_parent_node + "LD" + String.valueOf(local_dependency_node_counter);
                        }

                        else
                        {
                            local_dependency_node_counter++;
                            String first_four_characters_of_parent_node = parent_node_identifier.substring(0,4);
                            String last_two_characters_of_parent_node = parent_node_identifier.substring(parent_node_identifier.length()-3, parent_node_identifier.length());
                            new_node_identifier = first_four_characters_of_parent_node + last_two_characters_of_parent_node + "LD" + String.valueOf(local_dependency_node_counter);
                        }
                        

                    }
                    else
                    {
//                              if not root node
                        if(this.all_children().size()>0)
                        {
//                                  same logic for g count
                            if(node_type.equals("Resource group node")){

                                int count = 0;
                                for(int x= 0; x < this.all_children().size();x++)
                                {
//                                  check if the children nodes has other resource groups
                                    if(this.all_children().get(x).node_type.equals("Resource group node"))
                                    {
                                        count++;
                                    }
                                }
    //                            simply increase last value of last child.. lets say A3 then A4 
    //                            with G for resource
                                String next_2nd_last_character = String.valueOf((char)(parent_node_identifier.charAt(parent_node_identifier.length()-2) + 1));
                                String new_next_value = next_2nd_last_character + "1";
                                new_next_value = new_next_value.substring(0, new_next_value.length()-1)+ (char)('1' + this.all_children().size());
                                new_node_identifier = parent_node_identifier + "G" + String.valueOf(count + 1)+ new_next_value;
                            }
                            else
                            {
//                              Check if node is resource
                                if(this.get_node_type().equals("Resource group node"))
                                {
//                                  parent node is resource
                                    int count = 0;
                                    for(int x= 0; x < this.all_children().size();x++)
                                    {
    //                                  check if the children nodes has other resource groups
                                        if(this.all_children().get(x).node_type.equals("Simple resource node") ||this.all_children().get(x).node_type.equals("Weak resource node"))
                                        {
                                            count++;
                                        }
                                    }
        //                            simply increament last value of last child.. lets say A3 then A4 
        //                            with G for resource
                                String next_alphabet_of_parent_node = String.valueOf((char)(parent_node_identifier.charAt(parent_node_identifier.length()-2) + 1));
                                new_node_identifier = parent_node_identifier + next_alphabet_of_parent_node + String.valueOf(count+1);
                                }
                                else
                                {
//                                    here node is simple or weak and parent also simple or weak
                                    int count=0;
                                    for(int x= 0; x < this.all_children().size();x++)
                                    {
    //                                  check if the children nodes has other resource groups
                                        if(this.all_children().get(x).node_type.equals("Simple resource node") ||this.all_children().get(x).node_type.equals("Weak resource node"))
                                        {
                                            count++;
                                        }
                                    }
                                    
                                    String next_alphabet_of_parent_node = String.valueOf((char)(parent_node_identifier.charAt(parent_node_identifier.length()-2) + 1));
                                    new_node_identifier = parent_node_identifier + next_alphabet_of_parent_node + String.valueOf(count+1);
                                }
                            }

                        }
                        else{
    //                        if parent does not have child... then increment 2nd last character
    //                        which is alphabet
                            if(node_type.equals("Resource group node")){
                                String next_2nd_last_character = String.valueOf((char)(parent_node_identifier.charAt(parent_node_identifier.length()-2) + 1));
                                String new_next_value = next_2nd_last_character + "1";
                                new_node_identifier = parent_node_identifier +  "G1" + new_next_value;
                            }
                            else
                            {
                                String next_2nd_last_character = String.valueOf((char)(parent_node_identifier.charAt(parent_node_identifier.length()-2) + 1));
                                String new_next_value = next_2nd_last_character + "1";
                                new_node_identifier = parent_node_identifier + new_next_value;
                            }

                        }
                    } 
                }
                
                this.children.add(new Node(data,new_node_identifier,node_type,link_type));
                flag = true;
                return flag;
            }
        }
        else
        {
            for(int x = 0; x < items.size();x++)
            {
                flag =  items.get(x).add_child_at_specific_position(parent_node_identifier,data,new_node_identifier,node_type,link_type,flag,same_flag);
                if(flag)
                    return flag;
                    
            }
        }
        return flag;
    }
    
    
    
    
    public String get_data()
    {
        return this.data;
    }
    
    public String get_identifier()
    {
        return this.identifier;
    }
    
    public void set_identifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    public String get_node_type()
    {
        return this.node_type;
    }
    
    public String get_link_type()
    {
        return this.link_type;
    }
    public void setGDN(){
        global_dependency_node_counter = 0;
    }
    public void setLDN(){
        local_dependency_node_counter = 0;
    }
    
}
