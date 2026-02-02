package com.wdbyte.bing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Markdown备份生成器 - 从JSON数据生成MD备份文件
 * @author niulang
 * @date 2026/02/03
 */
public class MarkdownBackupGenerator {
    
    private static final Path EN_MD_PATH = Paths.get("bing-wallpaper.md");
    private static final Path ZH_CN_MD_PATH = Paths.get("zh-cn", "bing-wallpaper.md");
    
    /**
     * 从JSON数据生成MD备份文件
     */
    public static void generateMarkdownBackup() {
        try {
            System.out.println("开始生成MD备份文件...");
            
            // 生成英文区MD文件
            generateEnglishMarkdown();
            
            // 生成中文区MD文件
            generateChineseMarkdown();
            
            System.out.println("MD备份文件生成完成");
            
        } catch (Exception e) {
            System.err.println("MD备份生成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 生成英文区MD文件
     */
    private static void generateEnglishMarkdown() throws Exception {
        List<Images> englishImages = JsonDataManager.readDataByRegion("en-us");
        
        StringBuilder content = new StringBuilder();
        content.append("## Bing Wallpaper\n");
        
        // 按日期倒序排列
        englishImages.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .forEach(img -> {
                    content.append(img.getDate())
                           .append(" | [")
                           .append(img.getDesc())
                           .append("](")
                           .append(img.getUrl())
                           .append(")\n\n");
                });
        
        // 确保目录存在
        Files.createDirectories(EN_MD_PATH.getParent());
        
        // 写入文件
        Files.write(EN_MD_PATH, content.toString().getBytes("UTF-8"));
        
        System.out.println("英文区MD文件生成完成: " + englishImages.size() + " 条数据");
    }
    
    /**
     * 生成中文区MD文件
     */
    private static void generateChineseMarkdown() throws Exception {
        List<Images> chineseImages = JsonDataManager.readDataByRegion("zh-cn");
        
        StringBuilder content = new StringBuilder();
        content.append("## 必应壁纸\n");
        
        // 按日期倒序排列
        chineseImages.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .forEach(img -> {
                    content.append(img.getDate())
                           .append(" | [")
                           .append(img.getDesc())
                           .append("](")
                           .append(img.getUrl())
                           .append(")\n\n");
                });
        
        // 确保目录存在
        Files.createDirectories(ZH_CN_MD_PATH.getParent());
        
        // 写入文件
        Files.write(ZH_CN_MD_PATH, content.toString().getBytes("UTF-8"));
        
        System.out.println("中文区MD文件生成完成: " + chineseImages.size() + " 条数据");
    }
    
    /**
     * 验证MD文件完整性
     */
    public static void validateMarkdownFiles() {
        try {
            System.out.println("=== MD文件验证 ===");
            
            // 验证英文MD文件
            if (Files.exists(EN_MD_PATH)) {
                long enLines = Files.lines(EN_MD_PATH).count();
                System.out.println("英文MD文件: " + enLines + " 行");
            } else {
                System.out.println("英文MD文件: 不存在");
            }
            
            // 验证中文MD文件
            if (Files.exists(ZH_CN_MD_PATH)) {
                long zhLines = Files.lines(ZH_CN_MD_PATH).count();
                System.out.println("中文MD文件: " + zhLines + " 行");
            } else {
                System.out.println("中文MD文件: 不存在");
            }
            
        } catch (Exception e) {
            System.err.println("MD文件验证失败: " + e.getMessage());
        }
    }
}
