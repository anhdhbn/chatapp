package npclient.exception;

public class DuplicateGroupException extends Exception {
    public DuplicateGroupException(String group) {
        super("Group " + group + " has been joined!");
    }
}
