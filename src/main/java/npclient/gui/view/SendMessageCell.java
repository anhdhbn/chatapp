package npclient.gui.view;

import javafx.fxml.FXMLLoader;

public class SendMessageCell extends MessageCell {

    @Override
    protected FXMLLoader generateCorrespondingLoader() {
        return new FXMLLoader(getClass().getResource("/fxml/send_msg.fxml"));
    }
}
