
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login {

    @FXML
    private Button cancelbutton;

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelbutton.getScene().getWindow();
        stage.close();

    }

    @FXML
    private Label warningmsg;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Button enterbutton;

    public void loginButtonOnAction(ActionEvent e) {
        // warningmsg.setText("Please provide information properly!");

        if (username.getText().isBlank() == false && password.getText().isBlank() == false) {
            // warningmsg.setText("Both fields are filled!");
            validateLogin();
        } else {
            warningmsg.setText("Please Enter both fields!");
        }

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

    public void validateLogin() {
    Connection conn = null;

    try {
        DatabaseConnection dc = new DatabaseConnection();
        conn = dc.getConnection();

        String loginquery = "select count(1) from user where username=? and password=?";
        
        PreparedStatement st = conn.prepareStatement(loginquery);
        st.setString(1, username.getText());
        st.setString(2, password.getText());

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            if (rs.getInt(1) == 1) {
                warningmsg.setText("Welcome");
            } else {
                warningmsg.setText("Invalid Login. Please try again...!");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
@FXML
private Button regButton;

@FXML
void loginToRegiserButton(ActionEvent event) {
    //once entered registration close login window
    Stage stage = (Stage) cancelbutton.getScene().getWindow();
        stage.close();
    createRegisterForm();

}

public void createRegisterForm(){
    
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage primaryStage=new Stage();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Register");
           // primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();

    }catch(Exception e){
        e.printStackTrace();
    }
}


}
