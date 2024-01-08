package com.jackdaw.jinjobbackendcommon.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
    public static String getTextFromHtml(String htmlString) {
        return Jsoup.parse(htmlString).text().toString().trim();
    }
}
