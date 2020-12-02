package npclient.gui.view;

import npclient.gui.controller.FileMessageController;
import nputils.FileInfo;

import java.io.InputStream;

public class FileMessageView extends AbstractMessageView<FileInfo, FileMessageController> {

    public FileMessageView() {
        super("/fxml/file_message.fxml");
    }

    public void setIcon(InputStream url) {
        getController().setIcon(url);
    }

    @Override
    public void setContent(FileInfo content) {
        getController().setFileInfo(content);
    }

    @Override
    public void changeBackground(boolean isFromMe) {
        super.changeBackground(isFromMe);

        if (isFromMe)
            setIcon(getClass().getResourceAsStream("/img/download-light.png"));
        else
            setIcon(getClass().getResourceAsStream("/img/download.png"));

        getController().setFromMe(isFromMe);
    }
}
