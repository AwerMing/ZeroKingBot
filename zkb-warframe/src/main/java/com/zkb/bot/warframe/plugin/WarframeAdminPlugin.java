package com.zkb.bot.warframe.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.zkb.bot.utils.InitConfigs;
import com.zkb.bot.utils.Msg;
import com.zkb.bot.warframe.task.RivenDispositionUpdatesTask;
import com.zkb.bot.warframe.utils.WarframeTraUtils;
import com.zkb.bot.warframe.utils.market.RenewMarketUtil;
import com.zkb.common.load.ReadAdminConfig;
import com.zkb.common.load.ReadWarframeConfig;
import com.zkb.common.utils.spring.SpringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.zkb.bot.enums.WarframeTypeEnum.*;

@Component
public class WarframeAdminPlugin extends BotPlugin {

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if(event.getRawMessage().trim().length()==0){
            return MESSAGE_IGNORE;
        }

        if (event.getUserId() == ReadAdminConfig.getAdmin()) {
            if (TYPE_RES_MARKET_ITEMS.getType().equals(event.getRawMessage())) {
                int x = RenewMarketUtil.resMarketItems();
                bot.sendPrivateMsg(event.getUserId(), "更新成功，共更新" + x + "条数据!", false);
            }
            if (TYPE_RES_MARKET_RIVEN.getType().equals(event.getRawMessage())) {
                int x = RenewMarketUtil.resMarketRiven();
                bot.sendPrivateMsg(event.getUserId(), "更新成功，共更新" + x + "条数据!", false);
            }
            if ("更新紫卡倾向变动".equals(event.getRawMessage())) {
                try {
                    new RivenDispositionUpdatesTask().renewRivenDisposition();
                    bot.sendPrivateMsg(event.getUserId(), "已执行请稍后，在群内使用 紫卡倾向变动查看", false);
                    return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if ("更新信条".equals(event.getRawMessage())) {
                Msg msg = new Msg();
                int[] i = RenewMarketUtil.resMarketSister();
                msg.text("更新信条武器条数：" + i[0]).text("\n更新信条幻纹条数：" + i[1]).build();
                bot.sendPrivateMsg(event.getUserId(), msg.build(), false);
            }
            if ("更新翻译".equals(event.getRawMessage())) {
                bot.sendPrivateMsg(event.getUserId(), "正在准备更新", false);
                int i = SpringUtils.getBean(WarframeTraUtils.class).getUserDict();
                bot.sendPrivateMsg(event.getUserId(), "更新完成，共更新：" + i + "条数据", false);

            }
            if("更新WF指令".equals(event.getRawMessage())){
                bot.sendPrivateMsg(event.getUserId(), "正在准备更新", false);
                ReadWarframeConfig.initWarframeConfig();
                InitConfigs.initWarframeConfig();
                bot.sendPrivateMsg(event.getUserId(), "更新完成", false);
            }
            if (TYPE_CODE.getType().equals(event.getRawMessage())) {
                bot.sendPrivateMsg(event.getUserId(), "更新WM物品\n更新WM紫卡\n更新信条\n更新翻译\n更新紫卡倾向变动\n更新WF指令", false);
            }

        }
        return 0;
    }
}
