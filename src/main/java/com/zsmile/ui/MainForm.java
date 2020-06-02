package com.zsmile.ui;

import com.zsmile.selenium.SeleniumConfig;
import com.zsmile.selenium.TBHomeSearchS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainForm {

    private JPanel MainPanel;
    private JTextField taobaoAccount;
    private JTextField taobaoPwd;
    private JRadioButton firefoxRadioButton;
    private JRadioButton chromeRadioButton;
    private JTextField browserPath;
    private JTextField driverPath;
    private JButton startButton;
    private JTextField apiGetPath;
    private JTextField apiSubmitPath;

    private int maxThread = 5;

    private ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThread);

    public MainForm() {

        // 添加按钮的点击事件监听器
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int activeCount = fixedThreadPool.getActiveCount();
                if (activeCount >= maxThread) {
                    messageDialog("最多开通" + maxThread + "个线程");
                    return;
                }
                // 获取到的事件源就是按钮本身
                // JButton btn = (JButton) e.getSource();

                System.out.println("按钮被点击");

                String account = taobaoAccount.getText();
                String taobaoPwdText = taobaoPwd.getText();
                String browserPathText = browserPath.getText();
                String driverPathText = driverPath.getText();
                String apiPathText = apiGetPath.getText();
                String apiSubmitPathText = apiSubmitPath.getText();

                if ("".equalsIgnoreCase(account)) {
                    messageDialog("账号不能为空");
                    return;
                } else if ("".equalsIgnoreCase(taobaoPwdText)) {
                    messageDialog("密码不能为空");
                    return;
                } else if ("".equalsIgnoreCase(browserPathText)) {
                    messageDialog("浏览器路径不能空");
                    return;
                } else if ("".equalsIgnoreCase(driverPathText)) {
                    messageDialog("驱动路径不能为空");
                    return;
                } else if ("".equalsIgnoreCase(apiPathText)) {
                    messageDialog("api路径不能为空");
                    return;
                } else if ("".equalsIgnoreCase(apiSubmitPathText)) {
                    messageDialog("api提交路径不能为空");
                    return;
                }


                SeleniumConfig seleniumConfig = new SeleniumConfig();
                seleniumConfig.setTaobaoAccount(account);
                seleniumConfig.setTaobaoPwd(taobaoPwdText);
                seleniumConfig.setBrowserPath(browserPathText);
                seleniumConfig.setDriverPath(driverPathText);
                seleniumConfig.setApiGetPath(apiPathText);
                seleniumConfig.setApiSubmitPath(apiSubmitPathText);
                if (chromeRadioButton.isSelected()) {
                    seleniumConfig.setBrowserType("chrome");
                } else if (firefoxRadioButton.isSelected()) {
                    seleniumConfig.setBrowserType("firefox");
                }

                TBHomeSearchS tbHomeSearchS = new TBHomeSearchS(seleniumConfig);
                fixedThreadPool.execute(tbHomeSearchS);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("淘宝爬虫");
        frame.setContentPane(new MainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void messageDialog(String message) {
        JOptionPane.showMessageDialog(
                MainPanel,
                message,
                "消息标题",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
