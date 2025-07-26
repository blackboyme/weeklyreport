package com.caac.weeklyreport.biz.user.controller;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.biz.user.entity.vo.LoginAutoVO;
import com.caac.weeklyreport.biz.user.entity.vo.LoginVO;
import com.caac.weeklyreport.biz.user.service.UserService;
import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.biz.user.entity.PhoneInfo;
import com.caac.weeklyreport.exception.BusinessException;
import com.caac.weeklyreport.util.LogBacks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "登录",tags="登录")
public class UserController {

    @Autowired
    private WxMaUserService wxMaUserService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "自行登录", tags = "登录")
    @PostMapping("/login")
    public ResultBean<?> login(@RequestBody LoginAutoVO vo) {
        if (StringUtils.isEmpty(vo.getOpenId())) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        UserInfo userInfo = userService.login(vo.getPhoneNo(),vo.getOpenId());
        if (userInfo == null) {
            return ResultBean.fail("登录失败：用户不存在");
        }
        return ResultBean.success(userInfo);
    }
    /*
    * 新登录接口
    * */
    @ApiOperation(value = "微信登录", tags = "登录")
    @PostMapping("/loginAndGetPhone")
    public ResultBean<?> getPhoneNumber(@RequestBody LoginVO loginVO) {
        String encryptedData = loginVO.getEncryptedData();
        String iv = loginVO.getIv();
        String code = loginVO.getCode();
        LogBacks.error("iv:",iv);
        LogBacks.error("code:",code);
        if(StringUtils.isEmpty(iv) && StringUtils.isEmpty(code)) {
            throw new BusinessException("iv和code为空");
        }
        if(StringUtils.isEmpty(iv)) {
            throw new BusinessException("iv为空");
        }
        if(StringUtils.isEmpty(code)) {
            throw new BusinessException("code为空");
        }
        WxMaJscode2SessionResult session = null;
        try {
            session = wxMaUserService.getSessionInfo(code);
        } catch (WxErrorException e) {
            LogBacks.error("微信获取session失败:"+e.getMessage());
            throw new BusinessException("微信获取session失败:"+e.getMessage());
        }
        if(session == null) {
            LogBacks.error("微信获取session失败:session为空");
            throw new BusinessException("微信获取session失败:session为空");
        }
        LogBacks.error("session:",session);
        String key = session.getSessionKey();
        String openid = session.getOpenid();
        if(StringUtils.isAnyBlank(key,openid)) {
            LogBacks.error("微信调取失败：key或者openId为空");
            throw new BusinessException("微信调取失败：key或者openId为空");
        }
        LogBacks.error("openId:",openid);



        // 解密encryptedData
        String phoneNumber = decryptPhoneNumber(encryptedData, key, iv);
        //{"phoneNumber":"18502820522","purePhoneNumber":"18502820522","countryCode":"86","watermark":{"timestamp":1749630283,"appid":"wxb92b2b0cb5c17a28"}}
        ObjectMapper objectMapper = new ObjectMapper();
        // 将JSON字符串转换为PhoneInfo对象
        PhoneInfo phoneInfo = null;
        try {
            phoneInfo = objectMapper.readValue(phoneNumber, PhoneInfo.class);
        } catch (JsonProcessingException e) {
            LogBacks.error("微信解析参数失败:"+e.getMessage());
            throw new BusinessException("微信解析参数失败:"+e.getMessage());
        }
        String number = phoneInfo.getPhoneNumber();
        if (StringUtils.isEmpty(number)) {
            LogBacks.error("手机号不能为空");
            return ResultBean.fail("手机号不能为空");
        }
        UserInfo userInfo = userService.login(number,openid);
        if (userInfo != null) {
            return ResultBean.success(userInfo);
        } else {
            return ResultBean.fail("登录失败：用户不存在");
        }
    }
    private String decryptPhoneNumber(String encryptedData, String sessionKey, String iv) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }


    @ApiOperation(value = "验证token", tags = "登录")
    @PostMapping("/validate-token")
    public ResultBean<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResultBean.fail("Invalid Authorization header");
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        UserInfo userInfo = userService.validateUserByToken(token);
        if (userInfo != null) {
            return ResultBean.success(userInfo);
        } else {
            return ResultBean.fail("无效的token");
        }
    }

    @ApiOperation(value = "刷新token", tags = "登录")
    @PostMapping("/refresh-token")
    public ResultBean<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResultBean.fail("Invalid Authorization header");
        }
        String oldToken = authHeader.substring(7); // Remove "Bearer " prefix

        String newToken = userService.refreshToken(oldToken);
        if (newToken != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", newToken);
            return ResultBean.success(response);
        } else {
            return ResultBean.fail("无效的token");
        }
    }

    @ApiOperation(value = "获取团队所有用户", tags = "用户管理")
    @PostMapping("/getAllUserByTeamId/{teamId}")
    public ResultBean<?> getAllUserByTeamId(@PathVariable String teamId) {
        return ResultBean.success(userService.getAllUserByTeamId(teamId));
    }

    @ApiOperation(value = "获取所有用户", tags = "用户管理")
    @PostMapping("/getAllUser")
    public ResultBean<?> getAllUser() {
        return ResultBean.success(userService.getAllUser());
    }
}
