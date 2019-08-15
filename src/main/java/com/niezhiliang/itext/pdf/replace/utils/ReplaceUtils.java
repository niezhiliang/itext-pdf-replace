package com.niezhiliang.itext.pdf.replace.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;
import com.niezhiliang.itext.pdf.replace.dto.OverAreaDTO;
import com.niezhiliang.itext.pdf.replace.dto.OverTextDTO;

import java.io.IOException;
import java.util.*;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-15 10:41
 */
public class ReplaceUtils {


    public static void doOverText(OverTextDTO overTextDTO) throws IOException {

        Map<String,String> keyMap = new HashMap<String, String>();

        //为key自动加上占位符并转义
        for(String key : overTextDTO.getReplaceMap().keySet()) {
            keyMap.put(key,"\\$\\{" + key + "\\}");
        }
        Map<String,List<OverAreaDTO>> map =
                getTextPosition(overTextDTO.getSourceFilePath(),overTextDTO.getFinishFilePath(),keyMap);

        overText(overTextDTO.getSourceFilePath(),overTextDTO.getFinishFilePath(),map,overTextDTO.getFont(),overTextDTO.getReplaceMap());
    }

    /**
     * 获取关键字的坐标
     * @param keyMap
     * @return
     */
    public static Map<String,List<OverAreaDTO>> getTextPosition(String sourcePath,String finishPath, Map<String,String> keyMap) throws IOException {

        PdfReader reader = new PdfReader(sourcePath);
        PdfDocument pdfDocument = new PdfDocument(reader, new PdfWriter(finishPath));
        Map<String,List<OverAreaDTO>> map = new HashMap(20);
        for (String key  : keyMap.keySet()) {

            List<OverAreaDTO> overAreaDTOS = new ArrayList<OverAreaDTO>();

            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {

                PdfPage page = pdfDocument.getPage(i);

                RegexBasedLocationExtractionStrategy strategy = new RegexBasedLocationExtractionStrategy(keyMap.get(key));
                PdfCanvasProcessor canvasProcessor = new PdfCanvasProcessor(strategy);
                canvasProcessor.processPageContent(page);
                Collection<IPdfTextLocation> resultantLocations = strategy.getResultantLocations();
                PdfCanvas pdfCanvas = new PdfCanvas(page);
                pdfCanvas.setLineWidth(0.5f);

                for (IPdfTextLocation location : resultantLocations) {
                    Rectangle rectangle = location.getRectangle();
                    pdfCanvas.rectangle(rectangle);
                    pdfCanvas.setStrokeColor(ColorConstants.RED);
                    pdfCanvas.stroke();

                    OverAreaDTO overAreaDTO = new OverAreaDTO();
                    overAreaDTO.setPageNum(location.getPageNumber());
                    overAreaDTO.setX(rectangle.getX());
                    overAreaDTO.setY(rectangle.getY());
                    overAreaDTO.setWidth(rectangle.getWidth());
                    overAreaDTO.setHeight(rectangle.getHeight());

                    overAreaDTOS.add(overAreaDTO);
                }
            }

            map.put(key,overAreaDTOS);

        }
        pdfDocument.close();

        return map;
    }

    /**
     * 覆盖原有的内容 并填充新内容
     *
     * @param sourcePath 源文件
     * @param finishPath 替换后的文件
     * @param map
     */
    public static void overText(String sourcePath,String finishPath,Map<String,List<OverAreaDTO>> map,PdfFont font,Map<String,String> textValueMap) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourcePath), new PdfWriter(finishPath));
        //pdfDoc.getFirstPage().newContentStreamAfter() 会覆盖掉字体
        //pdfDoc.getFirstPage().newContentStreamBefore() 只会在字体的下层添加一个背景色
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamAfter(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);

        canvas.saveState();
        //添加内容覆盖白色背景
        for (String key : map.keySet()) {

            List<OverAreaDTO> overAreaDTOList = map.get(key);
            for (OverAreaDTO overArea : overAreaDTOList) {
                canvas.setFillColor(ColorConstants.WHITE);
                //覆盖的时候y + 0.2   填充字体的时候 + 1.5 主要就是避免覆盖占位符下面的线
                canvas.rectangle(overArea.getX(), overArea.getY() + 0.7, overArea.getWidth(), overArea.getHeight());
            }
            canvas.fill();
        }
        canvas.restoreState();

        //新内容填充
        for (String key : map.keySet()) {
            List<OverAreaDTO> overAreaDTOList = map.get(key);
            canvas.beginText();
            for (OverAreaDTO overArea : overAreaDTOList) {
                canvas.setFontAndSize(font,overArea.getHeight());
                canvas.setTextMatrix(overArea.getX(),overArea.getY() + 1.5f);
                canvas.newlineShowText(textValueMap.get(key));
            }
            canvas.endText();
        }
        pdfDoc.close();

    }


    public static void main(String[] args) throws IOException {

        OverTextDTO overTextDTO = new OverTextDTO();

        overTextDTO.setSourceFilePath("/Users/huluwa/Desktop/test.pdf");
        overTextDTO.setFinishFilePath("/Users/huluwa/Desktop/test2.pdf");

        PdfFont font = PdfFontFactory.createFont("/Users/huluwa/Desktop/SIMSUN.TTF", PdfEncodings.IDENTITY_H);

        overTextDTO.setFont(font);

        Map<String,String> replaceMap = new HashMap<String, String>();

        replaceMap.put("name1","天真");
        replaceMap.put("name2","假如");
        replaceMap.put("idCard1","987654321123456789");
        replaceMap.put("idCard2","123456789987654321");
        replaceMap.put("moneyD","伍万贰仟圆整");
        replaceMap.put("moneyX","52000");
        replaceMap.put("beginYear","2019");
        replaceMap.put("beginMonth","8");
        replaceMap.put("beginDay","15");
        replaceMap.put("endYear","2020");
        replaceMap.put("endMonth","05");
        replaceMap.put("endDay","20");
        replaceMap.put("sumMonth","10");
        replaceMap.put("interest","0.05");
        replaceMap.put("sumInterest","5200");
        replaceMap.put("returnType","一次性付清");
        replaceMap.put("year","2020");
        replaceMap.put("month","05");
        replaceMap.put("day","20");
        replaceMap.put("moneyWD","伍佰贰拾元整");
        replaceMap.put("moneyWX","520");
        replaceMap.put("sender","天真");
//        replaceMap.put("receiver","假如");
//        replaceMap.put("signYear","2019");
//        replaceMap.put("signMonth","08");
//        replaceMap.put("signDay","15");


        overTextDTO.setReplaceMap(replaceMap);

        doOverText(overTextDTO);

    }

}
