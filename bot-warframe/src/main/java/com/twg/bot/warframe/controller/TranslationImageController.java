package com.twg.bot.warframe.controller;

import com.twg.bot.warframe.utils.HtmlToImage;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;

@RestController
@RequestMapping
public class TranslationImageController {
    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getTraImage/{key}")
    public void getImage(HttpServletResponse response, @PathVariable String key) throws IOException {
        response.setHeader("Content-Type", "image/gif");
        ByteArrayOutputStream out = SpringUtils.getBean(HtmlToImage.class).translationImage(URLDecoder.decode(key, "UTF-8"));
        response.getOutputStream().write(out.toByteArray());
    }
}
