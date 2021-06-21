package com.lhg;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class DocFileParsing {

    //文件目录
    private static final String DOC_PATH = "C:/新建文件夹 (2)/";
    //导出目录
    private static final String WRITE_PATH = "C:/新建文件夹/test1.xls";
    //文件中第几个表格
    private static final int NUM = 1;

    public static void main(String[] args) throws IOException {
        File file = new File(DOC_PATH);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        processFiles(files, sheet);
        OutputStream os = new FileOutputStream(WRITE_PATH);
        workbook.write(os);
    }

    private static void processDocFile(XWPFDocument xwpfDocument, Sheet sheet) {
        //获取所有的表格
        Iterator<XWPFTable> tablesIterator = xwpfDocument.getTablesIterator();
        int index = 0;
        //遍历每个表格
        while (tablesIterator.hasNext()) {
            index++;
            if (index != NUM) {
                continue;
            }
            //得到单个表格
            XWPFTable next = tablesIterator.next();
            //获取单个表格的所有行
            List<XWPFTableRow> rows = next.getRows();
            int lastRowNum = sheet.getLastRowNum();
            for (XWPFTableRow row : rows) {
                lastRowNum++;
                Row sheetRow = sheet.createRow(lastRowNum);
                //获取单行数据的所有单元列
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (int i = 0; i < tableCells.size(); i++) {
                    XWPFTableCell xwpfTableCell = tableCells.get(i);
                    Cell sheetRowCell = sheetRow.createCell(i);
                    sheetRowCell.setCellValue(xwpfTableCell.getText());
                }
            }
            break;
        }
    }

    private static void processDocFile(HWPFDocument hwpfDocument, Sheet sheet) {
        Range range = hwpfDocument.getRange();
        TableIterator it = new TableIterator(range);
        int index = 0;
        while (it.hasNext()) {
            index++;
            if (index != NUM) {
                continue;
            }
            Table next = it.next();
            int i = next.numRows();
            int lastRowNum = sheet.getLastRowNum();
            for (int j = 0; j < i; j++) {
                lastRowNum++;
                Row sheetRow = sheet.createRow(lastRowNum);
                TableRow row = next.getRow(j);
                for (int k = 0; k < row.numCells(); k++) {
                    Cell cell = sheetRow.createCell(k);
                    Paragraph paragraph = row.getParagraph(k);
                    cell.setCellValue(paragraph.text());
                }
            }
            break;
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
