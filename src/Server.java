import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private void serverRequestProtocolRecieve(BufferedReader in) {
        int clientID = -1;
        ClientRequests request = null;
        try {
            clientID = Integer.parseInt(in.readLine());
            request = ClientRequests.parseCommandCode(Integer.parseInt(in.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Tearing down client " + clientID + " who requested " + ClientRequests.getCommandCode(request));
    }

    private void serverRequestProtocolSend(PrintWriter out) {

    }

    public void start(int port) {
        try  {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, listening on: " + serverSocket.getInetAddress());
            boolean finished = false;
            while (!finished) {
                System.out.println("Polling for a client...");
                clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                serverRequestProtocolRecieve(in);

                out.close();
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
