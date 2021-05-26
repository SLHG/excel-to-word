package com.lhg.service;

import com.lhg.lister.BeginTransformButtonActionLister;
import com.lhg.lister.SelectFilePathButtonActionLister;

import javax.swing.*;
import java.awt.*;

public class FrameService {

    private JLabel sourceFilePathJLabel;
    private JLabel templateFilePathJLabel;
    private JLabel writeFilePathJLabel;
    private JTextArea jta;

    public FrameService(JLabel sourceFilePathJLabel, JLabel templateFilePathJLabel, JLabel writeFilePathJLabel, JTextArea jta) {
        this.sourceFilePathJLabel = sourceFilePathJLabel;
        this.templateFilePathJLabel = templateFilePathJLabel;
        this.writeFilePathJLabel = writeFilePathJLabel;
        this.jta = jta;
    }

    /**
     * 初始化程序窗口
     */
    public void initFrame() {
        //创建Frame窗口
        JFrame frame = new JFrame("程序");
        JPanel p1 = new JPanel();    //面板1
        JButton sourceButton = new JButton("选择源文件");
        sourceButton.addActionListener(new SelectFilePathButtonActionLister(sourceFilePathJLabel, JFileChooser.FILES_ONLY));
        p1.add(sourceButton);
        p1.add(sourceFilePathJLabel);
        JButton templateButton = new JButton("选择模板");
        templateButton.addActionListener(new SelectFilePathButtonActionLister(templateFilePathJLabel, JFileChooser.FILES_ONLY));
        p1.add(templateButton);
        p1.add(templateFilePathJLabel);
        JButton writeButton = new JButton("选择导出地址");
        writeButton.addActionListener(new SelectFilePathButtonActionLister(writeFilePathJLabel, JFileChooser.DIRECTORIES_ONLY));
        p1.add(writeButton);
        p1.add(writeFilePathJLabel);
        JScrollPane jsp = new JScrollPane(jta);    //将文本域放入滚动窗口
        Dimension size = jta.getPreferredSize();    //获得文本域的首选大小
        jsp.setBounds(110, 90, size.width, size.height);
        p1.add(jsp);
        p1.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
        JButton beginTransformButton = new JButton("开始导出");
        beginTransformButton.addActionListener(new BeginTransformButtonActionLister(sourceFilePathJLabel, templateFilePathJLabel, writeFilePathJLabel, beginTransformButton, jta));
        p1.add(beginTransformButton);
        frame.add(p1);
        frame.setBounds(300, 200, 500, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
