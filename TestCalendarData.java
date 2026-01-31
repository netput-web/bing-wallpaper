import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import com.wdbyte.bing.utils.CalendarDataGenerator;
import java.util.List;
import java.util.stream.Collectors;

public class TestCalendarData {
    public static void main(String[] args) throws Exception {
        System.out.println("🚀 测试日历数据生成...");
        
        List<Images> bingImages = BingFileUtils.readBing();
        bingImages = bingImages.stream().filter(img -> img.getUrl() != null).collect(Collectors.toList());
        
        System.out.println("📊 读取到 " + bingImages.size() + " 张壁纸数据");
        
        // 生成日历数据文件
        CalendarDataGenerator.generateAllCalendarDataFiles(bingImages, "docs");
        
        System.out.println("✅ 测试完成！");
    }
}
