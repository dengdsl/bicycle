package com.bicycle.utils;


import com.bicycle.annotation.Excel;
import com.bicycle.enums.HttpEnum;
import com.bicycle.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ExcelImportByPictureUtil<T> {
    
    /**
     * 实体对象
     * */
    private Class<T> clazz;
    
    public ExcelImportByPictureUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 读取Excel图片并解析数据，仅解析文件后缀为xlsx文件
     * @param file 上传的Excel图片
     * @param i excel表中表头所在行的下标
     * @return 解析得到的数据
     * */
    public List<T> readExcelImageAndData(MultipartFile file, int i) throws IOException, InstantiationException, IllegalAccessException {
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        // 获取工作簿
        Sheet sheet = wb.getSheetAt(0);
        // 1.获取图片信息
        Map<String, PictureData> pictureData = readExcelPicture(sheet);
        return readExcelInfoAndPutClass(sheet, i, pictureData);
    }

    /**
     * 解析Excel图片数据并将数据转化为实体类
     * @param sheet Excel表格
     * @param i excel表中表头所在行的下标
     * @param pictureData 图片数据
     * */
    private List<T> readExcelInfoAndPutClass(Sheet sheet, int i, Map<String, PictureData> pictureData) throws InstantiationException, IllegalAccessException {
        // 存储实体
        List<T> list = new ArrayList<>();
        // 获取每个抬头及对应的实体字段进行映射到相应的下标上
        Map<Integer, Object[]> headerMap = getFiledsMap(sheet, i);
        // 获取数据不为空的总行数
        int rowSize = sheet.getPhysicalNumberOfRows();
        // 便利每一行获取处理图片以外的其他字段信息
        for (int rowIndex = i + 1; rowIndex < rowSize; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isRowEmpty(row)) continue;
            // 简历实体类对象
            T entity = null;
            for (Map.Entry<Integer, Object[]> entry : headerMap.entrySet()) {
                Object val = row.getCell(entry.getKey());
                // 实体不存在就创建
                entity = entity == null ? clazz.newInstance() : entity;
                Field field = (Field) entry.getValue()[0];
                Excel attr = (Excel) entry.getValue()[1];
                // 取得类型，并根据对象设置值
                Class<?> fieldType = field.getType();
                // 判断自定义属性并设置相关信息
                putValByCustomAttribute(fieldType, field, attr, rowIndex, val, entry, pictureData, entity, row);
            }
            list.add(entity);
        }
        return list;
    }

    /**
     * 根据自定义属性设置相应的值
     * */
    private void putValByCustomAttribute(Class<?> fieldType, Field field, Excel attr, int rowIndex, Object val, Map.Entry<Integer, Object[]> entry, Map<String, PictureData> pictureDataMap, T entity, Row row) {
        // 判断字段类型来设置正确的值

        if (fieldType != null) {
            String  propertyName = field.getName();
            // 需要设置图片
            if (attr.getPicture()) {
                String rowAndCell = rowIndex + "_" + entry.getKey();
                PictureData pictureData = pictureDataMap.get(rowAndCell);
                if (Objects.nonNull(pictureData)) {
                    val = pictureData;
                }else {
                    val = null;
                }
            }
            ReflectionUtils.invokeSetter(entity, propertyName,val);
        }
    }

    /**
     * row是否为空
     * */
    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell!= null && cell.getCellType()!= CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取每个抬头及对应的实体字段进行映射到相应的下标上
     * @param sheet Excel表格
     * @param i excel表中表头所在行的下标
     * @return 映射表
     * */
    private Map<Integer, Object[]> getFiledsMap(Sheet sheet, int i) {
        Map<String, Integer> cellMap = new HashMap<>();
        // 获取抬头行
        Row headerRow = sheet.getRow(i);
        int columnSize = headerRow.getPhysicalNumberOfCells();
        // 便利每一行的值
        for (int cellNum = 0; cellNum < columnSize; cellNum++) {
            Cell cell = headerRow.getCell(cellNum);
            if (cell != null) {
                cellMap.put(cell.toString(), cellNum);
            }
        }
        // 只有数据时菜进行处理得到类的所有field
        List<Object[]> fields = this.getFields();
        Map<Integer, Object[]> fieldsMap = new HashMap<>();
        for (Object[] objects : fields) {
            Excel attr = (Excel) objects[1];
            Integer column = cellMap.get(attr.name());
            if (column!= null) {
                fieldsMap.put(column, objects);
            }
        }
        return fieldsMap;
    }

    /**
     * 获取字段注解信息
     * */
    private List<Object[]> getFields() {
        List<Object[]> fields = new ArrayList<>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            Excel attr = field.getAnnotation(Excel.class);
            field.setAccessible(true);
            fields.add(new Object[] { field, attr});

        }
        return fields;
    }

    /**
     * 读取Excel图片数据，仅读取后缀名为xlsx的文件
     * @param sheet Excel表格
     * @return 图片数据
     * */
    private Map<String, PictureData> readExcelPicture(Sheet sheet) {
        Map<String, PictureData> pictureData;
        try {
            pictureData = getPictureXSS((XSSFSheet)sheet);
        }catch (Exception ex) {
            log.error("解析图片异常：" + ex.getMessage());
            throw  new CustomException(HttpEnum.FAILED.getCode(), "解析图片异常：" + ex.getMessage());
        }
        return pictureData;
    }

    /**
     * 获取图片数据，读取的文件后缀为xlsx
     * @param sheet Excel表格
     * @return 图片数据
     * */
    private Map<String, PictureData> getPictureXSS(XSSFSheet sheet) {
        Map<String, PictureData> pictureDataMap = new HashMap<>();
        for (POIXMLDocumentPart dr : sheet.getRelations()){
            XSSFDrawing drawing = (XSSFDrawing) dr;
            List<XSSFShape> shapes = drawing.getShapes();
            for (XSSFShape shape : shapes) {
                XSSFPicture picture = (XSSFPicture)shape;
                // 解决图片空指针报错异常
                XSSFClientAnchor anchor = (XSSFClientAnchor)picture.getAnchor();
                String key = anchor.getRow1() + "_" + anchor.getCol2();
                pictureDataMap.put(key, picture.getPictureData());
            }
        }
        return pictureDataMap;
    }
}
