package com.jackdaw.jinjobbackendcommon.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SpiderTest {
    public static void main(String[] args) {

        /*String url = "https://fe.ecool.fun/api/exercise/list?vid=9&tagId=&pageNum=1&pageSize=10000&orderBy=updateTime&order=desc";
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "https://fe.ecool.fun");
        String response = OKHttpUtils.getRequest(url, header);
        System.out.println(response);

        String html = OKHttpUtils.getRequest("https://fe.ecool.fun/topic/3a6b4cce-0829-42dc-9629-c97fa85312f0", header);
        //  System.out.println(html);

        String[] dataArray = html.split("\n");

        String data = null;

        for (String item : dataArray) {
            if (item.contains("window.g_initialProps")) {
                data = item;
            }
        }
        data = data.substring(data.indexOf("{"));
        data = data.substring(0, data.lastIndexOf("}"));

        System.out.println(data);*/

        List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        System.out.println(StringUtils.join(data, ","));
    }
}
