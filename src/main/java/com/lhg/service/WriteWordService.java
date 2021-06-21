package com.lhg.service;

import com.lhg.beans.ResultInfo;
import com.lhg.beans.ResultInfoEnum;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.List;
import java.util.Map;

public class WriteWordService {

    private static final String WORD_DOC = ".doc";

    public ResultInfo<?> getWordTemplate(String templatePath) throws IOException {
        File file = new File(templatePath);
        if (!file.exists() || !file.isFile()) {
            return new ResultInfo<>("模板文件错误", ResultInfoEnum.FAIL.getCode());
        }
        String fileName = file.getName().toLowerCase();
        InputStream in = new FileInputStream(file);
        if (fileName.endsWith(WORD_DOC)) {
            return new ResultInfo<>(ResultInfoEnum.SUCCESS, new HWPFDocument(in));
        } else {
            return new ResultInfo<>(ResultInfoEnum.SUCCESS, new XWPFDocument(in));
        }
    }

    public void replaceSymbolByInfo(XWPFDocument xwpfDocument, Map<String, String> symbolInfoMap) {
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                for (Map.Entry<String, String> entry : symbolInfoMap.entrySet()) {
                    text = text.replaceAll(entry.getKey(), entry.getValue());
                }
                run.setText(text, 0);
            }
        }
    }

    public void replaceSymbolByInfo(HWPFDocument hwpfDocument, Map<String, String> symbolInfoMap) {
        Range range = hwpfDocument.getRange();
        for (int i = 0; i < range.numSections(); i++) {
            Section section = range.getSection(i);
            for (int j = 0; j < section.numParagraphs(); j++) {
                Paragraph paragraph = section.getParagraph(j);
                for (int k = 0; k < paragraph.numCharacterRuns(); k++) {
                    CharacterRun characterRun = paragraph.getCharacterRun(k);
                    String text = characterRun.text();
                    for (Map.Entry<String, String> entry : symbolInfoMap.entrySet()) {
                        if (text.contains(entry.getKey())) {
                            characterRun.replaceText(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        }
    }

    public void writeWord(Object result, String path) throws IOException {
        String fileName = "word.doc";
        String filePath = path + "\\" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                return;
            }
        }
        OutputStream out = new FileOutputStream(file);
        if (result instanceof HWPFDocument) {
            ((HWPFDocument) result).write(out);
        } else {
            ((XWPFDocument) result).write(out);
        }
    }
}
