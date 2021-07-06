package com.lhg;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DocFileParsing {

    //文件目录
    private static final String DOC_PATH = "C:/新建文件夹/21年所有报名表/";
    //导出目录
    private static final String WRITE_PATH = "C:/新建文件夹/表格.xlsx";

    public static void main(String[] args) throws IOException {
        File file = new File(DOC_PATH);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("姓名全拼");
        row.createCell(2).setCellValue("证件类型");
        row.createCell(3).setCellValue("证件号码");
        row.createCell(4).setCellValue("出生日期");
        row.createCell(5).setCellValue("性别");
        row.createCell(6).setCellValue("报考级别");
        row.createCell(7).setCellValue("民族");
        row.createCell(8).setCellValue("国籍");
        row.createCell(9).setCellValue("电话号码");
        row.createCell(10).setCellValue("住址");
        row.createCell(11).setCellValue("邮编");
        processFiles(files, sheet);
        OutputStream os = new FileOutputStream(WRITE_PATH);
        workbook.write(os);
    }

    private static void processDocFile(XWPFDocument xwpfDocument, Sheet sheet) {
        //获取所有的表格
        Iterator<XWPFTable> tablesIterator = xwpfDocument.getTablesIterator();
        int lastRowNum1 = sheet.getLastRowNum();
        Row sheetRow = sheet.createRow(lastRowNum1 + 1);
        if (tablesIterator.hasNext()) {
            //得到单个表格
            XWPFTable next = tablesIterator.next();
            //获取单个表格的所有行
            List<XWPFTableRow> rows = next.getRows();
            for (int j = 0; j < rows.size(); j++) {
                XWPFTableRow xwpfTableRow = rows.get(j);
                //获取单行数据的所有单元列
                List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
                for (int k = 0; k < tableCells.size(); k++) {
                    XWPFTableCell xwpfTableCell = tableCells.get(k);
                    String text = xwpfTableCell.getText();
                    setCellValue(sheetRow, j, k, text);
                }
            }
        }
    }

    private static void setCellValue(Row sheetRow, int j, int k, String text) {
        if (j == 0 && k == 0) {
            sheetRow.createCell(0).setCellValue(text.replaceAll("姓名", "").replaceAll("：", "").trim());
        }
        if (j == 0 && k == 1) {
            sheetRow.createCell(1).setCellValue(text.replaceAll("拼音", "").replaceAll("：", "").trim());
        }
        if (j == 0 && k == 2) {
            sheetRow.createCell(4).setCellValue(formatDate(text));
        }
        if (j == 1 && k == 0) {
            sheetRow.createCell(7).setCellValue(text.replaceAll("民族", "").replaceAll("：", "").replaceAll("族", "").trim());
        }
        if (j == 1 && k == 2) {
            sheetRow.createCell(2).setCellValue("居民身份证");
            sheetRow.createCell(3).setCellValue(text.replaceAll("身份证号", "").replaceAll("：", "").trim());
        }
        if (j == 2 && k == 0) {
            sheetRow.createCell(5).setCellValue(text.replaceAll("性别", "").replaceAll("：", "").trim());
        }
        if (j == 2 && k == 1) {
            String trim = text.replaceAll("报考级别", "").replaceAll("级", "").replaceAll("：", "").replaceAll("报考", "").replaceAll("級", "").trim();
            sheetRow.createCell(6).setCellValue(HASH_MAP.getOrDefault(trim, trim));
        }
        if (j == 3 && k == 0) {
            sheetRow.createCell(10).setCellValue(text.replaceAll("通讯地址", "").replaceAll("：", "").trim());
            sheetRow.createCell(11).setCellValue("450000");
        }
        if (j == 3 && k == 1) {
            sheetRow.createCell(8).setCellValue("中国");
            sheetRow.createCell(9).setCellValue(text.replaceAll("电话", "").replaceAll("：", "").trim());
        }
    }

    private static String formatDate(String date) {
        return date.replaceAll("出生年月日", "")
                .replaceAll("：", "")
                .replaceAll("出生", "")
                .replaceAll(":", "")
                .replaceAll(":", "")
                .replaceAll("日", "")
                .replaceAll("号", "")
                .replaceAll("\\.", "-")
                .replaceAll("年", "-")
                .replaceAll("月", "-")
                .replaceAll("/", "-")
                .replaceAll(" +", "")
                .replaceAll(" ", "")
                .replaceAll("\\s*", "")
                .replaceAll(" ", "")
                .replaceAll("\\u00A0", "");
    }

    private static final Map<String, String> HASH_MAP = new HashMap<String, String>() {
        {
            put("一", "1");
            put("二", "2");
            put("三", "3");
            put("四", "4");
            put("五", "5");
            put("六", "6");
            put("七", "7");
            put("八", "8");
            put("九", "9");
            put("十", "10");
        }
    };

    private static void processDocFile(HWPFDocument hwpfDocument, Sheet sheet) {
        Range range = hwpfDocument.getRange();
        TableIterator it = new TableIterator(range);
        int lastRowNum1 = sheet.getLastRowNum();
        Row sheetRow = sheet.createRow(lastRowNum1 + 1);
        if (it.hasNext()) {
            Table next = it.next();
            int i = next.numRows();
            for (int j = 0; j < i; j++) {
                TableRow row = next.getRow(j);
                for (int k = 0; k < row.numCells(); k++) {
                    String text = row.getParagraph(k).text();
                    setCellValue(sheetRow, j, k, text.substring(0, text.length() - 1));
                }
            }
        }
    }

    private static void processFiles(File[] files, Sheet sheet) throws IOException {
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                processFiles(file.listFiles(), sheet);
            } else {
                String fileName = file.getName().toLowerCase();
                InputStream in = new FileInputStream(file);
                if (fileName.endsWith(".doc")) {
                    HWPFDocument hwpfDocument = new HWPFDocument(in);
                    processDocFile(hwpfDocument, sheet);
                } else if (fileName.endsWith(".docx")) {
                    XWPFDocument xwpfDocument = new XWPFDocument(in);
                    processDocFile(xwpfDocument, sheet);
                } else {
                    System.out.println("文件格式错误" + fileName);
                }
            }
        }
    }
}
