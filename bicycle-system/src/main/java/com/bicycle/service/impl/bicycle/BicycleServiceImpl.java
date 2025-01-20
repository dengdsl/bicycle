package com.bicycle.service.impl.bicycle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bicycle.entry.bicycle.BicycleEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.enums.RandomPrefix;
import com.bicycle.exception.CustomException;
import com.bicycle.mapper.bicycle.BicycleMapper;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

@Slf4j
@Service
public class BicycleServiceImpl implements BicycleService {

    private BicycleMapper bicycleMapper;
    private RandomService randomService;

    public BicycleServiceImpl(BicycleMapper bicycleMapper, RandomService randomService) {
        this.bicycleMapper = bicycleMapper;
        this.randomService = randomService;
    }

    /**
     * 创建数据列表查询对象
     * */
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
     * 批量导入自行车信息
     */
    @Override
    public AjaxResult<Object> importBicycle(MultipartFile file, HttpServletRequest request) {
        ExcelImportByPicture<ExcelRowDetailVo> importByPicture = new ExcelImportByPicture(ExcelRowDetailVo.class);

        try {
            List<ExcelRowDetailVo> rowDetails = importByPicture.readExcelImageAndData(file, 1);
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
            for (ExcelRowDetailVo vo : rowDetails) {
                BicycleEntry bicycleEntry = new BicycleEntry();
                String id = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());

                bicycleEntry.setId(checkId(id, "id", ids));
                int model = (int)Double.parseDouble(vo.getModel());
                bicycleEntry.setModel(model);
                bicycleEntry.setFrameNo(vo.getFrameNo());
                bicycleEntry.setConclusion(vo.getConclusion().equals("通过") ? 1 : 0);
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
        //queryWrapper.eq("is_del", 0);
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
     * */
    private String saveImage(List<XSSFPictureData> pictureDataList, HttpServletRequest request) {
            List<String> imageUrls = new ArrayList<>();
            for (PictureData picData : pictureDataList) {
                byte[] pictureBytes = picData.getData();
                String ext = getPictureExtension(picData.getPictureType());
                // 生成时间戳
                long timestamp = System.currentTimeMillis() / 1000;
                // 生成随机数
                int randomNum = new Random().nextInt(90000) + 10000;
                String fileName = String.format("%d%d%s", timestamp , randomNum , ext);
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
                    if (serverUrl!= null) {
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
     * */
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

}
