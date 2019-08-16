package com.niezhiliang.itext.pdf.replace.utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
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
        List<List<OverAreaDTO>> lists =
                getTextPosition(overTextDTO.getSourceFilePath(),overTextDTO.getFinishFilePath(),keyMap,overTextDTO.getReplaceMap());

        overText(overTextDTO.getSourceFilePath(),overTextDTO.getFinishFilePath(),lists,overTextDTO.getFont());
    }

    /**
     * 获取关键字的坐标
     * @param keyMap
     * @return
     */
    public static  List<List<OverAreaDTO>> getTextPosition(String sourcePath,String finishPath, Map<String,String> keyMap,Map<String,String> replaceMap) throws IOException {

        PdfReader reader = new PdfReader(sourcePath);
        PdfDocument pdfDocument = new PdfDocument(reader, new PdfWriter(finishPath));

        List<List<OverAreaDTO>> list = new ArrayList<List<OverAreaDTO>>();

        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {


            List<OverAreaDTO> overAreaDTOS = new ArrayList<OverAreaDTO>();

            for (String key  : keyMap.keySet()) {

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
                    overAreaDTO.setKey(key);
                    overAreaDTO.setValue(replaceMap.get(key));
                    overAreaDTOS.add(overAreaDTO);
                }
            }

            list.add(overAreaDTOS);
        }
        pdfDocument.close();

        return list;
    }

    /**
     * 覆盖原有的内容 并填充新内容
     *
     * @param sourcePath 源文件
     * @param finishPath 替换后的文件
     * @param list
     */
    public static void overText(String sourcePath,String finishPath,List<List<OverAreaDTO>> list,PdfFont font) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourcePath), new PdfWriter(finishPath));
        //pdfDoc.getFirstPage().newContentStreamAfter() 会覆盖掉字体
        //pdfDoc.getFirstPage().newContentStreamBefore() 只会在字体的下层添加一个背景色
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(i).newContentStreamAfter(),
                    pdfDoc.getPage(i).getResources(), pdfDoc);

            canvas.saveState();
            List<OverAreaDTO> overAreaDTOS = list.get(i-1);
            //用白色背景覆盖原本的字体
            for (OverAreaDTO overArea :overAreaDTOS) {
                canvas.setFillColor(ColorConstants.WHITE);
                //覆盖的时候y + 0.35   填充字体的时候 + 1.5 主要就是避免覆盖占位符下面的线
                canvas.rectangle(overArea.getX(), overArea.getY() + 0.35, overArea.getWidth(), overArea.getHeight());
            }
            canvas.fill();
            canvas.restoreState();

            //填充新内容
            canvas.beginText();
            for (OverAreaDTO overArea :overAreaDTOS) {
                canvas.setFontAndSize(font,overArea.getHeight());
                canvas.setTextMatrix(overArea.getX(),overArea.getY() + 1.5f);
                canvas.newlineShowText(overArea.getValue());
            }
            canvas.endText();
        }

        pdfDoc.close();

    }
}
