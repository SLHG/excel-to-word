package com.lhg.lister;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectFilePathButtonActionLister implements ActionListener {

    private JLabel filePathJLabel;
    private int chooserType;

    public SelectFilePathButtonActionLister(JLabel filePathJLabel, int chooserType) {
        this.filePathJLabel = filePathJLabel;
        this.chooserType = chooserType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser("C:\\");
        fc.setFileSelectionMode(chooserType);
        //文件打开对话框
        int val = fc.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            //正常选择文件
            filePathJLabel.setText(fc.getSelectedFile().toString());
        }
    }
}
