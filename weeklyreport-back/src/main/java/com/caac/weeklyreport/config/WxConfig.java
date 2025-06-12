package com.caac.weeklyreport.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {
    @Value("${wechat.appid}")
    private String miniAppId;
    @Value("${wechat.secret}")
    private String miniSecret;
    private WxMaUserService wxMaUserService;

    @javax.annotation.PostConstruct
    public void init() {
        //微信小程序
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(miniAppId);
        wxMaConfig.setSecret(miniSecret);

        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaConfig);
        this.wxMaUserService = wxMaService.getUserService();
    }

    @Bean(name = "wxMaUserService")
    public WxMaUserService wxMaUserService(){
        return this.wxMaUserService;
    }



}


