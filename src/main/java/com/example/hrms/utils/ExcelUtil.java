package com.example.hrms.utils;
//Excel 导出工具类，支持所有模块导出
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import java.nio.charset.StandardCharsets;

@Component
public class ExcelUtil {
    /**
     * 通用Excel导出（适配调用处参数：response + 数据列表 + 实体类 + 文件名）
     * @param response 响应对象
     * @param data 数据列表
     * @param clazz 实体类Class（如Dept.class）
     * @param fileName 文件名
     */
    public void exportExcel(HttpServletResponse response, List<?> data, Class<?> clazz, String fileName) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 防止文件名乱码
            String encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodeFileName + ".xlsx");

            // 导出Excel（EasyExcel）
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                    .sheet(fileName) // 工作表名称
                    .doWrite(data); // 写入数据
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Excel导出失败：" + e.getMessage());
        }
    }

    /**
     * 导出Excel
     * @param response 响应对象
     * @param title 表名
     * @param headers 表头数组
     * @param data 数据列表（Map的key对应表头，value对应值）
     * @param fileName 文件名
     */
    public void exportExcel(HttpServletResponse response, String title, String[] headers, List<Map<String, Object>> data, String fileName) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet(title);
        // 创建表头行
        Row headerRow = sheet.createRow(0);
        // 设置表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 写入表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            // 自动列宽
            sheet.autoSizeColumn(i);
        }

        // 写入数据
        for (int i = 0; i < data.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            Map<String, Object> rowData = data.get(i);
            for (int j = 0; j < headers.length; j++) {
                Cell cell = dataRow.createCell(j);
                Object value = rowData.get(headers[j]);
                if (value != null) {
                    cell.setCellValue(value.toString());
                } else {
                    cell.setCellValue("");
                }
                // 数据样式
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setAlignment(HorizontalAlignment.CENTER);
                dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                cell.setCellStyle(dataStyle);
            }
        }

        // 响应设置
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", java.nio.charset.StandardCharsets.UTF_8));
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");

        // 写入输出流
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}