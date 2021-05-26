package com.lhg.lister;

import com.lhg.beans.ResultInfo;
import com.lhg.beans.ResultInfoEnum;
import com.lhg.service.ReadHtmlFileService;
import com.lhg.service.WriteWordService;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BeginTransformButtonActionLister implements ActionListener {

    private JLabel sourceFilePathJLabel;
    private JLabel templateFilePathJLabel;
    private JLabel writeFilePathJLabel;
    private JButton beginTransformButton;
    private JTextArea jta;
    int num = 1;

    //获取系统换行符
    private static final String LINE_PROPERTY = System.getProperty("line.separator");

    public BeginTransformButtonActionLister(JLabel sourceFilePathJLabel, JLabel templateFilePathJLabel, JLabel writeFilePathJLabel, JButton beginTransformButton, JTextArea jta) {
        this.sourceFilePathJLabel = sourceFilePathJLabel;
        this.templateFilePathJLabel = templateFilePathJLabel;
        this.writeFilePathJLabel = writeFilePathJLabel;
        this.beginTransformButton = beginTransformButton;
        this.jta = jta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        beginTransformButton.setEnabled(false);
        ReadHtmlFileService readHtmlFileService = new ReadHtmlFileService();
        ResultInfo<String> fileReturn;
        printInfo("读取数据源");
        try {
            fileReturn = readHtmlFileService.getFileInfo(sourceFilePathJLabel.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            printInfo("错误:" + ex.getMessage(), true);
            return;
        }
        if (ResultInfoEnum.FAIL.getCode().equals(fileReturn.getRtnCode())) {
            printInfo(fileReturn.getRtnMsg(), true);
            return;
        }
        printInfo("读取模板");
        WriteWordService writeWordService = new WriteWordService();
        ResultInfo<?> templateResult;
        try {
            templateResult = writeWordService.getWordTemplate(templateFilePathJLabel.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
            printInfo("错误:" + ex.getMessage(), true);
            return;
        }
        if (ResultInfoEnum.FAIL.getCode().equals(templateResult.getRtnCode())) {
            printInfo(templateResult.getRtnMsg(), true);
            return;
        }
        printInfo("开始写入文档");
        Map<String, String> map = new HashMap<>();
        map.put("${info}", fileReturn.getResult());
        Object result = templateResult.getResult();
        if (result instanceof HWPFDocument) {
            writeWordService.replaceSymbolByInfo((HWPFDocument) result, map);
            try {
                writeWordService.writeWord((HWPFDocument) result, writeFilePathJLabel.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                printInfo("错误:" + ex.getMessage(), true);
                return;
            }
        } else {
            writeWordService.replaceSymbolByInfo((XWPFDocument) result, map);
            try {
                writeWordService.writeWord((XWPFDocument) result, writeFilePathJLabel.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                printInfo("错误:" + ex.getMessage(), true);
                return;
            }
        }
        printInfo("完成", true);
    }

    private void printInfo(String msg, boolean isEnable) {
        jta.append(num + "." + msg + LINE_PROPERTY);
        jta.append("====================" + LINE_PROPERTY);
        if (isEnable) {
            beginTransformButton.setEnabled(true);
        }
        num = 1;
    }

    private void printInfo(String msg) {
        jta.append(num + "." + msg + LINE_PROPERTY);
        num++;
    }
}
