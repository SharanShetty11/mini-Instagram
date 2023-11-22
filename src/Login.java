
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login {
    // @FXML
    // private Stage stage;
    // // private Scene scene;
    // // private Parent root;

    @FXML
    private Button cancelbutton;

    @FXML
    private Button minimizebutton;

    @FXML
    private Button enterbutton;

    @FXML
    private PasswordField password;

    @FXML
    private Hyperlink regButton;

    @FXML
    private TextField username;

    @FXML
    private Label warningmsg;

    Socket socket;
    BufferedReader br;
    PrintWriter prnt;
    ObjectInputStream obread;
    InputStream input;

    public String user;

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelbutton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onClickMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void loginToRegiserButton(ActionEvent event) {
        // warningmsg.setText("Registration button pressed");
        // once entered registration close login window
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage primaryStage = new Stage();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Register");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
            Stage stage = (Stage) cancelbutton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) {
        try {

            if (username.getText().equals("") && password.getText().equals("")) {
                warningmsg.setText("Please fill both feilds");
            } else {
                // System.out.println(event);
                warningmsg.setText("");
                socket = new Socket("127.0.0.1", 8000); // connect here
                System.out.println("connected to server");
                // br=new BufferedReader(new InputStreamReader(socket.getInputStream())); //read
                // form socket
                prnt = new PrintWriter(socket.getOutputStream()); // write to socket
                input = socket.getInputStream();
                obread = new ObjectInputStream(input);

                user = username.getText(); // sending user_id to dashboard

                String user1 = username.getText().toString();
                String pass1 = password.getText().toString();
                String fina = user1 + " " + pass1;
                username.setText("");
                password.setText("");
                prnt.println(fina);
                prnt.flush();
                String res = (String) obread.readObject();
                // String ip = (String) obread.readObject();
                // System.out.println(res);

                if (res.equals("success")) {
                    try {
                        // warningmsg.setText("Login success!");
                        Parent root;
                        try {
                            root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                            Stage primaryStage = new Stage();

                            Scene scene = new Scene(root);

                            primaryStage.setTitle("Dashboard");
                            primaryStage.initStyle(StageStyle.UNDECORATED);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                            Stage stage = (Stage) cancelbutton.getScene().getWindow();
                            stage.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                    }
                } else {
                    warningmsg.setText("Invalid credentials");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
