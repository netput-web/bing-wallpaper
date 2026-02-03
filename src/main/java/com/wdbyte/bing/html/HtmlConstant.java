package com.wdbyte.bing.html;

import java.util.Calendar;
import java.util.Map;

import com.wdbyte.bing.Images;
import com.wdbyte.bing.Wallpaper;

/**
 * @author niulang
 * @date 2022/08/18
 */
public class HtmlConstant {

    /**
     * 现代化导航栏
     */
    public static class Navigation {
        public static final String VAR_NAVIGATION = "${navigation}";
        
        // 区域信息映射
        private static final String[][] REGIONS = {
            {"us", "en-US", "United States", "🇺🇸"},
            {"cn", "zh-CN", "中国", "🇨🇳"},
            {"jp", "ja-JP", "日本", "🇯🇵"},
            {"in", "en-IN", "India", "🇮🇳"},
            {"br", "pt-BR", "Brasil", "🇧🇷"},
            {"fr", "fr-FR", "France", "🇫🇷"},
            {"de", "de-DE", "Deutschland", "🇩🇪"},
            {"ca", "en-CA", "Canada", "🇨🇦"},
            {"gb", "en-GB", "United Kingdom", "🇬🇧"},
            {"it", "it-IT", "Italia", "🇮🇹"},
            {"es", "es-ES", "España", "🇪🇸"},
            {"fr-ca", "fr-CA", "Canada (FR)", "🇨🇦"}
        };
        
        // 现代化导航栏模板
        private static final String MODERN_NAVIGATION = ""
            + "<nav class=\"modern-nav\">\n"
            + "  <div class=\"nav-container\">\n"
            + "    <div class=\"nav-brand\">\n"
            + "      <a href=\"/\" class=\"brand-link\">\n"
            + "        <span class=\"brand-icon\">🖼️</span>\n"
            + "        <span class=\"brand-text\">Bing Wallpaper</span>\n"
            + "      </a>\n"
            + "    </div>\n"
            + "    \n"
            + "    <div class=\"nav-menu\">\n"
            + "      <div class=\"nav-item dropdown\">\n"
            + "        <a href=\"#\" class=\"nav-link dropdown-toggle\" onclick=\"toggleDropdown('region-dropdown')\">\n"
            + "          <span class=\"flag-icon\">${current_flag}</span>\n"
            + "          <span class=\"region-name\">${current_region_name}</span>\n"
            + "          <span class=\"dropdown-arrow\">▼</span>\n"
            + "        </a>\n"
            + "        <div id=\"region-dropdown\" class=\"dropdown-menu\">\n"
            + "          <div class=\"dropdown-header\">选择地区</div>\n"
            + "          ${region_options}\n"
            + "        </div>\n"
            + "      </div>\n"
            + "      \n"
            + "      <a href=\"#\" class=\"nav-link\" onclick=\"w3_open()\">\n"
            + "        <span class=\"nav-icon\">📅</span>\n"
            + "        <span class=\"nav-text\">历史归档</span>\n"
            + "      </a>\n"
            + "      \n"
            + "      <a href=\"/download.html\" class=\"nav-link\">\n"
            + "        <span class=\"nav-icon\">📱</span>\n"
            + "        <span class=\"nav-text\">客户端</span>\n"
            + "      </a>\n"
            + "    </div>\n"
            + "    \n"
            + "    <div class=\"nav-search\">\n"
            + "      <div class=\"search-container\">\n"
            + "        <input type=\"text\" class=\"search-input\" placeholder=\"搜索壁纸...\" id=\"search\">\n"
            + "        <button class=\"search-btn\" onclick=\"performSearch()\">\n"
            + "          <span>🔍</span>\n"
            + "        </button>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</nav>\n"
            + "\n"
            + "<style>\n"
            + "/* 现代化导航栏样式 */\n"
            + ".modern-nav {\n"
            + "  position: fixed;\n"
            + "  top: 0;\n"
            + "  left: 0;\n"
            + "  right: 0;\n"
            + "  background: rgba(255, 255, 255, 0.95);\n"
            + "  backdrop-filter: blur(10px);\n"
            + "  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);\n"
            + "  z-index: 1000;\n"
            + "  transition: all 0.3s ease;\n"
            + "}\n"
            + "\n"
            + ".nav-container {\n"
            + "  max-width: 1200px;\n"
            + "  margin: 0 auto;\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  justify-content: space-between;\n"
            + "  padding: 0 20px;\n"
            + "  height: 64px;\n"
            + "}\n"
            + "\n"
            + ".nav-brand {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "}\n"
            + "\n"
            + ".brand-link {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  text-decoration: none;\n"
            + "  color: #333;\n"
            + "  font-weight: 600;\n"
            + "  font-size: 18px;\n"
            + "  transition: color 0.3s ease;\n"
            + "}\n"
            + "\n"
            + ".brand-link:hover {\n"
            + "  color: #0078d4;\n"
            + "}\n"
            + "\n"
            + ".brand-icon {\n"
            + "  font-size: 24px;\n"
            + "  margin-right: 8px;\n"
            + "}\n"
            + "\n"
            + ".brand-text {\n"
            + "  font-weight: 700;\n"
            + "}\n"
            + "\n"
            + ".nav-menu {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  gap: 8px;\n"
            + "}\n"
            + "\n"
            + ".nav-item {\n"
            + "  position: relative;\n"
            + "}\n"
            + "\n"
            + ".nav-link {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  gap: 6px;\n"
            + "  padding: 8px 16px;\n"
            + "  text-decoration: none;\n"
            + "  color: #666;\n"
            + "  border-radius: 8px;\n"
            + "  transition: all 0.3s ease;\n"
            + "  font-weight: 500;\n"
            + "}\n"
            + "\n"
            + ".nav-link:hover {\n"
            + "  background: rgba(0, 120, 212, 0.1);\n"
            + "  color: #0078d4;\n"
            + "}\n"
            + "\n"
            + ".nav-icon {\n"
            + "  font-size: 16px;\n"
            + "}\n"
            + "\n"
            + ".nav-text {\n"
            + "  font-size: 14px;\n"
            + "}\n"
            + "\n"
            + "/* 下拉菜单样式 */\n"
            + ".dropdown {\n"
            + "  position: relative;\n"
            + "}\n"
            + "\n"
            + ".dropdown-toggle {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  gap: 6px;\n"
            + "}\n"
            + "\n"
            + ".dropdown-arrow {\n"
            + "  font-size: 12px;\n"
            + "  transition: transform 0.3s ease;\n"
            + "}\n"
            + "\n"
            + ".dropdown.active .dropdown-arrow {\n"
            + "  transform: rotate(180deg);\n"
            + "}\n"
            + "\n"
            + ".dropdown-menu {\n"
            + "  position: absolute;\n"
            + "  top: 100%;\n"
            + "  left: 0;\n"
            + "  background: white;\n"
            + "  border-radius: 12px;\n"
            + "  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);\n"
            + "  min-width: 200px;\n"
            + "  opacity: 0;\n"
            + "  visibility: hidden;\n"
            + "  transform: translateY(-10px);\n"
            + "  transition: all 0.3s ease;\n"
            + "  z-index: 1001;\n"
            + "}\n"
            + "\n"
            + ".dropdown.active .dropdown-menu {\n"
            + "  opacity: 1;\n"
            + "  visibility: visible;\n"
            + "  transform: translateY(0);\n"
            + "}\n"
            + "\n"
            + ".dropdown-header {\n"
            + "  padding: 12px 16px;\n"
            + "  font-weight: 600;\n"
            + "  color: #333;\n"
            + "  border-bottom: 1px solid #f0f0f0;\n"
            + "  font-size: 14px;\n"
            + "}\n"
            + "\n"
            + ".dropdown-item {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  gap: 12px;\n"
            + "  padding: 10px 16px;\n"
            + "  color: #666;\n"
            + "  text-decoration: none;\n"
            + "  transition: all 0.2s ease;\n"
            + "  font-size: 14px;\n"
            + "}\n"
            + "\n"
            + ".dropdown-item:hover {\n"
            + "  background: rgba(0, 120, 212, 0.1);\n"
            + "  color: #0078d4;\n"
            + "}\n"
            + "\n"
            + ".flag-icon {\n"
            + "  font-size: 18px;\n"
            + "}\n"
            + "\n"
            + ".region-name {\n"
            + "  font-weight: 500;\n"
            + "}\n"
            + "\n"
            + "/* 搜索框样式 */\n"
            + ".nav-search {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "}\n"
            + "\n"
            + ".search-container {\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  background: #f5f5f5;\n"
            + "  border-radius: 20px;\n"
            + "  padding: 4px;\n"
            + "  transition: all 0.3s ease;\n"
            + "}\n"
            + "\n"
            + ".search-container:focus-within {\n"
            + "  background: white;\n"
            + "  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n"
            + "}\n"
            + "\n"
            + ".search-input {\n"
            + "  border: none;\n"
            + "  background: transparent;\n"
            + "  outline: none;\n"
            + "  padding: 6px 12px;\n"
            + "  font-size: 14px;\n"
            + "  width: 200px;\n"
            + "  color: #333;\n"
            + "}\n"
            + "\n"
            + ".search-input::placeholder {\n"
            + "  color: #999;\n"
            + "}\n"
            + "\n"
            + ".search-btn {\n"
            + "  border: none;\n"
            + "  background: #0078d4;\n"
            + "  color: white;\n"
            + "  border-radius: 50%;\n"
            + "  width: 28px;\n"
            + "  height: 28px;\n"
            + "  display: flex;\n"
            + "  align-items: center;\n"
            + "  justify-content: center;\n"
            + "  cursor: pointer;\n"
            + "  transition: all 0.3s ease;\n"
            + "  font-size: 12px;\n"
            + "}\n"
            + "\n"
            + ".search-btn:hover {\n"
            + "  background: #106ebe;\n"
            + "  transform: scale(1.05);\n"
            + "}\n"
            + "\n"
            + "/* 响应式设计 */\n"
            + "@media (max-width: 768px) {\n"
            + "  .nav-container {\n"
            + "    padding: 0 16px;\n"
            + "  }\n"
            + "  \n"
            + "  .nav-text {\n"
            + "    display: none;\n"
            + "  }\n"
            + "  \n"
            + "  .search-input {\n"
            + "    width: 120px;\n"
            + "  }\n"
            + "  \n"
            + "  .brand-text {\n"
            + "    display: none;\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "@media (max-width: 480px) {\n"
            + "  .nav-search {\n"
            + "    display: none;\n"
            + "  }\n"
            + "  \n"
            + "  .nav-menu {\n"
            + "    gap: 4px;\n"
            + "  }\n"
            + "  \n"
            + "  .nav-link {\n"
            + "    padding: 8px 12px;\n"
            + "  }\n"
            + "}\n"
            + "</style>\n"
            + "\n"
            + "<script>\n"
            + "// 下拉菜单功能\n"
            + "function toggleDropdown(id) {\n"
            + "  const dropdown = document.getElementById(id);\n"
            + "  const parent = dropdown.parentElement;\n"
            + "  \n"
            + "  // 关闭其他下拉菜单\n"
            + "  document.querySelectorAll('.dropdown').forEach(d => {\n"
            + "    if (d !== parent) d.classList.remove('active');\n"
            + "  });\n"
            + "  \n"
            + "  // 切换当前下拉菜单\n"
            + "  parent.classList.toggle('active');\n"
            + "}\n"
            + "\n"
            + "// 点击外部关闭下拉菜单\n"
            + "document.addEventListener('click', function(e) {\n"
            + "  if (!e.target.closest('.dropdown')) {\n"
            + "    document.querySelectorAll('.dropdown').forEach(d => {\n"
            + "      d.classList.remove('active');\n"
            + "    });\n"
            + "  }\n"
            + "});\n"
            + "\n"
            + "// 搜索功能\n"
            + "function performSearch() {\n"
            + "  const searchInput = document.getElementById('search');\n"
            + "  const query = searchInput.value.trim();\n"
            + "  \n"
            + "  if (query) {\n"
            + "    // 这里可以实现搜索逻辑\n"
            + "    console.log('搜索:', query);\n"
            + "    // 暂时跳转到搜索结果页面\n"
            + "    // window.location.href = '/search?q=' + encodeURIComponent(query);\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "// 回车搜索\n"
            + "document.addEventListener('DOMContentLoaded', function() {\n"
            + "  const searchInput = document.getElementById('search');\n"
            + "  if (searchInput) {\n"
            + "    searchInput.addEventListener('keypress', function(e) {\n"
            + "      if (e.key === 'Enter') {\n"
            + "        performSearch();\n"
            + "      }\n"
            + "    });\n"
            + "  }\n"
            + "});\n"
            + "</script>";
        
        public static String getModernNavigation(String currentRegion) {
            String result = MODERN_NAVIGATION;
            
            // 查找当前区域信息
            String currentFlag = "🌍";
            String currentRegionName = "Select Region";
            String regionOptions = "";
            
            for (String[] regionInfo : REGIONS) {
                String code = regionInfo[0];
                String region = regionInfo[1];
                String name = regionInfo[2];
                String flag = regionInfo[3];
                
                // 映射到实际的目录名
                String actualRegion;
                switch (code) {
                    case "us":
                        actualRegion = ""; // 美国区域是根目录
                        break;
                    case "jp":
                        actualRegion = "ja-jp";
                        break;
                    case "in":
                        actualRegion = "en-in";
                        break;
                    case "br":
                        actualRegion = "pt-br";
                        break;
                    case "fr":
                        actualRegion = "fr-fr";
                        break;
                    case "de":
                        actualRegion = "de-de";
                        break;
                    case "ca":
                        actualRegion = "en-ca";
                        break;
                    case "gb":
                        actualRegion = "en-gb";
                        break;
                    case "it":
                        actualRegion = "it-it";
                        break;
                    case "es":
                        actualRegion = "es-es";
                        break;
                    case "cn":
                        actualRegion = "zh-cn";
                        break;
                    case "fr-ca":
                        actualRegion = "fr-ca";
                        break;
                    default:
                        actualRegion = code;
                        break;
                }
                
                // 为US区域生成特殊链接，其他区域生成标准链接
                String regionOption;
                if ("us".equals(code)) {
                    regionOption = String.format(
                        "<a href=\"/index.html\" class=\"dropdown-item\">\n" +
                        "  <span class=\"flag-icon\">%s</span>\n" +
                        "  <span class=\"region-name\">%s</span>\n" +
                        "</a>",
                        flag, name
                    );
                } else {
                    regionOption = String.format(
                        "<a href=\"/%s/index.html\" class=\"dropdown-item\">\n" +
                        "  <span class=\"flag-icon\">%s</span>\n" +
                        "  <span class=\"region-name\">%s</span>\n" +
                        "</a>",
                        actualRegion, flag, name
                    );
                }
                
                regionOptions += regionOption;
                
                // 设置当前区域信息
                if (region.equalsIgnoreCase(currentRegion)) {
                    currentFlag = flag;
                    currentRegionName = name;
                }
            }
            
            // 替换模板变量
            result = result.replace("${current_flag}", currentFlag);
            result = result.replace("${current_region_name}", currentRegionName);
            result = result.replace("${region_options}", regionOptions);
            
            return result;
        }
    }

    /**
     * 侧边栏目录的归档菜单
     */
    public static class Sidebar{
        public static final String VAR_SIDABAR = "${sidabar}";
        public static final String VAR_SIDABAR_NOW_COLOR = "w3-green";
        public static final String VAR_SIDABAR_COLOR = "w3-hover-green";
        /**
         * <a href="#" class="w3-bar-item w3-button w3-hover-text-green w3-large">2022-08</a>
         */
        private static final String SIDABAR_MENU = "<a href=\"${sidabar_href_url}\" class=\"w3-bar-item w3-button w3-hover-green w3-large\">${sidabar_href_title}</a>";

        public static String getSidabarMenuList(String hrefUrl, String hrefTitle) {
            String result = SIDABAR_MENU.replace("${sidabar_href_url}", hrefUrl);
            return result.replace("${sidabar_href_title}", hrefTitle);
        }
    }

    /**
     * 头部图片
     */
    public static class Head{
        public static final String HEAD_IMG_URL = "${head_img_url}";
        public static final String HEAD_IMG_DESC = "${head_img_desc}";
        public static final String HEAD_TITLE = "${head_title}";
    }

    /**
     * 图片列表
     */
    public static class ImgCard {
        public static final String VAR_IMG_CARD_LIST = "${img_card_list}";
        private static final String VAR_IMG_CARD_URL = "${img_card_url}";
        private static final String VAR_IMG_DETAIL_URL = "${img_detail_url}";
        private static final String VAR_IMG_CRARD_REGION = "${img_card_region}";

        private static final String VAR_IMG_CARD_DOWNLOAD_URL_PREVIEW = "${img_card_download_url_preview}";
        private static final String VAR_IMG_CARD_DOWNLOAD_URL = "${img_card_download_url}";
        private static final String VAR_IMG_CARD_DATE = "${img_card_date}";
        private static final String VAR_IMG_CARD_TITLE = "${img_card_title}";
        private static final String IMG_CARD = ""
            + "<div class=\"img-card\" style=\"width: 30%; margin-bottom: 20px;\">\n"
            + "  <div class=\"img-container\" style=\"position: relative; width: 100%; padding-bottom: 56.25%; overflow: hidden; border-radius: 8px;\">\n"
            + "    <img class=\"smallImg\" src=\"${img_card_url}&pid=hp&w=50\" style=\"position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; filter: blur(10px); opacity: 0;\" />\n"
            + "    <a href=\"${img_detail_url}\" target=\"_blank\">\n"
            + "      <img class=\"bigImg\" src=\"${img_card_download_url_preview}&pid=hp&w=384&h=216&rs=1&c=4\" style=\"position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s ease;\" onload=\"imgloading(this)\" />\n"
            + "    </a>\n"
            + "  </div>\n"
            + "  <div class=\"img-info\" style=\"padding: 12px 16px;\">\n"
            + "    <div class=\"img-title\" style=\"font-size: 14px; font-weight: 500; color: #333; margin-bottom: 8px; line-height: 1.5; height: 21px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;\">${img_card_title}</div>\n"
            + "    <div class=\"img-meta\" style=\"display: flex; align-items: center; justify-content: space-between; font-size: 12px; color: #666;\">\n"
            + "      <span class=\"img-date\" style=\"color: #999;\">${img_card_date}</span>\n"
            + "      <div class=\"img-actions\" style=\"display: flex; gap: 6px; align-items: center;\">\n"
            + "        <a href=\"${img_detail_url}\" target=\"_blank\" class=\"download-link\" style=\"color: #1976d2; text-decoration: none; padding: 3px 8px; border-radius: 3px; transition: all 0.2s ease; font-size: 11px; border: 1px solid #e0e0e0; background: #fafafa;\">4K</a>\n"
            + "        <button class=\"like-button img-btn\" onclick=\"updateLove('${img_card_region}','${img_card_date}')\" style=\"background: #fff; color: #999; border: 1px solid #e0e0e0; border-radius: 3px; padding: 3px 8px; font-size: 11px; cursor: pointer; transition: all 0.2s ease;\">♥</button>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</div>";

        public static String getImgCard(Images bingImage) {
            String result = IMG_CARD.replace(VAR_IMG_CARD_URL, bingImage.getSimpleUrl());
            result = result.replace(VAR_IMG_CARD_DOWNLOAD_URL_PREVIEW, bingImage.getSimpleUrl());
            result = result.replace(VAR_IMG_CARD_DOWNLOAD_URL, bingImage.getUrl());
            result = result.replace(VAR_IMG_DETAIL_URL, bingImage.getDetailUrlPath());
            result = result.replace(VAR_IMG_DETAIL_URL, bingImage.getDetailUrlPath());
            result = result.replace(VAR_IMG_CRARD_REGION, Wallpaper.CURRENT_REGION.toLowerCase());
            result = result.replace(VAR_IMG_CARD_DATE, bingImage.getDate());
            return result.replace(VAR_IMG_CARD_TITLE, bingImage.getDesc());
        }
    }
    /**
     * 底部归档 - Fluent Design 日历
     */
    public static class MonthHistory{
        public static final String VAR_MONTH_HISTORY = "${month_history}";
        public static final String VAR_MONTH_HISTORY_NOW_MONTH_COLOR = "w3-green";
        public static final String VAR_MONTH_HISTORY_MONTH_COLOR = "w3-light-grey";
        private static final String VAR_MONTH_HISTORY_HREF_URL = "${month_href_url}";
        private static final String VAR_MONTH_HISTORY_HREF_TITLE = "${month_href_title}";
        
        // 新增预览区域变量
        private static final String VAR_PREVIEW_IMG_URL = "${preview_img_url}";
        private static final String VAR_PREVIEW_TITLE = "${preview_title}";
        private static final String VAR_PREVIEW_DESC = "${preview_desc}";
        private static final String VAR_PREVIEW_DOWNLOAD_URL = "${preview_download_url}";
        private static final String VAR_PREVIEW_DETAIL_URL = "${preview_detail_url}";
        private static final String VAR_PREVIEW_DATE = "${preview_date}";
        
        // 新的Fluent Design日历结构 - 带预览区域和年份选择器
        private static final String FLUENT_CALENDAR = ""
            + "<div class=\"calendar-preview-container\">\n"
            + "  <div class=\"calendar-section\">\n"
            + "    <div class=\"calendar-header-section\">\n"
            + "      <h3 class=\"calendar-title\">搜索历史壁纸</h3>\n"
            + "      <div class=\"calendar-nav\">\n"
            + "        <div class=\"nav-controls\">\n"
            + "          <button class=\"nav-button nav-prev\" onclick=\"calendarNavigate('prev')\">◀</button>\n"
            + "          <div class=\"nav-info\">\n"
            + "            <div class=\"nav-year-month\" style=\"cursor: pointer; display: flex; align-items: center; gap: 8px;\">\n"
            + "              <span class=\"nav-year\" onclick=\"showYearSelector()\">2026</span>\n"
            + "              <span class=\"nav-month\" onclick=\"showMonthSelector()\">1月</span>\n"
            + "            </div>\n"
            + "            <div class=\"year-selector\">\n"
            + "              <!-- 年份列表将在这里动态生成 -->\n"
            + "            </div>\n"
            + "          </div>\n"
            + "          <button class=\"nav-button nav-next\" onclick=\"calendarNavigate('next')\">▶</button>\n"
            + "        </div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "    \n"
            + "    <div class=\"calendar-grid\">\n"
            + "      <div class=\"calendar-day-header\">日</div>\n"
            + "      <div class=\"calendar-day-header\">一</div>\n"
            + "      <div class=\"calendar-day-header\">二</div>\n"
            + "      <div class=\"calendar-day-header\">三</div>\n"
            + "      <div class=\"calendar-day-header\">四</div>\n"
            + "      <div class=\"calendar-day-header\">五</div>\n"
            + "      <div class=\"calendar-day-header\">六</div>\n"
            + "      ${calendar_days}\n"
            + "    </div>\n"
            + "    \n"
            + "    <div class=\"calendar-stats\">\n"
            + "      📊 <span class=\"stats-text\">2026年共有 31 张壁纸</span>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "  \n"
            + "  <div class=\"preview-section\">\n"
            + "    <div class=\"preview-header\">\n"
            + "      <h3>壁纸预览</h3>\n"
            + "      <div class=\"preview-date\" id=\"preview-date\">${preview_date}</div>\n"
            + "    </div>\n"
            + "    <div class=\"preview-container\">\n"
            + "      <div class=\"preview-placeholder\" id=\"preview-placeholder\" style=\"display: none;\">\n"
            + "        <div class=\"preview-icon\">📅</div>\n"
            + "        <p>点击日历中的日期查看壁纸</p>\n"
            + "      </div>\n"
            + "      <div class=\"preview-content\" id=\"preview-content\" style=\"display: flex;\">\n"
            + "        <div class=\"preview-image-container\">\n"
            + "          <div class=\"preview-small-bg\" style=\"position: absolute; bottom: 0; left: 0; width: 100%; height: 100%; background-image: url('${preview_img_url}&pid=hp&w=400'); background-position: center; background-repeat: no-repeat; background-size: cover; filter: blur(2px); -webkit-filter: blur(2px); opacity: 0.8; z-index: 1;\"></div>\n"
            + "          <div class=\"preview-large-bg\" style=\"position: absolute; bottom: 0; left: 0; width: 100%; height: 100%; background-image: url('${preview_img_url}&pid=hp&w=1200'); background-position: center; background-repeat: no-repeat; background-size: cover; z-index: 2;\"></div>\n"
            + "        </div>\n"
            + "        <div class=\"preview-info\">\n"
            + "          <h4 id=\"preview-title\">${preview_title}</h4>\n"
            + "          <p id=\"preview-desc\">${preview_desc}</p>\n"
            + "          <div class=\"preview-actions\">\n"
            + "            <a id=\"preview-download\" href=\"${preview_download_url}\" target=\"_blank\" class=\"preview-btn\">下载4k</a>\n"
            + "            <a id=\"preview-detail\" href=\"${preview_detail_url}\" target=\"_blank\" class=\"preview-btn\">查看详情</a>\n"
            + "          </div>\n"
            + "        </div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</div>\n";
        
        // 单个日期模板 - 添加预览功能
        private static final String CALENDAR_DAY = ""
            + "<div class=\"calendar-day ${has_wallpaper}\" onclick=\"showPreview('${date_url}', '${preview_url}', '${title}', '${desc}', '${download_url}')\" data-count=\"${wallpaper_count}\">\n"
            + "  ${day_number}\n"
            + "  ${wallpaper_indicator}\n"
            + "</div>\n";
        
        // 转义JavaScript字符串中的特殊字符
        private static String escapeJavaScript(String str) {
            if (str == null) return "";
            return str.replace("'", "\\'")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "\\r");
        }
        
        // 旧的月度历史链接（保留兼容性）
        private static final String MONTH_HISTORY_HREF = "<a class=\"w3-tag w3-button w3-hover-green w3-light-grey w3-margin-bottom\" href=\"${month_href_url}\">${month_href_title}</a>";
        
        public static String getMonthHistory(String url,String title) {
            String result = MONTH_HISTORY_HREF.replace(VAR_MONTH_HISTORY_HREF_URL, url);
            return result.replace(VAR_MONTH_HISTORY_HREF_TITLE, title);
        }
        
        // 新的日历生成方法
        public static String getFluentCalendar(Map<String, Object> calendarData) {
            String result = FLUENT_CALENDAR;
            
            // 生成日历数据
            String calendarDays = generateCalendarDays(calendarData);
            
            // 替换日历数据
            result = result.replace("${calendar_days}", calendarDays);
            result = result.replace("2026", String.valueOf(calendarData.get("currentYear")));
            result = result.replace("1月", calendarData.get("currentMonth") + "月");
            result = result.replace("2026年共有 31 张壁纸", generateStatsText(calendarData));
            
            // 替换预览区域的默认内容
            String defaultImgUrl = (String) calendarData.get("defaultPreviewImgUrl");
            String defaultTitle = (String) calendarData.get("defaultPreviewTitle");
            String defaultDesc = (String) calendarData.get("defaultPreviewDesc");
            String defaultDownloadUrl = (String) calendarData.get("defaultPreviewDownloadUrl");
            String defaultDetailUrl = (String) calendarData.get("defaultPreviewDetailUrl");
            String defaultDate = (String) calendarData.get("defaultPreviewDate");
            
            result = result.replace("${preview_img_url}", defaultImgUrl != null ? defaultImgUrl : "https://www.bing.com/th?id=OHR.VeniceView_EN-US3244163136_UHD.jpg");
            result = result.replace("${preview_title}", defaultTitle != null ? defaultTitle : "Aerial view of Venice, Italy (© Clement Leonard/Getty Images)");
            result = result.replace("${preview_desc}", defaultDesc != null ? defaultDesc : "Aerial view of Venice, Italy (© Clement Leonard/Getty Images)");
            result = result.replace("${preview_download_url}", defaultDownloadUrl != null ? defaultDownloadUrl : "https://www.bing.com/th?id=OHR.VeniceView_EN-US3244163136_UHD.jpg&rf=LaDigue_UHD.jpg&pid=hp&w=3840&h=2160&rs=1&c=4");
            result = result.replace("${preview_detail_url}", defaultDetailUrl != null ? defaultDetailUrl : "day/202601/03.html");
            result = result.replace("${preview_date}", defaultDate != null ? defaultDate : "2026年1月3日");
            
            return result;
        }
        
        private static String generateCalendarDays(Map<String, Object> calendarData) {
            StringBuilder days = new StringBuilder();
            int currentMonth = (Integer) calendarData.get("currentMonth");
            int currentYear = (Integer) calendarData.get("currentYear");
            
            // 计算月份第一天是星期几
            Calendar calendar = Calendar.getInstance();
            calendar.set(currentYear, currentMonth - 1, 1);
            int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 调整为0-6
            
            // 添加空白日期
            for (int i = 0; i < firstDayOfWeek; i++) {
                days.append("<div class=\"calendar-day empty\"></div>\n");
            }
            
            // 添加月份日期
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Map<String, Integer> wallpaperCounts = (Map<String, Integer>) calendarData.get("wallpaperCounts");
            Map<String, Map<String, String>> wallpaperData = (Map<String, Map<String, String>>) calendarData.get("wallpaperData");
            
            for (int day = 1; day <= daysInMonth; day++) {
                String dateKey = String.format("%04d-%02d-%02d", currentYear, currentMonth, day);
                int wallpaperCount = wallpaperCounts.getOrDefault(dateKey, 0);
                
                boolean hasWallpaper = wallpaperCount > 0;
                String wallpaperClass = hasWallpaper ? "has-wallpaper" : "";
                
                // 获取壁纸数据
                String dateUrl = "";
                String previewUrl = "";
                String title = "";
                String desc = "";
                String downloadUrl = "";
                
                if (hasWallpaper && wallpaperData.containsKey(dateKey)) {
                    Map<String, String> data = wallpaperData.get(dateKey);
                    dateUrl = String.format("day/%04d%02d/%02d.html", currentYear, currentMonth, day);
                    previewUrl = data.get("previewUrl");
                    title = escapeJavaScript(data.get("title"));
                    desc = escapeJavaScript(data.get("desc"));
                    downloadUrl = data.get("downloadUrl");
                }
                
                String wallpaperIndicator = hasWallpaper ? 
                    (wallpaperCount > 1 ? "<div class=\"wallpaper-count\" data-count=\"" + wallpaperCount + "\"></div>" : "<div class=\"wallpaper-dot\"></div>") : "";
                
                String dayHtml = CALENDAR_DAY
                    .replace("${has_wallpaper}", wallpaperClass)
                    .replace("${date_url}", dateUrl)
                    .replace("${preview_url}", previewUrl)
                    .replace("${title}", title)
                    .replace("${desc}", desc)
                    .replace("${download_url}", downloadUrl)
                    .replace("${wallpaper_count}", String.valueOf(wallpaperCount))
                    .replace("${day_number}", String.valueOf(day))
                    .replace("${wallpaper_indicator}", wallpaperIndicator);
                
                days.append(dayHtml);
            }
            
            return days.toString();
        }
        
        private static String generateStatsText(Map<String, Object> calendarData) {
            int currentYear = (Integer) calendarData.get("currentYear");
            Map<Integer, Integer> yearStats = (Map<Integer, Integer>) calendarData.get("yearStats");
            int yearCount = yearStats.getOrDefault(currentYear, 0);
            int totalCount = yearStats.values().stream().mapToInt(Integer::intValue).sum();
            
            return String.format("%d年共有 %d 张壁纸", currentYear, yearCount);
        }
    }

    /**
     * 头部图片
     */
    public static class ImgDetail{
        public static final String HEAD_TITLE = "${TITLE}";
        public static final String IMG_URL = "${IMG_URL}";
        public static final String IMG_DATE = "${IMG_DATE}";
        public static final String IMG_DESC = "${IMG_DESC}";
    }
}
