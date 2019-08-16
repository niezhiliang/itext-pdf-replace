package com.niezhiliang.itext.pdf.replace.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-15 10:50
 */
@Data
@ToString
public class OverAreaDTO {

    private String key;

    private String value;

    private Integer pageNum;

    private Float x;

    private Float y;

    private Float width;

    private Float height;
}
