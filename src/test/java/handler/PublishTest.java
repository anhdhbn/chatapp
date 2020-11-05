package handler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Constants;
import utils.HandlerManagement;

import java.io.IOException;

public class PublishTest extends  ServerHandlerTest{

    @Test
    public void clientSubscribedTopicCanReceiveMessageFromThis() throws IOException {
        DataTransfer dataSub = new DataTransfer("test topic", "anhdh", Constants.SUBSCRIBE);
        DataTransfer dataPub = new DataTransfer("test topic", "anhdh2", Constants.PUBLISH, "String", "message");
        handler.sendObj(dataSub);
        handler2.sendObj(dataPub);
        DataTransfer dataRecv = handler.receiveObj();
        Assertions.assertEquals(dataPub.className, dataRecv.className);
    }

    @Test
    public void twoClientCanChatEachOther() throws IOException {
        DataTransfer dataPub = new DataTransfer("anhdh-anhdh2", "anhdh", Constants.PUBLISH, "String", "message");
        handler.sendObj(dataPub);
        DataTransfer dataRecv = handler2.receiveObj();
        Assertions.assertEquals(dataPub.className, dataRecv.className);
    }
}
