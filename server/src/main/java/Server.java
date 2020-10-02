import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String path = "server/src/main/resources/";

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(8189)) {
            System.out.println("Server started");
            Socket socket = server.accept();
            System.out.println("Client accepted");
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            while (true) {
                // 1) name
                String fileName = is.readUTF();
                if (fileName.equals("./getFilesList")) {
                    File dir = new File(path);
                    String [] files = dir.list();
                    if (files != null) {
                        dos.writeInt(files.length);
                        for (String file : files) {
                            dos.writeUTF(file);
                        }
                    } else {
                        dos.writeInt(0);
                    }
                    dos.flush();
                } else {
                    System.out.println("Save file: " + fileName);
                    File file = new File("server/src/main/resources/" + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        System.out.println("ERROR");
                    }
                    FileOutputStream os = new FileOutputStream(file);
                    // 2) file length
                    long fileLength = is.readLong();
                    System.out.println("Wait: " + fileLength + " bytes");
                    // 3) file bytes
                    byte[] buffer = new byte[8192];
                    for (int i = 0; i < (fileLength + 8191) / 8192; i++) {
                        int cnt = is.read(buffer);
                        os.write(buffer, 0, cnt);
                    }
                    System.out.println("File successfully uploaded!");
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
