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

    public static Path MONTH_PATH = Paths.get("picture/");



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
     * 写入 README.md（保留项目说明功能）
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
        
        // 简化的README写入，只写入基本信息
        Files.write(README_PATH, "## Bing Wallpaper".getBytes("UTF-8"));
        Files.write(README_PATH, System.lineSeparator().getBytes("UTF-8"), StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes("UTF-8"), StandardOpenOption.APPEND);

        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        // 归档
        String archiveHeader = "### Historical Archive";
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
                Files.write(README_PATH, System.lineSeparator().getBytes("UTF-8"), StandardOpenOption.APPEND);
            }
        }
    }


    /**
     * 按月份创建图片目录
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
            // 不再生成README.md文件，只创建目录结构
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

    
}
