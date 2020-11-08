package npclient.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String computeMd5(File file) throws IOException, NoSuchAlgorithmException {
        InputStream is = Files.newInputStream(file.toPath());

        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream dis = new DigestInputStream(is, md);
        byte[] hash = md.digest();

        //converting byte array to Hexadecimal String
        StringBuilder sb = new StringBuilder(2*hash.length);
        for (byte b : hash) {
            sb.append(String.format("%02x", b&0xff));
        }

        return sb.toString();
    }
}
