package com.wdbyte.bing.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdbyte.bing.Images;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 🚀 优化的日历数据生成器 - 支持增量更新
 */
public class CalendarDataGeneratorOptimized {
    
    // 数据指纹文件，用于检测数据变化
    private static final String FINGERPRINT_FILE = "data/calendar-fingerprint.json";
    
    /**
     * 智能生成日历数据文件 - 只在数据变化时重新生成
     */
    public static void generateCalendarDataFilesSmart(List<Images> bingImages, String outputDir) throws IOException {
        System.out.println("🧠 开始智能日历数据生成检查...");
        
        // 生成当前数据指纹
        String currentFingerprint = generateDataFingerprint(bingImages);
        
        // 读取之前的数据指纹
        String previousFingerprint = readPreviousFingerprint(outputDir);
        
        // 比较指纹，判断是否需要重新生成
        if (currentFingerprint.equals(previousFingerprint)) {
            System.out.println("✅ 数据未变化，跳过日历数据生成");
            return;
        }
        
        System.out.println("🔄 数据发生变化，重新生成日历数据...");
        
        // 数据有变化，执行完整生成
        generateAllCalendarDataFiles(bingImages, outputDir);
        
        // 保存新的数据指纹
        saveDataFingerprint(outputDir, currentFingerprint);
        
        System.out.println("✅ 日历数据生成完成，已更新数据指纹");
    }
    
    /**
     * 生成数据指纹 - 基于壁纸数据的哈希值
     */
    private static String generateDataFingerprint(List<Images> bingImages) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 创建指纹数据：数量 + 最新日期 + 所有URL的哈希
            StringBuilder fingerprintData = new StringBuilder();
            fingerprintData.append("count:").append(bingImages.size()).append(";");
            
            if (!bingImages.isEmpty()) {
                // 添加最新壁纸日期
                fingerprintData.append("latest:").append(bingImages.get(0).getDate()).append(";");
                
                // 添加前10个壁纸的URL哈希（避免计算所有数据）
                for (int i = 0; i < Math.min(10, bingImages.size()); i++) {
                    Images img = bingImages.get(i);
                    fingerprintData.append(img.getUrl()).append(";");
                }
            }
            
            byte[] hash = md.digest(fingerprintData.toString().getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    /**
     * 读取之前的数据指纹
     */
    private static String readPreviousFingerprint(String outputDir) {
        try {
            File fingerprintFile = new File(outputDir, FINGERPRINT_FILE);
            if (!fingerprintFile.exists()) {
                return ""; // 文件不存在，视为首次运行
            }
            
            String content = new String(Files.readAllBytes(fingerprintFile.toPath()));
            JSONObject json = JSON.parseObject(content);
            return json.getString("fingerprint");
            
        } catch (Exception e) {
            System.err.println("读取数据指纹失败: " + e.getMessage());
            return ""; // 读取失败，触发重新生成
        }
    }
    
    /**
     * 保存数据指纹
     */
    private static void saveDataFingerprint(String outputDir, String fingerprint) throws IOException {
        File fingerprintFile = new File(outputDir, FINGERPRINT_FILE);
        File parentDir = fingerprintFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        JSONObject fingerprintData = new JSONObject();
        fingerprintData.put("fingerprint", fingerprint);
        fingerprintData.put("timestamp", LocalDate.now().toString());
        fingerprintData.put("version", "1.0");
        
        try (FileWriter writer = new FileWriter(fingerprintFile)) {
            writer.write(JSON.toJSONString(fingerprintData));
        }
        
        System.out.println("💾 数据指纹已保存: " + fingerprintFile.getAbsolutePath());
    }
    
    /**
     * 强制重新生成所有数据（忽略指纹检查）
     */
    public static void forceRegenerateAll(List<Images> bingImages, String outputDir) throws IOException {
        System.out.println("🔄 强制重新生成所有日历数据...");
        generateAllCalendarDataFiles(bingImages, outputDir);
        saveDataFingerprint(outputDir, generateDataFingerprint(bingImages));
        System.out.println("✅ 强制重新生成完成");
    }
    
    /**
     * 原有的完整生成方法（保持向后兼容）
     */
    public static void generateAllCalendarDataFiles(List<Images> bingImages, String outputDir) throws IOException {
        // 按年月分组数据
        Map<String, List<Images>> monthlyData = groupImagesByMonth(bingImages);
        
        // 确保输出目录存在
        File calendarDataDir = new File(outputDir, "data/calendar");
        if (!calendarDataDir.exists()) {
            calendarDataDir.mkdirs();
        }
        
        System.out.println("📊 开始生成 " + monthlyData.size() + " 个月的日历数据...");
        
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
     * 按年月分组壁纸数据
     */
    private static Map<String, List<Images>> groupImagesByMonth(List<Images> bingImages) {
        Map<String, List<Images>> monthlyData = new TreeMap<>();
        
        for (Images image : bingImages) {
            String date = image.getDate();
            if (date != null && date.length() >= 7) {
                String yearMonth = date.substring(0, 7); // YYYY-MM格式
                monthlyData.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(image);
            }
        }
        
        return monthlyData;
    }
    
    /**
     * 生成单个月的日历数据
     */
    private static Map<String, Object> generateMonthCalendarData(String yearMonth, List<Images> monthImages) {
        Map<String, Object> calendarData = new HashMap<>();
        
        // 解析年月
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        
        calendarData.put("year", year);
        calendarData.put("month", month);
        calendarData.put("yearMonth", yearMonth);
        
        // 生成该月的壁纸数据
        Map<String, Map<String, Object>> wallpapers = new HashMap<>();
        
        for (Images image : monthImages) {
            String date = image.getDate();
            if (date != null && date.length() == 10) {
                String day = date.substring(8, 10); // 提取日期
                
                Map<String, Object> wallpaperInfo = new HashMap<>();
                wallpaperInfo.put("date", date);
                wallpaperInfo.put("dateUrl", generateDateUrl(date));
                wallpaperInfo.put("previewUrl", image.getSimpleUrl());
                wallpaperInfo.put("title", image.getDesc());
                wallpaperInfo.put("desc", image.getDesc());
                wallpaperInfo.put("downloadUrl", image.getUrl());
                wallpaperInfo.put("copyright", image.getDesc());
                
                wallpapers.put(day, wallpaperInfo);
            }
        }
        
        calendarData.put("wallpapers", wallpapers);
        
        // 生成统计信息
        Map<String, Object> stats = new HashMap<>();
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        stats.put("total", daysInMonth);
        stats.put("hasWallpaper", wallpapers.size());
        double coverage = (double) wallpapers.size() / daysInMonth * 100;
        stats.put("coverage", String.format("%.1f%%", coverage));
        
        calendarData.put("stats", stats);
        
        return calendarData;
    }
    
    /**
     * 生成索引文件
     */
    private static void generateIndexFile(Map<String, List<Images>> monthlyData, String outputDir) throws IOException {
        List<Map<String, Object>> monthList = new ArrayList<>();
        
        for (Map.Entry<String, List<Images>> entry : monthlyData.entrySet()) {
            String yearMonth = entry.getKey();
            List<Images> monthImages = entry.getValue();
            
            String[] parts = yearMonth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            
            Map<String, Object> monthInfo = new HashMap<>();
            monthInfo.put("yearMonth", yearMonth);
            monthInfo.put("year", year);
            monthInfo.put("month", month);
            monthInfo.put("count", monthImages.size());
            monthInfo.put("dataFile", "data/calendar/" + yearMonth + ".json");
            
            monthList.add(monthInfo);
        }
        
        Map<String, Object> indexData = new HashMap<>();
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
        String[] parts = date.split("-");
        return String.format("day/%s%s/%s.html", parts[0], parts[1], parts[2]);
    }
}
