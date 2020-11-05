package npserver.handler;
import nputils.DataTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import nputils.Constants;

import java.io.IOException;

public class PublishTest extends  ServerHandlerTest{

    @Test
    public void clientSubscribedTopicCanReceiveMessageFromThis() throws IOException, InterruptedException {
        this.generateClient(2);
        ReadWriteHandler handler1 = this.handlers.get(0);
        ReadWriteHandler handler2 = this.handlers.get(1);
        Thread.sleep(3000);

        DataTransfer dataSub = new DataTransfer("test topic", handler1.name, Constants.SUBSCRIBE);
        DataTransfer dataPub = new DataTransfer("test topic", handler1.name, Constants.PUBLISH, "String", "message");
        handler1.sendObj(dataSub);
        Thread.sleep(3000);
        handler2.sendObj(dataPub);
        Thread.sleep(3000);

        DataTransfer dataRecv = handler1.receiveObj();
        Assertions.assertEquals(dataPub.className, dataRecv.className);
    }

    @Test
    public void twoClientCanChatEachOther() throws IOException, InterruptedException {
        this.generateClient(2);
        ReadWriteHandler handler1 = this.handlers.get(0);
        ReadWriteHandler handler2 = this.handlers.get(1);
        Thread.sleep(3000);

        String topic = handler1.name + "-" + handler2.name;
        DataTransfer dataPub = new DataTransfer(topic, handler1.name, Constants.PUBLISH, "String", "message");

        handler1.sendObj(dataPub);

        DataTransfer dataRecv = handler2.receiveObj();
        Assertions.assertEquals(dataPub.className, dataRecv.className);
    }
}
