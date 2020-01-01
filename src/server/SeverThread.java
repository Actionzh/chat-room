package server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SeverThread extends Thread {

    BufferedWriter bw;
    BufferedReader br;
    Sever sever;
    List<SeverThread> sts;
    String name;

    public SeverThread(Socket socket, Sever sever, List<SeverThread> sts) throws IOException {
        //获取输入输出流
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.sever = sever;
        //当前在线的用户列表
        this.sts = sts;
        //获取客户端姓名
        name = br.readLine();

        //存储IP，姓名和时间
        Vector<String> vector = new Vector<>();
        vector.add(socket.getInetAddress().toString());
        vector.add(name);

        //转化当前时间的时间格式
        String format = "hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String format2 = sdf.format(new Date(System.currentTimeMillis()));
        vector.add(format2);
        //将当前用户加入用户列表中
        sever.addUser(vector);
        //将用户进入聊天室发送给所有人
        sendMessageToAll("系统消息：用户" + name + "已经进入聊天室！");
        //将用户进入聊天室打印出来
        sever.ShowMessage("系统消息：用户" + name + "已经进入聊天室！");
    }


    private void sendMessageToAll(String string) {
        //循环给当前在线的人发送消息
        for (SeverThread severThread : sts) {
            severThread.sendMessage(string);
        }
    }

    private void sendMessage(String string) {
        try {
            //利用输出流发送消息
            bw.write(string);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            String readLine;
            try {
                //循环显示接受到的信息
                readLine = br.readLine();
                //转化当前时间的时间格式
                String format = "hh:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                String format2 = sdf.format(new Date(System.currentTimeMillis()));

                //将消息发送给所有的在线用户
                sendMessageToAll(name + "(" + format2 + ")");
                sendMessageToAll(readLine);
                //将消息打印在服务端的面板上
                sever.ShowMessage(name + "(" + format2 + ")" + readLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}