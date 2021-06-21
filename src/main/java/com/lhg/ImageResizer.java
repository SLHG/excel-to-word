package com.lhg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
        String inputPath = "C:/新建文件夹/总照片";
        File file = new File(inputPath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        processFile(files);
    }

    private static void processFile(File[] files) throws IOException {
        if (files == null || files.length == 0) {
            System.out.println("文件列表为空");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                File[] files1 = file.listFiles();
                processFile(files1);
            }
            if (file.isFile()) {
                BufferedImage inputImage = ImageIO.read(file);
                //裁剪图片
                BufferedImage image = resize(inputImage);
                //输出图片
                writeImageFile(file, image);
            }

        }
    }

    private static void writeImageFile(File file, BufferedImage outImage) throws IOException {
        String path = file.getPath();
        String formatName = file.getName().split("\\.")[1];
        String substring = path.substring(0, path.indexOf("."));
        try (OutputStream outputStream = new FileOutputStream(substring + "-min." + formatName)) {
            ImageIO.write(outImage, formatName, outputStream);
            boolean delete = file.delete();
            if (!delete) {
                System.out.println("删除失败");
                System.out.println(path);
                System.out.println("================");
            }
        } catch (Exception e) {
            System.out.println("写入图片错误");
            throw e;
        }
    }

}
