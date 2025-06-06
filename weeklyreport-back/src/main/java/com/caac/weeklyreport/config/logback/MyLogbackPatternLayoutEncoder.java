package com.caac.weeklyreport.config.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import lombok.Data;

/**
 * @CLASS_NAME MyLogbackPatternLayoutEncoder
 * @AUTHOR gonghao
 * @DATE 2022/6/21
 **/
@Data
public class MyLogbackPatternLayoutEncoder extends PatternLayoutEncoder {
    /**
     * 正则替换规则
     */
    private MyLogbackReplaces replaces = new MyLogbackReplaces();
    /**
     * 是否开启脱敏，默认关闭(false）
     */
    private Boolean sensitive = false;

    /**
     * 使用自定义TbspLogbackPatternLayout格式化输出
     */
    @Override
    public void start() {
        MyLogbackPatternLayout patternLayout = new MyLogbackPatternLayout(replaces, sensitive);
        patternLayout.setContext(context);
        patternLayout.setPattern(this.getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        started = true;
    }

}

