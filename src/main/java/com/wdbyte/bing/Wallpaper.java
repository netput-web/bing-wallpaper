package com.wdbyte.bing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.wdbyte.bing.html.HtmlFileUtils;
import com.wdbyte.bing.html.WebSiteGenerator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private static String[] regions =  {"en-US", "zh-CN", "ja-JP", "en-IN", "pt-BR", "fr-FR", "de-DE", "en-CA", "en-GB", "it-IT", "es-ES", "fr-CA"};

    public static String CURRENT_REGION = "en-US";

    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== Bing Wallpaper 数据处理开始 ===");
        
        // 显示当前数据统计
        JsonDataManager.printStatistics();
        
        for (String region : regions) {
            System.out.println("\n--- 处理区域: " + region + " ---");
            changeConfig(region);
            
            try {
                // 1. 从API获取新图片
                System.out.println("检查新数据...");
                boolean hasNewData = BingApiFetcher.hasNewData(region);
                
                List<Images> newImages = new ArrayList<>();
                if (hasNewData) {
                    System.out.println("发现新数据，开始获取...");
                    newImages = BingApiFetcher.fetchLatestImages(region);
                } else {
                    System.out.println("没有新数据");
                }
                
                // 2. 读取现有JSON数据
                List<Images> existingData = JsonDataManager.readDataByRegion(region.toLowerCase());
                
                // 3. 合并数据
                List<Images> allData = JsonDataManager.mergeData(existingData, newImages);
                
                // 4. 验证数据完整性
                JsonDataManager.validateData(allData);
                
                // 5. 更新JSON文件（只更新当前区域的数据）
                updateRegionDataInJson(region.toLowerCase(), allData);
                
                // 6. 生成HTML文件
                System.out.println("生成HTML文件...");
                WebSiteGenerator generator = new WebSiteGenerator();
                generator.htmlGenerator();
                
                System.out.println("区域 " + region + " 处理完成");
                
            } catch (Exception e) {
                System.err.println("处理区域 " + region + " 时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 7. 最终统计
        System.out.println("\n=== 最终数据统计 ===");
        JsonDataManager.printStatistics();
        
        System.out.println("=== Bing Wallpaper 数据处理完成 ===");
    }
    
    /**
     * 更新指定区域的数据到JSON文件
     */
    private static void updateRegionDataInJson(String region, List<Images> regionData) throws Exception {
        // 读取所有数据
        List<Images> allData = JsonDataManager.readAllData();
        
        // 移除当前区域的旧数据
        allData = allData.stream()
                .filter(img -> !region.equalsIgnoreCase(img.getRegion()))
                .collect(Collectors.toList());
        
        // 添加当前区域的新数据
        allData.addAll(regionData);
        
        // 重新写入JSON
        JsonDataManager.writeData(allData);
        
        System.out.println("区域 " + region + " 数据已更新: " + regionData.size() + " 条");
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
        
        // 统一配置：所有区域都使用JSON数据存储
        if ("en-us".equalsIgnoreCase(region)) {
            BingFileUtils.README_PATH = Paths.get("README.md");
            BingFileUtils.MONTH_PATH = Paths.get("docs/day/");
            HtmlFileUtils.BING_HTML_ROOT = Paths.get("docs/");
        } 
        // 中文区 - 使用区域特定的路径
        else if ("zh-cn".equalsIgnoreCase(region)) {
            BingFileUtils.README_PATH = Paths.get("zh-cn/README.md");
            BingFileUtils.MONTH_PATH = Paths.get("zh-cn/picture/");
            HtmlFileUtils.BING_HTML_ROOT = Paths.get("docs/zh-cn/");
        }
        // 其他区域 - 统一使用JSON存储
        else {
            BingFileUtils.README_PATH = Paths.get("README.md"); // 使用主README
            BingFileUtils.MONTH_PATH = Paths.get("docs/day/"); // 使用统一路径
            HtmlFileUtils.BING_HTML_ROOT = Paths.get("docs/" + region + "/"); // HTML仍生成到对应目录
        }
    }

}
