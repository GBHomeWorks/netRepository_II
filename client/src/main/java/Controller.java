import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ListView<String> listView;
    //    Path path = Paths.get("nio-client/src/main/resources/client_dir"); // путь не задействован
    String path = "client/src/main/resources";
    SocketChannel sChannel;                              // не забыть закрывать по нажатию кнопки

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket("localhost", 8189);
            sChannel = socket.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(ActionEvent actionEvent) {
        String file = listView.getSelectionModel().getSelectedItem();
        System.out.println(file);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(50);
            FileChannel cannel = new RandomAccessFile(path + "/" + file, "rw").getChannel();
            while ( cannel.read(buffer) > 0){
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.print((char) buffer.get());
                    sChannel.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(ActionEvent actionEvent) {

    }

    private void refreshList() {
        File file = new File(path); // IO
        String[] files = file.list();
        listView.getItems().clear();  // FX
        if (files != null) {
            for (String name : files) {
                listView.getItems().add(name);  // FX
            }
        }
    }

    public void refreshList(ActionEvent actionEvent) {
        refreshList();
    }

    public void clientList(ActionEvent actionEvent) {
        refreshList();
    }

    public void serverList(ActionEvent actionEvent) {
        try {
            listView.getItems().clear();
            listView.getItems().addAll(getServerFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getServerFiles() throws IOException {
        List<String> files = new ArrayList<>();
//        os.writeUTF("./getFilesList");
//        os.flush();
//        int listSize = is.readInt();
//        for (int i = 0; i < listSize; i++) {
//            files.add(is.readUTF());
//        }
        return files;
    }
}
