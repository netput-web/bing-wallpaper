import com.wdbyte.bing.*;
import java.util.List;

public class TestEnglishOnly {
    public static void main(String[] args) throws Exception {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 测试英文区HTML生成 ===");
        
        // 设置为英文区
        Wallpaper.CURRENT_REGION = "en-us";
        Wallpaper.changeConfig("en-US");
        
        // 读取英文区数据
        List<Images> englishImages = JsonDataManager.readDataByRegion("en-us");
        System.out.println("英文区数据: " + englishImages.size() + " 条");
        
        // 生成HTML
        WebSiteGenerator generator = new WebSiteGenerator();
        generator.htmlGenerator();
        
        System.out.println("英文区HTML生成完成");
        
        // 检查1月31号文件
        System.out.println("\n=== 检查1月31号文件 ===");
        java.nio.file.Path filePath = java.nio.file.Paths.get("docs/day/202601/31.html");
        if (java.nio.file.Files.exists(filePath)) {
            String content = new String(java.nio.file.Files.readAllBytes(filePath), "UTF-8");
            if (content.contains("${IMG_URL}")) {
                System.out.println("❌ 1月31号文件包含未替换的占位符");
            } else {
                System.out.println("✅ 1月31号文件占位符已替换");
            }
            
            if (content.contains("smallImg\"></div>")) {
                System.out.println("❌ 1月31号文件smallImg为空");
            } else {
                System.out.println("✅ 1月31号文件smallImg有内容");
            }
        } else {
            System.out.println("❌ 1月31号文件不存在");
        }
    }
}
