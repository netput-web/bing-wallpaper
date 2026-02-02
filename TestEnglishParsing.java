import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 测试英文区数据解析
 */
public class TestEnglishParsing {
    
    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 测试英文区数据解析 ===");
        
        Path path = Paths.get("bing-wallpaper.md");
        if (!Files.exists(path)) {
            System.out.println("文件不存在: " + path);
            return;
        }
        
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        
        for (int i = 0; i < lines.size() && i < 10; i++) {
            String line = lines.get(i);
            
            if (line.trim().isEmpty() || line.startsWith("##")) {
                continue;
            }
            
            System.out.println("原始行: " + line);
            
            // 解析逻辑
            int descEnd = line.indexOf("]");
            int urlStart = line.lastIndexOf("(") + 1;
            int urlEnd = line.lastIndexOf(")");
            
            System.out.println("descEnd: " + descEnd + ", urlStart: " + urlStart + ", urlEnd: " + urlEnd);
            
            if (descEnd > 14) {
                String descContent = line.substring(14, descEnd);
                System.out.println("描述内容: " + descContent);
            }
            
            if (urlStart > 0 && urlEnd > urlStart) {
                String url = line.substring(urlStart, urlEnd);
                System.out.println("URL内容: " + url);
            }
            
            System.out.println("---");
        }
        
        System.out.println("=== 测试完成 ===");
    }
}
