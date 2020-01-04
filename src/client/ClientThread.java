package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author zhanghui
 */
public class ClientThread extends Thread {
    BufferedReader br;
    BufferedWriter bw;
    Client client;

    public ClientThread(String ip, int port, String name, Client client) throws UnknownHostException, IOException {
        //根据ip和port端口号获得Socket连接
        Socket socket = new Socket(ip, port);
        //获得Socket连接的输出流和输入流
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
    }

    public void sendMessage(String text) throws IOException {
        //用输出流发送消息
        bw.write(text);
        bw.newLine();
        bw.flush();
    }

    @Override
    public void run() {
        while (true) {
            try {
                //循环显示输入流接收到的信息
                client.showMessage(br.readLine() + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}