package com.niezhiliang.itext.pdf.replace.dto;

import com.itextpdf.kernel.font.PdfFont;
import lombok.Data;

import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-15 10:44
 */
@Data
public class OverTextDTO {

    private String sourceFilePath;

    private String finishFilePath;

    private PdfFont font;

    private Map<String,String> replaceMap;
}
