package npclient.gui.view;

import nputils.FileInfo;
import npclient.gui.controller.FileMessageController;

public class FileMessageView extends AbstractMessageView<FileInfo, FileMessageController> {

    public FileMessageView() {
        super("/fxml/file_message.fxml");
    }

    @Override
    public void setContent(FileInfo content) {
        getController().setFileInfo(content);
    }
}
