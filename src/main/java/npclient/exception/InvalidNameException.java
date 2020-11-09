package npclient.exception;

public class InvalidNameException extends Exception {
    public InvalidNameException(String name) {
        super(String.format("Name '%s' is invalid! Please try another name.", name));
    }
}
