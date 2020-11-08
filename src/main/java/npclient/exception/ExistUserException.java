package npclient.exception;

public class ExistUserException extends Exception {
    public ExistUserException() {
        super("Found Existing User. Login Failure!");
    }
}
