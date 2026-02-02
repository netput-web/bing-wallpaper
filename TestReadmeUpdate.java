import com.wdbyte.bing.BingFileUtils;
import com.wdbyte.bing.Images;
import com.wdbyte.bing.Wallpaper;
import java.util.ArrayList;
import java.util.List;

public class TestReadmeUpdate {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        System.out.println("=== 测试README.md自动更新 ===");
        
        // 设置正确的配置
        Wallpaper.changeConfig("en-us");
        System.out.println("MONTH_PATH设置为: " + BingFileUtils.MONTH_PATH.toString());
        
        // 创建测试数据
        List<Images> testImages = new ArrayList<>();
        
        // 添加几个测试图片
        Images img1 = new Images();
        img1.setDate("2026-02-03");
        img1.setDesc("Test Image 1");
        img1.setUrl("https://test.com/1.jpg");
        testImages.add(img1);
        
        Images img2 = new Images();
        img2.setDate("2026-01-15");
        img2.setDesc("Test Image 2");
        img2.setUrl("https://test.com/2.jpg");
        testImages.add(img2);
        
        Images img3 = new Images();
        img3.setDate("2025-12-20");
        img3.setDesc("Test Image 3");
        img3.setUrl("https://test.com/3.jpg");
        testImages.add(img3);
        
        // 测试写入README.md
        System.out.println("测试写入README.md...");
        BingFileUtils.writeReadme(testImages);
        
        System.out.println("README.md更新完成！");
        System.out.println("请检查README.md文件末尾的历史归档链接格式");
    }
}
