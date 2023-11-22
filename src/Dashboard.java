
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Dashboard {

    @FXML
    private Stage stage;
    private Scene scene;

    @FXML
    private Button viewFeedButton;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button likeButton;

    @FXML
    private Label welcomeLabel;

    private int likeCount = 0;

    @FXML
    private Button cancelbutton;

    @FXML
    private Button minimizebutton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/insta";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sharan@11";

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
    private void viewFeed(ActionEvent event) {
        showAlert("View Feed", "Implement the logic for viewing the feed here.");
    }

    @FXML
    private void uploadPhoto(ActionEvent event) {
        showAlert("Upload Photo", "Implement the logic for uploading a photo here.");
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        // You may open the login/register window here
    }

    @FXML
    private void likeAction(ActionEvent event) {
        likeCount++; // Increment the like count
        // updateWelcomeLabel(); // Update the welcome label
        saveLikeCountToDatabase(); // Save the updated like count to the database
    }

    // private void updateWelcomeLabel() {
    // welcomeLabel.setText("Welcome! You have " + likeCount + " likes.");
    // }

    private void getUserInfoFromDatabase(String userId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT fname, lname FROM user WHERE user_id = ?")) {

            preparedStatement.setString(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name").toString();
                    String lastName = resultSet.getString("last_name").toString();
                    
                    welcomeLabel.setText("Welcome " + firstName + " " + lastName);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify the initData method to accept user_id
     Login log = new Login();
    String user_id = log.user;
    public void initData() {
        getUserInfoFromDatabase(user_id);

    }

    private int getLikeCountFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT like_count FROM likes");
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("like_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default to 0 if an error occurs
    }

    private void saveLikeCountToDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement preparedStatement = connection
                        .prepareStatement("UPDATE user_likes SET like_count = ?")) {

            preparedStatement.setInt(1, likeCount);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
