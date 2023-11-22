import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login2Server {
    private ServerSocket server;

    public login2Server() {
        try {
            server = new ServerSocket(8000);
            System.out.println("Server is listening");

            while (true) {
                Socket socket = server.accept();
                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter prnt = new PrintWriter(socket.getOutputStream());
            ObjectOutputStream obwrite = new ObjectOutputStream(socket.getOutputStream())
        ) {
            String ss = br.readLine();
            Multiclient mltclient = new Multiclient(ss, obwrite, socket);
            new Thread(mltclient).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Multiclient implements Runnable {
        private String user;
        private String pass;
        private ObjectOutputStream obwrite;
        private Socket socket;

        public Multiclient(String ss, ObjectOutputStream obwrite, Socket socket) {
            String[] det = ss.split(" ");
            user = det[0];
            pass = det[1];
            this.obwrite = obwrite;
            this.socket = socket;
        }

        @Override
        public void run() {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/insta", "root", "Sharan@11")) {
                String query = "SELECT user_id, password, first_name, last_name FROM user WHERE user_id=? AND password=?";
                try (PreparedStatement prst = conn.prepareStatement(query)) {
                    prst.setString(1, user);
                    prst.setString(2, pass);

                    try (ResultSet rslt = prst.executeQuery()) {
                        if (rslt.next()) {
                            InetSocketAddress connectedsocket = (InetSocketAddress) socket.getRemoteSocketAddress();
                            String clientip = connectedsocket.getAddress().getHostAddress();
                            String firstName = rslt.getString("first_name");
                            String lastName = rslt.getString("last_name");

                            obwrite.writeObject("success");
                            obwrite.writeObject(clientip);
                            obwrite.writeObject(firstName);
                            obwrite.writeObject(lastName);
                        } else {
                            obwrite.writeObject("unsuccess");
                        }
                    }
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        login2Server so = new login2Server();
    }
}
