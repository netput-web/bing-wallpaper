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
     * 侧边栏目录的归档菜单
     */
    public static class Sidebar{
        public static final String VAR_SIDABAR = "${sidabar}";
        public static final String VAR_SIDABAR_NOW_COLOR = "w3-green";
        public static final String VAR_SIDABAR_COLOR = "w3-hover-green";
        /**
         * <a href="#" onclick="w3_close()" class="w3-bar-item w3-button w3-hover-text-green w3-large">2022-08</a>
         */
        private static final String SIDABAR_MENU = "<a href=\"${sidabar_href_url}\" onclick=\"w3_close()\" class=\"w3-bar-item w3-button w3-hover-green w3-large\">${sidabar_href_title}</a>";

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
        private static final String IMG_CARD = ""
            + "<div style=\"width: 30%; position: relative; height: 0; padding-bottom: 18.75%; margin-bottom: 32px; box-sizing: border-box\">\n"
            +"  <img class=\"smallImg\" src=\"${img_card_url}&pid=hp&w=50\"  style=\"width:95%; position: absolute; top: 0; left: 2.5%;\" />"
            + "<a href=\"${img_detail_url}\"  target=\"_blank\"> <img class=\"bigImg w3-hover-shadow\" src=\"${img_card_download_url_preview}&pid=hp&w=384&h=216&rs=1&c=4\" style=\"width:95%; position: absolute; top: 0; left: 2.5%;\" onload=\"imgloading(this)\"></a>\n"
            + " <div style=\"position: absolute; bottom: -16px; left: 0; right: 0; text-align: center; padding: 8px 0; box-sizing: border-box;\">${img_card_date} <a href=\"${img_card_download_url}\" target=\"_blank\">Download 4k</a> "
            + "<button class=\"like-button img-btn\" onclick=\"updateLove('${img_card_region}','${img_card_date}')\">喜欢</button></div>\n"
            + "</div>";

        public static String getImgCard(Images bingImage) {
            String result = IMG_CARD.replace(VAR_IMG_CARD_URL, bingImage.getSimpleUrl());
            result = result.replace(VAR_IMG_CARD_DOWNLOAD_URL_PREVIEW, bingImage.getSimpleUrl());
            result = result.replace(VAR_IMG_CARD_DOWNLOAD_URL, bingImage.getUrl());
            result = result.replace(VAR_IMG_DETAIL_URL, bingImage.getDetailUrlPath());
            result = result.replace(VAR_IMG_DETAIL_URL, bingImage.getDetailUrlPath());
            result = result.replace(VAR_IMG_CRARD_REGION, Wallpaper.CURRENT_REGION.toLowerCase());
            return result.replace(VAR_IMG_CARD_DATE, bingImage.getDate());
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
            + "            <div class=\"nav-year\" onclick=\"toggleYearSelector()\" style=\"cursor: pointer;\">2026</div>\n"
            + "            <div class=\"nav-month\" onclick=\"showMonthSelector()\">1月</div>\n"
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
            + "      <div class=\"preview-date\" id=\"preview-date\">请选择日期</div>\n"
            + "    </div>\n"
            + "    <div class=\"preview-container\">\n"
            + "      <div class=\"preview-placeholder\" id=\"preview-placeholder\">\n"
            + "        <div class=\"preview-icon\">📅</div>\n"
            + "        <p>点击日历中的日期查看壁纸</p>\n"
            + "      </div>\n"
            + "      <div class=\"preview-content\" id=\"preview-content\" style=\"display: none;\">\n"
            + "        <div class=\"preview-image-container\">\n"
            + "          <img id=\"preview-image\" src=\"\" alt=\"壁纸预览\" />\n"
            + "        </div>\n"
            + "        <div class=\"preview-info\">\n"
            + "          <h4 id=\"preview-title\">壁纸标题</h4>\n"
            + "          <p id=\"preview-desc\">壁纸描述</p>\n"
            + "          <div class=\"preview-actions\">\n"
            + "            <a id=\"preview-download\" href=\"\" target=\"_blank\" class=\"preview-btn\">下载4k</a>\n"
            + "            <a id=\"preview-detail\" href=\"\" target=\"_blank\" class=\"preview-btn\">查看详情</a>\n"
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
