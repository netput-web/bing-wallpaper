import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckJsonData {
    public static void main(String[] args) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("docs/images.json")), "UTF-8");
        JSONArray jsonArray = JSON.parseArray(json);
        
        System.out.println("JSON总记录数: " + jsonArray.size());
        
        // 统计各区域数据
        int enUsCount = 0;
        int zhCnCount = 0;
        int otherCount = 0;
        
        for (int i = 0; i < jsonArray.size(); i++) {
            String region = jsonArray.getJSONObject(i).getString("region");
            if ("en-us".equals(region)) {
                enUsCount++;
            } else if ("zh-cn".equals(region)) {
                zhCnCount++;
            } else {
                otherCount++;
            }
        }
        
        System.out.println("英文区数据: " + enUsCount + " 条");
        System.out.println("中文区数据: " + zhCnCount + " 条");
        System.out.println("其他区域: " + otherCount + " 条");
        
        // 检查最新数据
        if (jsonArray.size() > 0) {
            System.out.println("\n最新5条数据:");
            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                com.alibaba.fastjson.JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println((i+1) + ". " + obj.getString("date") + " | " + obj.getString("region") + " | " + obj.getString("desc"));
            }
        }
    }
}
