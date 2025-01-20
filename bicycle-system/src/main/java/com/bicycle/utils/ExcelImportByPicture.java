package com.bicycle.utils;


import com.bicycle.annotation.Excel;
import com.bicycle.annotation.Excels;
import com.bicycle.enums.HttpEnum;
import com.bicycle.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@Slf4j
public class ExcelImportByPicture<T> {

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelImportByPicture(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * @description: 获取excel上传中的图片及数据信息
     * @param: [file, titleNum]
     * @return: java.util.List<T>
     * @date: 2023/11/9 15:51
     * @version: 1.0
     **/
    public List<T> readExcelImageAndData(MultipartFile file, int titleNum) throws InstantiationException, IllegalAccessException {
        // 读取上传excel
        Workbook wb = readExcel(file);
        //Workbook wb = WorkbookFactory.create(file.getInputStream());
        if (wb == null) {
            return null;
        }
        // 获取当前页，这里如果有多页时可以获取更多的页，通过循环获取每页的数据
        // 获取总页数：wb.getNumberOfSheets(),获取某一个：wb.getSheet(sheetName)
        Sheet sheet = wb.getSheetAt(0);
        //1：获取图片集合。行-列为key
        Map<String, List<PictureData>> pictureDataMap = readExcelPicture(sheet);
        //2:获取excel中的数据（这里的数据不含图片信息）
        // 将图片信息传入,再通过实体字段属性将每个数据映射到字段上，包括获取到的图片信息
        return readExcelInfoAndPutClass(sheet, titleNum, pictureDataMap);
    }


    /**
     * @description: 将图片信息传入, 再通过实体字段属性将每个数据映射到字段上，包括获取到的图片信息
     * @param: [sheet, titleNum, pictureDataMap]
     * @return: java.util.List<T>
     * @date: 2023/11/9 16:42
     * @version: 1.0
     **/
    private List<T> readExcelInfoAndPutClass(Sheet sheet, int titleNum, Map<String, List<PictureData>> pictureDataMap) throws InstantiationException, IllegalAccessException {
        //存储实体list
        List<T> list = new ArrayList<>();
        //获取每个抬头及对应的实体字段进行映射到相应的下标上
        Map<Integer, Object[]> fieldsMap = getFieldsMap(sheet, titleNum);
        // 获取不为空的总行数
        int rowSize = sheet.getPhysicalNumberOfRows();
        // 遍历每一行，获取除了图片信息外的字段信息
        for (int rowNum = titleNum + 1; rowNum < rowSize; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (isRowEmpty(row)) {
                continue;
            }
            //建立所映射的实体对象
            T entity = null;
            for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet()) {
                Object val = row.getCell(entry.getKey());
                // 如果不存在实例则新建.
                entity = (entity == null ? clazz.newInstance() : entity);
                // 从map中得到对应列的field.
                Field field = (Field) entry.getValue()[0];
                Excel attr = (Excel) entry.getValue()[1];
                // 取得类型,并根据对象类型设置值.
                Class<?> fieldType = field.getType();
                //判断自定义属性并设置相关信息
                putValByCustomAttribute(fieldType, field, attr, rowNum, val, entry, pictureDataMap, entity, row);
            }
            list.add(entity);
        }
        return list;
    }

    /**
     * @description: 根据自定义属性设置相应的值
     * @param: [fieldType, field, attr, rowNum, val, entry, pictureDataMap, entity]
     * @return: void
     * @date: 2023/11/9 16:20
     * @version: 1.0
     */
    private void putValByCustomAttribute(Class<?> fieldType, Field field, Excel attr,
                                         int rowNum, Object val, Map.Entry<Integer, Object[]> entry,
                                         Map<String, List<PictureData>> pictureDataMap, T entity, Row row) {
        //判断字段的类型来设置正确的值
        if (String.class == fieldType) {
            String s = Convert.toStr(val);
            if (StringUtils.endsWith(s, ".0")) {
                val = StringUtils.substringBefore(s, ".0");
            } else {
                String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                if (StringUtils.isNotEmpty(dateFormat)) {
                    val = parseDateToStr(dateFormat, val);
                } else {
                    val = Convert.toStr(val);
                    if (ObjectUtils.isEmpty(val)) {
                        val = null;
                    }
                }
            }
        } else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val))) {
            val = Convert.toInt(val);
        } else if ((Long.TYPE == fieldType || Long.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val))) {
            val = Convert.toLong(val);
        } else if (Double.TYPE == fieldType || Double.class == fieldType) {
            val = Convert.toDouble(val);
        } else if (Float.TYPE == fieldType || Float.class == fieldType) {
            val = Convert.toFloat(val);
        } else if (BigDecimal.class == fieldType) {
            val = Convert.toBigDecimal(val);
        } else if (Date.class == fieldType) {
            val = row.getCell(entry.getKey()).getDateCellValue();
        } else if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
            val = Convert.toBool(val, false);
        }
        if (null != fieldType) {
            //自定义属性没有的话可以删除某一个属性判断
            String propertyName = field.getName();
            if (attr.getPicture()) {
                String rowAndCell = rowNum + "-" + entry.getKey();
                List<PictureData> pictureDatas = pictureDataMap.get(rowAndCell);
                if (Objects.nonNull(pictureDatas)) {
                    val = pictureDatas;
                } else {
                    val = null;
                }
            }
            ReflectUtils.invokeSetter(entity, propertyName, val);
        }
    }


    /**
     * @description: 获取每个抬头及对应的实体字段进行映射到相应的下标上
     * @param: [sheet, titleNum]
     * @return: java.util.Map<java.lang.Integer, java.lang.Object [ ]>
     * @date: 2023/11/9 16:05
     * @version: 1.0
     **/
    private Map<Integer, Object[]> getFieldsMap(Sheet sheet, int titleNum) {
        //存储每列标题和每列的下标值
        Map<String, Integer> cellMap = new HashMap<>();
        //获取抬头行
        Row titleRow = sheet.getRow(titleNum);
        //获取标头行的总列数
        int columnSize = sheet.getRow(titleNum).getPhysicalNumberOfCells();
        // 遍历一行中每列值
        for (int cellNum = 0; cellNum < columnSize; cellNum++) {
            Cell cell = titleRow.getCell(cellNum);
            if (cell != null) {
                cellMap.put(cell.toString(), cellNum);
            }
        }
        // 有数据时才处理 得到类的所有field.
        List<Object[]> fields = this.getFields();
        Map<Integer, Object[]> fieldsMap = new HashMap<>();
        for (Object[] objects : fields) {
            Excel attr = (Excel) objects[1];
            Integer column = cellMap.get(attr.name());
            if (column != null) {
                fieldsMap.put(column, objects);
            }
        }
        return fieldsMap;
    }


    /**
     * @description: 获取图片集合
     * @param: [file, sheet]
     * @return: java.util.Map<java.lang.String, org.apache.poi.ss.usermodel.PictureData>
     * @date: 2023/11/7 17:30
     * @version: 1.0
     **/
    private Map<String, List<PictureData>> readExcelPicture(Sheet sheet) {
        // 声明当前页图片的集合
        Map<String, List<PictureData>> sheetImageMap;
        // 获取图片
        try {
            //2003版本的excel，用.xls结尾
            sheetImageMap = getPicturesHSS((HSSFSheet) sheet);
        } catch (Exception ex) {
            try {
                //2007版本的excel，用.xlsx结尾
                sheetImageMap = getPicturesXSS((XSSFSheet) sheet);
            } catch (Exception e) {
                log.error(ex.getMessage());
                throw new CustomException(HttpEnum.FAILED.getCode(), "解析图片异常！");
            }
        }
        return sheetImageMap;
    }

    /**
     * 获取图片和位置 (xls)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    private Map<String, List<PictureData>> getPicturesHSS(HSSFSheet sheet) {
        Map<String, List<PictureData>> map = new HashMap<String, List<PictureData>>();
        List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
        for (HSSFShape shape : list) {
            if (shape instanceof HSSFPicture) {
                HSSFPicture picture = (HSSFPicture) shape;
                HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
                PictureData pdata = picture.getPictureData();
                if (pdata != null && cAnchor != null) {
                    // 使用 "row-col" 作为图片的唯一标识
                    String key = cAnchor.getRow1() + "-" + cAnchor.getCol2();
                    map.computeIfAbsent(key, k -> new ArrayList<>()).add(pdata);
                }
            }
        }
        return map;
    }


    /**
     * 获取图片和位置 (xlsx)
     *
     * @param sheet
     * @return
     * @throws IOException
     */
    private Map<String, List<PictureData>> getPicturesXSS(XSSFSheet sheet) {
        Map<String, List<PictureData>> sheetIndexPicMap = new HashMap<String, List<PictureData>>();
        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    //解决图片空指针报错问题   2021-12-27
                    XSSFClientAnchor anchor = (XSSFClientAnchor) shape.getAnchor();
                    //下面这个处理时间较长
                    //XSSFClientAnchor anchor = pic.getPreferredSize();
                    //CTMarker marker = anchor.getFrom();
                    // 行号-列号，该取用方式列准确率不高
                    //String key = marker.getRow() + "-" + marker.getCol();
                    //行号-列号
                    String key = anchor.getRow1() + "-" + (anchor.getCol2());
                    XSSFPictureData pictureData = pic.getPictureData();
                    if (pictureData != null) {
                        sheetIndexPicMap.computeIfAbsent(key, k -> new ArrayList<>()).add(pictureData);
                    }
                }
            }
        }
        return sheetIndexPicMap;
    }


    /**
     * 读取excel
     *
     * @param file
     * @return
     */
    private Workbook readExcel(MultipartFile file) {
        Workbook wb = null;
        ZipSecureFile.setMinInflateRatio(0);
        if (file == null) {
            return null;
        }
        InputStream is;
        try {
            is = file.getInputStream();
            //2003版本的excel，用.xls结尾
            //得到工作簿
            wb = new HSSFWorkbook(is);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            try {
                //2007版本的excel，用.xlsx结尾
                is = file.getInputStream();
                //得到工作簿
                wb = new XSSFWorkbook(is);
            } catch (IOException e) {
                log.error(ex.getMessage());
            }
        }
        return wb;
    }

    /**
     * 获取字段注解信息
     */
    private List<Object[]> getFields() {
        List<Object[]> fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            // 单注解
            if (field.isAnnotationPresent(Excel.class)) {
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type)) {
                    field.setAccessible(true);
                    fields.add(new Object[]{field, attr});
                }
            }
            // 多注解
            if (field.isAnnotationPresent(Excels.class)) {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = (Excel[]) attrs.value();
                for (Excel attr : excels) {
                    if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type)) {
                        field.setAccessible(true);
                        fields.add(new Object[]{field, attr});
                    }
                }
            }
        }
        return fields;
    }

    /**
     * 判断是否是空行
     *
     * @param row 判断的行
     * @return
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 格式化不同类型的日期对象
     *
     * @param dateFormat 日期格式
     * @param val        被格式化的日期对象
     * @return 格式化后的日期字符
     */
    private String parseDateToStr(String dateFormat, Object val) {
        if (val == null) {
            return "";
        }
        String str;
        if (val instanceof Date) {
            str = new SimpleDateFormat(dateFormat).format((Date) val);
        } else if (val instanceof LocalDateTime) {
            str = new SimpleDateFormat(dateFormat).format(((LocalDateTime) val).atZone(ZoneId.systemDefault()).toInstant());
        } else if (val instanceof LocalDate) {
            LocalDate temporalAccessor = (LocalDate) val;
            LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
            ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
            str = new SimpleDateFormat(dateFormat).format(Date.from(zdt.toInstant()));
        } else {
            str = val.toString();
        }
        return str;
    }
}

