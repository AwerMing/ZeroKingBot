package com.zkb.bot.warframe.controller.freemarker.mission;

import com.zkb.bot.warframe.dao.GlobalStates;
import com.zkb.bot.warframe.dao.SocketGlobalStates;
import com.zkb.bot.warframe.service.IWarframeTranslationService;
import com.zkb.common.annotation.LogInfo;
import com.zkb.common.core.redis.RedisCache;
import com.zkb.common.enums.BusinessType;
import com.zkb.common.enums.TitleType;
import com.zkb.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.zkb.bot.enums.WarframeEnum.REDIS_MISSION_KEY;

@Controller
@RequestMapping("/warframe/mission")
public class InvasionsHtmlController {
    @Autowired
    RedisCache redisCache;

    @Autowired
    IWarframeTranslationService trans;

    @GetMapping(value = "/{uuid}/getInvasionsHtml")
    @LogInfo(title = TitleType.Warframe, orderType = "入侵", businessType = BusinessType.SELECT)
    public String getHtml(Model model) {
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        List<GlobalStates.Invasions> invasions = sgs.getPacket().getData().getInvasions();
        for (GlobalStates.Invasions invasion : invasions) {
            if (invasion.getAttackerReward().getCountedItems().size() > 0) {

                GlobalStates.Invasions.AttackerReward reward = new GlobalStates.Invasions.AttackerReward();
                reward.setCredits(invasion.getDefenderReward().getCredits());
                reward.setColor(invasion.getDefenderReward().getColor());
                reward.setThumbnail(invasion.getDefenderReward().getThumbnail());
                reward.setAsString(invasion.getDefenderReward().getAsString());
                List<GlobalStates.Invasions.AttackerReward.CountedItems> countedItems = new ArrayList<>();

                for (GlobalStates.Invasions.AttackerReward.CountedItems countedItem : invasion.getAttackerReward().getCountedItems()) {
                    countedItem.setType(trans.enToZh(countedItem.getType()));
                    countedItems.add(countedItem);
                }

                reward.setCountedItems(countedItems);
                invasion.setAttackerReward(reward);

            }
            if (invasion.getDefenderReward().getCountedItems().size() > 0) {
                GlobalStates.Invasions.DefenderReward defenderReward = new GlobalStates.Invasions.DefenderReward();
                defenderReward.setCredits(invasion.getDefenderReward().getCredits());
                defenderReward.setColor(invasion.getDefenderReward().getColor());
                defenderReward.setThumbnail(invasion.getDefenderReward().getThumbnail());
                defenderReward.setAsString(invasion.getDefenderReward().getAsString());
                List<GlobalStates.Invasions.DefenderReward.CountedItems> countedItems = new ArrayList<>();

                for (GlobalStates.Invasions.DefenderReward.CountedItems countedItem : invasion.getDefenderReward().getCountedItems()) {
                    countedItem.setType(trans.enToZh(countedItem.getType()));
                    countedItems.add(countedItem);
                }

                defenderReward.setCountedItems(countedItems);
                invasion.setDefenderReward(defenderReward);


            }
            invasion.setNode(invasion.getNode().replace(StringUtils.quStr(invasion.getNode()), trans.enToZh(StringUtils.quStr(invasion.getNode()))));
            invasion.setCompletion(String.format("%.2f", Double.valueOf(invasion.getCompletion())));
            invasion.setDesc(trans.enToZh(invasion.getDesc()));
        }
        model.addAttribute("inv", invasions);
        return "html/invasions";
    }
}
