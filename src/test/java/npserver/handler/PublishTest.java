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
        this.delay();
        String topic = this.generateGroupTopic("test topic");
        DataTransfer dataSub = new DataTransfer(topic, handler1.name, Constants.SUBSCRIBE);
        DataTransfer dataPub = new DataTransfer(topic, handler1.name, Constants.PUBLISH, "String", "message");
        handler1.sendObj(dataSub);
        this.delay();
        handler2.sendObj(dataPub);
        this.delay();

        DataTransfer dataRecv = handler1.receiveObj();
        Assertions.assertEquals(dataPub.className, dataRecv.className);
    }

    @Test
    public void twoClientCanChatEachOther() throws IOException, InterruptedException {
        this.generateClient(2);
        ReadWriteHandler handler1 = this.handlers.get(0);
        ReadWriteHandler handler2 = this.handlers.get(1);
        this.delay();

        DataTransfer dataSub1 = new DataTransfer(this.generateChatTopic(handler2.name), handler1.name, Constants.SUBSCRIBE);
        DataTransfer dataSub2 = new DataTransfer(this.generateChatTopic(handler1.name), handler2.name, Constants.SUBSCRIBE);
        handler1.sendObj(dataSub1);
        handler2.sendObj(dataSub2);
        this.delay();

        DataTransfer dataPub1 = new DataTransfer(this.generateChatTopic(handler2.name), handler1.name, Constants.PUBLISH, "String", "message");
        handler1.sendObj(dataPub1);

        DataTransfer dataRecv = handler2.receiveObj();
        Assertions.assertEquals(dataPub1.className, dataRecv.className);
    }
}
