package com.lhg.service;

import com.lhg.beans.AmountInfo;
import com.lhg.beans.ResultInfo;
import com.lhg.beans.ResultInfoEnum;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadHtmlFileService {

    private static final String HTML = "html";
    //获取系统换行符
    private static final String LINE_PROPERTY = System.getProperty("line.separator");
    //款项类型
    private static final String PRINCIPAL_TYPE = "本金";

    public ResultInfo<String> getFileInfo(String filePath) throws IOException, ParseException {
        File excelFile = new File(filePath);
        if (!excelFile.exists() || !excelFile.isFile()) {
            return new ResultInfo<>("文件不存在", ResultInfoEnum.FAIL.getCode());
        }
        String lowerCase = excelFile.getName().toLowerCase();
        if (!(lowerCase.endsWith(HTML))) {
            return new ResultInfo<>("非html文件", ResultInfoEnum.FAIL.getCode());
        }
        String fileToString = FileUtils.readFileToString(excelFile, "GBK");
        Document parse = Jsoup.parse(fileToString);
        Elements table = parse.getElementsByTag("table");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
        StringBuilder sb = new StringBuilder();
        for (Element element : table) {
            Elements tr = element.getElementsByTag("tr");
            AmountInfo[] temp = new AmountInfo[2];
            for (int i = tr.size() - 1; i >= 1; i--) {
                Element element1 = tr.get(i);
                Elements td = element1.getElementsByTag("td");
                //获取时间
                String time = td.get(5).text();
                Date parse1 = dateFormat.parse(time);
                String format2 = dateFormat1.format(parse1);
                //获取类型
                String type = td.get(6).text();
                //获取金额
                String amount = td.get(7).text().replaceAll(",", "");
                processInfo(temp, format2, type, amount, sb);
                if (i == 1) {
                    getStr(temp[0], sb);
                }
            }
        }
        return new ResultInfo<>(ResultInfoEnum.SUCCESS, sb.toString());
    }

    private void processInfo(AmountInfo[] temp, String dateFormat, String type, String amount, StringBuilder sb) {
        if (temp[0] == null) {
            temp[0] = new AmountInfo(dateFormat, type, new BigDecimal(amount), 1);
            return;
        }
        if (temp[0].getAmountDate().equals(dateFormat)) {
            if (temp[0].getAmountType().equals(type)) {
                temp[0].setAmount(temp[0].getAmount().add(new BigDecimal(amount)));
                return;
            }
            AmountInfo temp1 = temp[1];
            if (temp1 == null) {
                temp[1] = new AmountInfo(dateFormat, type, new BigDecimal(amount), temp[0].getIndex() + 1);
            } else {
                temp[1].setAmount(temp[1].getAmount().add(new BigDecimal(amount)));
            }
            return;
        }
        getStr(temp[0], sb);
        if (temp[1] != null) {
            getStr(temp[1], sb);
            temp[0].setIndex(temp[0].getIndex() + 1);
            temp[1] = null;
        }
        temp[0].setIndex(temp[0].getIndex() + 1);
        temp[0].setAmount(new BigDecimal(amount));
        temp[0].setAmountDate(dateFormat);
        temp[0].setAmountType(type);
    }

    private void getStr(AmountInfo amountInfo, StringBuilder sb) {
        if (amountInfo.getAmountType().equals(PRINCIPAL_TYPE)) {
            sb.append(amountInfo.getIndex()).append(".").append(amountInfo.getAmountDate()).append("，申请人中原银行股份有限公司郑州分行向被申请人银行账号6236606900235059转账支付本金").append(amountInfo.getAmount().stripTrailingZeros().toPlainString()).append("元。").append(LINE_PROPERTY);
        } else {
            sb.append(amountInfo.getIndex()).append(".").append("申请人于").append(amountInfo.getAmountDate()).append("收到利息").append(amountInfo.getAmount().stripTrailingZeros().toPlainString()).append("元。").append(LINE_PROPERTY);
        }
    }
}
