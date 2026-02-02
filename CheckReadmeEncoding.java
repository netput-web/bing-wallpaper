import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckReadmeEncoding {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        String content = new String(Files.readAllBytes(Paths.get("README.md")), "UTF-8");
        
        // 查找包含"历史"的部分
        int index = content.indexOf("历史");
        if (index != -1) {
            String section = content.substring(Math.max(0, index - 10), Math.min(content.length(), index + 50));
            System.out.println("包含'历史'的部分:");
            System.out.println(section);
            
            // 检查字符编码
            for (int i = 0; i < section.length(); i++) {
                char c = section.charAt(i);
                if (c > 127) {
                    System.out.println("特殊字符: '" + c + "' (Unicode: " + (int)c + ") 位置: " + i);
                }
            }
        }
    }
}
