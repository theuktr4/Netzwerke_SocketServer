import javax.sound.sampled.Port;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FreeDiskSpaceServer {
    private static final int PORT =4711;

    public FreeDiskSpaceServer(){
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server running");
            while(true) {
                /*
                Thread t = new Thread(() -> {
                    try {
                        Socket client = server.accept();
                        System.out.println("new thread");
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF8"));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF8"));
                        String line = in.readLine();
                        Path path = Paths.get(line);
                        long totalSpace = Files.getFileStore(path).getTotalSpace();
                        long freeSpace = Files.getFileStore(path).getUnallocatedSpace();
                        String res = String.format("Info for path %s %d bytes of %d free", line, freeSpace, totalSpace);
                        System.out.printf("Sending: Info for path %s %d bytes of %d free %n", line, freeSpace, totalSpace);

                        out.write(res + "\n");
                        out.newLine();
                        out.flush();
                    } catch (IOException cex) {
                        System.out.println("Client disconnected");
                    }
                });
                t.start();
                 */
                try {
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF8"));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF8"));
                    String line = in.readLine();
                    Path path = Paths.get(line);
                    long totalSpace = Files.getFileStore(path).getTotalSpace();
                    long freeSpace = Files.getFileStore(path).getUnallocatedSpace();
                    String res = String.format("Info for path %s %d bytes of %d free", line, freeSpace, totalSpace);
                    System.out.printf("Sending: Info for path %s %d bytes of %d free %n", line, freeSpace, totalSpace);

                    out.write(res + "\n");
                    out.newLine();
                    out.flush();
                } catch (IOException cex) {
                    System.out.println("Client disconnected");
                }
            }
        } catch (IOException e) {
            System.out.println("Server closed");
        }

    }
    public static void main(String[] args) {
        new FreeDiskSpaceServer();
    }

}
