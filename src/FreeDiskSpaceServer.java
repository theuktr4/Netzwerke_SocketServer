import javax.sound.sampled.Port;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.file.*;

public class FreeDiskSpaceServer {
    private static final int PORT = 4711;
    public static final String CHARSET_NAME = "UTF8";

    public FreeDiskSpaceServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server running");
            while (true) {
                try (Socket client = server.accept()) {
                    System.out.printf("Client connected: %s%n", client.getInetAddress().toString());
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), CHARSET_NAME));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), CHARSET_NAME));

                    String line = in.readLine();
                    System.out.printf("Input: \"%s\"%n", line);
                    String res = "";
                    try {
                        res = getFreeDiskSpace(line);
                    } catch (IOException pathfail) {
                        res = "Ungültiger Pfad";
                        System.err.println(res);
                    }
                    out.write(res + "\n");
                    System.err.printf("Output: \"%s\"%n", res);
                    out.newLine();
                    out.flush();

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        } catch (IOException e) {
            System.err.println("Server closed");
        }
    }

    String getFreeDiskSpace(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        long totalSpace = Files.getFileStore(path).getTotalSpace();
        long freeSpace = Files.getFileStore(path).getUnallocatedSpace();
        return String.format("Info for path \"%s\": %d bytes of %d free", pathString, freeSpace, totalSpace);
    }

    public static void main(String[] args) {
        new FreeDiskSpaceServer();
    }

}
