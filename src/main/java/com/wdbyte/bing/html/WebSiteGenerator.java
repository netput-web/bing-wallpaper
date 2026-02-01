package com.wdbyte.bing.html;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import com.wdbyte.bing.Wallpaper;
import com.wdbyte.bing.html.HtmlConstant.Head;
import com.wdbyte.bing.html.HtmlConstant.ImgCard;
import com.wdbyte.bing.html.HtmlConstant.ImgDetail;
import com.wdbyte.bing.html.HtmlConstant.MonthHistory;
import com.wdbyte.bing.html.HtmlConstant.Sidebar;
import com.wdbyte.bing.utils.CalendarDataGenerator;
import com.wdbyte.bing.utils.CalendarDataGeneratorOptimized;

/**
 * @author niulang
 * @date 2022/07/31
 */
public class WebSiteGenerator {

    public static void main(String[] args) throws IOException {
        System.out.println("🎯 WebSiteGenerator main() 方法开始执行...");
        WebSiteGenerator generator = new WebSiteGenerator();

        List<Images> bingImages = BingFileUtils.readBing();
        bingImages = bingImages.stream().filter(img -> img.getUrl() != null).collect(Collectors.toList());
        // 基于URL去重处理，避免重复显示相同图片
        bingImages = bingImages.stream()
                .collect(Collectors.toMap(
                    Images::getUrl, 
                    img -> img, 
                    (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // 按日期倒序排列
                .collect(Collectors.toList());

        Map<String, List<Images>> monthMap = BingFileUtils.convertImgListToMonthMap(bingImages);
        generator.htmlGeneratorIndex(bingImages, monthMap);
        generator.htmlGeneratorMonth(monthMap);
        generator.htmlGeneratorImgDetail(bingImages);
        generator.htmlGeneratorImgJson(bingImages);
        
        System.out.println("🎯 准备调用日历数据生成...");
        // 🚀 第一阶段：生成日历数据文件
        generator.generateCalendarDataFiles(bingImages);
        
        System.out.println("🎯 WebSiteGenerator main() 方法执行完成！");
    }

    public void htmlGenerator() throws IOException {
        List<Images> bingImages = BingFileUtils.readBing();
        bingImages = bingImages.stream().filter(img -> img.getUrl() != null).collect(Collectors.toList());
        // 基于URL去重处理，避免重复显示相同图片
        bingImages = bingImages.stream()
                .collect(Collectors.toMap(
                    Images::getUrl, 
                    img -> img, 
                    (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // 按日期倒序排列
                .collect(Collectors.toList());
        Map<String, List<Images>> monthMap = BingFileUtils.convertImgListToMonthMap(bingImages);
        htmlGeneratorIndex(bingImages, monthMap);
        htmlGeneratorToday(bingImages);
        htmlGeneratorMonth(monthMap);
        htmlGeneratorImgDetail(bingImages);
        htmlGeneratorImgJson(bingImages);
        
        // 🚀 第一阶段：生成日历数据文件
        generateCalendarDataFiles(bingImages);
    }

    private void htmlGeneratorToday(List<Images> bingImages) throws IOException {
        String url = bingImages.get(0).getUrl();
        String fileName = String.format("%s_%s.jpg", Wallpaper.CURRENT_REGION, bingImages.get(0).getDate());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file_name", fileName);
        jsonObject.put("url", url);
        jsonObject.put("date", bingImages.get(0).getDate());
        jsonObject.put("region", Wallpaper.CURRENT_REGION);
        jsonObject.put("desc", bingImages.get(0).getDesc());
        HtmlFileUtils.writeToday(jsonObject.toString());
    }

    public void htmlGeneratorIndex(List<Images> bingImages, Map<String, List<Images>> monthMap) throws IOException {
        String templateFile = HtmlFileUtils.readIndexTemplateFile();
        // 替换头部图片和描述
        String indexHtml = replaceHead(templateFile, bingImages.get(0), null);
        // 替换侧边目录
        indexHtml = replaceSidebar(indexHtml, monthMap, null);
        // 替换图片列表
        indexHtml = replaceImgList(indexHtml, bingImages.size() > 30 ? bingImages.subList(0, 30) : bingImages);
        // 替换底部月度历史 - 使用新的Fluent Design日历
        indexHtml = replaceMonthHistoryWithCalendar(indexHtml, bingImages, null);
        // 写到文件
        HtmlFileUtils.writeIndexHtml(indexHtml);
    }

    public void htmlGeneratorImgDetail(List<Images> bingImages) throws IOException {
        String templateFile = HtmlFileUtils.readDetailTemplateFile();
        for (Images bingImage : bingImages) {
            String detailHtml = templateFile.replace(ImgDetail.HEAD_TITLE, bingImage.getDesc());
            detailHtml = detailHtml.replace(ImgDetail.IMG_URL, bingImage.getSimpleUrl());
            detailHtml = detailHtml.replace(ImgDetail.IMG_DATE, bingImage.getDate());
            detailHtml = detailHtml.replace(ImgDetail.IMG_DESC, bingImage.getDesc());
            // 写到文件
            HtmlFileUtils.writeDetailHtml(detailHtml, bingImage.getDetailUrlPath());
        }
    }

    public void htmlGeneratorImgJson(List<Images> imagesList) throws IOException {
        String imagesJson = HtmlFileUtils.readImagesJson();
        JSONArray imagesJsonArray = JSON.parseArray(imagesJson);
        if (imagesJsonArray == null){
            imagesJsonArray = new JSONArray();
        }
        for (Images images : imagesList) {
            Map<String, String> imgMap = new HashMap<>(8);
            imgMap.put("date", images.getDate());
            imgMap.put("desc", images.getDesc());
            imgMap.put("url", images.getSimpleUrl());
            imgMap.put("region", Wallpaper.CURRENT_REGION);
            imagesJsonArray.add(imgMap);
        }
        List<Object> json = imagesJsonArray.stream().distinct().collect(Collectors.toList());
        HtmlFileUtils.writeImagesJson(JSON.toJSONString(json));
    }

    public void htmlGeneratorMonth(Map<String, List<Images>> monthMap) throws IOException {
        for (String month : monthMap.keySet()) {
            List<Images> bingImages = monthMap.get(month);
            String templateFile = HtmlFileUtils.readIndexTemplateFile();
            // 替换头部图片和描述
            String html = replaceHead(templateFile, bingImages.get(0), month);
            // 替换侧边目录
            html = replaceSidebar(html, monthMap, month);
            // 替换图片列表
            html = replaceImgList(html, bingImages);
            // 替换底部月度历史
            html = replaceMonthHistory(html, monthMap, month);
            // 写到文件
            HtmlFileUtils.writeMonthHtml(month, html);
        }
    }

    public String replaceSidebar(String html, Map<String, List<Images>> monthMap, String nowMonth) {
        StringBuilder sidebar = new StringBuilder();
        for (String month : monthMap.keySet()) {
            String sidabarMenu = Sidebar.getSidabarMenuList(month + ".html", month);
            if (month != null && month.equals(nowMonth)) {
                sidabarMenu = sidabarMenu.replace(Sidebar.VAR_SIDABAR_COLOR, Sidebar.VAR_SIDABAR_NOW_COLOR);
            }
            sidebar.append(sidabarMenu);
        }
        return html.replace(Sidebar.VAR_SIDABAR, sidebar.toString());
    }

    /**
     * 更新头部大图和描述
     *
     * @param html
     * @param images
     * @param month
     * @return
     */
    public String replaceHead(String html, Images images, String month) {
        html = html.replace(Head.HEAD_IMG_URL, images.getSimpleUrl());
        html = html.replace(Head.HEAD_IMG_DESC, images.getDesc());
        if (month != null) {
            html = html.replace(Head.HEAD_TITLE, "Bing Wallpaper\n(" + month + ")");
        } else {
            html = html.replace(Head.HEAD_TITLE, "Bing Wallpaper");
        }
        return html;
    }

    public String replaceImgList(String html, List<Images> bingImages) {
        StringBuilder imgList = new StringBuilder();
        for (Images bingImage : bingImages) {
            imgList.append(ImgCard.getImgCard(bingImage));
        }
        return html.replace(ImgCard.VAR_IMG_CARD_LIST, imgList.toString());
    }

    /**
     * 替换底部月度历史 - 使用新的Fluent Design日历
     * @param html
     * @param bingImages
     * @param nowMonth
     * @return
     */
    public String replaceMonthHistoryWithCalendar(String html, List<Images> bingImages, String nowMonth) {
        System.out.println("🔧 开始替换月度历史为日历...");
        System.out.println("📊 输入HTML长度: " + html.length());
        System.out.println("📊 壁纸数量: " + bingImages.size());
        
        // 构建日历数据
        Map<String, Object> calendarData = buildCalendarData(bingImages);
        
        // 生成Fluent Design日历
        String calendarHtml = MonthHistory.getFluentCalendar(calendarData);
        System.out.println("📅 生成的日历HTML长度: " + calendarHtml.length());
        
        // 替换占位符
        String result = html.replace(MonthHistory.VAR_MONTH_HISTORY, calendarHtml);
        System.out.println("📊 替换后HTML长度: " + result.length());
        
        // 检查是否成功替换
        if (result.contains("calendar-preview-container")) {
            System.out.println("✅ 日历HTML替换成功！");
        } else {
            System.out.println("❌ 日历HTML替换失败！");
            System.out.println("🔍 查找占位符: " + html.contains(MonthHistory.VAR_MONTH_HISTORY));
        }
        
        return result;
    }
    
    /**
     * 构建日历数据
     * @param bingImages
     * @return
     */
    private Map<String, Object> buildCalendarData(List<Images> bingImages) {
        Map<String, Object> calendarData = new HashMap<>();
        
        // 获取当前年月
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1; // Calendar月份是0-based
        
        calendarData.put("currentYear", currentYear);
        calendarData.put("currentMonth", currentMonth);
        
        // 统计每日壁纸数量和详细数据
        Map<String, Integer> wallpaperCounts = new HashMap<>();
        Map<String, Map<String, String>> wallpaperData = new HashMap<>();
        Map<Integer, Integer> yearStats = new HashMap<>();
        
        for (Images image : bingImages) {
            String date = image.getDate();
            String[] dateParts = date.split("-");
            if (dateParts.length == 3) {
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                
                // 统计每日数量
                wallpaperCounts.put(date, wallpaperCounts.getOrDefault(date, 0) + 1);
                
                // 构建壁纸详细数据 - 使用更大的预览尺寸
                Map<String, String> data = new HashMap<>();
                // 使用更大的预览尺寸，确保图片质量
                data.put("previewUrl", image.getSimpleUrl() + "&pid=hp&w=1920&h=1080&rs=1&c=4");
                data.put("title", image.getDesc());
                data.put("desc", image.getDesc());
                data.put("downloadUrl", image.getUrl());
                data.put("detailUrl", image.getDetailUrlPath());
                
                wallpaperData.put(date, data);
                
                // 统计年度数量
                yearStats.put(year, yearStats.getOrDefault(year, 0) + 1);
            }
        }
        
        calendarData.put("wallpaperCounts", wallpaperCounts);
        calendarData.put("wallpaperData", wallpaperData);
        calendarData.put("yearStats", yearStats);
        
        return calendarData;
    }

    /**
     * 替换底部月度历史 - 使用新的日历系统
     * @param html
     * @param monthMap
     * @param nowMonth
     * @return
     */
    public String replaceMonthHistory(String html, Map<String, List<Images>> monthMap, String nowMonth) {
        // 🚀 使用新的日历系统替换月度历史
        Map<String, Object> calendarData = prepareCalendarData(monthMap);
        String calendarHtml = MonthHistory.getFluentCalendar(calendarData);
        
        return html.replace(MonthHistory.VAR_MONTH_HISTORY, calendarHtml);
    }
    
    /**
     * 准备日历数据
     */
    private Map<String, Object> prepareCalendarData(Map<String, List<Images>> monthMap) {
        Map<String, Object> calendarData = new HashMap<>();
        
        // 设置当前年月（默认为最新数据）
        calendarData.put("currentYear", 2026);
        calendarData.put("currentMonth", 1);
        
        // 处理壁纸数据
        Map<String, Integer> wallpaperCounts = new HashMap<>();
        Map<String, Map<String, String>> wallpaperData = new HashMap<>();
        Map<Integer, Integer> yearStats = new HashMap<>();
        
        for (Map.Entry<String, List<Images>> entry : monthMap.entrySet()) {
            String yearMonth = entry.getKey();
            List<Images> images = entry.getValue();
            
            for (Images image : images) {
                String date = image.getDate();
                if (date != null && date.length() == 10) {
                    wallpaperCounts.put(date, 1);
                    
                    // 构建壁纸数据
                    Map<String, String> data = new HashMap<>();
                    data.put("previewUrl", image.getSimpleUrl());
                    data.put("title", image.getDesc());
                    data.put("desc", image.getDesc());
                    data.put("downloadUrl", image.getUrl());
                    
                    wallpaperData.put(date, data);
                    
                    // 统计年度数量
                    int year = Integer.parseInt(date.substring(0, 4));
                    yearStats.put(year, yearStats.getOrDefault(year, 0) + 1);
                }
            }
        }
        
        calendarData.put("wallpaperCounts", wallpaperCounts);
        calendarData.put("wallpaperData", wallpaperData);
        calendarData.put("yearStats", yearStats);
        
        return calendarData;
    }
    
    /**
     * 🚀 第一阶段：生成日历数据文件（优化版本）
     * 为所有历史月份生成JSON数据文件，供前端动态加载
     * 使用智能增量更新，只在数据变化时重新生成
     */
    public void generateCalendarDataFiles(List<Images> bingImages) throws IOException {
        System.out.println("=================================================");
        System.out.println("🧠🧠🧠 开始智能日历数据生成检查... 🧠🧠🧠");
        System.out.println("📊 壁纸数据数量: " + bingImages.size());
        System.out.println("=================================================");
        
        try {
            // 🚀 获取当前区域的HTML根目录
            String dataRoot = HtmlFileUtils.BING_HTML_ROOT.toString();
            System.out.println("🌍 当前区域数据根目录: " + dataRoot);
            
            // 🚀 使用优化的智能生成器，传入当前区域的数据根目录
            CalendarDataGeneratorOptimized.generateCalendarDataFilesSmart(bingImages, dataRoot);
            
            System.out.println("=================================================");
            System.out.println("✅✅✅ 智能日历数据生成完成！ ✅✅✅");
            System.out.println("📁 文件位置: " + dataRoot + "/data/calendar/");
            System.out.println("📋 索引文件: " + dataRoot + "/data/calendar-index.json");
            System.out.println("🧠 指纹文件: " + dataRoot + "/data/calendar-fingerprint.json");
            System.out.println("=================================================");
            
        } catch (Exception e) {
            System.err.println("❌❌❌ 智能日历数据生成失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 🔧 强制重新生成所有日历数据文件（用于调试或手动刷新）
     */
    public void forceRegenerateCalendarDataFiles(List<Images> bingImages) throws IOException {
        System.out.println("=================================================");
        System.out.println("🔄🔄🔄 强制重新生成日历数据... 🔄🔄🔄");
        System.out.println("📊 壁纸数据数量: " + bingImages.size());
        System.out.println("=================================================");
        
        try {
            CalendarDataGeneratorOptimized.forceRegenerateAll(bingImages, "docs");
            
            System.out.println("=================================================");
            System.out.println("✅✅✅ 强制重新生成完成！ ✅✅✅");
            System.out.println("=================================================");
            
        } catch (Exception e) {
            System.err.println("❌❌❌ 强制重新生成失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
