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

        Thread thread = new Thread(()->{
            DataTransfer dataRecv = handler.receiveObj();
            Assertions.assertEquals(dataPub.className, dataRecv.className);
        });
        thread.start();
        handler2.sendObj(dataPub);
    }

    @Test
    public void twoClientCanChatEachOther() throws IOException {
        DataTransfer dataPub = new DataTransfer("test topic", "anhdh-anhdh2", Constants.PUBLISH, "String", "message");


        Thread thread = new Thread(()->{
            DataTransfer dataRecv = handler2.receiveObj();
            Assertions.assertEquals(dataPub.className, dataRecv.className);
        });
        thread.start();
        handler.sendObj(dataPub);
    }
}
