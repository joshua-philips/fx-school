
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import helpers.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.Student;

public class AddStudentController implements Initializable {

    @FXML
    private TextField nameFid;

    @FXML
    private DatePicker birthFid;

    @FXML
    private TextField addressFid;

    @FXML
    private TextField emailFid;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;

    private boolean update;
    int studentId;

    @FXML
    void clean() {
        nameFid.setText(null);
        birthFid.setValue(null);
        addressFid.setText(null);
        emailFid.setText(null);
    }

    @FXML
    void save(MouseEvent event) {
        connection = DBConnect.getConnection();

        String name = nameFid.getText();
        String birth = String.valueOf(birthFid.getValue());
        String address = addressFid.getText();
        String email = emailFid.getText();

        if (name.isEmpty() || birth.isEmpty() || address.isEmpty() || email.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please fill all data");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
        }
    }

    private void getQuery() {
        if (update == false) {

            query = "INSERT INTO `student`( `name`, `birth`, `address`, `email`) VALUES (?,?,?,?)";

        } else {
            query = "UPDATE `student` SET " + "`name`=?," + "`birth`=?," + "`address`=?," + "`email`= ? WHERE id = '"
                    + studentId + "'";
        }
    }

    private void insert() {
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFid.getText());
            preparedStatement.setString(2, String.valueOf(birthFid.getValue()));
            preparedStatement.setString(3, addressFid.getText());
            preparedStatement.setString(4, emailFid.getText());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setTextField(int id, String name, LocalDate toLocalDate, String adress, String email) {

        studentId = id;
        nameFid.setText(name);
        birthFid.setValue(toLocalDate);
        addressFid.setText(adress);
        emailFid.setText(email);

    }

    void setUpdate(boolean b) {
        this.update = b;

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

}
