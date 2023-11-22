import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class register {

    @FXML
    private PasswordField cPassword;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private Label confirmPasswordLable;

    @FXML
    private Button registerButton;

    @FXML
    private TextField userName;

    @FXML
    private Hyperlink registerToLogin;

    @FXML
    private Button minimizebutton;

    @FXML
    void onClickMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void onClickCancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void registerToLoginOnAction(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage primaryStage = new Stage();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Login");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
            Stage stage = (Stage) registerToLogin.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickRegisterButton(ActionEvent event) {
        // confirmPasswordLable.setText("Registration Successful!");
        registerUser();

    }

    public class DatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/insta";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "Sharan@11";

        public Connection getConnection() {
            Connection conn = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conn;
        }
    }

    public void registerUser() {

        if (userName.getText().isBlank() == false && password.getText().isBlank() == false) {
            if (password.getText().equals(cPassword.getText())) {
                try {
                    DatabaseConnection dc = new DatabaseConnection();
                    Connection conn = dc.getConnection();

                    String fname = firstName.getText();
                    String lname = lastName.getText();
                    String uname = userName.getText();
                    String pass = password.getText();

                 

                    // Create a prepared statement to prevent SQL injection
                    String insertQuery = "INSERT INTO user (fname, lname, user_id, password) VALUES (?, ?, ?, ?)";

                    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                    preparedStatement.setString(1, fname);
                    preparedStatement.setString(2, lname);
                    preparedStatement.setString(3, uname);
                    preparedStatement.setString(4, pass);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        confirmPasswordLable.setText("Registration successful");
                    } else {
                        confirmPasswordLable.setText("Registration failed");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception gracefully, e.g., show an error message
                    confirmPasswordLable.setText("Registration failed. Please try again.");
                }

            } else {
                confirmPasswordLable.setText("Password does not match");
            }

        } else {
            confirmPasswordLable.setText("Fill every value!");
        }

    }
   
}
