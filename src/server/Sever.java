package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Sever extends JFrame {

    private JButton jbt;
    private JTextField jtf;
    private JTextArea jta;
    private JTable jt;
    private Vector<Vector<String>> vectorData;
    List<SeverThread> sts = new ArrayList<>();

    public static void main(String[] args) {
        Sever sever = new Sever();
        //初始化服务端
        sever.Init();
    }

    private void Init() {
        //设置窗口大小
        setSize(500, 400);
        //设置容器绝对布局
        setLayout(null);
        //将窗口设置在屏幕中间
        setLocationRelativeTo(null);
        //使用 System exit 方法退出应用程序
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //在窗口上面添加相关的组件
        add();
        //给按钮添加监听
        addListener();
        //窗口构建结束，开始画图
        setVisible(true);
    }


    private void add() {
        //设置对应的标签组件
        JLabel jl1 = new JLabel("端口号");
        JLabel jl2 = new JLabel("在线用户");
        //设置端口号输入框
        jtf = new JTextField(10);
        jtf.setText("8888");
        //设置服务端按钮
        jbt = new JButton("服务端");
        //设置表头
        Vector<String> vector = new Vector<>();
        vector.add("IP地址");
        vector.add("昵称");
        vector.add("登录时间");
        //当前在线人的相关数据
        vectorData = new Vector<>();
        //初始化左边的在线人数列表
        jt = new JTable(vectorData, vector);
        JScrollPane jsp = new JScrollPane(jt);
        //设置聊天面版大小
        jta = new JTextArea(10, 10);
        //初始化右边显示面板
        JScrollPane jsp1 = new JScrollPane(jta);
        //设置在线用户标签
        jl2.setBounds(20, 10, 60, 30);
        add(jl2);
        //设置端口号标签
        jl1.setBounds(230, 10, 60, 30);
        add(jl1);
        //设置端口号输入框
        jtf.setBounds(280, 14, 100, 20);
        add(jtf);
        //设置"服务端"按钮
        jbt.setBounds(400, 14, 80, 20);
        add(jbt);
        //设置左边在线人数面板
        jsp.setBounds(20, 50, 200, 300);
        add(jsp);
        //设置右边聊天内容面板
        jsp1.setBounds(250, 50, 200, 300);
        add(jsp1);
    }

    private void addListener() {
        //监听服务端按钮，当点击的时候开始监听对应的端口
        jbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            //监听输入的端口号，默认为8888
                            ServerSocket ss = new ServerSocket(Integer.parseInt(jtf.getText()));
                            //将文字设置为关闭服务
                            jbt.setText("关闭服务");
                            while (true) {
                                //循环获取接受到的socket连接
                                Socket socket = ss.accept();
                                //初始化线程对象
                                SeverThread st = new SeverThread(socket, Sever.this, sts);
                                //将当前的线程对象加入到用户列表中
                                sts.add(st);
                                //启动服务端线程
                                st.start();
                            }
                        } catch (NumberFormatException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    public void addUser(Vector<String> vector) {
        //将用户加入到用户列表
        vectorData.add(vector);
        //刷新列表组件
        jt.updateUI();
    }

    public void ShowMessage(String string) {
        //在输入框中添加消息
        jta.append(string + "\r\n");
    }
}