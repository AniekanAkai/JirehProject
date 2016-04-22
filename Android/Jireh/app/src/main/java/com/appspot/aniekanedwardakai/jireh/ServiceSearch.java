//package com.appspot.aniekanedwardakai.jireh;
//
///**
// * Created by Teddy on 1/21/2016.
// */
//import com.google.android.gms.drive.events.ChangeListener;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.ArrayList;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//
//
//
//public class ServiceSearch {
//
//    ObservableList<String> entries = FXCollections.observableArrayList();
//    ListView list = new ListView();
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Service Search");
//        Button btn = new Button();
//        btn.setText("Choose");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent event) {
//                System.exit(0);
//            }
//        });
//
//        TextField txt = new TextField();
//        txt.setPromptText("Search");
//        txt.textProperty().addListener(new ChangeListener() {
//            public void changed(ObservableValue observable,
//                                Object firstVal, Object nextVal) {
//                serviceSearchMethod((String) firstVal, (String) nextVal);
//            }
//        });
//
//        //List view setup
//        list.setMaxHeight(180);
//        // fills in the list's entries
//        for(int i = 0; i<100;i++)
//        {
//            entries.add("Item " + i);
//        }
//
//        entries.add("Chauffeur");
//        entries.add("Driving Instructor");
//        entries.add("Tutoring");
//        entries.add("Laundry");
//        entries.add("Cleaning");
//        entries.add("Moving");
//        entries.add("Barber");
//        entries.add("Cooking");
//        entries.add("Hairdresser");
//
//        list.setItems(entries);
//
//        VBox root = new VBox();
//        root.setPadding(new Insets(10,10,10,10));
//        root.setSpacing(2);
//        root.getChildren().addAll(txt,list,btn);
//        primaryStage.setScene(new Scene(root, 300,250));
//        primaryStage.show();
//
//    }
//
//
//    public void serviceSearchMethod(String firstVal, String nextVal) {
//
//        if (firstVal != null && (nextVal.length() < firstVal.length())) {
//            list.setItems(entries);
//        }
//
//        String[] parts = nextVal.toUpperCase().split(" ");
//
//        ObservableList<String> subentries = FXCollections.observableArrayList();
//        for (Object entry : list.getItems()) {
//            boolean match = true
//            String entryText = (String) entry;
//            for (String part : parts) {
//                if (entryText.toUpperCase().contain(part)) {
//                    match = false;
//                    break;
//                }
//            }
//
//            if (match) {
//                subentries.add(entryText);
//            }
//        }
//        list.setItems(subentries);
//    }
//}
