import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FixReadmeEncoding {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 修复README.md编码问题 ===");
        
        // 读取README.md文件
        String content = new String(Files.readAllBytes(Paths.get("README.md")), "UTF-8");
        
        // 替换乱码的标题
        String[] corruptedTexts = {"### ʷ鵵", "### ????鵵??"};
        String newText = "### Historical Archive";
        
        boolean found = false;
        for (String oldText : corruptedTexts) {
            if (content.contains(oldText)) {
                content = content.replace(oldText, newText);
                System.out.println("找到乱码: " + oldText + "，已替换为英文标题");
                found = true;
                break;
            }
        }
        
        // 写入修复后的文件
        Files.write(Paths.get("README.md"), content.getBytes("UTF-8"));
        
        System.out.println("README.md编码修复完成！");
    }
}
