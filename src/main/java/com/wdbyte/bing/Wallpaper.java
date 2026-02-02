package com.wdbyte.bing;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.wdbyte.bing.html.HtmlFileUtils;
import com.wdbyte.bing.html.WebSiteGenerator;

/**
 * @author niujinpeng
 * @date 2021/02/08
 * @link https://github.com/niumoo
 */
public class Wallpaper {

    // BING API
    private static final String BING_API_TEMPLATE = "https://global.bing.com/HPImageArchive.aspx?format=js&idx=0&n=9&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160&setmkt=%s&setlang=en";
    private static String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=10&nc=1612409408851&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160";

    private static String BING_URL = "https://cn.bing.com";

    /**
     *
     * {"en-US", "zh-CN", "ja-JP", "en-IN", "pt-BR", "fr-FR", "de-DE", "en-CA", "en-GB", "it-IT", "es-ES", "fr-CA"};
     */
    private static String[] regions =  {"zh-CN"};

    public static String CURRENT_REGION = "en-US";

    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        for (String region : regions) {
            changeConfig(region);
            
            // 跳过API获取，直接生成HTML
            List<Images> imagesList = BingFileUtils.readBing();
            BingFileUtils.writeBing(imagesList);
            
            // 创建WebSiteGenerator实例并调用
            WebSiteGenerator generator = new WebSiteGenerator();
            generator.htmlGenerator();
        }
    }
    
    /**
     * 简化描述，去掉括号中的版权信息
     */
    private static String simplifyDescription(String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            return desc;
        }
        
        // 查找第一个括号的位置
        int firstParenIndex = desc.indexOf('(');
        if (firstParenIndex > 0) {
            // 去掉括号及其内容
            String simplified = desc.substring(0, firstParenIndex).trim();
            // 去掉末尾可能的空格和标点
            if (simplified.endsWith(",")) {
                simplified = simplified.substring(0, simplified.length() - 1).trim();
            }
            return simplified;
        }
        return desc;
    }

    public static void changeConfig(String region) {
        region = region.toLowerCase();
        CURRENT_REGION = region;
        if ("en-us".equalsIgnoreCase(region)) {
            BingFileUtils.README_PATH = Paths.get("README.md");
            BingFileUtils.BING_PATH = Paths.get("bing-wallpaper.md");
            BingFileUtils.MONTH_PATH = Paths.get("picture/");
            HtmlFileUtils.BING_HTML_ROOT = Paths.get("docs/");
        } else {
            BingFileUtils.README_PATH = Paths.get(region + "/README.md");
            BingFileUtils.BING_PATH = Paths.get(region + "/bing-wallpaper.md");
            BingFileUtils.MONTH_PATH = Paths.get(region + "/picture/");
            HtmlFileUtils.BING_HTML_ROOT = Paths.get("docs/" + region + "/");
        }
    }

}
