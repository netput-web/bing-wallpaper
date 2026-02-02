package com.wdbyte.bing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据清理工具 - 彻底解决编码问题
 */
public class DataCleaner {
    
    public static void main(String[] args) throws IOException {
        System.out.println("=== 开始彻底清理编码问题 ===");
        
        // 清理英文数据
        cleanMarkdownFile("bing-wallpaper.md");
        
        // 清理中文数据
        cleanMarkdownFile("zh-cn/bing-wallpaper.md");
        
        System.out.println("=== 编码清理完成 ===");
    }
    
    private static void cleanMarkdownFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("文件不存在: " + filePath);
            return;
        }
        
        System.out.println("清理文件: " + filePath);
        
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> cleanedLines = new ArrayList<>();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                cleanedLines.add(line);
                continue;
            }
            
            // 跳过标题行
            if (line.startsWith("##")) {
                cleanedLines.add(line);
                continue;
            }
            
            // 清理描述中的乱码
            String cleanedLine = cleanDescription(line);
            cleanedLines.add(cleanedLine);
            
            if (!line.equals(cleanedLine)) {
                System.out.println("  修复: " + line);
                System.out.println("  ->  " + cleanedLine);
            }
        }
        
        // 写回文件
        Files.write(path, cleanedLines, StandardCharsets.UTF_8);
        System.out.println("文件清理完成: " + filePath);
    }
    
    private static String cleanDescription(String line) {
        // 修复常见的乱码字符
        line = line.replace("漏", "©");
        line = line.replace("漏", "©");
        line = line.replace("漏", "©");
        line = line.replace("漏", "©");
        
        // 修复其他可能的编码问题
        line = line.replace("闃垮皵鍗戞柉灞卞湡鎷ㄩ紶", "阿尔卑斯山土拨鼠");
        line = line.replace("闇嶈但闄舵仼鍥藉", "霍赫陶恩");
        line = line.replace("濂ュ湴鍒", "奥地利");
        
        // 使用正则表达式清理各种乱码模式
        line = line.replaceAll("[\\uFFFD\\u00BF\\u00A1]", "");
        
        return line;
    }
}
