package npclient.gui.view;

import npclient.gui.controller.EmojiMessageController;
import npclient.gui.controller.FileMessageController;
import nputils.Emoji;
import nputils.FileInfo;

public class EmojiView extends AbstractMessageView<Emoji, EmojiMessageController> {

    public EmojiView() {
        super("/fxml/emoji_message.fxml");
    }

    @Override
    public void setContent(Emoji content) {
        getController().setEmoji(content);
    }
}
