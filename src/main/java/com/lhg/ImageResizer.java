package com.lhg;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作图片的代码.
 */
public class ImageResizer {

    /**
     * 图片大小裁剪
     *
     * @param inputImage 输入图片
     * @return 裁剪后图片
     */
    public static BufferedImage resize(BufferedImage inputImage) {
        Image scaledInstance = inputImage.getScaledInstance(SCALED_WIDTH, SCALED_HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(SCALED_WIDTH,
                SCALED_HEIGHT, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(scaledInstance, SCALED_X, SCALED_Y, null);
        g2d.dispose();
        return outputImage;
    }

    //裁剪后图片宽
    private static final int SCALED_WIDTH = 358;
    //裁剪后图片高
    private static final int SCALED_HEIGHT = 441;
    //裁剪起始位置x,y
    private static final int SCALED_X = 0;
    private static final int SCALED_Y = 0;


    public static void main(String[] args) throws IOException {
        String inputPath = "C:/新建文件夹/21年所有电子照";
        File file = new File(inputPath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        Map<String, String> nameMap = getNameMap();
        processFile(files, nameMap);
        for (Map.Entry<String, String> stringStringEntry : nameMap.entrySet()) {
            if(!stringStringEntry.getValue().equals("")){
                System.out.println(stringStringEntry.getKey()+stringStringEntry.getValue());
            }
        }
    }

    private static Map<String, String> getNameMap() throws IOException {
        File file = new File("C:/新建文件夹/表格.xlsx");
        InputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);
            String name = row.getCell(0).getStringCellValue();
            String num = row.getCell(3).getStringCellValue();
            map.put(name, num);
        }
        System.out.println("读取到姓名数量" + map.size());
        return map;
    }

    private static void processFile(File[] files, Map<String, String> nameMap) throws IOException {
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                File[] files1 = file.listFiles();
                processFile(files1, nameMap);
            }
            if (file.isFile()) {
                BufferedImage inputImage = ImageIO.read(file);
                //裁剪图片
                BufferedImage image = resize(inputImage);
                //输出图片
                writeImageFile(file, image, nameMap);
            }

        }
    }

    private static void writeImageFile(File file, BufferedImage outImage, Map<String, String> nameMap) throws IOException {
        String[] split = file.getName().split("\\.");
        String formatName = split[1];
        String initName = split[0].replaceAll("[0-9]", "").replaceAll("\\(", "").replaceAll("\\)", "").trim();
        String name = nameMap.get(initName);
        if ("".equals(name)) {
            return;
        }
        String fileName;
        if (name == null) {
            System.out.println("未找到对应身份证号" + split[0]);
            fileName = split[0];
        } else {
            fileName = name;
            nameMap.put(initName, "");
        }
        try (OutputStream outputStream = new FileOutputStream("C:/新建文件夹/照片/" + fileName + "." + formatName)) {
            ImageIO.write(outImage, formatName, outputStream);
        } catch (Exception e) {
            System.out.println("写入图片错误");
            throw e;
        }
    }

}
