package npserver.server;

import npserver.ServerTest;
import npserver.handler.ReadWriteHandler;
import nputils.Constants;
import nputils.DataTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class ExtensionTest extends ServerTest {
    @Test
    public void clientSubscribeAndReceiveOnlineNotification() throws IOException, InterruptedException {
        this.generateClient();
        ReadWriteHandler handler1 = this.handlers.get(0);
        DataTransfer dataSub = new DataTransfer(Constants.ONLINE_TOPIC, handler1.name, Constants.SUBSCRIBE);
        handler1.sendObj(dataSub);
        this.delay();

        this.generateClient();
        this.delay();
        DataTransfer data = handler1.receiveObj();
        data = handler1.receiveObj();
        ArrayList<String> members = (ArrayList<String>)data.data;
        Assertions.assertEquals(handlers.size(), members.size());
    }
}
