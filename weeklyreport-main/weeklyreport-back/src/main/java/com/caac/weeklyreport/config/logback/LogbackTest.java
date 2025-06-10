package com.caac.weeklyreport.config.logback;//package com.abchina.access.config.logback;
//
//import com.abchina.access.utils.LogBacks;
//import org.springframework.stereotype.Component;
//
///**
// * @CLASS_NAME LogbackTest
// * @AUTHOR gonghao
// * @DATE 2022/6/22
// **/
//@Component
//public class LogbackTest{
////    @Override
////    public void run(ApplicationArguments args) throws Exception {
////        test1();
////    }
//
//    /**
//     * 测试
//     */
//    public void test1() {
//        // 11位手机号
//        LogBacks.info("mobile={}", "13511114444");
//        LogBacks.info("mobile={},手机号：{}", "13511112222", "13511113333");
//        LogBacks.info("手机号：{}", "13511115555");
//        // 固定电话（带区号-）
//        LogBacks.info("tel：{},座机={}", "0791-83376222", "021-88331234");
//        LogBacks.info("tel：{}", "0791-83376222");
//        LogBacks.info("座机={}", "021-88331234");
//
//        // 身份证号码
//        LogBacks.info("idCard：{}，身份证号={}", "360123202111111122", "360123202111111122");
//        LogBacks.info("身份证号={}", "360123202111111122");
//
//        // 邮箱
//        LogBacks.info("邮箱:{}", "zhangs12345@google.com");
//        LogBacks.info("email={}", "zhangs12345@google.com");
//
//        // 姓名
//        LogBacks.info("name:{}", "c韩仁杰c");
//    }
//}
