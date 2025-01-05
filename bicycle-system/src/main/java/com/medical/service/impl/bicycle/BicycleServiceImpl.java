package com.medical.service.impl.bicycle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.entry.bicycle.BicycleEntry;
import com.medical.enums.RandomPrefix;
import com.medical.mapper.bicycle.BicycleMapper;
import com.medical.service.random.RandomService;
import com.medical.service.bicycle.BicycleService;
import com.medical.utils.AjaxResult;
import com.medical.validate.bicycle.BicycleCreateValidate;
import com.medical.validate.bicycle.BicycleSearchValidate;
import com.medical.validate.bicycle.BicycleUpdateValidate;
import com.medical.validate.page.PageValidate;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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

        // 生成编号
        String randomId = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());
        randomId = checkId(randomId);
        BicycleEntry createEntry = new BicycleEntry();
        createEntry.setId(randomId);
        createEntry.setTitle(createValidate.getTitle());
        createEntry.setImage(createValidate.getImage());
        createEntry.setRemark(createValidate.getRemark());
        createEntry.setCreateTime(new Date());
        createEntry.setUpdateTime(new Date());
        bicycleMapper.insert(createEntry);
        log.info("新增自行车数据成功：" + createEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑自行车信息
     */
    @Override
    public AjaxResult<Object> editBicycle(BicycleUpdateValidate updateValidate) {
        // 查询要修改的供应商信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", updateValidate.getId());
        BicycleEntry bicycleEntry = bicycleMapper.selectOne(queryWrapper);
        if (bicycleEntry == null) {
            log.info("要修改的自行车数据不存在：id = " + updateValidate.getId());
            return AjaxResult.failed("数据不存在");
        }
        // 修改供应商信息
        bicycleEntry.setTitle(updateValidate.getTitle());
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
    public AjaxResult<Object> importBicycle(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        //1.2.获取Sheet
        Sheet sheet = workbook.getSheetAt(0);//参数：索引
        //1.3.获取Sheet中的每一行，和每一个单元格

        List<BicycleEntry> list = new ArrayList<>();
        System.out.println(sheet.getLastRowNum());
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);//根据索引获取每一个行
            BicycleEntry bicycleEntry = new BicycleEntry();
            bicycleEntry.setId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()));
            bicycleEntry.setTitle(getCellValue(row.getCell(1)).toString());
            bicycleEntry.setImage(getCellValue(row.getCell(2)).toString());
            bicycleEntry.setRemark(getCellValue(row.getCell(3)).toString());
            bicycleEntry.setCreateTime(new Date());
            bicycleEntry.setUpdateTime(new Date());
            bicycleEntry.setIsDel(0);
            list.add(bicycleEntry);
        }
        int insertCount = bicycleMapper.insertList(list);
        if (insertCount == 0) {
            return AjaxResult.failed("导入失败，请检查数据是否正确");
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
     * 判断当前id是否可用
     */
    public String checkId(String id) {
        // 根据id查询当前id是否被占用
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if (supplierEntry != null) {
            return checkId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()));
        }
        return id;
    }

    //格式装换
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
}
