package com.niezhiliang.itext.pdf.replace;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;
import java.util.*;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-13 11:21
 */
public class TextPosition {

    public static void main(String[] args) throws Exception {
        findPosition();
    }

    /**
     * 关键字定位
     * @throws Exception
     */
    public static void findPosition() throws Exception {
        String sourceFolder2 = "/Users/huluwa/Desktop/hello.pdf";
        String output = "/Users/huluwa/Desktop/test2.pdf";
        PdfReader reader = new PdfReader(sourceFolder2);
        PdfDocument pdfDocument = new PdfDocument(reader, new PdfWriter(output));
        PdfPage lastPage = pdfDocument.getLastPage();
        RegexBasedLocationExtractionStrategy strategy = new RegexBasedLocationExtractionStrategy("ello");
        PdfCanvasProcessor canvasProcessor = new PdfCanvasProcessor(strategy);
        canvasProcessor.processPageContent(lastPage);
        Collection<IPdfTextLocation> resultantLocations = strategy.getResultantLocations();
        PdfCanvas pdfCanvas = new PdfCanvas(lastPage);
        pdfCanvas.setLineWidth(0.5f);
        List<IPdfTextLocation> sets = new ArrayList();
        for (IPdfTextLocation location : resultantLocations) {
            Rectangle rectangle = location.getRectangle();
            pdfCanvas.rectangle(rectangle);
            pdfCanvas.setStrokeColor(ColorConstants.RED);
            pdfCanvas.stroke();
            System.out.println(rectangle.getX() + "," + rectangle.getY() + "," + rectangle.getLeft() + "," +
                    rectangle.getRight() + "," + rectangle.getTop() + "," + rectangle.getBottom() + "," +
                    rectangle.getWidth() + "," + rectangle.getHeight());
            System.out.println(location.getText());
            sets.add(location);
        }
        Collections.sort(sets, new Comparator<IPdfTextLocation>() {

            public int compare(IPdfTextLocation o1, IPdfTextLocation o2) {
                return o1.getRectangle().getY() - o2.getRectangle().getY() > 0 ? 1 : o1.getRectangle().getY() - o2.getRectangle().getY() == 0 ? 0 : -1;
            }
        });
        System.out.println(sets.get(0).getRectangle().getY());
        pdfDocument.close();
    }

}
