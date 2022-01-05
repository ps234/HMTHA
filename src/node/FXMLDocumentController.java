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

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * Controller class for handling the mouse and keyboard input.
 */


class Circle1
{
    int x_pos;
    int y_pos;
    
}

public class FXMLDocumentController implements Initializable {
    
    Node node;
    
    @FXML
    Label myMessage;
    
    @FXML
    Circle myCircle;
    
    @FXML
    TextField project_name;
    
    @FXML
    TextField node_name;
    
    @FXML
    ComboBox node_identifier;
    
    @FXML
    TextField application_id;
    
    @FXML
    ComboBox parent_node;
    
    @FXML
    Pane my_pane;
    
    @FXML
    Pane my_pane1;
    
    @FXML
    Button add_node_btn;
    
    @FXML
    MenuButton node_type;
    
    @FXML
    Tab tab_2;
    
    @FXML
    Tab tab_1;
    
    @FXML
    MenuButton link_type;
    
    @FXML
    Button btn_to_save_to_database;
    
    @FXML
    Button remove;
    
    @FXML
    TabPane tab_pane;
    
    @FXML
    Button hide_btn;
    
    
    
    int hide_even_odd_count = 0;
    
    
    
    
    ArrayList<String> array = new  ArrayList<>();
    
    int radius = 40;
    
    int gap_between_circles = radius*3+10;
    
    boolean hide_identifier = false;
    
    
    HashMap<String, Circle1> obj = new HashMap<>();
    HashMap<String, Circle1> obj_pane_2 = new HashMap<>();
    
    String node_id;
    
    Connection connection;
    
    String text_line_for_text_file = "";
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    //set node_type combo_box text from menuitem
    @FXML
    private void node_type_function(ActionEvent event){
        node_type.setText(((MenuItem)event.getSource()).getText());
    }
    
    //set link_type combo_box text from menuitem
    @FXML
    private void link_type_function(ActionEvent event){
        link_type.setText(((MenuItem)event.getSource()).getText());
    }
    
    //set link_type combo_box text from menuitem
    @FXML
    private void function_node_identifier(ActionEvent event){
        if(node_identifier.getValue().toString().equals("Existing Node Identifiers")){
            node_identifier.getSelectionModel().clearSelection();
        }
    }
    
    
    
    
    //set link_type combo_box text from menuitem
    @FXML
    private void rotate(ActionEvent event){
        my_pane1.setRotate(180+my_pane1.getRotate());
        for (javafx.scene.Node node : my_pane1.getChildren()) {
            if (node instanceof Text) {
                ((Text)node).setRotate(180 + ((Text)node).getRotate());
            }
            if (node instanceof Label) {
                ((Label)node).setRotate(180 + ((Label)node).getRotate());
            }
        }
    }
    
    @FXML
    private void open_pdf(ActionEvent event){
        File file = new File("./AvailabilityModelling.pdf");
        if(Desktop.isDesktopSupported()){
                new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    @FXML
    private void hide(ActionEvent event){
        my_pane1.getChildren().stream().filter((node_3) -> (node_3 instanceof Label)).forEachOrdered((node_2) -> {
            if(hide_even_odd_count %2 == 0)
            {
                ((Label)node_2).setVisible(false);       
            }
            else
            {
                ((Label)node_2).setVisible(true);
            }
        });
        if(hide_even_odd_count %2 != 0)
        {
            hide_btn.setText("Hide Identifier");   
        }
        else
        {
            hide_btn.setText("Show Identifier");
        }
        hide_even_odd_count++;
    }
    
    
    
    @FXML
    private void function_to_save_to_database(){
        if(project_name.getText().equals(""))
        {
                // create a alert 
                Alert a = new Alert(Alert.AlertType.WARNING); 
                a.setTitle("Enter Project Name");
                a.setContentText("Enter Project Name");
                a.show();
        }
        else
        {
            String query = "";
            query = "Delete * from node where project_name = '"+ project_name.getText()+ "'";
            try {
                Statement st = connection.createStatement();
                // execute the query, and get a java resultset
                st.executeQuery(query);
            } catch (Exception e) {
            }

            save_in_database(node,"",project_name.getText());
                        // create a alert 
            Alert a = new Alert(Alert.AlertType.CONFIRMATION); 
            a.setTitle("Saved in Database Successfully");
            a.setContentText("Saved in Database Successfully");
            a.show();
            
        }
        
        
        
    }
    
    public void save_in_database(Node node,String parent,String project_name){
        
        String query = "";
        try {

            if(node.get_node_type().equals("Application node"))
            {
                query = "insert into node (identifier,node_name,project_name,node_type) values ('"+  node.get_identifier()+ "','"+node.get_data()+"','"+project_name+"', 'Application node')";
            }
            else
            {
                query = "insert into node (identifier,parent_id,node_type,node_name,project_name) values ('" + node.get_identifier()
                + "', '" + parent + "','" + node.get_node_type() + "','"+ node.get_data()+"','"+project_name+"')";
            }
        Statement st = connection.createStatement();
        st.executeUpdate(query);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Node>items =  node.all_children();
        for(int x = 0; x < items.size();x++)
        {
            save_in_database(items.get(x),node.get_identifier(),project_name);
        }
    }
    
    @FXML
    private void remove_node(){
        String node_id_to_remove = node_identifier.getValue().toString();
        node.remove_specific_node(node_id_to_remove,false);
        obj.clear();
        parent_node.getItems().clear();
        node_identifier.getItems().clear();
        array.clear();
        node_identifier.getItems().add("Existing Node Identifiers");
        get_all_identfiers_in_node(node);
        get_node();
        
    }
    
    @FXML
    private void function_to_load_to_database(){
        
        application_id.setDisable(true);
        node_name.setDisable(false);
        node_identifier.setDisable(false);
        link_type.setDisable(false);
        node_type.setDisable(false);
        parent_node.setDisable(false);
        add_node_btn.setDisable(false);
//        btn_to_save_to_database.setDisable(true);
//        remove.setDisable(true);
        project_name.setDisable(true);
        
        if(project_name.getText().equals(""))
        {
                // create a alert 
                Alert a = new Alert(Alert.AlertType.WARNING); 
                a.setTitle("Enter Project Name");
                a.setContentText("Enter Project Name");
                a.show();
        }
        else
        {
            
            String query = "Select * from node where project_name = '"+project_name.getText()+"'";
            
            try {
                Statement st = connection.createStatement();
                ResultSet rs;

                // execute the query, and get a java resultset
                rs = st.executeQuery(query);
                
                while ( rs.next() ) {
                    String identifier = rs.getString("identifier");
                    String parent_id = rs.getString("parent_id");
                    String node_type_from_database = rs.getString("node_type");
                    String node_name_from_database = rs.getString("node_name");
                    System.out.println(identifier);
                    if(node_type_from_database.equals("Application node"))
                    {
                        node = new Node(node_name_from_database,identifier,node_type_from_database,"");   
                    }
                    else
                    {
                        if(node_type_from_database.equals("Resource group node"))
                        {
                            node.add_child_at_specific_position(parent_id, node_name_from_database,
                            application_id.getText()+"G1A1", node_type_from_database,link_type.getText(),false,false);

                        }
                        else
                        {
                            node.add_child_at_specific_position(parent_id, node_name_from_database,
                            application_id.getText()+"A1", node_type_from_database,link_type.getText(),false,false);
                        }
                    }
                }
                get_all_identfiers_in_node(node);
                get_node();
            } catch (Exception e) {
            }
        }
        
        
    }
    
    @FXML
    private void reset_all_fields(ActionEvent event)
    {
        reset();
    }
    
    public void reset(){
        project_name.setText("");
        application_id.setText("");
        node_name.setText("");
        node = new Node();
        my_pane.getChildren().clear();
        my_pane1.getChildren().clear();
        obj.clear();
        node.setGDN();
        node.setLDN();
        application_id.setDisable(false);
        node_name.setDisable(false);
        node_identifier.setDisable(false);
        link_type.setDisable(false);
        parent_node.setDisable(false);
        add_node_btn.setDisable(false);
        btn_to_save_to_database.setDisable(false);
//        remove.setDisable(true);
        project_name.setDisable(false);
        
        parent_node.getItems().clear();
        node_identifier.getItems().clear();
        array.clear();
    }
    
    
    @FXML
    private void tab_selected(Event event){
        if(tab_1 != null && tab_2 != null){
            if(tab_1.isSelected()){
                obj.clear();
                get_node();
            }
            if(tab_2.isSelected()){
                    my_pane1.getChildren().clear();
                    obj_pane_2.clear();
                    get_node_pane_2();
                    my_pane1.setRotate(0);

                for (javafx.scene.Node node : my_pane1.getChildren()) {
                    if (node instanceof Circle) {
                        ((Circle)node).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                            
                            if(((Circle)node).getStroke().toString().equals("0xff0000ff"))
                            {

                            }
                            else
                            {
                                ((Circle)node).setStroke(javafx.scene.paint.Color.RED);
                                text_line_for_text_file = text_line_for_text_file + "{" +((Circle)node).getId() + "}\n";
                            }
                            
                        });                   
                    }
                    
                    if (node instanceof Ellipse) {
                        ((Ellipse)node).addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                            ((Ellipse)node).setStroke(javafx.scene.paint.Color.RED);
                            if(((Ellipse)node).getStroke().toString().equals("0xff0000ff"))
                            {

                            }
                            else
                            {
                                ((Circle)node).setStroke(javafx.scene.paint.Color.RED);
                                text_line_for_text_file = text_line_for_text_file +  "{" + ((Ellipse)node).getId()+ "}\n";
                            }
                            
                        });                   
                    }
                }
            }
        }
    }
    
    @FXML
    private void save_to_text_fie(ActionEvent event){
        File selected_file = null;
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        File file;
        if(selectedFile == null){
            file = new File("./model.txt");    
        }
        else
        {
            file = new File(selectedFile.getAbsolutePath());
        }

        try {
            PrintWriter out = new PrintWriter(file);
            out.println("{\n<Application>\n"+ text_line_for_text_file + "}");
            out.close();
            Alert a = new Alert(Alert.AlertType.CONFIRMATION); 
            a.setTitle("Links Saved in File");
            a.setContentText("Links Saved in File"+file.getName());
            a.show();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    
    //populate node identifiers in parent node and node identifier combo box
    public void get_all_identfiers_in_node(Node node)
    {
        
        if(!array.contains(node.get_identifier()) )
        {
            array.add(node.get_identifier());
            parent_node.getItems().add(node.get_identifier());
            node_identifier.getItems().add(node.get_identifier());
        }   
        ArrayList<Node> items =  node.all_children();
        for(int x = 0; x < items.size();x++)
        {
            if(items.get(x).all_children().isEmpty())
            {
                if(!array.contains(items.get(x).get_identifier()))
                {
                    array.add(items.get(x).get_identifier());
                    parent_node.getItems().add(items.get(x).get_identifier());
                    node_identifier.getItems().add(items.get(x).get_identifier());
                }   
            }
            else
            {
                
                get_all_identfiers_in_node(items.get(x));
            }
        }
    }
    
    
    
//    add node function
    @FXML
    private void add_node(ActionEvent event) {
        obj.clear();
        if(node.get_data() == null)
        {
            
//            checking if application text is empty
            if(application_id.getText().equals(""))
            {
                // create a alert 
                Alert a = new Alert(Alert.AlertType.WARNING); 
                a.setTitle("Enter Application id");
                a.setContentText("Enter Application id");
                a.show();
            }
            else
            {
//              if not then set node type to application node and link to empty
//              and make fields appear
                node = new Node(node_name.getText(),application_id.getText(),"Application node","");
                node_type.setDisable(false);
                link_type.setDisable(false);
                node_identifier.setDisable(false);
                parent_node.setDisable(false);
                application_id.setDisable(true);
                
                get_node();
            }
        }
        else
        {
//            check if not the starting node then whether parent is selected and node types and link types are selected
            if(parent_node.getValue() == null)
            {
                // create a alert 
                Alert a = new Alert(Alert.AlertType.WARNING); 
                a.setTitle("Select parent identifier");
                a.setContentText("Select parent identifier");
                a.show();
                
            }
            else
            {
//                if existing node is not selected
                if(node_identifier.getValue() == null)
                {
//                    checking if node type or link type is selected or not
                    if(link_type.getText().equals("Choose Link Type"))
                    {
                        // create a alert 
                        Alert a = new Alert(Alert.AlertType.WARNING); 
                        a.setTitle("Select Node Type");
                        a.setContentText("Select Node Type");
                        a.show();
                    }
                    else if(link_type.getText().equals("Choose Node Type"))
                    {
                        // create a alert 
                        Alert a = new Alert(Alert.AlertType.WARNING); 
                        a.setTitle("Select Node Type");
                        a.setContentText("Select Node Type");
                        a.show();
                    }
                    else
                    {
                        if(node_type.getText().equals("Resource group node"))
                        {
                            node.add_child_at_specific_position(parent_node.getValue().toString(), node_name.getText(),
                            application_id.getText()+"G1A1", node_type.getText(),link_type.getText(),false,false);
                            get_node();
                        }
                        else
                        {
                            node.add_child_at_specific_position(parent_node.getValue().toString(), node_name.getText(),
                            application_id.getText()+"A1", node_type.getText(),link_type.getText(),false,false);
                            get_node();
                        }
                        
                    }
                    
                }
                else
                {
//                  if an existing identifier is selected

                    node_id = node_identifier.getValue().toString();

//                  add the selected node again to make the link with new parent
//                  but will not output circles

//                  if everything is selected then adding the node to specific parent is done using parent identifier
                    node.add_child_at_specific_position(parent_node.getValue().toString(), node_name.getText(),
                    node_id ,node_type.getText(),link_type.getText(),false,true);
                    get_node();

                }

            }
            
        }
        node.get_all_children("    ");
        
        parent_node.getItems().clear();
        node_identifier.getItems().clear();
        array.clear();
        node_identifier.getItems().add("Existing Node Identifiers");
        get_all_identfiers_in_node(node);
    }
    
    
    
    private void get_node_pane_2()
    {

//        display circe nodes for application node and then calling p_s function for sub nodes
        if(node.get_data() != null)
        {
            int x_pos = radius+10;
        int y_pos = radius+10;
        
        
        Circle circle = new Circle(radius,javafx.scene.paint.Color.TRANSPARENT);
        circle.setStroke(javafx.scene.paint.Color.BLUE);
        circle.setLayoutX(0);
        circle.setLayoutY(0);
        circle.setId(node.get_identifier() + "null");
        circle.getStrokeDashArray().addAll(2d);
        
        Text label1 = new Text(node.get_identifier());
        
        Label label = new Label(node.get_identifier());
        label.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
        label.setLayoutY(y_pos-radius/2);
        

        TranslateTransition translate_transition = new  TranslateTransition();
        translate_transition.setDuration(Duration.seconds(2));
        translate_transition.setNode(circle);
        translate_transition.setToX(x_pos);
        translate_transition.setToY(y_pos);
        translate_transition.play();
        
        
        Text label_node_name = new Text(node.get_data());
        label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
        label_node_name.setLayoutY(y_pos+12);
        
        my_pane1.getChildren().add(label);
        my_pane1.getChildren().add(circle);
        my_pane1.getChildren().add(label_node_name);
        
//        remove 10 from y_pos because 10 was added for left space so that it does not align with border
        p_s_pane_2(node, x_pos, y_pos+gap_between_circles,x_pos,y_pos + y_pos-10,radius,gap_between_circles);
        }
        
    }
    
    
    public int p_s_pane_2(Node node,int x_pos,int y_pos,int previous_x,int previous_y,int radius,int gap_between_circles)
    {
        ArrayList<Node>items =  node.all_children();
        for(int x = 0; x < items.size() ; x++)
        {
            //variable for link text
            String raw_link_text;


            //append 'L' and then first 4 letters c1 g1 
            //and then append child to parent

            Text label1 = new Text(node.get_identifier());
            label1.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
            
            Label label = new Label(items.get(x).get_identifier());
            label.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
            label.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
            label.setLayoutY(y_pos-radius/3);
            my_pane1.getChildren().add(label);
            
            if(obj_pane_2.containsKey(items.get(x).get_identifier())){
                Line line = new Line(obj_pane_2.get(items.get(x).get_identifier()).x_pos +radius,
                        obj_pane_2.get(items.get(x).get_identifier()).y_pos -radius,previous_x,previous_y);
                my_pane1.getChildren().add(line);
                label.setVisible(false);
            }
            else
            {
                if(items.get(x).get_node_type().equals("Dependency node"))
                {                
                    //dependency node is an ellipse
                        Ellipse ellipse = new Ellipse(radius+10,radius-10);
                        ellipse.setStroke(javafx.scene.paint.Color.BLUE);
                        ellipse.setFill(null);
                        ellipse.setLayoutX(0);
                        ellipse.setLayoutY(0);
                        ellipse.setId(items.get(x).get_identifier() + " | " + node.get_identifier());
                        if(node.get_node_type().equals("Application node") || node.get_node_type().equals("Resource group node"))
                        {
                            ellipse.setStrokeWidth(5);   
                        }
                        TranslateTransition translate_transition = new  TranslateTransition();
                        translate_transition.setDuration(Duration.seconds(2));
                        translate_transition.setNode(ellipse);
                        translate_transition.setToX(x_pos);
                        translate_transition.setToY(y_pos);
                        translate_transition.play();
                        my_pane1.getChildren().add(ellipse);


    //                  line having previous values come from earlier function call
                        Line line = new Line(x_pos, y_pos - radius+10,previous_x,previous_y);
                        my_pane1.getChildren().add(line);


                        raw_link_text = "L"+ items.get(x).get_identifier().substring(0, 4) +
                        items.get(x).get_identifier().substring(items.get(x).get_identifier().length()-3, items.get(x).get_identifier().length()) +
                        node.get_identifier().substring(node.get_identifier().length()-2, node.get_identifier().length());

    //                    link text will be position to average of x and y of line
                        Text link_text = new Text(x_pos-(radius/2),(y_pos-radius + previous_y)/2,raw_link_text);
                        link_text.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
                        my_pane1.getChildren().add(link_text);
                        Circle1 ellipse_coordinates = new Circle1();

                        ellipse_coordinates.x_pos = x_pos;
                        ellipse_coordinates.y_pos = y_pos-20+(radius*2);
                        obj_pane_2.put(label.getText(), ellipse_coordinates);


                        Text label_node_name = new Text(items.get(x).get_data());
                        label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
                        label_node_name.setLayoutY(y_pos+12);
                        my_pane1.getChildren().add(label_node_name);

    //                    ellipse specific call... subtract 10 as for ellipse

                    if(!items.get(x).all_children().isEmpty())
                    {
                        x_pos =  p_s_pane_2(items.get(x), x_pos, y_pos+gap_between_circles,x_pos,y_pos+radius-10,radius,gap_between_circles);
                    }
                    else
                    {
                        x_pos = x_pos + gap_between_circles;
                    }
                }
                else
                {

                    Circle circle = new Circle(radius,javafx.scene.paint.Color.TRANSPARENT);
                    circle.setStroke(javafx.scene.paint.Color.BLUE);
                    circle.setLayoutX(0);
                    circle.setLayoutY(0);
                    circle.setId(items.get(x).get_identifier() + " | " + node.get_identifier());
                    if(items.get(x).get_node_type().equals("Weak resource node"))
                    {
                        circle.getStrokeDashArray().addAll(5d);
                    }
                    else if(items.get(x).get_node_type().equals("Resource group node"))
                    {
                        circle.setStrokeWidth(5);
                    }

                    TranslateTransition translate_transition = new  TranslateTransition();
                    translate_transition.setDuration(Duration.seconds(2));
                    translate_transition.setNode(circle);
                    translate_transition.setToX(x_pos);
                    translate_transition.setToY(y_pos);
                    translate_transition.play();
                    my_pane1.getChildren().add(circle);
                    Line line = new Line(x_pos,y_pos-radius,previous_x,previous_y);
                    my_pane1.getChildren().add(line);


                    raw_link_text = "L"+ items.get(x).get_identifier().substring(0, 4) +
                    items.get(x).get_identifier().substring(items.get(x).get_identifier().length()-2, items.get(x).get_identifier().length()) +
                    node.get_identifier().substring(node.get_identifier().length()-2, node.get_identifier().length());

                    Text link_text = new Text(x_pos-(radius/2),(y_pos-radius + previous_y)/2,raw_link_text);

                    link_text.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
                    my_pane1.getChildren().add(link_text);
                    Circle1 circle_coordinates = new Circle1();
                    circle_coordinates.x_pos = x_pos;
                    circle_coordinates.y_pos = y_pos+radius;
                    obj_pane_2.put(label.getText(), circle_coordinates);


                    Text label_node_name = new Text(items.get(x).get_data());
                    label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
                    label_node_name.setLayoutY(y_pos+12);
                    my_pane1.getChildren().add(label_node_name);

//                    if(tab_2.isSelected())
//                    {
//                        circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
//                            circle.setStroke(javafx.scene.paint.Color.RED);
//                            circle.getStrokeDashArray().addAll(5d);
//                        });                        
//                    }


                    if(!items.get(x).all_children().isEmpty())
                    {

                        //simple call for circle
                        x_pos =  p_s_pane_2(items.get(x), x_pos, y_pos+gap_between_circles,x_pos,y_pos+radius,radius -5 ,(radius - 5)*3+10);

                    }
                    else
                    {
                        x_pos = x_pos + gap_between_circles;
                    }
                }
            }
            

        }
        
        return x_pos;
    }
    
    
    private void get_node()
    {
        my_pane.getChildren().clear();
//        display circe nodes for application node and then call p_s function for sub nodes
        

        if(node.get_data() != null)
        {
            int x_pos = radius+10;
        int y_pos = radius+10;
        
        
        Circle circle = new Circle(radius,javafx.scene.paint.Color.TRANSPARENT);
        circle.setStroke(javafx.scene.paint.Color.BLUE);
        circle.setLayoutX(0);
        circle.setLayoutY(0);
        circle.setId(node.get_identifier() + "null");
        circle.getStrokeDashArray().addAll(2d);
        
        Text label1 = new Text(node.get_identifier());
        
        Label label = new Label(node.get_identifier());
        label.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
        label.setLayoutY(y_pos-radius/2);
        my_pane.getChildren().add(label);

        TranslateTransition translate_transition = new  TranslateTransition();
        translate_transition.setDuration(Duration.seconds(2));
        translate_transition.setNode(circle);
        translate_transition.setToX(x_pos);
        translate_transition.setToY(y_pos);
        translate_transition.play();
        my_pane.getChildren().add(circle);
        
        
        Text label_node_name = new Text(node.get_data());
        label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
        label_node_name.setLayoutY(y_pos+12);
        my_pane.getChildren().add(label_node_name);
        
//        remove 10 from y_pos because 10 was added for left space so that it does not align with border
        p_s(node, x_pos, y_pos+gap_between_circles,x_pos,y_pos + y_pos-10,radius,gap_between_circles);
        }
        
    }
    
    
    public int p_s(Node node,int x_pos,int y_pos,int previous_x,int previous_y,int radius,int gap_between_circles)
    {
        ArrayList<Node>items =  node.all_children();
        for(int x = 0; x < items.size() ; x++)
        {
            //variable for link text
            String raw_link_text;


            //append 'L' and then first 4 letters c1 g1 
            //and then append child to parent

            Text label1 = new Text(node.get_identifier());
            label1.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
            
            Label label = new Label(items.get(x).get_identifier());
            label.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
            label.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
            label.setLayoutY(y_pos-radius/3);
            my_pane.getChildren().add(label);
            
            if(obj.containsKey(items.get(x).get_identifier())){
                Line line = new Line(obj.get(items.get(x).get_identifier()).x_pos +radius,
                        obj.get(items.get(x).get_identifier()).y_pos -radius,previous_x,previous_y);
                my_pane.getChildren().add(line);
                label.setVisible(false);
            }
            else
            {
                if(items.get(x).get_node_type().equals("Dependency node"))
                {                
                    //dependency node is an ellipse
                        Ellipse ellipse = new Ellipse(radius+10,radius-10);
                        ellipse.setStroke(javafx.scene.paint.Color.BLUE);
                        ellipse.setFill(null);
                        ellipse.setLayoutX(0);
                        ellipse.setLayoutY(0);
                        ellipse.setId(items.get(x).get_identifier() + " | " + node.get_identifier());
                        if(node.get_node_type().equals("Application node") || node.get_node_type().equals("Resource group node"))
                        {
                            ellipse.setStrokeWidth(5);   
                        }
                        
                        TranslateTransition translate_transition = new  TranslateTransition();
                        translate_transition.setDuration(Duration.seconds(2));
                        translate_transition.setNode(ellipse);
                        translate_transition.setToX(x_pos);
                        translate_transition.setToY(y_pos);
                        translate_transition.play();
                        my_pane.getChildren().add(ellipse);


    //                  line having previous values coming from earlier function call
                        Line line = new Line(x_pos, y_pos - radius+10,previous_x,previous_y);
                        my_pane.getChildren().add(line);


                        if(node.get_node_type().equals("Dependency node"))
                        {
                            raw_link_text = "L"+ items.get(x).get_identifier().substring(0, 4) +
                            items.get(x).get_identifier().substring(items.get(x).get_identifier().length()-3, items.get(x).get_identifier().length()) +
                            node.get_identifier().substring(node.get_identifier().length()-3, node.get_identifier().length());

                        }
                        else
                        {
                            raw_link_text = "L"+ items.get(x).get_identifier().substring(0, 4) +
                            items.get(x).get_identifier().substring(items.get(x).get_identifier().length()-3, items.get(x).get_identifier().length()) +
                            node.get_identifier().substring(node.get_identifier().length()-2, node.get_identifier().length());    
                        }
                        

    //                    link text will be position to average of x and y of line
                        Text link_text = new Text(x_pos-(radius/2),(y_pos-radius + previous_y)/2,raw_link_text);
                        link_text.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
                        my_pane.getChildren().add(link_text);
                        Circle1 ellipse_coordinates = new Circle1();

                        ellipse_coordinates.x_pos = x_pos;
                        ellipse_coordinates.y_pos = y_pos-20+(radius*2);
                        obj.put(label.getText(), ellipse_coordinates);


                        Text label_node_name = new Text(items.get(x).get_data());
                        label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
                        label_node_name.setLayoutY(y_pos+12);
                        my_pane.getChildren().add(label_node_name);

    //                    ellipse specific call... subtracting 10 as for ellipse

                    if(!items.get(x).all_children().isEmpty())
                    {
                        x_pos =  p_s(items.get(x), x_pos, y_pos+gap_between_circles,x_pos,y_pos+radius-10,radius,gap_between_circles);
                    }
                    else
                    {
                        x_pos = x_pos + gap_between_circles;
                    }
                }
                else
                {

                    Circle circle = new Circle(radius,javafx.scene.paint.Color.TRANSPARENT);
                    circle.setStroke(javafx.scene.paint.Color.BLUE);
                    circle.setLayoutX(0);
                    circle.setLayoutY(0);
                    circle.setId(items.get(x).get_identifier() + " | " + node.get_identifier());
                    if(items.get(x).get_node_type().equals("Weak resource node"))
                    {
                        circle.getStrokeDashArray().addAll(5d);
                    }
                    else if(items.get(x).get_node_type().equals("Resource group node"))
                    {
                        circle.setStrokeWidth(5);
                    }

                    TranslateTransition translate_transition = new  TranslateTransition();
                    translate_transition.setDuration(Duration.seconds(2));
                    translate_transition.setNode(circle);
                    translate_transition.setToX(x_pos);
                    translate_transition.setToY(y_pos);
                    translate_transition.play();
                    my_pane.getChildren().add(circle);
                    Line line = new Line(x_pos,y_pos-radius,previous_x,previous_y);
                    my_pane.getChildren().add(line);


                    raw_link_text = "L"+ items.get(x).get_identifier().substring(0, 4) +
                    items.get(x).get_identifier().substring(items.get(x).get_identifier().length()-2, items.get(x).get_identifier().length()) +
                    node.get_identifier().substring(node.get_identifier().length()-2, node.get_identifier().length());

                    Text link_text = new Text(x_pos-(radius/2),(y_pos-radius + previous_y)/2,raw_link_text);

                    link_text.setFont((Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, radius/4)));
                    my_pane.getChildren().add(link_text);
                    Circle1 circle_coordinates = new Circle1();
                    circle_coordinates.x_pos = x_pos;
                    circle_coordinates.y_pos = y_pos+radius;
                    obj.put(label.getText(), circle_coordinates);


                    Text label_node_name = new Text(items.get(x).get_data());
                    label_node_name.setLayoutX(x_pos-label1.getLayoutBounds().getWidth()/2);
                    label_node_name.setLayoutY(y_pos+12);
                    my_pane.getChildren().add(label_node_name);



                    if(!items.get(x).all_children().isEmpty())
                    {

                        //simple call for circle
                        x_pos =  p_s(items.get(x), x_pos, y_pos+gap_between_circles,x_pos,y_pos+radius,radius -5 ,(radius - 5)*3+10);

                    }
                    else
                    {
                        x_pos = x_pos + gap_between_circles;
                    }
                }
            }
            

        }
        
        return x_pos;
    }
    
    @FXML
    private void input_change(InputEvent event) {
        
        project_name.getText();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        node = new Node();
        final int numCols = 5 ;
        final int numRows = 5 ;
        node_type.setDisable(true);
        link_type.setDisable(true);
        node_identifier.setDisable(true);
        parent_node.setDisable(true);
        
        
        try {

            File file = new File("./db.configuration"); 
  
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            String driver = String.valueOf(br.readLine()).split("=")[1];
            String db_url = String.valueOf(br.readLine()).split("=")[1];
            String username = String.valueOf(br.readLine()).split("=")[1];
            String password = String.valueOf(br.readLine()).split("=")[1];

            Class.forName(driver);
            connection = DriverManager.getConnection(db_url, username, password);
            
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
