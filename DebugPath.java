import com.wdbyte.bing.BingFileUtils;
import java.nio.file.Paths;

public class DebugPath {
    public static void main(String[] args) throws Exception {
        System.out.println("=== 调试路径配置 ===");
        
        // 检查当前路径配置
        System.out.println("MONTH_PATH: " + BingFileUtils.MONTH_PATH.toString());
        System.out.println("MONTH_PATH 类型: " + BingFileUtils.MONTH_PATH.getClass().getName());
        
        // 测试路径拼接
        String testDate = "2026-02";
        String yearMonth = testDate.replace("-", "");
        String link = String.format("[%s](%s%s/) | ", testDate, BingFileUtils.MONTH_PATH.toString(), yearMonth);
        System.out.println("生成的链接: " + link);
        
        // 检查正确的路径应该是
        System.out.println("期望的路径: docs/day/" + yearMonth + "/");
    }
}
