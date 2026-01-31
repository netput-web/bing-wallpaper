import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleTest {
    public static void main(String[] args) throws Exception {
        System.out.println("🚀 简单测试开始...");
        
        // 1. 测试读取数据
        List<Images> bingImages = BingFileUtils.readBing();
        bingImages = bingImages.stream().filter(img -> img.getUrl() != null).collect(Collectors.toList());
        
        System.out.println("📊 读取到 " + bingImages.size() + " 张壁纸数据");
        
        // 2. 显示前5个数据
        for (int i = 0; i < Math.min(5, bingImages.size()); i++) {
            Images img = bingImages.get(i);
            System.out.println("  " + (i+1) + ". " + img.getDate() + " - " + img.getDesc());
        }
        
        System.out.println("✅ 简单测试完成！");
    }
}
