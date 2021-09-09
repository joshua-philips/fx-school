import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import helpers.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Student;

public class TableViewController implements Initializable {

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, String> idCol;

    @FXML
    private TableColumn<Student, String> nameCol;

    @FXML
    private TableColumn<Student, String> birthCol;

    @FXML
    private TableColumn<Student, String> addressCol;

    @FXML
    private TableColumn<Student, String> emailCol;

    @FXML
    private TableColumn<Student, String> editCol;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;

    ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("AddStudent.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Student");
            stage.initStyle(StageStyle.UTILITY);
            // stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void print(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {

            boolean successDialog = printerJob.showPrintDialog(stage.getOwner());

            if (successDialog) {
                boolean success = printerJob.printPage(studentsTable);
                if (success) {
                    printerJob.endJob();
                }
            }
        }
    }

    @FXML
    void refreshTable() {
        try {
            studentList.clear();
            query = "SELECT * FROM student";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDate("birth"), resultSet.getString("address"), resultSet.getString("email")));
            }
            studentsTable.setItems(studentList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadDate();

    }

    private void loadDate() {
        connection = DBConnect.getConnection();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add cell of button edit
        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = (
                TableColumn<Student, String> param) -> {
            // make cell containing buttons
            final TableCell<Student, String> cell = new TableCell<Student, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button deleteIcon = new Button("D");
                        Button editIcon = new Button("E");

                        deleteIcon.setStyle(" -fx-cursor: hand ;"

                        );
                        editIcon.setStyle(" -fx-cursor: hand ;"

                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            try {
                                student = studentsTable.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `student` WHERE id  =" + student.getId();
                                connection = DBConnect.getConnection();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();

                            } catch (SQLException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println("SQL Exception");

                            } catch (Exception e) {
                                e.printStackTrace();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Error");
                                alert.setContentText("Make sure to select entire row before editing or deleting");
                                alert.showAndWait();
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            student = studentsTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("AddStudent.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);

                            }

                            AddStudentController addStudentController = loader.getController();
                            addStudentController.setUpdate(true);
                            addStudentController.setTextField(student.getId(), student.getName(),
                                    student.getBirth().toLocalDate(), student.getAddress(), student.getEmail());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };
            return cell;

        };
        editCol.setCellFactory(cellFactory);
        studentsTable.setItems(studentList);
    }

}
