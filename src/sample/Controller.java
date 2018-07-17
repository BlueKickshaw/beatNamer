package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;

public class Controller {
    private Stage stage;
    private Scene scene;
    private static Controller controller;
    private File folder;
    private ObservableList<File> observableList;

    @FXML
    private Button rnAllBtn;

    @FXML
    private void rnAll(){
        for (File file : folder.listFiles()) {
            renameSong(file);
        }
    }

    @FXML
    private Button rnSelectedBtn;

    @FXML
    private void rnSelected(){
        File selected = getSelected();
        renameSong(selected);
    }

    @FXML
    private Button chooseFolderBtn;

    @FXML
    private void chooseFolder(){
        folder = new DirectoryChooser().showDialog(scene.getWindow());
        folderText.setText(folder.getAbsolutePath());
        observableList = FXCollections.observableList(Arrays.asList(folder.listFiles()));

        TreeItem<File> root = new TreeItem<>(folder);
        treeView.setRoot(root);
        root.setExpanded(true);

        for (File file : folder.listFiles()) {
            root.getChildren().add(new TreeItem(file));
        }
    }

    @FXML
    private TreeView<File> treeView;


    @FXML
    private TextField folderText;

    @FXML
    private void initialize() {
        controller = this;
    }

    public static Controller getInstance(){
        return controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
    }

    private File getSelected(){
        return treeView.getSelectionModel().getSelectedItem().getValue();
    }

    private void renameSong(File selected){
        if (selected != null) {
            boolean infoExists = false;
            File info = null;

            for (File file : selected.listFiles()){
                if (file.getName().equals("info.json")) {
                    infoExists = true;
                    info = file;
                }
            }

            if (infoExists) {
                try {
                    boolean changed = false;
                    String newData = "";

                    if (selected.getName().split("-").length != 2) {
                        System.out.printf("Invalid name length for '%s'%n",selected.getName());
                    } else {

                        String songName = selected.getName().split("-")[1];
                        songName = songName.substring(1, songName.length());

                        String author = selected.getName().split("-")[0];
                        author = author.substring(0, author.length() - 1);

                        FileReader fr = new FileReader(info);
                        BufferedReader br = new BufferedReader(fr);

                        for (String s : br.readLine().split(",")) {
                            if (s.contains(":")) {
                                String olds = s;
                                if (s.contains("songSubName")) {
                                    s = "\"songSubName\":\"\"";
                                }
                                if (s.contains("songName")) {
                                    s = "{\"songName\":\"" + songName + "\"";
                                }
                                if (s.contains("authorName")) {
                                    s = "\"authorName\":\"" + author + "\"";
                                }

                                if (!s.equals(olds)){
                                    changed = true;
                                }
                            }
                            newData += s + ",";
                        }

                        if (changed) {
                            FileWriter fw = new FileWriter(info);
                            fw.write(newData.substring(0, newData.length() - 1));
                            fw.close();
                            System.out.println("changing "+selected.getName());
                        }
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No Selected File");
        }
    }
}
