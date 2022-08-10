package com.twg.bot.warframe.controller.mission;

import com.twg.bot.warframe.dao.Nightwave;
import com.twg.bot.warframe.utils.HtmlToImage;
import com.twg.bot.warframe.utils.WarframeUtils;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/warframe/mission")
public class NighTwaveImageController {

    @Autowired
    RedisCache redis;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getNighTwaveImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");

        Nightwave n = SpringUtils.getBean(WarframeUtils.class).getNighTwave();
        ByteArrayOutputStream out = SpringUtils.getBean(HtmlToImage.class).nighTwaveImage(n);
        response.getOutputStream().write(out.toByteArray());

    }
}
