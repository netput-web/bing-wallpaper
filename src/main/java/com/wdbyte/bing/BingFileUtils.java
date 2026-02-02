package com.wdbyte.bing;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件操作工具类
 *
 * @author niujinpeng
 * @date 2021/02/08
 * @link https://github.com/niumoo
 */
public class BingFileUtils {

    public static Path README_PATH = Paths.get("README.md");
    public static Path BING_PATH = Paths.get("bing-wallpaper.md");

    public static Path MONTH_PATH = Paths.get("picture/");

    /**
     * 读取 bing-wallpaper.md
     *
     * @return
     * @throws IOException
     */
    public static List<Images> readBing() throws IOException {
        if (!Files.exists(BING_PATH)) {
            Path parent = BING_PATH.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectory(parent);
            }
            Files.createFile(BING_PATH);
        }
        
        // 使用InputStreamReader确保UTF-8编码
        List<Images> imgList = new ArrayList<>();
        imgList.add(new Images());
        
        try (InputStreamReader reader = new InputStreamReader(
                Files.newInputStream(BING_PATH), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                // 跳过第一行标题
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // 找到描述的结束位置
                int descStart = line.indexOf("[") + 1;
                int descEnd = line.indexOf("]");
                
                // 找到URL的开始和结束位置（最后一个括号对）
                int lastOpenParen = line.lastIndexOf("(");
                int lastCloseParen = line.lastIndexOf(")");

                String date = line.substring(0, 10);
                
                // 安全检查描述内容
                String descContent = "";
                if (descEnd > descStart) {
                    descContent = line.substring(descStart, descEnd);
                } else {
                    System.out.println("DEBUG: 跳过格式错误的行: " + line);
                    continue;
                }
                
                // 安全检查URL内容
                String url = "";
                if (lastOpenParen > descEnd && lastCloseParen > lastOpenParen) {
                    url = line.substring(lastOpenParen + 1, lastCloseParen);
                } else {
                    System.out.println("DEBUG: 跳过URL格式错误的行: " + line);
                    continue;
                }
                
                // 调试输出，检查读取的内容
                System.out.println("DEBUG: 读取的描述内容: " + descContent);
                System.out.println("DEBUG: 读取的URL内容: " + url);
                
                imgList.add(new Images(descContent, date, url));
            }
        }
        
        LogUtils.log("read bing wallpaper,path:%s,size:%d", BING_PATH.toString(), imgList.size());
        return imgList;
    }

    /**
     * 写入 bing-wallpaper.md
     *
     * @param imgList
     * @throws IOException
     */
    public static void writeBing(List<Images> imgList) throws IOException {
        if (!Files.exists(BING_PATH)) {
            Files.createFile(BING_PATH);
        }
        
        // 使用OutputStreamWriter确保UTF-8编码
        try (OutputStreamWriter writer = new OutputStreamWriter(
                Files.newOutputStream(BING_PATH), StandardCharsets.UTF_8)) {
            writer.write("## Bing Wallpaper");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
            
            for (Images images : imgList) {
                writer.write(images.formatMarkdown());
                writer.write(System.lineSeparator());
                writer.write(System.lineSeparator());
                writer.write(System.lineSeparator());
            }
        }
        
        LogUtils.log("write bing wallpaper,path:%s,size:%d", BING_PATH.toString(), imgList.size());
    }

    /**
     * 读取 README.md
     *
     * @return
     * @throws IOException
     */
    public static List<Images> readReadme() throws IOException {
        if (!Files.exists(README_PATH)) {
            Files.createFile(README_PATH);
        }
        List<String> allLines = Files.readAllLines(README_PATH, StandardCharsets.UTF_8);
        List<Images> imgList = new ArrayList<>();
        for (int i = 3; i < allLines.size(); i++) {
            String content = allLines.get(i);
            Arrays.stream(content.split("\\|"))
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    int dateStartIndex = s.indexOf("[", 3) + 1;
                    int urlStartIndex = s.indexOf("(", 4) + 1;
                    String date = s.substring(dateStartIndex, dateStartIndex + 10);
                    String url = s.substring(urlStartIndex, s.length() - 1);
                    return new Images(null, date, url);
                })
                .forEach(imgList::add);
        }
        return imgList;
    }

    /**
     * 写入 README.md
     *
     * @param imgList
     * @throws IOException
     */
    public static void writeReadme(List<Images> imgList) throws IOException {
        if (!Files.exists(README_PATH)) {
            Files.createFile(README_PATH);
        }
        List<Images> imagesList = new ArrayList<>(0);
        if (imgList.size() > 30) {
            imagesList = imgList.subList(0, 30);
        } else {
            imagesList = imgList;
        }
        writeFile(README_PATH, imagesList, null);

        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        // 归档
        String archiveHeader = "### 历史归档：";
        Files.write(README_PATH, archiveHeader.getBytes("UTF-8"), StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes("UTF-8"), StandardOpenOption.APPEND);
        List<String> dateList = imgList.stream()
            .map(Images::getDate)
            .map(date -> date.substring(0, 7))
            .distinct()
            .collect(Collectors.toList());
        int i = 0;
        for (String date : dateList) {
            // 将YYYY-MM格式转换为YYYYMM格式
            String yearMonth = date.replace("-", "");
            // 生成新的链接格式：docs/day/YYYYMM/
            String link = String.format("[%s](%s%s/) | ", date, MONTH_PATH.toString(), yearMonth);
            Files.write(README_PATH, link.getBytes("UTF-8"), StandardOpenOption.APPEND);
            i++;
            if (i % 8 == 0) {
                Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }
        }
    }


    /**
     * 按月份写入图片信息
     *
     * @param imgList
     * @throws IOException
     */
    public static void writeMonthInfo(List<Images> imgList) throws IOException {
        Map<String, List<Images>> monthMap = convertImgListToMonthMap(imgList);
        for (String key : monthMap.keySet()) {
            Path path = MONTH_PATH.resolve(key);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            path = path.resolve("README.md");
            writeFile(path, monthMap.get(key), key);
        }
    }

    /**
     * 转换图片列表为月度 Map
     *
     * @param imagesList
     * @return
     */
    public static Map<String, List<Images>> convertImgListToMonthMap( List<Images> imagesList){
        Map<String, List<Images>> monthMap = new LinkedHashMap<>();
        for (Images images : imagesList) {
            if (images.getUrl() == null){
                continue;
            }
            String key = images.getDate().substring(0, 7);
            if (monthMap.containsKey(key)) {
                monthMap.get(key).add(images);
            } else {
                ArrayList<Images> list = new ArrayList<>();
                list.add(images);
                monthMap.put(key, list);
            }
        }
        return monthMap;
    }

    /**
     * 写入图片列表到指定位置
     *
     * @param path
     * @param imagesList
     * @param name
     * @throws IOException
     */
    private static void writeFile(Path path, List<Images> imagesList, String name) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        String title = "## Bing Wallpaper";
        if (name != null) {
            title = "## Bing Wallpaper (" + name + ")";
        }
        Files.write(path, title.getBytes());
        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(path, imagesList.get(0).toLarge().getBytes(), StandardOpenOption.APPEND);
        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(path, "|      |      |      |".getBytes(), StandardOpenOption.APPEND);
        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(path, "| :----: | :----: | :----: |".getBytes(), StandardOpenOption.APPEND);
        Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        int i = 1;
        for (Images images : imagesList) {
            Files.write(path, ("|" + images.toString()).getBytes(), StandardOpenOption.APPEND);
            if (i % 3 == 0) {
                Files.write(path, "|".getBytes(), StandardOpenOption.APPEND);
                Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }
            i++;
        }
        if (i % 3 != 1) {
            Files.write(path, "|".getBytes(), StandardOpenOption.APPEND);
        }
    }

}
