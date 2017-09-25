/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoserver;

/**
 *
 * @author admin
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

public class DemoServer {

    int requestCount = 0;
    int serverPort = 3402;
    ServerSocket ss;

    public DemoServer(int port) {
        this.serverPort = port;
        this.requestCount = 0;
    }

    void start() {
        try {
            ss = new ServerSocket(serverPort);
            waitForClient();
        } catch (Exception e) {
            System.out.println("An error has occured while creating the server");
        }
    }

    void waitForClient() {
        try {
            while (true) {

                Socket s = ss.accept();
                requestCount++;
                System.out.println("Request Count: " + requestCount);
                serveClient(s);

            }
        } catch (Exception e) {
            System.out.println("An error has occured while responding to the client");
        }
    }

    void serveClient(Socket cs) throws IOException {
        readRequest(cs);
        //respondToRequest(cs);
    }

    void readRequest(Socket c) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
        //PrintWriter out = new PrintWriter(c.getOutputStream());
        String str, fileName = "";
        do {
            str = in.readLine();
            String[] spl = str.split(" ");
            if (spl[0].equals("GET")) {
                for (int i = 1; i < spl[1].length(); i++) {
                    if (spl[1].charAt(i) == '/') {
                        fileName += "\\";
                    } else {
                        fileName += spl[1].charAt(i);
                    }
                }

            }
            System.out.println(str);
        } while (!str.equals(""));
        respondToRequest(c, fileName);
    }

    void respondToRequest(Socket cl, String fileName) throws IOException {
        PrintWriter out = new PrintWriter(cl.getOutputStream());
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: application/force-download");
        //out.println("Content-Type: text/html");
        out.println("Server: DemoServer");
        out.println("");

        System.out.println("Filename: " + fileName);

//        FileInputStream b = new FileInputStream(new File(fileName));
        
        FileReader f = new FileReader(fileName);
        BufferedReader b = new BufferedReader(f);
        //InputStream b;
        //b = new FileInputStream(fileName);

        //while(b.read()) {
        //  out.print(b.read());
        //}
        //OutputStream o = cl.getOutputStream();
        //IOUtils.copy(b, o);
        String writeable;
        /*byte[] w = new byte[b.available()];
        System.out.println(b.available());
        int aa=0;
        while((aa = b.read(w))!=-1) {
            //writeable = b.toString();
            out.print(w);
            
        } 
        System.out.println(w[1]);
        */
        //out.println("<CENTER>");
        //out.println(writeable);
        //out.print("<H1 style='color:red'>");
        
        
        while ((writeable = b.readLine()) != null) {
            //System.out.println(writeable);
            out.println(writeable);
        } 
        //out.println("<H1>");
        //out.println("<H2>This page has been visited <span style='color:green'>" + requestCount + " time(s)</span></H2>");
        //out.println("</CENTER>");

        /*
        out.println("<CENTER>");
        out.println("<H1 style='color:red'>Welcome to the matrix<H1>");
        out.println("<H2>This page has been visited <span style='color:green'>" + requestCount + " time(s)</span></H2>");
        out.println("</CENTER>");
         */
        out.flush();
        out.close();
        b.close();
        f.close();
        cl.close();

    }

    public static void main(String args[]) {
        DemoServer ds = new DemoServer(3402);
        ds.start();
    }

}
