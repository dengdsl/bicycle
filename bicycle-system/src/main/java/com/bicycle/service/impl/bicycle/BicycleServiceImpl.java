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
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.util.CellRangeAddressList;


import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        queryWrapper.eq("is_del", 0);
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
        if ((searchValidate.getProduceTimeStart() != null && !searchValidate.getProduceTimeStart().isEmpty()) && !(searchValidate.getProduceTimeEnd() != null && !searchValidate.getProduceTimeEnd().isEmpty())) {
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
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        return AjaxResult.success(supplierEntry);
    }

    /**
     * 新增自行车信息
     */
    @Override
    public AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate, HttpServletRequest request) {
        // 查询当前车架号是否已经存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("frame_no", createValidate.getFrameNo());
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry != null) {
            return AjaxResult.failed("该车架号已存在");
        }
        // 查询当前商家名称是否已经存在
        // 生成编号
        String randomId = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());
        randomId = checkId(randomId, "id");
        BicycleEntry createEntry = new BicycleEntry();
        createEntry.setId(randomId);
        createEntry.setModel(createValidate.getModel());
        createEntry.setFrameNo(createValidate.getFrameNo());
        createEntry.setConclusion(createValidate.getConclusion());
        createEntry.setProduceTime(createValidate.getProduceTime());
        createEntry.setImage(createValidate.getImage());
        createEntry.setRemark(createValidate.getRemark());
        createEntry.setCreateTime(new Date());
        createEntry.setUpdateTime(new Date());

        // 生成二维码
        Map<String, String> qrcodeInfo = generateQrcode(new ArrayList<>());
        createEntry.setQrcode(qrcodeInfo.get("qrcode"));
        createEntry.setQrImg(qrcodeInfo.get("qrUrl"));
        bicycleMapper.insert(createEntry);
        log.info("新增自行车数据成功：" + createEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑自行车信息
     */
    @Override
    public AjaxResult<Object> editBicycle(BicycleUpdateValidate updateValidate) {
        // 查询要修改的自行车信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", updateValidate.getId());
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry == null) {
            log.info("要修改的自行车数据不存在：id = " + updateValidate.getId());
            return AjaxResult.failed("数据不存在");
        }
        // 查找车架号是否存在
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("frame_no", updateValidate.getFrameNo());
        BicycleEntry bicycleEntry2 = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry2 != null && !bicycleEntry2.getId().equals(updateValidate.getId())) {
            return AjaxResult.failed("该车架号已存在");
        }
        // 修改自行车信息
        bicycleEntry.setModel(updateValidate.getModel());
        bicycleEntry.setFrameNo(updateValidate.getFrameNo());
        bicycleEntry.setConclusion(updateValidate.getConclusion());
        bicycleEntry.setProduceTime(updateValidate.getProduceTime());
        bicycleEntry.setImage(updateValidate.getImage());
        bicycleEntry.setRemark(updateValidate.getRemark());
        bicycleEntry.setUpdateTime(new Date());
        bicycleMapper.updateById(bicycleEntry);
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
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry == null) {
            log.info("要删除的数据不存在：id = " + id);
            return AjaxResult.failed("数据不存在");
        }
        // 修改供应商信息
        bicycleEntry.setIsDel(1);
        bicycleEntry.setDeleteTime(new Date());
        bicycleMapper.updateById(bicycleEntry);
        log.info("数据删除成功：" + bicycleEntry);
        return AjaxResult.success("删除成功");
    }

    /**
     * 下载批量导入模版
     */
    @Override
    public void downloadImportTemplate(HttpServletResponse response) throws IOException, DecoderException {
        // 创建一个新的 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        // 根据当前年月日和时间戳创建动态文件名
        Calendar instance = Calendar.getInstance();
        String year = String.valueOf(instance.get(Calendar.YEAR));
        String month = String.format("%02d", instance.get(Calendar.MONTH) + 1);
        String day = String.format("%02d", instance.get(Calendar.DAY_OF_MONTH));

        String fileName = String.format("%s%s%s%d", year, month, day, System.currentTimeMillis());
        Sheet sheet = workbook.createSheet(fileName);

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
        String[] headers = {"型号", "车架号", "X光图片", "生产日期", "结论", "备注"};

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
        queryWrapper.in("dict_type", "model", "conclusion");
        List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(queryWrapper);
        // 第二行：表头
        Row headerRow = sheet.createRow(1);  // 表头从第2行开始
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 30 * 256);
        }
        // 设置型号单元格为下拉选项
        String[] modelOptions = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("model")).map(SystemDictDataEntry::getName).toArray(String[]::new);;


        // 创建数据验证规则（设置下拉选项）
        DataValidationHelper validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(modelOptions);

        // 设置验证区域，假设只对第2行至最后一行（第一列）进行数据验证
        CellRangeAddressList addressList = new CellRangeAddressList(2, 1000, 0, 0); // 对A列进行下拉验证
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);  // 如果选择无效，显示错误框
        sheet.addValidationData(validation);

        // 设置结论单元格为下拉选项
        String[] conclusionOptions = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("conclusion")).map(SystemDictDataEntry::getName).toArray(String[]::new);

        // 创建数据验证规则
        validationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint conclusionConstraint = validationHelper.createExplicitListConstraint(conclusionOptions);
        // 设置验证区域，从第二行验证到最后一行
        addressList = new CellRangeAddressList(2, 1000, 4, 4);
        DataValidation conclusionValidation = validationHelper.createValidation(conclusionConstraint, addressList);
        conclusionValidation.setShowErrorBox(true);
        sheet.addValidationData(conclusionValidation);

        // 设置响应头信息，文件名，保证浏览器能够正常识别下载
        String headerContentType = String.format("attachment; filename=%s", URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerContentType);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 输出到响应流
        workbook.write(response.getOutputStream());
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
            queryWrapper.select("id", "qrcode");
            // 执行查询
            List<BicycleEntry> bicycleList = bicycleMapper.selectList(queryWrapper);
            List<String> ids = bicycleList.stream().map(BicycleEntry::getId).toList();
            List<String> qrcodeList = bicycleList.stream().map(BicycleEntry::getQrcode).toList();
            queryWrapper = new QueryWrapper<>();
            queryWrapper.select("frame_no");
            queryWrapper.eq("is_del", 0);
            List<String> frameNos = bicycleMapper.selectList(queryWrapper).stream().map(BicycleEntry::getFrameNo).toList();
            // 判断上传的车架号是否已经存在
            List<String> list = rowDetails.stream().map(ExcelRowDetailVo::getFrameNo).filter(frameNos::contains).toList();
            if (!list.isEmpty()) {
                // 将list用换行符进行拼接
                return AjaxResult.failed(HttpEnum.CONFIRM_FAILED.getCode(), "车架号已存在：" + String.join("\n\r", list));
            }
            // 获取数据库中存在型号和结论
            QueryWrapper<SystemDictDataEntry> dictQueryWrapper = new QueryWrapper<>();
            queryWrapper.in("dict_type", "model", "conclusion");
            List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(dictQueryWrapper);
            // 型号列表
            List<SystemDictDataEntry> modelList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("model")).toList();
            // 结论列表
            List<SystemDictDataEntry> conclusionList = systemDictDataEntries.stream().filter(dictDataEntry -> dictDataEntry.getDictType().equals("conclusion")).toList();

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
            List<String> conclusionNames =  conclusionList.stream().map(SystemDictDataEntry::getName).toList();
            List<String> conclusions = rowDetails.stream()
                    .map(ExcelRowDetailVo::getConclusion)
                    .filter(model -> !conclusionNames.contains(model))
                    .toList();
            if (!conclusions.isEmpty()) {
                return AjaxResult.failed(HttpEnum.FAILED.getCode(), "结论数据不合法：" + String.join("\n\r", conclusions));
            }

            // 提取出所有的型号和法值
            Map<String, String> modelMap = modelList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));

            Map<String, String> conclusionMap = conclusionList.stream().collect(Collectors.toMap(SystemDictDataEntry::getName, SystemDictDataEntry::getValue));

            // 验证通过，开始插入数据库
            for (ExcelRowDetailVo vo : rowDetails) {
                BicycleEntry bicycleEntry = new BicycleEntry();
                String id = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());

                bicycleEntry.setId(checkId(id, "id", ids));
                bicycleEntry.setModel(modelMap.get(vo.getModel()));
                bicycleEntry.setFrameNo(vo.getFrameNo());
                bicycleEntry.setConclusion(conclusionMap.get(vo.getConclusion()));
                bicycleEntry.setProduceTime(vo.getProduceTime());
                // 处理X光图片
                String imageUrls = saveImage(vo.getImages(), request);
                bicycleEntry.setImage(imageUrls);
                bicycleEntry.setRemark(vo.getRemark());
                bicycleEntry.setCreateTime(new Date());
                bicycleEntry.setUpdateTime(new Date());
                bicycleEntry.setIsDel(0);

                //生成二维码信息
                Map<String, String> qrcodeMap = generateQrcode(qrcodeList);
                bicycleEntry.setQrcode(qrcodeMap.get("qrcode"));
                String serverUrl = ConfigUtils.getServerUrl();
                if (serverUrl != null) {
                    serverUrl = request.getScheme() + "://" + request.getServerName();
                }
                bicycleEntry.setQrImg(serverUrl + qrcodeMap.get("qrUrl"));
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
    public AjaxResult<Object> exportBicycle() {
        return null;
    }

    /**
     * 根据二维码查询信息
     */
    @Override
    public AjaxResult<Object> queryByQrcode(String qrcode) {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.and(wrapper -> wrapper.eq("qrcode", qrcode).or().eq("frame_no", qrcode));
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        return AjaxResult.success(bicycleEntry);
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
    public Map<String, String> generateQrcode(List<String> qrcodeList) {
        String qrCode = randomService.randomQrcode();
        // 避免生成的二维码编号重复
        qrCode = checkId(qrCode, "qrcode", qrcodeList);

        String basePath = FilePathUtil.generateBasePath("qrcode");

        String dirPath = String.format("%s/%s", ConfigUtils.getFilePath(), basePath);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成图片二维码
        QrCodeUtils.generateQRCode(qrCode, String.format("%s/%s.png", dirPath, qrCode));
        Map<String, String> qrcodeMap = new HashMap<>();
        qrcodeMap.put("qrcode", qrCode);
        qrcodeMap.put("qrUrl", String.format("/static/%s/%s.png", basePath, qrCode));
        return qrcodeMap;
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
                String serverUrl = ConfigUtils.getServerUrl();
                if (serverUrl != null) {
                    serverUrl = request.getScheme() + "://" + request.getServerName();
                }
                imageUrls.add(serverUrl + "/static/" + basePath + "/" + fileName);
            } catch (IOException e) {
                log.error("保存文件失败：" + e.getMessage());
            }
        }
        return StringUtils.join(imageUrls, ";");
    }

    /**
     * 获取图片扩展名
     */
    private static String getPictureExtension(int pictureType) {
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
     * 将输入的颜色值转换为十六进制颜色
     */
    private byte[] convertColorToHex(String color) throws DecoderException {
        // 去掉前缀 "0x"
        if (color.startsWith("0x")) {
            color = color.substring(2);
        }
        // 去掉颜色字符串的 "#" 符号
        if (color.startsWith("#")) {
            color = color.substring(1);
        }
        return Hex.decodeHex(color);
    }

    /**
     * 获取导入模板说明描述
     */
    private static String[] getTemplateDescription(SystemConfigEntry templateDescription) {
        String[] instructions = {
                "*1.型号：请在下拉选项中进行选择，如果不选择或不按配置项填写将会导致导入失败！",
                "*2.车架号：不能出现重复的车架号和已经录入系统的车架号，否则将导致导入失败！",
                "*3.X光图片：图片不能嵌入单元格，只需要插入即可，否则会导致图片信息提取不到！",
                "*4.生产日期：需要为日期格式，例如：2025/1/20！",
                "*5.结论：请通过下拉选项选择，如果不进行选择默认为通过！",
                "*6.备注：备注信息不能超过1024个字符！",
                "7.数据编号和二维码编号以及二维码由系统自动生成！"
        };
        if (templateDescription != null && templateDescription.getValue() != null && !templateDescription.getValue().isEmpty()) {
            instructions = templateDescription.getValue().split(";");
        }
        return instructions;
    }


}
