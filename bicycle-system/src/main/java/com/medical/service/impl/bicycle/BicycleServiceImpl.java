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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 获取自行车列表数据
     * */
    @Override
    public AjaxResult<Object> getBicycleList(PageValidate pageValidate, BicycleSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo != null) {
            pageNo = 1;
        }
        if (pageSize!= null) {
            pageSize = 15;
        }
        // 创建查询对象
        Page<BicycleEntry> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderByDesc("create_time");
        Page<BicycleEntry> supplierEntryPage = bicycleMapper.selectPage(page, queryWrapper);
        long total = supplierEntryPage.getTotal();
        List<BicycleEntry> records = supplierEntryPage.getRecords();
        Map<String, Object> results = new HashMap<>();
        results.put("count", total);
        results.put("lists", records);
        return AjaxResult.success(results);
    }

    /**
     * 获取自行车详情
     * */
    @Override
    public AjaxResult<Object> getBicycleDetail(String id) {
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.eq("supplier_id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        return AjaxResult.success(supplierEntry);
    }

    /**
     * 新增自行车信息
     * */
    @Override
    public AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate) {
        // 判断当前供应商名称和供应商代码或者供应商许可证号是否已经被占用
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_del", 0);
//        queryWrapper.like("title", createValidate.getTitle());
//
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if (supplierEntry != null) {
//            log.info("当前供应商名称和供应商代码或者供应商许可证号已经被占用：supplier_name=" + createValidate.getSupplierName() + "；supplier_code=" + createValidate.getSupplierCode() + "；license_number=" + createValidate.getLicenseNumber());
            return AjaxResult.failed("当前供应商名称和供应商代码或者供应商许可证号已经被占用");
        }
        // 生成供应商编号
        String randomId = randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix());
        randomId = checkDrugId(randomId);
        // 新增供应商
        supplierEntry = new BicycleEntry();
        supplierEntry.setCreateTime(new Date());
        supplierEntry.setUpdateTime(new Date());
        bicycleMapper.insert(supplierEntry);
        log.info("新增供应商信息成功：" + supplierEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑自行车信息
     * */
   @Override
    public AjaxResult<Object> editBicycle(BicycleUpdateValidate updateValidate) {
        // 查询要修改的供应商信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_deleted", 0);
//        queryWrapper.eq("supplier_id", updateValidate.getSupplierId());
//        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
//        if (supplierEntry == null) {
//            log.info("要修改的供应商信息不存在：supplier_id = " + updateValidate.getSupplierId());
//            return AjaxResult.failed("要修改的供应商信息不存在");
//        }
        // 判断当前供应商名称和供应商代码或者供应商许可证号是否已经被占用
        QueryWrapper<BicycleEntry> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq("is_deleted", 0);
//        queryWrapper1.and(wrapper -> wrapper.eq("supplier_name", updateValidate.getSupplierName())
//                      .or().eq("supplier_code", updateValidate.getSupplierCode())
//                      .or().eq("license_number", updateValidate.getLicenseNumber()));
//        queryWrapper1.ne("supplier_id", updateValidate.getSupplierId());
//
        // 修改供应商信息
//        supplierEntry.setUpdateTime(new Date());
//        bicycleMapper.updateById(supplierEntry);
//        log.info("更新供应商信息成功：" + supplierEntry);
        return AjaxResult.success("更新成功");
    }

    /**
     * 删除自行车信息
     * */
    @Override
    public AjaxResult<Object> deleteBicycle(String id) {
        // 查询要删除的供应商信息是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.eq("supplier_id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if (supplierEntry == null) {
            log.info("要删除的供应商信息不存在：supplier_id = " + id);
            return AjaxResult.failed("要删除的供应商信息不存在");
        }
        // 修改供应商信息
        supplierEntry.setIsDel(1);
        supplierEntry.setDeleteTime(new Date());
        bicycleMapper.updateById(supplierEntry);
        log.info("删除供应商信息成功：" + supplierEntry);
        return AjaxResult.success("删除成功");
    }

    public AjaxResult<Object> statusSupplier(String id) {
        // 查询要切换状态的供应商是否存在
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.eq("supplier_id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if (supplierEntry == null) {
            log.info("要切换状态的供应商信息不存在：supplier_id = " + id);
            return AjaxResult.failed("要切换状态的供应商信息不存在");
        }
        // 修改供应商信息
        supplierEntry.setUpdateTime(new Date());
        bicycleMapper.updateById(supplierEntry);
        log.info("切换供应商状态成功：" + supplierEntry);
        return AjaxResult.success("操作成功");
    }

    /**
     * 批量导入自行车信息
     * */
    @Override
    public AjaxResult<Object> importBicycle() {
        return null;
    }

    /**
     * 批量导出自行车信息
     * */
    @Override
    public AjaxResult<Object> exportBicycle() {
        return null;
    }

    /**
     * 判断当前id是否可用
     * */
    public String checkDrugId(String id){
        // 根据id查询当前id是否被占用
        QueryWrapper<BicycleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        BicycleEntry supplierEntry = bicycleMapper.selectOne(queryWrapper);
        if(supplierEntry != null) {
            return checkDrugId(randomService.randomId(RandomPrefix.BICYCLE_PREFIX.getDrugPrefix()));
        }
        return id;
    }
}
