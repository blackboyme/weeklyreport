package com.caac.weeklyreport.config.logback;

import lombok.Data;

import java.util.regex.Pattern;

/**
 * @CLASS_NAME RegexReplacement
 * @AUTHOR gonghao
 * @DATE 2022/6/21
 **/
@Data
public class RegexReplacement {
    /**
     * 脱敏匹配正则
     */
    private String regex = "";
    /**
     * 替换正则
     */
    private String replacement = "";
    /**
     * Perform the replacement.
     *
     * @param msg The String to match against.
     * @return the replacement String.
     */
    public String format(final String msg) {
        Pattern regexPatter = Pattern.compile(regex);
        return regexPatter.matcher(msg).replaceAll(replacement);
    }
}
