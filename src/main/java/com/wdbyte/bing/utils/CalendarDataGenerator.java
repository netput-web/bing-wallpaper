package com.wdbyte.bing.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdbyte.bing.Images;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日历数据生成器
 * 生成按年月组织的壁纸数据JSON文件
 */
public class CalendarDataGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * 生成所有历史月份的日历数据文件
     */
    public static void generateAllCalendarDataFiles(List<Images> bingImages, String outputDir) throws IOException {
        // 按年月分组数据
        Map<String, List<Images>> monthlyData = groupImagesByMonth(bingImages);
        
        // 确保输出目录存在
        File calendarDataDir = new File(outputDir, "data/calendar");
        if (!calendarDataDir.exists()) {
            calendarDataDir.mkdirs();
        }
        
        // 为每个月份生成JSON文件
        for (Map.Entry<String, List<Images>> entry : monthlyData.entrySet()) {
            String yearMonth = entry.getKey();
            List<Images> monthImages = entry.getValue();
            
            Map<String, Object> calendarData = generateMonthCalendarData(yearMonth, monthImages);
            
            // 写入JSON文件
            File outputFile = new File(calendarDataDir, yearMonth + ".json");
            String jsonContent = JSON.toJSONString(calendarData);
            
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(jsonContent);
            }
            
            System.out.println("Generated calendar data: " + outputFile.getAbsolutePath());
        }
        
        // 生成索引文件
        generateIndexFile(monthlyData, outputDir);
    }
    
    /**
     * 按年月分组图片数据
     */
    private static Map<String, List<Images>> groupImagesByMonth(List<Images> bingImages) {
        Map<String, List<Images>> monthlyData = new TreeMap<>();
        
        for (Images image : bingImages) {
            try {
                LocalDate date = LocalDate.parse(image.getDate(), DATE_FORMATTER);
                String yearMonth = String.format("%04d-%02d", date.getYear(), date.getMonthValue());
                
                monthlyData.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(image);
            } catch (Exception e) {
                System.err.println("Error parsing date for image: " + image.getDate());
            }
        }
        
        return monthlyData;
    }
    
    /**
     * 生成单个月的日历数据
     */
    private static Map<String, Object> generateMonthCalendarData(String yearMonth, List<Images> monthImages) {
        Map<String, Object> calendarData = new HashMap<>();
        
        // 基本信息
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        
        calendarData.put("year", year);
        calendarData.put("month", month);
        calendarData.put("yearMonth", yearMonth);
        
        // 壁纸数据
        Map<String, Map<String, String>> wallpapers = new HashMap<>();
        
        for (Images image : monthImages) {
            try {
                LocalDate date = LocalDate.parse(image.getDate(), DATE_FORMATTER);
                String dayKey = String.format("%02d", date.getDayOfMonth());
                
                Map<String, String> wallpaperInfo = new HashMap<>();
                wallpaperInfo.put("date", image.getDate());
                wallpaperInfo.put("dateUrl", generateDateUrl(image.getDate()));
                wallpaperInfo.put("previewUrl", generatePreviewUrl(image.getSimpleUrl()));
                wallpaperInfo.put("title", image.getDesc());
                wallpaperInfo.put("desc", image.getDesc());
                wallpaperInfo.put("downloadUrl", generateDownloadUrl(image.getUrl()));
                wallpaperInfo.put("copyright", image.getDesc());
                
                wallpapers.put(dayKey, wallpaperInfo);
            } catch (Exception e) {
                System.err.println("Error processing image: " + image.getDate());
            }
        }
        
        calendarData.put("wallpapers", wallpapers);
        
        // 统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", getDaysInMonth(year, month));
        stats.put("hasWallpaper", wallpapers.size());
        stats.put("coverage", String.format("%.1f%%", (double) wallpapers.size() / getDaysInMonth(year, month) * 100));
        
        calendarData.put("stats", stats);
        
        return calendarData;
    }
    
    /**
     * 生成索引文件
     */
    private static void generateIndexFile(Map<String, List<Images>> monthlyData, String outputDir) throws IOException {
        Map<String, Object> indexData = new HashMap<>();
        List<Map<String, Object>> monthList = new ArrayList<>();
        
        for (String yearMonth : monthlyData.keySet()) {
            String[] parts = yearMonth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            
            Map<String, Object> monthInfo = new HashMap<>();
            monthInfo.put("yearMonth", yearMonth);
            monthInfo.put("year", year);
            monthInfo.put("month", month);
            monthInfo.put("count", monthlyData.get(yearMonth).size());
            monthInfo.put("dataFile", "data/calendar/" + yearMonth + ".json");
            
            monthList.add(monthInfo);
        }
        
        indexData.put("months", monthList);
        indexData.put("totalMonths", monthList.size());
        indexData.put("generatedAt", LocalDate.now().toString());
        
        // 写入索引文件
        File indexFile = new File(outputDir, "data/calendar-index.json");
        String indexJsonContent = JSON.toJSONString(indexData);
        
        try (FileWriter writer = new FileWriter(indexFile)) {
            writer.write(indexJsonContent);
        }
        
        System.out.println("Generated calendar index: " + indexFile.getAbsolutePath());
    }
    
    /**
     * 生成日期URL
     */
    private static String generateDateUrl(String date) {
        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        return String.format("day/%04d%02d/%02d.html", 
            localDate.getYear(), 
            localDate.getMonthValue(), 
            localDate.getDayOfMonth());
    }
    
    /**
     * 生成预览URL
     */
    private static String generatePreviewUrl(String urlBase) {
        return urlBase + "&pid=hp&w=800&h=450&rs=1&c=4";
    }
    
    /**
     * 生成下载URL
     */
    private static String generateDownloadUrl(String urlBase) {
        return urlBase + "&rf=LaDigue_UHD.jpg&pid=hp&w=3840&h=2160&rs=1&c=4";
    }
    
    /**
     * 获取月份天数
     */
    private static int getDaysInMonth(int year, int month) {
        return LocalDate.of(year, month, 1).lengthOfMonth();
    }
}
