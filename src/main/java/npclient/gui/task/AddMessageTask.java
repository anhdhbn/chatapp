package npclient.gui.task;

import javafx.concurrent.Task;
import npclient.MyAccount;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.command.Publisher;
import npclient.gui.entity.*;
import npclient.gui.manager.MessageManager;
import nputils.DataTransfer;
import nputils.Emoji;
import nputils.FileInfo;

import java.io.File;

public class AddMessageTask extends Task<Messages> {

    private Object content;
    private final String topic;
    private OnStateChangeListener listener;

    public AddMessageTask(String topic) {
        this.topic = topic;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    protected Messages call() throws Exception {
        final String username = MyAccount.getInstance().getName();
        Message m;
        Object data;

        if (content instanceof File) {
            m = new FileMessage();
            FileInfo fileInfo = new FileInfo((File) content);
            ((FileMessage) m).setContent(fileInfo);
            data = fileInfo;

        } else if (content instanceof Emoji) {
            m = new EmojiMessage();
            ((EmojiMessage) m).setContent((Emoji) content);
            data = content;

        } else {
            m = new TextMessage();
            ((TextMessage) m).setContent(content.toString());
            data = content;
        }

        new Publisher(topic, username)
                .putData(data)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        m.setState(Message.State.SUCCESS);
                        listener.onChange(m, Message.State.SUCCESS);
                    }
                })
                .setErrorListener(new ErrorListener() {
                    @Override
                    public void onReceive(Exception err) {
                        m.setState(Message.State.FAILURE);
                        listener.onChange(m, Message.State.FAILURE);
                    }
                })
                .post();

        m.setState(Message.State.SENDING);
        m.setFrom(username);
        m.setTime(System.currentTimeMillis());

        return MessageManager.getInstance().append(topic, m);
    }

    public interface OnStateChangeListener {
        void onChange(Message message, Message.State state);
    }
}
