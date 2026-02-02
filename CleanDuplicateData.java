import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CleanDuplicateData {
    public static void main(String[] args) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("docs/images.json")), "UTF-8");
        JSONArray jsonArray = JSON.parseArray(json);
        
        System.out.println("=== 清理重复数据 ===");
        System.out.println("原始数据: " + jsonArray.size() + " 条");
        
        // 按日期和区域分组，去重
        Map<String, JSONObject> uniqueData = new LinkedHashMap<>();
        
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String date = obj.getString("date");
            String region = obj.getString("region");
            String url = obj.getString("url");
            String desc = obj.getString("desc");
            
            // 创建唯一键：日期+区域
            String key = date + "|" + region;
            
            // 过滤掉测试数据
            if (url.contains("example.com")) {
                System.out.println("跳过测试数据: " + date + " | " + region);
                continue;
            }
            
            // 如果键不存在，或者当前数据更完整，则保存
            if (!uniqueData.containsKey(key) || isBetterData(obj, uniqueData.get(key))) {
                uniqueData.put(key, obj);
            }
        }
        
        // 转换回JSONArray
        JSONArray cleanedArray = new JSONArray();
        for (JSONObject obj : uniqueData.values()) {
            cleanedArray.add(obj);
        }
        
        // 写入文件
        String cleanedJson = JSON.toJSONString(cleanedArray, true);
        Files.write(Paths.get("docs/images.json"), cleanedJson.getBytes("UTF-8"));
        
        System.out.println("清理后数据: " + cleanedArray.size() + " 条");
        System.out.println("重复数据清理完成！");
        
        // 显示清理后的统计
        Map<String, Long> regionCount = new HashMap<>();
        for (int i = 0; i < cleanedArray.size(); i++) {
            JSONObject obj = cleanedArray.getJSONObject(i);
            String region = obj.getString("region");
            regionCount.put(region, regionCount.getOrDefault(region, 0L) + 1);
        }
        
        System.out.println("\n=== 清理后统计 ===");
        regionCount.forEach((region, count) -> 
            System.out.println(region + ": " + count + " 条"));
    }
    
    private static boolean isBetterData(JSONObject newData, JSONObject existingData) {
        // 优先选择非测试数据
        String newUrl = newData.getString("url");
        String existingUrl = existingData.getString("url");
        
        if (newUrl.contains("example.com") && !existingUrl.contains("example.com")) {
            return false;
        }
        if (!newUrl.contains("example.com") && existingUrl.contains("example.com")) {
            return true;
        }
        
        // 如果都不是测试数据，选择URL更长的（通常更完整）
        return newUrl.length() > existingUrl.length();
    }
}
