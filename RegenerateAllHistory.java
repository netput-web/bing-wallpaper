import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import com.wdbyte.bing.Wallpaper;
import com.wdbyte.bing.html.WebSiteGenerator;

/**
 * 重新生成所有历史HTML文件 - 修复编码问题
 */
public class RegenerateAllHistory {
    
    public static void main(String[] args) throws IOException {
        // 强制设置系统编码为UTF-8
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 开始重新生成所有历史HTML文件 ===");
        
        // 设置中文区配置
        Wallpaper.changeConfig("zh-CN");
        
        // 读取所有历史数据
        List<Images> allImages = BingFileUtils.readBing();
        System.out.println("读取到 " + allImages.size() + " 条历史数据");
        
        // 按月份分组
        Map<String, List<Images>> monthMap = groupByMonth(allImages);
        System.out.println("分组得到 " + monthMap.size() + " 个月份");
        
        // 创建WebSiteGenerator实例
        WebSiteGenerator generator = new WebSiteGenerator();
        
        // 重新生成每个月的HTML
        for (String month : monthMap.keySet()) {
            System.out.println("重新生成月份: " + month);
            List<Images> monthImages = monthMap.get(month);
            
            try {
                // 调用WebSiteGenerator的月度生成方法
                generator.htmlGeneratorMonth(createMonthMap(month, monthImages));
                System.out.println("✅ " + month + " 生成成功");
            } catch (Exception e) {
                System.err.println("❌ " + month + " 生成失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 重新生成主页
        System.out.println("重新生成主页...");
        try {
            generator.htmlGenerator();
            System.out.println("✅ 主页生成成功");
        } catch (Exception e) {
            System.err.println("❌ 主页生成失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== 历史HTML文件重新生成完成 ===");
    }
    
    private static Map<String, List<Images>> groupByMonth(List<Images> images) {
        Map<String, List<Images>> monthMap = new TreeMap<>();
        
        for (Images img : images) {
            String date = img.getDate();
            if (date != null && date.length() >= 7) {
                String month = date.substring(0, 7); // YYYY-MM
                monthMap.computeIfAbsent(month, k -> new ArrayList<>()).add(img);
            }
        }
        
        return monthMap;
    }
    
    private static Map<String, List<Images>> createMonthMap(String month, List<Images> images) {
        Map<String, List<Images>> monthMap = new TreeMap<>();
        monthMap.put(month, images);
        return monthMap;
    }
}
