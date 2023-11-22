import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginServer {

    @FXML
    private Stage stage;
    ActionEvent event;

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter prnt;
    OutputStream output;
    ObjectOutputStream obwrite;
    Connection conn;
    ResultSet rslt;
    PreparedStatement prst;

    public LoginServer() {

        try {
            server = new ServerSocket(8000);
            System.out.println("server is listening");
            while (true) {
                socket = server.accept();
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                prnt = new PrintWriter(socket.getOutputStream());
                output = socket.getOutputStream();
                obwrite = new ObjectOutputStream(output);
                String ss = br.readLine();
                Multiclient mltclient = new Multiclient(ss);
                new Thread(mltclient).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Multiclient implements Runnable {
        String user;
        String pass;

        public Multiclient(String ss) {
            String[] det = ss.split(" ");
            user = det[0];
            pass = det[1];
        }

        @Override
        public void run() {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/insta", "root", "Sharan@11");
                prst = conn.prepareStatement("SELECT user_id,password FROM user WHERE user_id=? AND password=?");
                prst.setString(1, user);
                prst.setString(2, pass);

                rslt = prst.executeQuery();

                if (rslt.next()) {
                    try {
                        System.out.println("hi");
                        InetSocketAddress connectedsocket = (InetSocketAddress) socket.getRemoteSocketAddress();
                        String clientip = connectedsocket.getAddress().getHostAddress();
                        obwrite.writeObject("success");
                        obwrite.writeObject(clientip);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                } else {
                    try {
                        obwrite.writeObject("unsuccess");
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }

        }

    }

    public static void main(String[] args) {
        LoginServer so = new LoginServer();

    }
}