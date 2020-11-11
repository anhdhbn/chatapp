package npclient.core.command;

public abstract class AbstractPublisher extends AbstractTask {

    protected final String username;

    public AbstractPublisher(String username) {
        this.username = username;
    }

    public abstract void post();
}
