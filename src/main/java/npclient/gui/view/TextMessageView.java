package npclient.gui.view;

import npclient.gui.controller.TextMessageController;

public class TextMessageView extends AbstractMessageView<String, TextMessageController> {

    public TextMessageView() {
        super("/fxml/text_message.fxml");
    }

    @Override
    public void setContent(String content) {
        getController().setContent(content);
    }
}
