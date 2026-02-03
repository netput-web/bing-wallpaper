package com.wdbyte.bing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Bing API获取器 - 获取新图片数据
 * @author niulang
 * @date 2026/02/03
 */
public class BingApiFetcher {
    
    private static final String BING_API_TEMPLATE = "https://global.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160&setmkt=%s&setlang=en";
    
    /**
     * 从Bing API获取最新图片数据
     */
    public static List<Images> fetchLatestImages(String region) {
        List<Images> images = new ArrayList<>();
        
        try {
            String apiUrl = String.format(BING_API_TEMPLATE, region);
            System.out.println("正在从API获取数据: " + region);
            
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // 解析JSON响应
                JSONObject jsonResponse = JSON.parseObject(response.toString());
                JSONArray imagesArray = jsonResponse.getJSONArray("images");
                
                for (int i = 0; i < imagesArray.size(); i++) {
                    JSONObject imgObj = imagesArray.getJSONObject(i);
                    
                    String date = imgObj.getString("enddate");
                    // 转换日期格式 YYYYMMDD -> YYYY-MM-DD
                    date = formatDate(date);
                    
                    String urlBase = imgObj.getString("urlbase");
                    String fullUrl = "https://cn.bing.com" + urlBase + "_UHD.jpg";
                    
                    String desc = imgObj.getString("copyright");
                    // 简化描述，去掉版权信息
                    desc = simplifyDescription(desc);
                    
                    Images image = new Images(desc, date, fullUrl, region.toLowerCase());
                    images.add(image);
                }
                
                System.out.println("从API获取到 " + images.size() + " 条新数据");
                
            } else {
                System.err.println("API请求失败，响应码: " + responseCode);
            }
            
            connection.disconnect();
            
        } catch (Exception e) {
            System.err.println("API获取失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return images;
    }
    
    /**
     * 格式化日期 YYYYMMDD -> YYYY-MM-DD
     */
    private static String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            System.err.println("日期格式化失败: " + dateStr);
            return dateStr;
        }
    }
    
    /**
     * 简化描述，去掉括号中的版权信息
     */
    private static String simplifyDescription(String desc) {
        if (desc == null) return "";
        
        // 去掉括号中的版权信息
        desc = desc.replaceAll("\\s*\\([^)]*\\)$", "");
        
        // 去掉多余的空格
        desc = desc.trim();
        
        return desc;
    }
    
    /**
     * 检查是否有新数据
     */
    public static boolean hasNewData(String region) {
        try {
            List<Images> latestImages = fetchLatestImages(region);
            if (latestImages.isEmpty()) {
                return false;
            }
            
            // 获取最新日期
            String latestDate = latestImages.get(0).getDate();
            
            // 检查JSON中是否已有这个日期的数据
            List<Images> existingData = JsonDataManager.readDataByRegion(region.toLowerCase());
            return existingData.stream()
                    .noneMatch(img -> latestDate.equals(img.getDate()));
                    
        } catch (Exception e) {
            System.err.println("检查新数据失败: " + e.getMessage());
            return false;
        }
    }
}
