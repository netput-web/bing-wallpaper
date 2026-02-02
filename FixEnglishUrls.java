import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 修复英文区URL链接问题
 */
public class FixEnglishUrls {
    
    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 开始修复英文区URL链接 ===");
        
        // 从clean文件读取数据
        Path cleanFile = Paths.get("bing-wallpaper-clean.md");
        Path targetFile = Paths.get("bing-wallpaper.md");
        
        if (!Files.exists(cleanFile)) {
            System.out.println("clean文件不存在: " + cleanFile);
            return;
        }
        
        System.out.println("从clean文件读取数据...");
        
        List<String> lines = Files.readAllLines(cleanFile, StandardCharsets.UTF_8);
        List<String> fixedLines = new ArrayList<>();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                fixedLines.add(line);
                continue;
            }
            
            // 跳过标题行
            if (line.startsWith("##")) {
                fixedLines.add(line);
                continue;
            }
            
            // 修复版权符号和清理格式
            String fixedLine = fixLine(line);
            fixedLines.add(fixedLine);
            
            if (!line.equals(fixedLine)) {
                System.out.println("  修复: " + line.substring(0, Math.min(50, line.length())));
                System.out.println("  ->  " + fixedLine.substring(0, Math.min(50, fixedLine.length())));
            }
        }
        
        // 写入目标文件
        Files.write(targetFile, fixedLines, StandardCharsets.UTF_8);
        System.out.println("英文区URL修复完成");
        
        System.out.println("=== 英文区URL修复完成 ===");
    }
    
    private static String fixLine(String line) {
        // 修复版权符号
        line = line.replace("漏", "©");
        
        // 去除版权信息但保留URL
        // 使用正则表达式匹配格式：[描述 (版权信息)](URL)
        // 转换为：[描述](URL)
        line = line.replaceAll("\\s*\\([^)]*\\)\\]", "]");
        
        // 去除重复行（如果有）
        // 这里可以添加去重逻辑，但暂时保留所有行
        
        return line;
    }
}
