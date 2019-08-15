package com.niezhiliang.itext.pdf.replace;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-13 09:30
 */
public class HightLight {

    public static final String DEST
            = "/Users/huluwa/Desktop/test2.pdf";
    public static final String SRC
            = "/Users/huluwa/Desktop/test.pdf";

    public static final String FONT = "/Users/huluwa/Desktop/SIMSUN.TTF";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HightLight().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        //pdfDoc.getFirstPage().newContentStreamAfter() 会覆盖掉字体
        //pdfDoc.getFirstPage().newContentStreamBefore() 只会在字体的下层添加一个背景色
        // 覆盖的时候y + 1.5   填充字体的时候 + 3
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(2).newContentStreamAfter(),
                pdfDoc.getPage(2).getResources(), pdfDoc);

        canvas.saveState();
        canvas.setFillColor(ColorConstants.WHITE);
        canvas.rectangle(209, 591.5, 70, 14);
        canvas.fill();
        canvas.restoreState();

        canvas.beginText();

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        canvas.setFontAndSize(font,14);
        canvas.setTextMatrix(209,593);
        canvas.newlineShowText("362202199509052515");
        canvas.endText();

        pdfDoc.close();
    }
}
