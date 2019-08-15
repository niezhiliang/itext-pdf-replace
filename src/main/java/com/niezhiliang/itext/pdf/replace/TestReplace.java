package com.niezhiliang.itext.pdf.replace;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;

import java.io.File;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-12 14:25
 */
public class TestReplace {

    public static final String DEST = "/Users/huluwa/Desktop/test2.pdf";
    public static final String SRC = "/Users/huluwa/Desktop/hello.pdf";

    public static final String FONT = "/Users/huluwa/Desktop/SIMSUN.TTF";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TestReplace().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfPage page = pdfDoc.getFirstPage();
        PdfDictionary dict = page.getPdfObject();

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        PdfObject object = dict.get(PdfName.Contents);
        if (object instanceof PdfStream) {
            PdfStream stream = (PdfStream) object;
            byte[] data = stream.getBytes();
            stream.setData(new String(data).replace("Hello World", "HELLO WORLD123456789 qwertyuioplkjhgfdsazxcvbnm哈哈").getBytes("UTF-8"));
        }
        pdfDoc.close();
    }


}
