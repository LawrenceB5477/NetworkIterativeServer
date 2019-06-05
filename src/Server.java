import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void start(int port) {
        try  {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, listening on: " + serverSocket.getInetAddress());
            boolean finished = false;
            while (!finished) {
                clientSocket = serverSocket.accept();
                System.out.println("We are bound to " + clientSocket.getLocalAddress()
                        + ":" + clientSocket.getLocalPort());
                System.out.println("Local socket address: " + clientSocket.getLocalSocketAddress());
                System.out.println("Connected to client: " + serverSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String input = null;
                while ((input = in.readLine()) != null) {
                    if (input.equalsIgnoreCase("exit")) {
                        finished = true;
                    }
                    System.out.println(input);
                }
                in.close();
                clientSocket.close();
            }

            System.out.println("Tearing down the server, goodbye...");
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(5555);
    }
}
