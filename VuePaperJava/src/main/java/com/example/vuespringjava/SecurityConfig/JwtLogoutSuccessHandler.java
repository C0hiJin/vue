package com.example.vuespringjava.SecurityConfig;

import cn.hutool.json.JSONUtil;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    JwtUtils JwtUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        if (authentication!=null){
            new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        }
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        //        生成JWT(身份凭证),并放置到请求头中
        httpServletResponse.setHeader(JwtUtils.getHeader(),"");

        Result rusult = Result.succ("");


        outputStream.write(JSONUtil.toJsonStr(rusult).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
