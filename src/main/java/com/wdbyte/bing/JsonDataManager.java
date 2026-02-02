package com.wdbyte.bing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON数据管理器 - 主要数据源管理
 * @author niulang
 * @date 2026/02/03
 */
public class JsonDataManager {
    
    private static final Path JSON_PATH = Paths.get("docs", "images.json");
    
    /**
     * 从JSON文件读取所有数据
     */
    public static List<Images> readAllData() throws Exception {
        if (!Files.exists(JSON_PATH)) {
            System.out.println("JSON文件不存在，创建空文件: " + JSON_PATH);
            Files.createDirectories(JSON_PATH.getParent());
            Files.write(JSON_PATH, "[]".getBytes("UTF-8"));
            return new ArrayList<>();
        }
        
        String jsonContent = new String(Files.readAllBytes(JSON_PATH), "UTF-8");
        JSONArray jsonArray = JSON.parseArray(jsonContent);
        
        List<Images> images = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String date = jsonObj.getString("date");
            String url = jsonObj.getString("url");
            String desc = jsonObj.getString("desc");
            String region = jsonObj.getString("region");
            
            images.add(new Images(desc, date, url, region));
        }
        
        System.out.println("从JSON读取到 " + images.size() + " 条数据");
        return images;
    }
    
    /**
     * 读取指定区域的数据
     */
    public static List<Images> readDataByRegion(String region) throws Exception {
        List<Images> allData = readAllData();
        return allData.stream()
                .filter(img -> region.equalsIgnoreCase(img.getRegion()))
                .collect(Collectors.toList());
    }
    
    /**
     * 写入数据到JSON文件
     */
    public static void writeData(List<Images> images) throws Exception {
        // 去重处理
        List<Images> uniqueImages = images.stream()
                .collect(Collectors.toMap(
                    Images::getUrl, 
                    img -> img, 
                    (existing, replacement) -> replacement
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(Images::getDate).reversed())
                .collect(Collectors.toList());
        
        // 转换为JSON格式
        JSONArray jsonArray = new JSONArray();
        for (Images img : uniqueImages) {
            Map<String, String> imgMap = new HashMap<>();
            imgMap.put("date", img.getDate());
            imgMap.put("desc", img.getDesc());
            imgMap.put("url", img.getUrl());
            imgMap.put("region", img.getRegion());
            jsonArray.add(imgMap);
        }
        
        // 确保目录存在
        Files.createDirectories(JSON_PATH.getParent());
        
        // 写入文件
        String jsonContent = JSON.toJSONString(jsonArray, true);
        Files.write(JSON_PATH, jsonContent.getBytes("UTF-8"));
        
        System.out.println("写入JSON文件 " + uniqueImages.size() + " 条数据");
    }
    
    /**
     * 合并新数据到现有数据
     */
    public static List<Images> mergeData(List<Images> existingData, List<Images> newData) {
        Map<String, Images> mergedMap = new LinkedHashMap<>();
        
        // 先添加现有数据
        for (Images img : existingData) {
            mergedMap.put(img.getUrl(), img);
        }
        
        // 添加新数据（覆盖重复的）
        for (Images img : newData) {
            mergedMap.put(img.getUrl(), img);
        }
        
        // 转换为列表并按日期排序
        return mergedMap.values().stream()
                .sorted(Comparator.comparing(Images::getDate).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 检查数据完整性
     */
    public static void validateData(List<Images> images) {
        long validCount = images.stream()
                .filter(img -> img.getDate() != null && !img.getDate().isEmpty())
                .filter(img -> img.getUrl() != null && !img.getUrl().isEmpty())
                .filter(img -> img.getDesc() != null && !img.getDesc().isEmpty())
                .filter(img -> img.getRegion() != null && !img.getRegion().isEmpty())
                .count();
        
        System.out.println("数据验证: " + validCount + "/" + images.size() + " 条有效数据");
        
        if (validCount != images.size()) {
            System.out.println("⚠️  发现无效数据，请检查数据完整性");
        }
    }
    
    /**
     * 生成统计信息
     */
    public static void printStatistics() {
        try {
            List<Images> allData = readAllData();
            
            Map<String, Long> regionCount = allData.stream()
                    .collect(Collectors.groupingBy(Images::getRegion, Collectors.counting()));
            
            System.out.println("=== JSON数据统计 ===");
            System.out.println("总数据量: " + allData.size());
            regionCount.forEach((region, count) -> 
                System.out.println(region + ": " + count + " 条"));
            
            // 最新数据
            if (!allData.isEmpty()) {
                Images latest = allData.get(0);
                System.out.println("最新数据: " + latest.getDate() + " | " + latest.getRegion());
            }
            
        } catch (Exception e) {
            System.err.println("统计信息生成失败: " + e.getMessage());
        }
    }
}
