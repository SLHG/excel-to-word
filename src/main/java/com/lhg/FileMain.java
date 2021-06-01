package com.lhg;

import com.lhg.service.FrameService;

import javax.swing.*;
import java.awt.*;

public class FileMain {

    public static void main(String[] args) {
        JLabel sourceFilePathJLabel = new JLabel("请选择源文件");
        JLabel templateFilePathJLabel = new JLabel("请选择模板文件");
        JLabel writeFilePathJLabel = new JLabel("请选择导出地址");
        JTextArea jta = new JTextArea("", 20, 40);
        jta.setLineWrap(true);    //设置文本域中的文本为自动换行
        jta.setForeground(Color.BLACK);    //设置组件的背景色
        jta.setFont(new Font("楷体", Font.BOLD, 16));    //修改字体样式
        jta.setBackground(Color.WHITE);    //设置按钮背景色
        FrameService frameService = new FrameService(sourceFilePathJLabel, templateFilePathJLabel, writeFilePathJLabel, jta);
        frameService.initFrame();
    }

}
