package com.bicycle.service.impl.bicycle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bicycle.entry.bicycle.BicycleEntry;
import com.bicycle.entry.dict.SystemDictDataEntry;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.enums.RandomPrefix;
import com.bicycle.exception.CustomException;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.mapper.bicycle.BicycleMapper;
import com.bicycle.mapper.dict.SystemDictDataMapper;
import com.bicycle.service.random.RandomService;
import com.bicycle.service.bicycle.BicycleService;
import com.bicycle.utils.*;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.vo.bicycle.ExcelRowDetailVo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.util.CellRangeAddressList;


import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.poi.hssf.model.InternalSheet.createSheet;

@Slf4j
@Service
public class BicycleServiceImpl implements BicycleService {

    private final SystemConfigMapper systemConfigMapper;
    private BicycleMapper bicycleMapper;
    private RandomService randomService;
    private SystemDictDataMapper dictDataMapper;

    public BicycleServiceImpl(BicycleMapper bicycleMapper, RandomService randomService, SystemConfigMapper systemConfigMapper, SystemDictDataMapper dictDataMapper) {
        this.bicycleMapper = bicycleMapper;
        this.randomService = randomService;
        this.systemConfigMapper = systemConfigMapper;
        this.dictDataMapper = dictDataMapper;
    }

    /**
     * 创建数据列表查询对象
     */
    private static QueryWrapper<BicycleEntry> getBicycleEntryQueryWrapper(BicycleSearchValidate searchValidate) {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (searchValidate.getId() != null && !searchValidate.getId().isEmpty()) {
            queryWrapper.eq("id", searchValidate.getId());
        }
        if (searchValidate.getModel() != null) {
            queryWrapper.eq("model", searchValidate.getModel());
        }
        if (searchValidate.getFrameNo() != null && !searchValidate.getFrameNo().isEmpty()) {
            queryWrapper.eq("frame_no", searchValidate.getFrameNo());
        }
        if (searchValidate.getConclusion() != null && !searchValidate.getConclusion().isEmpty()) {
            queryWrapper.eq("conclusion", searchValidate.getConclusion());
        }
        if (searchValidate.getHollowHole() != null && !searchValidate.getHollowHole().isEmpty()) {
            queryWrapper.eq("hollow_hole", searchValidate.getHollowHole());
        }
        if (searchValidate.getInFold() != null && !searchValidate.getInFold().isEmpty()) {
            queryWrapper.eq("in_fold", searchValidate.getInFold());
        }
        if (searchValidate.getRaveling() != null && !searchValidate.getRaveling().isEmpty()) {
            queryWrapper.eq("raveling", searchValidate.getRaveling());
        }
        if (searchValidate.getProName() != null && !searchValidate.getProName().isEmpty()) {
            queryWrapper.like("pro_name", searchValidate.getProName());
        }
        return queryWrapper;
    }

    /**
     * 获取自行车列表数据
     */
    @Override
    public AjaxResult<Object> getBicycleList(PageValidate pageValidate, BicycleSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        // 创建查询对象
        Page<BicycleEntry> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BicycleEntry> queryWrapper = getBicycleEntryQueryWrapper(searchValidate);
        if ((searchValidate.getProduceTimeStart() != null && !searchValidate.getProduceTimeStart().isEmpty()) && (searchValidate.getProduceTimeEnd() != null && !searchValidate.getProduceTimeEnd().isEmpty())) {
            queryWrapper.between("produce_time", searchValidate.getProduceTimeStart(), searchValidate.getProduceTimeEnd());
        }
        Page<BicycleEntry> bicycleEntryPage = bicycleMapper.selectPage(page, queryWrapper);
        long total = bicycleEntryPage.getTotal();
        List<BicycleEntry> records = bicycleEntryPage.getRecords();
        Map<String, Object> results = new HashMap<>();
        results.put("count", total);
        results.put("lists", records);
        return AjaxResult.success(results);
    }

    /**
     * 获取自行车详情
     */
    @Override
    public AjaxResult<Object> getBicycleDetail(String id) {
        return AjaxResult.success(bicycleMapper.selectById(id));
    }

    /**
     * 新增自行车信息
     */
    @Override
    public AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate, HttpServletRequest request) {
        // 查询当前车架号是否已经存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("frame_no", createValidate.getFrameNo());
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry != null) {
            return AjaxResult.failed("产品编号已存在");
        }
        // 查询当前商家名称是否已经存在
        // 生成编号
        String randomId = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());
        randomId = checkId(randomId, "id");
        BicycleEntry createEntry = new BicycleEntry();
        createEntry.setId(randomId);
        createEntry.setProName(createValidate.getProName());
        createEntry.setModel(createValidate.getModel());
        createEntry.setFrameNo(createValidate.getFrameNo());
        createEntry.setConclusion(createValidate.getConclusion());
        createEntry.setHollowHole(createValidate.getHollowHole());
        createEntry.setInFold(createValidate.getInFold());
        createEntry.setRaveling(createValidate.getRaveling());
        createEntry.setProduceTime(createValidate.getProduceTime());
        createEntry.setImage(createValidate.getImage());
        createEntry.setRemark(createValidate.getRemark());
        createEntry.setCreateTime(new Date());
        createEntry.setUpdateTime(new Date());

        // 生成二维码
        String qrcodeUrl = generateQrcode(createValidate.getFrameNo());
        createEntry.setQrImg(qrcodeUrl);
        bicycleMapper.insert(createEntry);
        log.info("新增自行车数据成功：" + createEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑自行车信息
     */
    @Override
    public AjaxResult<Object> editBicycle(BicycleUpdateValidate updateValidate) {

        BicycleEntry bicycleEntry = bicycleMapper.selectById(updateValidate.getId());
        if (bicycleEntry == null) {
            log.info("要修改的自行车数据不存在：id = " + updateValidate.getId());
            return AjaxResult.failed("数据不存在");
        }

        // 查询要修改的自行车信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("frame_no", updateValidate.getFrameNo());
        BicycleEntry bicycleEntry2 = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry2 != null && !bicycleEntry2.getId().equals(updateValidate.getId())) {
            return AjaxResult.failed("产品编号已存在");
        }
        String oldQrCodeImg = bicycleEntry.getQrImg();
        // 如果产品编码修改了，重新生成二维码
        if (!bicycleEntry.getFrameNo().equals(updateValidate.getFrameNo())) {
            String qrcodeUrl = generateQrcode(updateValidate.getFrameNo());
            bicycleEntry.setQrImg(qrcodeUrl);
        }

        // 修改自行车信息
        bicycleEntry.setProName(updateValidate.getProName());
        bicycleEntry.setModel(updateValidate.getModel());
        bicycleEntry.setFrameNo(updateValidate.getFrameNo());
        bicycleEntry.setConclusion(updateValidate.getConclusion());
        bicycleEntry.setHollowHole(updateValidate.getHollowHole());
        bicycleEntry.setInFold(updateValidate.getInFold());
        bicycleEntry.setRaveling(updateValidate.getRaveling());
        bicycleEntry.setProduceTime(updateValidate.getProduceTime());
        bicycleEntry.setImage(updateValidate.getImage());
        bicycleEntry.setRemark(updateValidate.getRemark());
        bicycleEntry.setUpdateTime(new Date());
        bicycleMapper.updateById(bicycleEntry);
        // 删除原来的二维码图片
        deleteLocalFile(oldQrCodeImg);

        log.info("更新自行车数据成功：" + bicycleEntry);
        return AjaxResult.success("更新成功");
    }

    /**
     * 删除自行车信息
     */
    @Override
    public AjaxResult<Object> deleteBicycle(String id) {
        // 查询要删除的供应商信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        BicycleEntry bicycleEntry = bicycleMapper.selectById(id);
        if (bicycleEntry == null) {
            log.info("要删除的数据不存在：id = " + id);
            return AjaxResult.failed("数据不存在");
        }

        bicycleMapper.deleteById(bicycleEntry.getId());
        try {
            // 根据二维码路径和x光图片路径删除本地文件
            deleteLocalFile(bicycleEntry.getQrImg());

            String[] urlList = bicycleEntry.getImage().split((";"));
            for (String url : urlList) {
                // 如果url是以/static/ximg开头则不删除
                if(!url.startsWith("/static/ximg/")) {
                    deleteLocalFile(url);
                }
            }
        } catch (Exception e) {
            log.error("删除本地文件出错：", e.getMessage());
        }

        log.info("数据删除成功：" + bicycleEntry);
        return AjaxResult.success("删除成功");
    }

    /**
     * 下载批量导入模版
     */
    @Override
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        // 创建单元格样式
        CellStyle defaultStyle = workbook.createCellStyle();
        Font defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 18);  // 设置18号字
        defaultStyle.setFont(defaultFont);
        defaultStyle.setWrapText(true);

        // 创建红色字体样式
        CellStyle redStyle = workbook.createCellStyle();
        Font redFont = workbook.createFont();
        redFont.setFontHeightInPoints((short) 12);  // 设置12号字
        redFont.setColor(IndexedColors.RED.getIndex());  // 红色字体
        redStyle.setFont(redFont);
        redStyle.setWrapText(true);

        // 创建蓝色字体样式
        CellStyle blueStyle = workbook.createCellStyle();
        Font blueFont = workbook.createFont();
        blueFont.setFontHeightInPoints((short) 12);  // 设置12号字
        blueFont.setColor(IndexedColors.BLUE.getIndex());  // 蓝色字体
        blueStyle.setFont(blueFont);
        blueStyle.setWrapText(true);

        // 获取数据库模版配置信息
        SystemConfigEntry headerRowHeight = systemConfigMapper.getConfigByName("headerRowHeight");
        SystemConfigEntry templateDescription = systemConfigMapper.getConfigByName("templateDescription");

        // 创建表头背景颜色样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());  // 设置背景色为 #2682FC
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);  // 启用自动换行

        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());  // 白色字体
        headerFont.setBold(true);  // 加粗
        headerStyle.setFont(headerFont);
        // 表头信息
        String[] headers = {"产品名称", "产品型号", "产品编号", "X光图片", "生产日期", "结论", "空孔", "内折", "乱纱", "备注"};

        // 第一行：导入模板说明
        Row firstRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            firstRow.createCell(i);
        }
        firstRow.getCell(0).setCellStyle(defaultStyle);
        // 合并第一行的 6 列单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
        firstRow.createCell(0).setCellValue("导入模板说明：");

        String[] instructions = getTemplateDescription(templateDescription);

        // 创建一个富文本字符串来包含不同样式的文本
        XSSFRichTextString richText = new XSSFRichTextString("导入模板说明\n");
        richText.applyFont(0, richText.length(), defaultStyle.getFontIndex());
        // 添加每一条描述到富文本字符串，并应用不同的样式
        for (int i = 0; i < instructions.length; i++) {
            String instruction = instructions[i].trim();

            // 选择对应的样式
            CellStyle styleToApply;
            if (instruction.startsWith("*")) {
                instruction = instruction.substring(1);
                styleToApply = redStyle;  // 红色样式
            } else {
                styleToApply = blueStyle;  // 蓝色样式
            }

            // 为富文本字符串设置不同的样式
            int startIndex = richText.length();
            richText.append(instruction + "\n");
            richText.applyFont(startIndex, startIndex + instruction.length(), styleToApply.getFontIndex());
        }
        // 将富文本字符串设置到单元格中
        firstRow.getCell(0).setCellValue(richText);
        int lineHeight = 200;
        if (headerRowHeight != null && !headerRowHeight.getValue().isEmpty()) {
            lineHeight = Integer.parseInt(headerRowHeight.getValue());
        }
        firstRow.setHeightInPoints(lineHeight);

        // 获取数据库中存在的型号/和结论
        QueryWrapper<SystemDictDataEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dict_type", "model", "conclusion", "hollowHole", "inFold", "raveling", "proName");
        List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(queryWrapper);
        // 第二行：表头
        Row headerRow = sheet.createRow(1);  // 表头从第2行开始
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 30 * 256);
        }

        createSheetValidator(sheet, systemDictDataEntries, "proName", 2, 1000, 0, 0);

        createSheetValidator(sheet, systemDictDataEntries, "model", 2, 1000, 1, 1);

        createSheetValidator(sheet, systemDictDataEntries, "conclusion", 2, 1000, 5, 5);

        createSheetValidator(sheet, systemDictDataEntries, "hollowHole", 2, 1000, 6, 6);

        createSheetValidator(sheet, systemDictDataEntries, "inFold", 2, 1000, 7, 7);

        createSheetValidator(sheet, systemDictDataEntries, "raveling", 2, 1000, 8, 8);

        // 设置响应头信息，文件名，保证浏览器能够正常识别下载
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=import_template.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 输出到响应流
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        // 关闭工作簿
        workbook.close();
    }

    /**
     * 批量导入自行车信息
     */
    @Override
    public AjaxResult<Object> importBicycle(MultipartFile file, HttpServletRequest request) {
        ExcelImportByPicture<ExcelRowDetailVo> importByPicture = new ExcelImportByPicture(ExcelRowDetailVo.class);

        try {
            List<ExcelRowDetailVo> rowDetails = importByPicture.readExcelImageAndData(file, 1);
            if (rowDetails.isEmpty()) return AjaxResult.failed("文件数据为空，请检查上传之前文件是否保存！");
            log.info("文件转换成功：" + rowDetails.toString());
            List<BicycleEntry> bicycleEntries = new ArrayList<>();
            // 查询数据库中已经存在的ID和车架号
            QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id");
            // 执行查询
            List<BicycleEntry> bicycleList = bicycleMapper.selectList(queryWrapper);
            List<String> ids = bicycleList.stream().map(BicycleEntry::getId).toList();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.select("frame_no");

            List<String> frameNos = bicycleMapper.selectList(queryWrapper).stream().map(BicycleEntry::getFrameNo).toList();
            // 判断上传的车架号是否已经存在
            List<String> list = rowDetails.stream().map(ExcelRowDetailVo::getFrameNo).filter(frameNos::contains).toList();
            if (!list.isEmpty()) {
                // 将list用换行符进行拼接
                return AjaxResult.failed(HttpEnum.CONFIRM_FAILED.getCode(), "产品编号已存在：" + String.join("\n\r", list));
            }

            // 获取数据库中存在型号和结论
            QueryWrapper<SystemDictDataEntry> dictQueryWrapper = new QueryWrapper<>();
            queryWrapper.in("dict_type", "model", "conclusion", "hollowHole", "inFold", "raveling", "proName");
            List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(dictQueryWrapper);
            // 产品名称列表
            List<SystemDictDataEntry> proNameList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("proName")).toList();
            // 型号列表
            List<SystemDictDataEntry> modelList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("model")).toList();
            // 结论列表
            List<SystemDictDataEntry> conclusionList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("conclusion")).toList();
            // 空孔列表
            List<SystemDictDataEntry> hollowHoleList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("hollowHole")).toList();
            // 内折列表
            List<SystemDictDataEntry> inFoldList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("inFold")).toList();
            // 乱纱列表
            List<SystemDictDataEntry> ravelingList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("raveling")).toList();

            // 验证上传的数据是否合法
            // 判断产品名称是否存在
            List<String> proNames = proNameList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> pros = rowDetails.stream()
                    .map(ExcelRowDetailVo::getProName)
                    .filter(pro -> !proNames.contains(pro))
                    .toList();
            if (!pros.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "产品名称数据不合法：" + String.join("\n\r", pros));
            }

            // 判断型号是否存在
            List<String> modelNames = modelList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> models = rowDetails.stream()
                    .map(ExcelRowDetailVo::getModel)
                    .filter(model -> !modelNames.contains(model))
                    .toList();
            if (!models.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "型号数据不合法：" + String.join("\n\r", models));
            }

            // 判断结论值是否合法
            List<String> conclusionNames = conclusionList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> conclusions = rowDetails.stream()
                    .map(ExcelRowDetailVo::getConclusion)
                    .filter(conclusion -> !conclusionNames.contains(conclusion))
                    .toList();
            if (!conclusions.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "结论数据不合法：" + String.join("\n\r", conclusions));
            }

            // 判断空孔值是否合法
            List<String> hollowHoleNames = hollowHoleList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> hollowHoles = rowDetails.stream()
                    .map(ExcelRowDetailVo::getHollowHole)
                    .filter(hollowHole -> !hollowHoleNames.contains(hollowHole))
                    .toList();
            if (!hollowHoles.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "空孔数据不合法：" + String.join("\n\r", String.valueOf(hollowHoles)));
            }

            // 判断内折是否合法
            List<String> inFoldNames = inFoldList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> inFolds = rowDetails.stream()
                    .map(ExcelRowDetailVo::getInFold)
                    .filter(inFold -> !inFoldNames.contains(inFold))
                    .toList();
            if (!inFolds.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "内折数据不合法：" + String.join("\n\r", String.valueOf(inFolds)));
            }

            // 判断乱纱值是否合法
            List<String> ravelingNames = ravelingList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> ravelings = rowDetails.stream()
                    .map(ExcelRowDetailVo::getRaveling)
                    .filter(raveling -> !ravelingNames.contains(raveling))
                    .toList();
            if (!ravelings.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "乱纱数据不合法：" + String.join("\n\r", String.valueOf(ravelings)));
            }

            // 提取出所有产品名称的值
            Map<String, String> proNameMap = proNameList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));
            // 提取出所有的型号值
            Map<String, String> modelMap = modelList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));
            // 提取出所有的结论值
            Map<String, String> conclusionMap = conclusionList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));
            // 提取出所有的空孔值
            Map<String, String> hollowHoleMap = hollowHoleList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));
            // 提取出所有内折值
            Map<String, String> inFoldMap = inFoldList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));
            // 提取出所有乱纱值
            Map<String, String> ravelingMap = ravelingList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));

            // 验证通过，开始插入数据库
            for (ExcelRowDetailVo vo : rowDetails) {
                BicycleEntry bicycleEntry = new BicycleEntry();
                String id = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());

                bicycleEntry.setId(checkId(id, "id", ids));
                bicycleEntry.setProName(proNameMap.get(vo.getProName()));
                bicycleEntry.setModel(modelMap.get(vo.getModel()));
                bicycleEntry.setFrameNo(vo.getFrameNo());
                bicycleEntry.setConclusion(conclusionMap.get(vo.getConclusion()));
                bicycleEntry.setHollowHole(hollowHoleMap.get(vo.getHollowHole()));
                bicycleEntry.setInFold(inFoldMap.get(vo.getInFold()));
                bicycleEntry.setRaveling(ravelingMap.get(vo.getRaveling()));
                bicycleEntry.setProduceTime(vo.getProduceTime());
                // 处理X光图片
                String imageUrls = saveImage(vo.getImages(), request);
                bicycleEntry.setImage(imageUrls);
                bicycleEntry.setRemark(vo.getRemark());
                bicycleEntry.setCreateTime(new Date());
                bicycleEntry.setUpdateTime(new Date());

                //生成二维码信息
                String qrcodeUrl = generateQrcode(vo.getFrameNo());
                bicycleEntry.setQrImg(qrcodeUrl);
                bicycleEntries.add(bicycleEntry);
            }
            int insertCount = bicycleMapper.insertList(bicycleEntries);
            if (insertCount == 0) {
                return AjaxResult.failed("数据解析成功，保存失败！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(HttpEnum.FAILED.getCode(), "导入数据文件解析失败：" + e.getMessage());
        }
        return AjaxResult.success("导入成功");
    }

    /**
     * 批量导出自行车信息
     */
    @Override
    public void exportBicycle(HttpServletResponse response, List<String> ids) throws IOException {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (!ids.isEmpty()) {
            queryWrapper.in("id", ids);
        }
        List<BicycleEntry> bicycleEntries = bicycleMapper.selectList(queryWrapper);
        // 查询数据库中的结论字典数据和型号字典数据
        List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(new QueryWrapper<SystemDictDataEntry>().in("dict_type", "model", "conclusion", "hollowHole", "inFold", "raveling", "proName"));
        // 筛选出不同的字典类型数据
        List<SystemDictDataEntry> proNameList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("proName")).toList();
        List<SystemDictDataEntry> modelList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("model")).toList();
        List<SystemDictDataEntry> conclusionList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("conclusion")).toList();
        List<SystemDictDataEntry> hollowHoleList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("hollowHole")).toList();
        List<SystemDictDataEntry> inFoldList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("inFold")).toList();
        List<SystemDictDataEntry> ravelingList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("raveling")).toList();


        // 将数据转换为map，以value作为键、name作为值
        Map<String, String> proNameMap = proNameList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        Map<String, String> modelMap = modelList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        Map<String, String> conclusionMap = conclusionList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        Map<String, String> hollowHoleMap = hollowHoleList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        Map<String, String> inFoldMap = inFoldList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        Map<String, String> ravelingMap = ravelingList.stream().collect(Collectors.toMap(SystemDictDataEntry::getValue, SystemDictDataEntry::getName));
        // 创建excel表格将数据写入到表中最后以流的形式返回给前端
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> headerList = Arrays.asList("编号", "产品名称", "产品型号", "产品编号", "X光图片", "生产日期", "二维码图片", "结论", "空孔", "内折", "乱纱", "备注");

        // 创建表头背景颜色样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);  // 启用自动换行

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerList.get(i));
            cell.setCellStyle(headerStyle);
            if (headerList.get(i).equals("二维码编码")) {
                sheet.setColumnWidth(i, 42 * 256);
            } else {
                sheet.setColumnWidth(i, 20 * 256);
            }
        }
        // 创建数据
        XSSFClientAnchor anchor;
        BufferedImage bufferedImage;
        File file;
        String suffix;
        String path;
        ByteArrayOutputStream byteArrayOutputStream;
        XSSFDrawing drawingPatriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
        for (int i = 0; i < bicycleEntries.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.setHeight((short) (150 * 10));
            BicycleEntry bicycleEntry = bicycleEntries.get(i);
            // 循环headerList进行列的匹配
            for (int j = 0; j < headerList.size(); j++) {
                Cell cell = row.createCell(j);
                switch (headerList.get(j)) {
                    case "二维码图片":
                        try {
                            path = bicycleEntry.getQrImg().replaceAll("^((https?://[^/]+/static)|(/static))", ConfigUtils.getFilePath());
                            // 获取图片后缀
                            suffix = path.substring(path.lastIndexOf(".") + 1);
                            sheet.setColumnWidth(j, 4000);
                            file = new File(path);
                            byteArrayOutputStream = new ByteArrayOutputStream();
                            bufferedImage = ImageIO.read(file);
                            /* dx1:图片左边界距离单元格左边框像素值,
                             * dy1:图片上边界距离单元格上边框像素值,
                             * dx2:图片右边界距离单元格右边框像素值（负数）,
                             * dy2:图片下边界距离单元格下边框像素值（负数）,
                             * col1:列下标（0开始），
                             * row1:行下标（0开始），
                             * col2:列下标（1开始），
                             * row2:行下标（1开始）。*/
                            anchor = new XSSFClientAnchor(
                                    100000, 100000, -100000, -100000,  // dx1, dy1, dx2, dy2 控制图片的边距
                                    (short) j, i + 1,
                                    (short) (j + 1), i + 2
                            );
                            ImageIO.write(bufferedImage, suffix, byteArrayOutputStream);
                            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                            int pictureType = getPictureType(suffix);
                            drawingPatriarch.createPicture(anchor, workbook.addPicture(byteArrayOutputStream.toByteArray(), pictureType));
                        } catch (IOException e) {
                            log.error("写入二维码图片失败：" + e.getMessage());
                        }

                        break;
                    case "X光图片":
                        String[] filePathList = bicycleEntry.getImage().split(";");
                        sheet.setColumnWidth(j, 4000 * filePathList.length);
                        for (int k = 0; k < filePathList.length; k++) {
                            try {
                                path = filePathList[k].replaceAll("^((https?://[^/]+/static)|(/static))", ConfigUtils.getFilePath());
                                // 获取图片后缀
                                suffix = path.substring(path.lastIndexOf(".") + 1);
                                file = new File(path);
                                byteArrayOutputStream = new ByteArrayOutputStream();
                                bufferedImage = ImageIO.read(file);
                                anchor = new XSSFClientAnchor(
                                        (1000000 * k) + 100000, 100000, -((1000000 * filePathList.length + 100000) - 1000000 * (k + 1)), -100000,  // dx1, dy1, dx2, dy2 控制图片的边距
                                        (short) j, i + 1,
                                        (short) (j + 1), i + 2
                                );
                                ImageIO.write(bufferedImage, suffix, byteArrayOutputStream);
                                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                                drawingPatriarch.createPicture(anchor, workbook.addPicture(byteArrayOutputStream.toByteArray(), getPictureType(suffix)));
                            }catch (IOException e) {
                                log.error("写入X光片失败：" + e.getMessage());
                            }
                        }
                        break;
                    case "生产日期":
                        CellStyle cellStyle = workbook.createCellStyle();
                        CreationHelper creationHelper = workbook.getCreationHelper();
                        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        cellStyle.setAlignment(HorizontalAlignment.LEFT);
                        cell.setCellValue(bicycleEntry.getProduceTime());
                        cell.setCellStyle(cellStyle);
                        break;
                    case "产品名称":
                        cell.setCellValue(proNameMap.get(bicycleEntry.getProName()));
                        break;
                    case "备注":
                        cell.setCellValue(bicycleEntry.getRemark());
                        break;
                    case "产品编号":
                        cell.setCellValue(bicycleEntry.getFrameNo());
                        break;
                    case "编号":
                        cell.setCellValue(bicycleEntry.getId());
                        break;
                    case "产品型号":
                        cell.setCellValue(modelMap.get(bicycleEntry.getModel()));
                        break;
                    case "结论":
                        cell.setCellValue(conclusionMap.get(bicycleEntry.getConclusion()));
                        break;
                    case "空孔":
                        cell.setCellValue(hollowHoleMap.get(bicycleEntry.getHollowHole()));
                        break;
                    case "内折":
                        cell.setCellValue(inFoldMap.get(bicycleEntry.getInFold()));
                        break;
                    case "乱纱":
                        cell.setCellValue(ravelingMap.get(bicycleEntry.getRaveling()));
                        break;
                }
            }
        }

        createSheetValidator(sheet, systemDictDataEntries, "proName", 1, bicycleEntries.size() + 100, 1, 1);

        createSheetValidator(sheet, systemDictDataEntries, "model", 1, bicycleEntries.size() + 100, 2, 2);

        createSheetValidator(sheet, systemDictDataEntries, "conclusion", 1, bicycleEntries.size() + 100, 7, 7);

        createSheetValidator(sheet, systemDictDataEntries, "hollowHole", 1, bicycleEntries.size() + 100, 8, 8);

        createSheetValidator(sheet, systemDictDataEntries, "inFold", 1, bicycleEntries.size() + 100, 9, 9);

        createSheetValidator(sheet, systemDictDataEntries, "raveling", 1, bicycleEntries.size() + 100, 10, 10);

        // 设置响应头信息，文件名，保证浏览器能够正常识别下载
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bicycle_export.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 输出到响应流
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        // 关闭工作簿
        workbook.close();
    }

    /**
     * 批量下载二维码
     */
    @Override
    public void downloadQrcode(HttpServletResponse response, List<String> ids) {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (!ids.isEmpty()) {
            queryWrapper.in("id", ids);
        }
        List<BicycleEntry> bicycleEntries = bicycleMapper.selectList(queryWrapper);

        // 创建临时文件夹
        String temp_path = ConfigUtils.getFilePath() + "/qrcode_download";
        File temp_file = new File(temp_path);
        if (!temp_file.exists()) {
            temp_file.mkdirs();
        }
        // 开始下载
        // 下载二维码图片到临时文件夹
        for (BicycleEntry bicycleEntry : bicycleEntries) {
            try {
                String qrUrl = bicycleEntry.getQrImg().replaceAll("^((https?://[^/]+/static)|(/static))", ConfigUtils.getFilePath());

                File file_url = new File(qrUrl); // 本地文件创建方法
                File file = new File(temp_file, bicycleEntry.getFrameNo() + ".png");
                // 本地文件写入方式
                if (file_url.exists()) {
                    log.info("开始下载二维码图片：", qrUrl);
                    FileInputStream fis = new FileInputStream(file_url);
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } else {
                    log.error("二维码图片不存在：" + qrUrl);
                }
                //URL url = new URL(qrUrl); // 远程服务器路径创建方法
                //try (InputStream in = url.openStream();
                //     FileOutputStream out = new FileOutputStream(file)) {
                //    byte[] buffer = new byte[1024];
                //    int bytesRead;
                //    while ((bytesRead = in.read(buffer)) != -1) {
                //        out.write(buffer, 0, bytesRead);
                //    }
                //}
            } catch (IOException e) {
                log.error("下载图片到临时文件夹失败：", e.getMessage());
            }
        }
        // 压缩文件夹
        File zipFile = new File(ConfigUtils.getFilePath() + "/qrcode.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (File file : Objects.requireNonNull(temp_file.listFiles())) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
        } catch (IOException e) {
            log.error("压缩文件夹失败：", e.getMessage());
        }
        // 将ZIP文件写入响应
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=qrcode.zip");

        try (FileInputStream fis = new FileInputStream(zipFile);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("将压缩的文件夹写入响应中失败：", e.getMessage());
        }

        // 删除临时文件和文件夹
        for (File file : Objects.requireNonNull(temp_file.listFiles())) {
            file.delete();
        }
        temp_file.delete();
        zipFile.delete();
    }

    /**
     * 批量下载二维码编号
     * */
    @Override
    public void downloadFrameNo(HttpServletResponse response, List<String> ids) throws IOException {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "frame_no");
        if (!ids.isEmpty()) {
            queryWrapper.in("id", ids);
        }
        List<BicycleEntry> bicycleEntries = bicycleMapper.selectList(queryWrapper);
        // 创建excel表格将数据写入到表中最后以流的形式返回给前端
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<String> headerList = Arrays.asList("ID",  "二维码");

        // 创建表头背景颜色样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerList.get(i));
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 42 * 256);
        }
        for (int i = 0; i < bicycleEntries.size(); i++) {
            Row row = sheet.createRow(i + 1);
            BicycleEntry bicycleEntry = bicycleEntries.get(i);
            // 循环headerList进行列的匹配
            for (int j = 0; j < headerList.size(); j++) {
                Cell cell = row.createCell(j);
                switch (headerList.get(j)) {
                    case "二维码":
                        cell.setCellValue(bicycleEntry.getFrameNo());
                        break;
                    case "ID":
                        cell.setCellValue(bicycleEntry.getId());
                        break;
                }
            }
        }

        // 设置响应头信息，文件名，保证浏览器能够正常识别下载
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=frameNo_export.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 输出到响应流
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        // 关闭工作簿
        workbook.close();
    }

    /**
     * 重新生成二维码
     * */
    @Override
    public AjaxResult<Object> resetQrcode(String id) {
        BicycleEntry bicycleEntry = bicycleMapper.selectById(id);
        if (bicycleEntry == null) {
            return AjaxResult.failed("数据不存在");
        }
        // 删除原有的二维码
        deleteLocalFile(bicycleEntry.getQrImg());
        // 生成新的二维码
        String qrUrl = generateQrcode(bicycleEntry.getFrameNo());
        bicycleEntry.setQrImg(qrUrl);
        bicycleMapper.updateById(bicycleEntry);
        return AjaxResult.success(HttpEnum.SUCCESS.getCode(), HttpEnum.SUCCESS.getMessage(), qrUrl);
    }

    /**
     * 根据二维码查询信息
     */
    @Override
    public AjaxResult<Object> queryByQrcode(String qrcode) {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("frame_no", qrcode));
        List<BicycleEntry> bicycleEntries = bicycleMapper.selectList(queryWrapper);
        if (bicycleEntries.isEmpty()) {
            return AjaxResult.failed("未查询到相关数据");
        }
        return AjaxResult.success(bicycleEntries.get(0));
    }

    /**
     * 判断当前id是否可用
     */
    public String checkId(String id, String fieldName) {
        // 根据id查询当前id是否被占用
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(fieldName, id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if (supplierEntry != null) {
            return checkId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()), fieldName);
        }
        return id;
    }

    /**
     * 判断当前id是否可用
     */
    public String checkId(String id, String fieldName, List<String> ids) {
        if (ids.isEmpty()) {
            return checkId(id, fieldName);
        }
        // 判断id在ids中是否存在
        if (ids.contains(id)) {
            return checkId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()), fieldName, ids);
        }
        return id;
    }

    /**
     * 生成二维码
     */
    public String generateQrcode(String frameNo) {

        String basePath = FilePathUtil.generateBasePath("qrcode");

        String dirPath = String.format("%s/%s", ConfigUtils.getFilePath(), basePath);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成图片二维码
        QrCodeUtils.generateQRCode(frameNo, String.format("%s/%s.png", dirPath, frameNo));
        return String.format("/static/%s/%s.png", basePath, frameNo);
    }

    /**
     * 将解析出来的图片保存到本地
     */
    private String saveImage(List<XSSFPictureData> pictureDataList, HttpServletRequest request) {
        List<String> imageUrls = new ArrayList<>();
        for (PictureData picData : pictureDataList) {
            byte[] pictureBytes = picData.getData();
            String ext = getPictureExtension(picData.getPictureType());
            // 生成时间戳
            long timestamp = System.currentTimeMillis() / 1000;
            // 生成随机数
            int randomNum = new Random().nextInt(90000) + 10000;
            String fileName = String.format("%d%d%s", timestamp, randomNum, ext);
            String basePath = FilePathUtil.generateBasePath("images");
            // 创建图片路径
            String filePath = String.format("%s/%s/%s", ConfigUtils.getFilePath(), basePath, fileName);
            try {
                // 判断路径是否存在
                File file = new File(filePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                // 保存图片
                OutputStream fos = new FileOutputStream(file);
                fos.write(pictureBytes);
                fos.close();
                imageUrls.add("/static" + basePath + "/" + fileName);
            } catch (IOException e) {
                log.error("保存文件失败：" + e.getMessage());
            }
        }
        return StringUtils.join(imageUrls, ";");
    }

    /**
     * 获取图片扩展名
     */
    private String getPictureExtension(int pictureType) {
        switch (pictureType) {
            case Workbook.PICTURE_TYPE_EMF:
                return ".emf";  // 增强型图元文件 (Enhanced Metafile)
            case Workbook.PICTURE_TYPE_WMF:
                return ".wmf";  // Windows 图元文件 (Windows Metafile)
            case Workbook.PICTURE_TYPE_PICT:
                return ".pict"; // Apple Macintosh 图元文件
            case Workbook.PICTURE_TYPE_JPEG:
                return ".jpg";  // 常见的有损压缩图像 (JPEG)
            case Workbook.PICTURE_TYPE_PNG:
                return ".png";  // 无损压缩图像 (PNG)
            case Workbook.PICTURE_TYPE_DIB:
                return ".dib";  // 设备无关位图 (Device Independent Bitmap)
            default:
                return ".bin";  // 未知格式，使用 .bin 作为默认扩展名
        }
    }

    /**
     * 根据图片扩展名获取图片类型
     */
    private int getPictureType(String suffix) {
        switch (suffix) {
            case "emf":
                return Workbook.PICTURE_TYPE_EMF;  // 增强型图元文件 (Enhanced Metafile)
            case "wmf":
                return Workbook.PICTURE_TYPE_WMF;  // Windows 图元文件 (Windows Metafile)
            case "pict":
                return Workbook.PICTURE_TYPE_PICT; // Apple Macintosh 图元文件
            case "jpg":
                return Workbook.PICTURE_TYPE_JPEG;  // 常见的有损压缩图像 (JPEG)
            case "png":
                return Workbook.PICTURE_TYPE_PNG;  // 无损压缩图像 (PNG)
            case "dib":
                return Workbook.PICTURE_TYPE_DIB;  // 设备无关位图 (Device Independent Bitmap)
            default:
                return Workbook.PICTURE_TYPE_PNG;  // 未知格式，使用 .bin 作为默认扩展名
        }
    }

    /**
     * 获取导入模板说明描述
     */
    private String[] getTemplateDescription(SystemConfigEntry templateDescription) {
        String[] instructions = {
                "*1.产品名称、产品型号：请在下拉选项中进行选择，如果不选择或不按配置项填写将会导致导入失败！",
                "*2.车架号：不能出现重复的车架号和已经录入系统的车架号，否则将导致导入失败！",
                "*3.X光图片：图片不能嵌入单元格，只需要插入即可，否则会导致图片信息提取不到！",
                "*4.生产日期：需要为日期格式，例如：2025/1/20！",
                "*5.结论：请通过下拉选项选择，如果不进行选择默认为通过！",
                "*6.空孔：请通过下拉选项选择，如果不进行选择默认为合格！",
                "*7.内折：请通过下拉选项选择，如果不进行选择默认为合格！",
                "*8.乱纱：请通过下拉选项选择，如果不进行选择默认为合格！",
                "*9.备注：备注信息不能超过1024个字符！"
        };
        if (templateDescription != null && templateDescription.getValue() != null && !templateDescription.getValue().isEmpty()) {
            instructions = templateDescription.getValue().split(";");
        }
        return instructions;
    }

    /**
     * 创建工作簿验证规则
     */
    private void createSheetValidator(Sheet sheet, List<SystemDictDataEntry> dictDataList, String dictType, int firstRow, int lastRow, int firstCol, int lastCol) {
        String[] ravelingOptions = dictDataList.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals(dictType)).map(SystemDictDataEntry::getName).toArray(String[]::new);
        // 创建数据验证规则（设置下拉选项）
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(ravelingOptions);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol); // 对A列进行下拉验证
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    /**
     * 删除本地文件
     *
     * @param filePath 文件路径
     */
    private void deleteLocalFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        try {
            String localPath = filePath.replaceAll("^((https?://[^/]+/static)|(/static))", ConfigUtils.getFilePath());
            Path path = Paths.get(localPath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("成功删除文件：" + filePath);
            } else {
                log.warn("文件不存在：" + filePath);
            }
        } catch (Exception e) {
            log.error("删除文件失败：" + filePath, e);
        }
    }
}
