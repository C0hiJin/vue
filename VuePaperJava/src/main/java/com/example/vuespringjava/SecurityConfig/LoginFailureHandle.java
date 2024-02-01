package com.example.vuespringjava.SecurityConfig;

import cn.hutool.json.JSONUtil;
import com.example.vuespringjava.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandle implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        Result rusult = Result.fail(e.getMessage().equals("验证码错误")?"验证码错误":"用户名或者密码错误");
        outputStream.write(JSONUtil.toJsonStr(rusult).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
