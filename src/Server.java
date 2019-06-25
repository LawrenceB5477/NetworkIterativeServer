import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private void executeCommand(ClientRequests request, PrintWriter out) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = null;

            switch (request) {
                case DATETIME:
                    p = runtime.exec("date");
                    break;
                case UPTIME:
                    p = runtime.exec("uptime");
                    break;
                case MEMORYUSE:
                    p = runtime.exec("free -h");
                    break;
                case NETSTAT:
                    p = runtime.exec("netstat");
                    break;
                case CURRENTUSERS:
                    p = runtime.exec("who -a");
                    break;
                case RUNNINGPROCESSES:
                    p = runtime.exec("ps -a");
                    break;
                default:
                    System.err.println("Invalid command.");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String output = "";
            while ((output = br.readLine()) != null) {
                out.println(output);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void serverRequestProtocolRecieve(BufferedReader in, PrintWriter out) {
        int clientID = -1;
        ClientRequests request = null;
        try {
            clientID = Integer.parseInt(in.readLine());
            request = ClientRequests.values()[Integer.parseInt(in.readLine())];

            System.out.println("Client " + clientID + " requested: " + request);

            System.out.println("Sending response to client..." );
            executeCommand(request, out);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Tearing down client " + clientID + " who requested " + request);
    }

    private void serverRequestProtocolSend(PrintWriter out) {

    }

    public void start(int port) {
        try  {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, listening on: " + serverSocket.getInetAddress());
            boolean finished = false;
            while (!finished) {
                System.out.println("Polling for a client...\n");
                clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

                serverRequestProtocolRecieve(in, out);

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

        int port = -1;
        boolean collected = false;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter port number for server to listen on: " );
        while (!collected) {
            try {
                port = scanner.nextInt();
                if (port < 0 || port > 65535) {
                    throw new Exception();
                }
                collected = true;
            } catch (Exception e) {
                System.err.println("Please enter a valid port number (1 - 65536)\n");
                continue;
            }
        }

        server.start(port);
    }
}
