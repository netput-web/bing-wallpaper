import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import com.wdbyte.bing.utils.CalendarDataGeneratorOptimized;
import java.util.List;
import java.util.stream.Collectors;

public class TestOptimizedCalendar {
    public static void main(String[] args) throws Exception {
        System.out.println("🧪 测试优化版日历数据生成器...");
        
        // 读取数据
        List<Images> bingImages = BingFileUtils.readBing();
        bingImages = bingImages.stream().filter(img -> img.getUrl() != null).collect(Collectors.toList());
        
        System.out.println("📊 读取到 " + bingImages.size() + " 张壁纸数据");
        
        // 第一次生成（应该会生成）
        System.out.println("\n=== 第一次生成 ===");
        CalendarDataGeneratorOptimized.generateCalendarDataFilesSmart(bingImages, "docs");
        
        // 第二次生成（应该会跳过）
        System.out.println("\n=== 第二次生成（相同数据）===");
        CalendarDataGeneratorOptimized.generateCalendarDataFilesSmart(bingImages, "docs");
        
        // 修改数据后再次生成（应该会重新生成）
        System.out.println("\n=== 修改数据后生成 ===");
        if (!bingImages.isEmpty()) {
            Images firstImage = bingImages.get(0);
            bingImages.set(0, new Images("Modified Title", firstImage.getDate(), firstImage.getUrl()));
        }
        CalendarDataGeneratorOptimized.generateCalendarDataFilesSmart(bingImages, "docs");
        
        System.out.println("\n✅ 测试完成！");
    }
}
