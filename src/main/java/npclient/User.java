package npclient;

import npclient.core.Connection;
import nputils.DataTransfer;
import nputils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class User {

    private String name;

    private Connection connection;

    public User(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

    }
}
