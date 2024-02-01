package com.example.vuespringjava.controller;

import cn.hutool.core.map.MapUtil;
import com.example.vuespringjava.common.lang.Const;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.service.SysUserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
public class AuthController extends BaseController {
    @Autowired
    DefaultKaptcha producer;

    @Autowired
    SysUserService sysUserService;

    //    生成图片验证码
    @GetMapping("/captcha")
    public Result captcha() throws IOException {
//        使用UUID生成随机key
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
//        测试接口暂时设置成
        key = "11111";
        code = "11111";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder base64Encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String bsse64IMG = str + base64Encoder.encode(outputStream.toByteArray());

        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        System.out.println(redisUtil.hget(Const.CAPTCHA_KEY, code));
        return Result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", bsse64IMG)
                        .build()
        );
    }
//    用户信息接口

    /**
     *{
     *         "id": 2,
     *         "created": "2021-01-30T08:20:22",
     *         "updated": "2021-01-30T08:55:57",
     *         "statu": 1,
     *         "username": "test",
     *         "password": "$2a$10$0ilP4ZD1kLugYwLCs4pmb.ZT9cFqzOZTNaMiHxrBnVIQUGUwEvBIO",
     *         "avatar": "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg",
     *         "email": "test@qq.com",
     *         "city": null,
     *         "lastLogin": null,
     *         "roles": []
     *     }
     * @return
     */
    @GetMapping("/sys/userInfo")
    public Result UserInfo(Principal principal){
        SysUser Username = sysUserService.getByUsername(principal.getName());
        return Result.succ(MapUtil.builder()
                .put("id",Username.getId())
                .put(  "created",Username.getCreated())
                .put("updated",Username.getUpdated())
                .put("statu",Username.getStatu())
                .put("username",Username.getUsername())
                .put("password",Username.getPassword())
                .put("avatar",Username.getAvatar())
                .put("email",Username.getEmail())
                .put("city",Username.getCity())
                .put("lastLogin",Username.getLastLogin())
                .map()
        );
    }
}
