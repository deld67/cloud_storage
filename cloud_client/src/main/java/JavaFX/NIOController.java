package JavaFX;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ru.geekbrains.client.controller.NIOClientController;
import ru.geekbrains.common.property.Property;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class NIOController implements Initializable {
    public ListView<String> localView;
    public ListView<String> serverView;
    public TextField serverPath;
    public TextField localPath;
    public Button refresh;
    public TextField commandLine;


    private NIOClientController nioClientController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nioClientController = new NIOClientController(Property.getDefaultServerHost(), Property.getDefaultServerPort() );
        try {
            nioClientController.runApplication();
            localPath.setText(nioClientController.getClientLocalPath());
            serverPath.setText(nioClientController.getClientServerPath());
            nioClientController.refreshListView(localView, nioClientController.getClientLocalPath());
            nioClientController.sendCommand( "GetList", nioClientController.getClientServerPath(), 0);
            nioClientController.refreshServerList(serverView,  nioClientController.readAnswerList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NIOClientController getClientController() {
        return nioClientController;
    }

    public void putToServer(MouseEvent mouseEvent) {
        System.out.println("putToServer");
        System.out.println(localPath.getText()+"/"
                +localView.getSelectionModel().getSelectedItem().toString());
    }

    public void getFromServer(MouseEvent mouseEvent) throws IOException {
        Path fromPath = Paths.get( serverPath.getText() + "/"
                + serverView.getSelectionModel().getSelectedItem().toString());
        Path toPath = Paths.get( localPath.getText() + "/"
                + serverView.getSelectionModel().getSelectedItem().toString());
        System.out.println("getFromServer");
        System.out.println("fromPath:"+fromPath);
        System.out.println("toPath:"+toPath);
        nioClientController.getFromServer(fromPath,toPath);
    }

}
