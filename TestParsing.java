import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 测试解析逻辑
 */
public class TestParsing {
    
    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 测试解析逻辑 ===");
        
        // 测试数据行
        String testLine = "2026-01-31 | [St. Michael's Mount in Marazion, Cornwall, England (© Baxter Bradford/robertharding/Getty Images)](https://cn.bing.com/th?id=OHR.StMichaelsCornwall_EN-US0036057583_UHD.jpg&rf=LaDigue_UHD.jpg&pid=hp&w=3840&h=2160&rs=1&c=4)";
        
        System.out.println("测试行: " + testLine);
        
        // 解析逻辑
        int descEnd = testLine.indexOf("]");
        int urlStart = testLine.lastIndexOf("(") + 1;
        int urlEnd = testLine.lastIndexOf(")");
        
        System.out.println("descEnd: " + descEnd);
        System.out.println("urlStart: " + urlStart);
        System.out.println("urlEnd: " + urlEnd);
        
        if (descEnd > 14) {
            String descContent = testLine.substring(14, descEnd);
            System.out.println("描述内容: " + descContent);
        }
        
        if (urlStart > 0 && urlEnd > urlStart) {
            String url = testLine.substring(urlStart, urlEnd);
            System.out.println("URL内容: " + url);
        }
        
        System.out.println("=== 测试完成 ===");
    }
}
