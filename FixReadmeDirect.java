import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FixReadmeDirect {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 直接修复README.md编码问题 ===");
        
        // 读取README.md文件的所有行
        List<String> lines = Files.readAllLines(Paths.get("README.md"), StandardCharsets.UTF_8);
        
        // 查找并修复第16行（索引15）
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            // 查找包含###且包含乱码字符的行
            if (line.contains("###") && (line.contains("?") || line.contains(""))) {
                System.out.println("找到乱码行，第" + (i+1) + "行: " + line);
                // 替换为正确的英文标题
                lines.set(i, "### Historical Archive");
                System.out.println("已修复为: ### Historical Archive");
                break;
            }
        }
        
        // 写入修复后的文件
        Files.write(Paths.get("README.md"), lines, StandardCharsets.UTF_8);
        
        System.out.println("README.md编码修复完成！");
    }
}
