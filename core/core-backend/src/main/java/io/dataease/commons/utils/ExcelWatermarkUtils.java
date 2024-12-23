package io.dataease.commons.utils;

import io.dataease.api.permissions.user.vo.UserFormVO;
import io.dataease.utils.IPUtils;
import io.dataease.visualization.dto.WatermarkContentDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.awt.Font;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class ExcelWatermarkUtils {

    public static String transContent(String content, UserFormVO userInfo) {
        content = content.replaceAll("\\$\\{ip}", IPUtils.get()== null?"127.0.0.1":IPUtils.get());
        content = content.replaceAll("\\$\\{username}", userInfo.getAccount());
        content = content.replaceAll("\\$\\{nickName}", userInfo.getName());
        content = content.replaceAll("\\$\\{time}", new Date().toString());
        return content;
    }

    /**
     * 添加水印图片到工作簿并返回图片 ID
     */
    public static int addWatermarkImage(XSSFWorkbook wb, WatermarkContentDTO watermarkContent, UserFormVO userInfo) {
        byte[] imageBytes = createTextImage(transContent(watermarkContent.getContent(),userInfo), 300, 100, 12); // 生成文字水印图片
        return wb.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG); // 添加到工作簿并返回 ID
    }

    public static void addWatermarkToSheet(Sheet sheet, XSSFWorkbook wb, int pictureIdx) {
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 获取工作表的总列数和行数
        int lastRowNum = sheet.getLastRowNum();
        int totalColumns = 0;
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                totalColumns = Math.max(totalColumns, row.getLastCellNum());
            }
        }

        // 如果没有内容，则假设默认覆盖100行和50列
        if (lastRowNum == 0 && totalColumns == 0) {
            lastRowNum = 100;
            totalColumns = 50;
        }

        // 根据总行列数循环绘制水印
        for (int row = 0; row <= lastRowNum; row += 15) { // 每15行绘制一行水印
            for (int col = 0; col <= totalColumns; col += 8) { // 每8列绘制一列水印
                ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
                // 设置图片位置
                anchor.setCol1(col); // 起始列
                anchor.setRow1(row); // 起始行
                anchor.setCol2(col + 5); // 终止列
                anchor.setRow2(row + 10); // 终止行
                anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);  // 防止移动和调整大小
                XSSFPicture picture = drawing.createPicture(anchor, pictureIdx);
                // 锁定图片（不可移动、不可调整大小）
                picture.getCTPicture().getNvPicPr().getCNvPicPr().addNewPicLocks().setNoChangeAspect(true);
                picture.getCTPicture().getNvPicPr().getCNvPicPr().addNewPicLocks().setNoMove(true);
                picture.getCTPicture().getNvPicPr().getCNvPicPr().addNewPicLocks().setNoResize(true);
            }
        }
    }

    public static byte[] createTextImage(String text, int width, int height, int fontSize) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // 设置透明背景
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置字体
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        g2d.setColor(new Color(0, 0, 0, 50)); // 半透明颜色
        g2d.rotate(Math.toRadians(25), width / 2.0, height / 2.0); // 旋转文字

        // 绘制文字
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height + textHeight) / 2 - fontMetrics.getDescent();
        g2d.drawString(text, x, y);

        g2d.dispose();

        // 转为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
