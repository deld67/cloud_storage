package JavaFX;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ru.geekbrains.client.controller.ClientController;
import ru.geekbrains.common.property.Property;
import ru.geekbrains.common.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import static ru.geekbrains.common.Command.PutFileCommand;


public class Controller implements Initializable {
    public ListView<String> localView;
    public ListView<String> serverView;
    public TextField serverPath;
    public TextField localPath;


    private ClientController clientController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clientController = new ClientController(Property.getDefaultServerHost(), Property.getDefaultServerPort() );
            clientController.runApplication();
            localPath.setText(clientController.getClientLocalPath());
            serverPath.setText(clientController.getClientServerPath());
            getLocalFileList();
            getServerFileList();

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to connect to server! Please, check you network settings");
        }
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void sendToServer(MouseEvent mouseEvent) throws IOException {
            System.out.println(localPath.getText()+"/"+localView.getSelectionModel().getSelectedItem().toString());
            clientController.sendToServer(localPath.getText()+"/"+localView.getSelectionModel().getSelectedItem().toString(),
                    serverPath.getText()+"/"+localView.getSelectionModel().getSelectedItem().toString());
            getServerFileList();
    }

    public void getFromServer(MouseEvent mouseEvent) throws IOException {
            System.out.println(serverPath.getText() + "/" + serverView.getSelectionModel().getSelectedItem().toString());
            clientController.GetFromServer(serverPath.getText()+"/"+ serverView.getSelectionModel().getSelectedItem().toString(),
                    localPath.getText()+"/"+ serverView.getSelectionModel().getSelectedItem().toString());
    }

    private void getServerFileList() throws IOException {
        clientController.sendNavigateCommand(new File(serverPath.getText()));
        boolean waitAnswer = clientController.networkServiceIsGetAnswer();
        while (!waitAnswer ){
            waitAnswer = clientController.networkServiceIsGetAnswer();
            System.out.println("wait");
        }
        serverView.getItems().clear();
        for (File f: clientController.getNavigates()) {
            System.out.println(f.getName());
            serverView.getItems().add(f.getName());
        }

    }
    private void getLocalFileList() throws IOException {
        File dir = new File(localPath.getText());
        if (dir.exists()) {
            for (String f: Utility.getFilesString(dir) ) {
                localView.getItems().add(f);
            }
        }
    }

}
