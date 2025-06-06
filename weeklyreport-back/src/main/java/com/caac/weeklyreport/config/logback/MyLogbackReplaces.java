package com.caac.weeklyreport.config.logback;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @CLASS_NAME MyLogbackReplaces
 * @AUTHOR gonghao
 * @DATE 2022/6/21
 **/
@Data
public class MyLogbackReplaces {
    /**
     * 脱敏正则列表
     */
    private List<RegexReplacement> replace = new ArrayList<>();
    /**
     * 添加规则（因为replace类型是list，必须指定addReplace方法用以添加多个）
     *
     * @param replacement replacement
     */
    public void addReplace(RegexReplacement replacement) {
        replace.add(replacement);
    }
}
