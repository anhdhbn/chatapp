package npclient.exception;

public class BigFileTransferException extends Exception {
    public BigFileTransferException(String name) {
        super("Please choose a smaller file.");
    }
}
