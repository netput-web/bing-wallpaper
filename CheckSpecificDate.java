import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckSpecificDate {
    public static void main(String[] args) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("docs/images.json")), "UTF-8");
        JSONArray jsonArray = JSON.parseArray(json);
        
        System.out.println("=== 检查2026-01-31数据 ===");
        
        for (int i = 0; i < jsonArray.size(); i++) {
            com.alibaba.fastjson.JSONObject obj = jsonArray.getJSONObject(i);
            String date = obj.getString("date");
            String region = obj.getString("region");
            String url = obj.getString("url");
            String desc = obj.getString("desc");
            
            if ("2026-01-31".equals(date)) {
                System.out.println("日期: " + date);
                System.out.println("区域: " + region);
                System.out.println("URL: " + url);
                System.out.println("描述: " + desc);
                System.out.println("---");
            }
        }
    }
}
