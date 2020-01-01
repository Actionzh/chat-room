package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author zhanghui
 */
public class Client extends JFrame {
    public ClientThread ct;
    private JTextArea jta;

    public static void main(String[] args) {
        //进入main方法
        Client client = new Client();
        //初始化客户端界面
        client.Init();
        //设置登录窗口
        client.Dialog();
    }

    private void Dialog() {
        //初始化对话框
        JDialog jd = new JDialog(this, "登录", true);
        //设置大小
        jd.setSize(250, 200);
        jd.setLocationRelativeTo(null);
        jd.setLayout(new FlowLayout());
        //输入相关信息
        JLabel jl1 = new JLabel("IP 地址");
        JLabel jl2 = new JLabel("端口号");
        JLabel jl3 = new JLabel("昵称");

        //设置输入框
        JTextField jtf1 = new JTextField(15);
        JTextField jtf2 = new JTextField(15);
        JTextField jtf3 = new JTextField(15);
        //设置初始值，ip，端口号（端口号要与服务端一致，这里都设置为8888）
        jtf1.setText("127.0.0.1");
        jtf2.setText("8888");
        jtf3.setText("你好");
        //链接按钮
        JButton jbt = new JButton("链接");
        //给按钮添加监听
        jbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(jtf2.getText());
                try {
                    //初始化线程对象
                    ct = new ClientThread(jtf1.getText(), port, jtf3.getText(), Client.this);
                    //启动线程
                    ct.start();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(Client.this, "连接失败");

                }
                //创建客户端连接后关闭弹窗
                jd.dispose();
            }
        });

        //将元素都添加到窗口上
        jd.add(jl1);
        jd.add(jtf1);
        jd.add(jl2);
        jd.add(jtf2);
        jd.add(jl3);
        jd.add(jtf3);
        jd.add(jbt);
        //窗口构建结束，开始画图
        jd.setVisible(true);
    }

    private void Init() {
        //设置界面大小
        setSize(500, 400);
        //设置显示面板的大小
        jta = new JTextArea(16, 36);
        //设置显示面板类型
        JScrollPane jsp = new JScrollPane(jta);
        //输入文字的文本框
        JTextField jtf = new JTextField(28);
        //发送按钮
        JButton jbt = new JButton("发送");
        //监听发送按钮，当点击发送的时候调用sendMessage方法，并将输入框置为空
        jbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //调用clientthread对象发送信息
                    ct.sendMessage(jtf.getText());
                    //将输入框置为空
                    jtf.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //添加界面元素
        add(jsp);
        add(jtf);
        add(jbt);
        //屏幕组件的格式布局，默认为流式布局
        setLayout(new FlowLayout());
        //将窗口置于屏幕中间
        setLocationRelativeTo(null);
        //使用 System exit 方法退出应用程序
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //窗口构建结束，开始画图
        setVisible(true);
    }

    public void showMessage(String readLine) {
        //将信息加到显示面板上面
        jta.append(readLine + "\r\n");
    }
}