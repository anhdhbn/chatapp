package npclient.gui.view;

import npclient.gui.controller.ImageMessageController;
import nputils.FileInfo;

public class ImageMessageView extends AbstractMessageView<FileInfo, ImageMessageController> {

    public ImageMessageView() {
        super("/fxml/image_message.fxml");
    }

    @Override
    public void setContent(FileInfo content) {
        getController().setFileInfo(content);
    }

    @Override
    public void changeBackground(boolean isFromMe) {
//        setStyle("-fx-background-color: transparent");
        if (isFromMe)
            getController().hideDownloadButton();
        else
            getController().showDownloadButton();
    }
}
