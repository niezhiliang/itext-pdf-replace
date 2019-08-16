package com.niezhiliang.itext.pdf.replace;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.niezhiliang.itext.pdf.replace.dto.OverTextDTO;
import com.niezhiliang.itext.pdf.replace.utils.ReplaceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-16 10:18
 */
public class Test {


    public static void main(String[] args) throws IOException {

        OverTextDTO overTextDTO = new OverTextDTO();

        overTextDTO.setSourceFilePath("./data/test.pdf");
        overTextDTO.setFinishFilePath("./data/test2.pdf");

        PdfFont font = PdfFontFactory.createFont("./data/SIMSUN.TTF", PdfEncodings.IDENTITY_H);

        overTextDTO.setFont(font);

        Map<String,String> replaceMap = new HashMap<String, String>(25);

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
        replaceMap.put("receiver","假如");
        replaceMap.put("signYear","2019");
        replaceMap.put("signMonth","08");
        replaceMap.put("signDay","15");


        overTextDTO.setReplaceMap(replaceMap);

        ReplaceUtils.doOverText(overTextDTO);
    }
}
