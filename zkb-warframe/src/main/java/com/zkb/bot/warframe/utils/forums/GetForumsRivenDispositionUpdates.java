package com.zkb.bot.warframe.utils.forums;


import com.zkb.bot.enums.WarframeRivenTrendEnum;
import com.zkb.bot.warframe.domain.WarframeRivenTrend;
import com.zkb.common.utils.http.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 从Warframe论坛抓取紫卡倾向更新数据
 */
@Component
public class GetForumsRivenDispositionUpdates {

    /**
     * 获取查询最新的论坛Url
     *
     * @return Url地址
     */
    private static List<String> getRivenDispositionUpdateUrl() {
        String html = HttpUtils.sendGetOkHttp(
                "https://forums.warframe.com/search/?&q=Riven&type=forums_topic&quick=1&nodes=123&search_and_or=and&search_in=titles&sortby=relevancy");

        //返回变量
        List<String> newRiven = new ArrayList<>();
        //解析Html文档
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("ipsStream ipsList_reset ");

        if (!elements.isEmpty()) {
            for (Element e : elements) {
                Elements span = e.getElementsByClass("ipsStreamItem ipsStreamItem_contentBlock ipsStreamItem_expanded ipsAreaBackground_reset ipsPad  ");
                for (Element li : span) {
                    Elements a = li.getElementsByClass("ipsType_break ipsContained");
                    for (Element url : a) {
                        newRiven.add(url.getElementsByTag("a").attr("href"));
                    }
                }
            }
        }

        return newRiven;
    }


    /**
     * 获取Warframe论坛中的紫卡倾向列表
     *
     * @return 紫卡倾向集
     */
    public static List<WarframeRivenTrend> getRivenDispositionUpdates() {
        //获取文档Url地址
        List<String> urls = getRivenDispositionUpdateUrl();
        //用于存放返回的结果
        List<WarframeRivenTrend> trends = new ArrayList<>();
        for (String url : urls) {
            //Get请求获取Html文档
            String html = HttpUtils.sendGetOkHttp(url);

            //解析Html文档
            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByClass("ipsSpoiler_contents ipsClearfix");
            if (elements.size() > 1) {
                //获取发帖日期
                Elements times = document.getElementsByClass("ipsType_light ipsType_reset");
                String dateTime = "";
                for (Element el : times) {
                    dateTime = el.getElementsByTag("time").attr("datetime").replace("T", " ").replace("Z", "").trim();
                    break;
                }

                //根据类选择器 获取此类中的元素
                //Elements elements = document.getElementsByClass("ipsSpoiler_contents ipsClearfix");
                //遍历此类中元素
                for (Element element : elements) {
                    //根据标签选择器 获取此标签中的元素
                    Elements p = element.getElementsByTag("p");
                    //遍历 p 标签中的元素
                    for (Element e1 : p) {
                        //格式化
                        String br = String.valueOf(e1).replace("<br>", "HN");
                        //重新解析格式化后的Html
                        Document doc = Jsoup.parse(br);
                        //遍历此标签中的元素
                        for (Element e2 : doc.getElementsByTag("p")) {
                            String[] rivens = e2.text().split("HN");
                            for (String riven : rivens) {
                                String[] wep = riven.split(":");
                                String[] rmnt = wep[1].split("->");
                                WarframeRivenTrend trend = new WarframeRivenTrend();
                                trend.setRivenTrendName(wep[0]);
                                String newNum = rmnt[1].replaceAll("[^\\d.]", "");
                                trend.setRivenTrendNewNum(newNum);
                                trend.setRivenTrendOldNum(rmnt[0]);
                                trend.setRivenTrendNewDot(WarframeRivenTrendEnum.getRivenTrendDot(Double.parseDouble(newNum)));
                                trend.setRivenTrendOldDot(WarframeRivenTrendEnum.getRivenTrendDot(Double.parseDouble(rmnt[0])));
                                trend.setIsDate(dateTime);
                                trends.add(trend);
                            }
                        }
                    }
                }
            }
            if (trends.size() != 0) {
                return trends;
            }
        }

        return trends;
    }


}
