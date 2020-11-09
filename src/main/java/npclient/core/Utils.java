package npclient.core;

import java.io.*;
import java.net.*;
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

    public static void main(String[] args) throws IOException {
        String url = "https://ui-avatars.com/api/";
        String charset = "UTF-8";
        String background = "random";
        String name = "Tung Lam";

        String query = String.format("background=%s&name=%s",
                URLEncoder.encode(background, charset),
                URLEncoder.encode(name, charset));

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        InputStream response = connection.getInputStream();
        connection.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(response));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
//        connection.d
        System.out.println(content);
    }
}
