package com.bicycle.service.impl.bicycle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bicycle.entry.bicycle.BicycleEntry;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.enums.RandomPrefix;
import com.bicycle.exception.CustomException;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.mapper.bicycle.BicycleMapper;
import com.bicycle.service.random.RandomService;
import com.bicycle.service.bicycle.BicycleService;
import com.bicycle.utils.*;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.vo.bicycle.ExcelRowDetailVo;
import com.google.zxing.WriterException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class BicycleServiceImpl implements BicycleService {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private BicycleMapper bicycleMapper;
    private RandomService randomService;

    public BicycleServiceImpl(BicycleMapper bicycleMapper, RandomService randomService) {
        this.bicycleMapper = bicycleMapper;
        this.randomService = randomService;
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
    public AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate) {
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
        Map<String, String> qrcodeInfo = generateQrcode();
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
     * 批量导入自行车信息
     */
    @Override
    public AjaxResult<Object> importBicycle(MultipartFile file) {
        ExcelImportByPictureUtil<ExcelRowDetailVo> importByPicture = new ExcelImportByPictureUtil(ExcelRowDetailVo.class);

        try {
            List<ExcelRowDetailVo> rowDetails = importByPicture.readExcelImageAndData(file, 1);
            log.info("文件转换成功：" + rowDetails.toString());
            List<BicycleEntry> bicycleEntries = new ArrayList<>();
            for (ExcelRowDetailVo vo : rowDetails) {
                BicycleEntry bicycleEntry = new BicycleEntry();
                bicycleEntry.setId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()));
                int model = (int)Double.parseDouble(vo.getModel());
                bicycleEntry.setModel(model);
                bicycleEntry.setFrameNo(vo.getFrameNo());
                bicycleEntry.setConclusion(vo.getConclusion().equals("通过") ? 1 : 0);
                bicycleEntry.setProduceTime(vo.getProduceTime());
                // 处理X光图片
                //bicycleEntry.setImage(vo.getImage());

                bicycleEntry.setRemark(vo.getRemark());
                bicycleEntry.setCreateTime(new Date());
                bicycleEntry.setUpdateTime(new Date());
                bicycleEntry.setIsDel(0);

                //生成二维码信息
                Map<String, String> qrcodeMap = generateQrcode();
                bicycleEntry.setQrcode(qrcodeMap.get("qrcode"));
                bicycleEntry.setQrImg(qrcodeMap.get("qrUrl"));
                bicycleEntries.add(bicycleEntry);
            }
            int insertCount = bicycleMapper.insertList(bicycleEntries);
            if (insertCount == 0) {
                return AjaxResult.failed("导入失败，请检查数据是否正确");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(HttpEnum.FAILED.getCode(), "导入数据文件解析失败！");
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
     * 生成二维码
     */
    public Map<String, String> generateQrcode() {
        String qrCode = randomService.randomQrcode();
        // 避免生成的二维码编号重复
        qrCode = checkId(qrCode, "qrcode");

        // 根据日期创建文件夹并创建文件夹
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 补零处理
        String formattedMonth = (month < 10 ? "0" : "") + month;
        String formattedDay = (day < 10 ? "0" : "") + day;

        String dirPath = String.format("%s/qrcode/%d/%s/%s", ConfigUtils.getFilePath(), year, formattedMonth, formattedDay);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成图片二维码
        QrCodeUtils.generateQRCode(qrCode, String.format("%s/%s.png", dirPath, qrCode));
        Map<String, String> qrcodeMap = new HashMap<String, String>();
        qrcodeMap.put("qrcode", qrCode);
        qrcodeMap.put("qrUrl", String.format("%s/qrcode/%d/%s/%s/%s.png", ConfigUtils.getServerUrl(), year, formattedMonth, formattedDay, qrCode));
        return qrcodeMap;
    }

    /**
     * excel表格列格式转换
     */
    public static Object getCellValue(Cell cell) {
        //1.获取到单元格的属性类型
        CellType cellType = cell.getCellType();
        //2.根据单元格数据类型获取数据
        Object value = null;
        switch (cellType) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    //日期格式
                    value = cell.getDateCellValue();
                } else {
                    //数字
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //公式
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }

    private String extractImageFileName(String formula) {
        if (formula.startsWith("_xlfn.DISPIMG")) {
            int startIndex = formula.indexOf("\"") + 1;
            int endIndex = formula.indexOf("\"", startIndex);
            if (startIndex > 0 && endIndex > startIndex) {
                return formula.substring(startIndex, endIndex); // 提取图片文件名
            }
        }
        return null;
    }


    private String saveImageWithOriginalName(String imageUrl) throws IOException {
        // 保存路径
        String savePath = "F:\\workspace\\bicycle\\bicycle-system\\src\\main\\resources\\static\\image.png";

        // 下载图片并保存
        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream fos = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return savePath; // 返回保存路径
    }


}
