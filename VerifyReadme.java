import java.nio.file.Files;
import java.nio.file.Paths;

public class VerifyReadme {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 验证README.md更新结果 ===");
        
        String content = new String(Files.readAllBytes(Paths.get("README.md")), "UTF-8");
        
        // 检查是否还有picture链接
        int pictureCount = content.split("/picture/").length - 1;
        
        // 检查docs/day链接数量
        int docsDayCount = content.split("docs/day/").length - 1;
        
        // 检查历史归档部分
        boolean hasArchive = content.contains("### 历史归档");
        
        System.out.println("Picture链接数量: " + pictureCount);
        System.out.println("Docs/Day链接数量: " + docsDayCount);
        System.out.println("包含历史归档部分: " + hasArchive);
        
        // 显示历史归档部分
        int archiveIndex = content.indexOf("### 历史归档");
        if (archiveIndex != -1) {
            int endIndex = Math.min(archiveIndex + 200, content.length());
            String archiveSection = content.substring(archiveIndex, endIndex);
            System.out.println("\n历史归档部分预览:");
            System.out.println(archiveSection.substring(0, Math.min(200, archiveSection.length())));
        }
        
        // 检查picture目录是否存在
        boolean pictureExists = Files.exists(Paths.get("picture"));
        System.out.println("\nPicture目录存在: " + pictureExists);
        
        // 检查docs/day目录
        boolean docsDayExists = Files.exists(Paths.get("docs", "day"));
        System.out.println("Docs/Day目录存在: " + docsDayExists);
        
        if (docsDayExists) {
            Object[] months = Files.list(Paths.get("docs", "day")).toArray();
            System.out.println("Docs/Day目录下月份数量: " + months.length);
        }
    }
}
