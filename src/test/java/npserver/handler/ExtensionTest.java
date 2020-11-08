package npserver.handler;

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

    @Test
    public void clientCanCheckLogin() throws IOException, InterruptedException {
        this.generateClientWithName(this.user);
        ReadWriteHandler handler1 = this.handlers.get(0);
        DataTransfer dataSub = new DataTransfer(Constants.ONLINE_TOPIC, handler1.name, Constants.SUBSCRIBE);
        handler1.sendObj(dataSub);
        this.delay();

        this.generateClientWithName(this.user);
        ReadWriteHandler handler2 = this.handlers.get(1);
        String topic = Constants.PREFIX_LOGIN + Constants.SPLITTER + this.user; // login/x
        DataTransfer dataLogin = new DataTransfer(topic, handler1.name, Constants.PUBLISH);
        handler2.sendObj(dataLogin);
        DataTransfer resLogin = handler2.receiveObj();
        Assertions.assertEquals(false, (boolean)resLogin.data);
    }
}
