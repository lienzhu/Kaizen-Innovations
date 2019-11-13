/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kaizen;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import kaizen.DataModels.timesheetsDM;
import kaizen.UserData.KaizenDatabase;

/**
 * FXML Controller class
 *
 * @author wongad1
 */
public class EntriesScreenController implements Initializable {

    PageSwitchHelper pageSwitcher = new PageSwitchHelper();
    
    KaizenDatabase db = new KaizenDatabase();

    @FXML
    private ToggleButton handleKb;
    
    @FXML
    private ToggleButton deepFocus;
    @FXML
    private ToggleButton taskTracker;
    @FXML
    private ToggleButton timesheets;
    @FXML
    private ToggleButton dailyLearnings;
    @FXML
    private ToggleButton settings;
    @FXML
    private Button signOut;
    @FXML
    private Button update;
    
    @FXML
    private TableView<timesheetsDM> entriesView;  
    public static TableView<timesheetsDM> entriesView_2;
    
    @FXML
    private TableColumn<timesheetsDM, String> dateClm;    
    @FXML
    private TableColumn<timesheetsDM, String> categoryClm;
    @FXML
    private TableColumn<timesheetsDM, String> activityClm;
    @FXML
    private TableColumn<timesheetsDM, String> startClm;
    @FXML
    private TableColumn<timesheetsDM, String> endClm;
    @FXML
    private TableColumn<timesheetsDM, Number> durationClm;
    @FXML
    private TableColumn<timesheetsDM, String> descriptionClm;
    @FXML
    private Button backBtn;
    
    
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
     /*   entriesView.setOnMouseClicked((MouseEvent event) -> {
        if(event.getClickCount()>1){
            onEdit();
        }
    });*/
        entriesView_2 = entriesView;
        entriesView.setVisible(true);
        entriesView.setItems(this.getEntries());
        dateClm.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
        categoryClm.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
        activityClm.setCellValueFactory(cellData -> cellData.getValue().getActivityProperty());
        startClm.setCellValueFactory(cellData -> cellData.getValue().getStartProperty());
        endClm.setCellValueFactory(cellData -> cellData.getValue().getEndProperty());
        durationClm.setCellValueFactory(cellData -> cellData.getValue().getDurationProperty());
        descriptionClm.setCellValueFactory(cellData -> cellData.getValue().getDescProperty());
        
    }    
    public ObservableList<timesheetsDM> getEntries(){
        
        ObservableList<timesheetsDM> entries = FXCollections.observableArrayList();
        
        try {
            ResultSet rs = db.getResultSet("SELECT * FROM TIMESHEETS");
            
            while (rs.next()){
                entries.add(new timesheetsDM(rs.getString("ACTIVITY"), rs.getString("CATEGORYNAME"), rs.getString("DATE"), rs.getString("DESCR"), rs.getInt("DURATION"), rs.getString("START"), rs.getString("END")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DailyLearningsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FXCollections.observableArrayList(entries);
    }
    
  /*     
    public void onEdit(){
        if(entriesView.getSelectionModel().getSelectedItem()!=null){
            timesheetsDM ts = entriesView.getSelectionModel().getSelectedItem();
            dateClm.setText(ts.getDate());
            
       }
    }*/
    
    @FXML
    private void deleteRow(ActionEvent event){
        timesheetsDM selected = entriesView.getSelectionModel().getSelectedItem();
        
        try{
            db.insertStatement("DELETE FROM TIMESHEETS WHERE DATE = '"+ selected.getDate()+"' "
                    + "AND CATEGORYNAME = '"+ selected.getCategory()+"'"
                    + " AND DESC = '"+selected.getDesc()+"'");
        } catch (Exception e) {
            System.out.println("Can't delete from database!");
            e.printStackTrace();
        }
        try{
            entriesView.getItems().removeAll(entriesView.getSelectionModel().getSelectedItem());
        } catch(Exception e){
            System.out.println("can't remove from table");
            e.printStackTrace();
        }
    }
    @FXML
    private void editRow(ActionEvent event){
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("EditEntriesPopUp.fxml"));

                try {
                    Loader.load();
                } catch (IOException ex) {
                 ex.printStackTrace();

                    Logger.getLogger(EntriesScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

                EditEntriesPopUpController a = Loader.getController();
                a.setData(""+entriesView.getSelectionModel().getSelectedItem().getDate(),
                        entriesView.getSelectionModel().getSelectedItem().getActivity(), 
                        entriesView.getSelectionModel().getSelectedItem().getStart(), 
                        entriesView.getSelectionModel().getSelectedItem().getEnd(), 
                        entriesView.getSelectionModel().getSelectedItem().getDuration(), 
                        entriesView.getSelectionModel().getSelectedItem().getDesc(),
                        ""+entriesView.getSelectionModel().getSelectedItem().getCategory());
                Parent p = Loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.show();



            }

        
    
    
    @FXML
    private void handleKbBoard(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event, "KanbanBoard.fxml");
            }
    @FXML
    private void handleDeepFocus(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"DeepFocusMode.fxml");  
    }
    @FXML
    private void handleTaskTracker(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"TaskTracker.fxml");//TO CHANGE WHEN PAGE IS MADE
    }
        
    @FXML
    private void handleAbout(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"AboutScreen.fxml");
    }
    
    @FXML
    private void handleSettings(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"Settings.fxml"); //TO CHANGE WHEN PAGE IS MADE
    }
    @FXML
    private void handleSignOut(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"LoginScreen.fxml");
    }
    @FXML
    private void handleLearnings(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"DailyLearnings.fxml");   
}
    @FXML
    private void handleTimeSheets(ActionEvent event) throws IOException{
        pageSwitcher.switcher(event,"PieChart.fxml");
    }
}

