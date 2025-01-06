package com.bicycle.service.impl.dict;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bicycle.entry.dict.SystemDictDataEntry;
import com.bicycle.entry.dict.SystemDictEntry;
import com.bicycle.mapper.dict.SystemDictDataMapper;
import com.bicycle.mapper.dict.SystemDictMapper;
import com.bicycle.service.dict.SystemDictService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.dict.*;
import com.bicycle.validate.page.PageValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SystemDictServiceImpl implements SystemDictService {

    private SystemDictMapper dictMapper;

    private SystemDictDataMapper dictDataMapper;

    public SystemDictServiceImpl(SystemDictMapper dictMapper, SystemDictDataMapper dictDataMapper) {
        this.dictMapper = dictMapper;
        this.dictDataMapper = dictDataMapper;
    }

    @Override
    public AjaxResult<Object> dictList(PageValidate pageValidate, SystemDictSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        // 设置查询参数
        QueryWrapper<SystemDictEntry> queryWrapper = new QueryWrapper<>();
        if (!searchValidate.getDictType().isEmpty()) {
            queryWrapper.eq("dict_type", searchValidate.getDictType());
        }
        if (!searchValidate.getDictName().isEmpty()) {
            queryWrapper.eq("dict_name", searchValidate.getDictName());
        }
        if (searchValidate.getDictStatus() != null) {
            queryWrapper.eq("dict_status", searchValidate.getDictStatus());
        }
        // 查询分页数据
        Page<SystemDictEntry> systemDictEntryPage = dictMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<SystemDictEntry> records = systemDictEntryPage.getRecords();
        long total = systemDictEntryPage.getTotal();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("lists", records);
        resultMap.put("count", total);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult<Object> allDictList() {
        List<SystemDictEntry> allDictList = dictMapper.selectList(new QueryWrapper<>());
        return AjaxResult.success(allDictList);
    }

    @Override
    public AjaxResult<Object> addDict(SystemDictCreateValidate createValidate) {
        SystemDictEntry dictEntry = new SystemDictEntry();
        dictEntry = createValidate.toEntry(dictEntry);
        dictMapper.insert(dictEntry);
        log.info("=============新增字典类型成功=============");
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult<Object> getDictDetail(String id) {
        SystemDictEntry dictEntry = dictMapper.selectById(id);
        return AjaxResult.success(dictEntry);
    }

    @Override
    public AjaxResult<Object> editDict(SystemDictUpdateValidate updateValidate) {
        // 查询字典类型是否存在
        SystemDictEntry dictEntry = dictMapper.selectOne(new QueryWrapper<SystemDictEntry>().eq("dict_type", updateValidate.getDictType()).eq("dict_name", updateValidate.getDictName()));
        if (dictEntry == null) {
            return AjaxResult.failed("字典类型不存在");
        }
        dictEntry = updateValidate.toEntry(dictEntry);
        dictMapper.updateById(dictEntry);
        log.info("=============编辑字典类型成功=============");
        return AjaxResult.success("修改成功");
    }

    @Override
    public AjaxResult<Object> deleteDict(String id) {
        // 查询字典类型是否存在
        SystemDictEntry dictEntry = dictMapper.selectById(id);
        if (dictEntry == null) {
            return AjaxResult.failed("字典类型不存在");
        }
        dictMapper.deleteById(id);
        log.info("=============删除字典类型成功=============");
        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult<Object> dictDataList(PageValidate pageValidate, SystemDictDataSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        // 设置查询参数
        QueryWrapper<SystemDictDataEntry> queryWrapper = new QueryWrapper<>();
        if (searchValidate.getDictType() != null) {
            queryWrapper.eq("dict_type", searchValidate.getDictType());
        }
        if (searchValidate.getName() != null && !searchValidate.getName().isEmpty()) {
            queryWrapper.eq("name", searchValidate.getName());
        }
        if (searchValidate.getState() != null) {
            queryWrapper.eq("state", searchValidate.getState());
        }
        Page<SystemDictDataEntry> systemDictDataEntryPage = dictDataMapper.selectPage(new Page<SystemDictDataEntry>(pageNo, pageSize), queryWrapper);
        List<SystemDictDataEntry> records = systemDictDataEntryPage.getRecords();
        long total = systemDictDataEntryPage.getTotal();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("lists", records);
        resultMap.put("count", total);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult<Object> all(String dictType) {
        QueryWrapper<SystemDictDataEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        List<SystemDictDataEntry> systemDictDataEntries = dictDataMapper.selectList(queryWrapper);
        return AjaxResult.success(systemDictDataEntries);
    }

    @Override
    public AjaxResult<Object> addDictData(SystemDictDataCreateValidate createValidate) {
        QueryWrapper<SystemDictDataEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", createValidate.getDictType());
        queryWrapper.eq("name", createValidate.getName());
        queryWrapper.eq("value", createValidate.getValue());
        SystemDictDataEntry dictData = dictDataMapper.selectOne(queryWrapper);
        if (dictData != null) {
            return AjaxResult.failed("字典数据已存在");
        }
        SystemDictDataEntry dictDataEntry = new SystemDictDataEntry();
        dictDataEntry = createValidate.toEntry(dictDataEntry);
        dictDataMapper.insert(dictDataEntry);
        log.info("=============新增字典数据成功=============");
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult<Object> editDictData(SystemDictDataUpdateValidate updateValidate) {
        // 查询字典类型是否存在
        SystemDictDataEntry dictDataEntry = dictDataMapper.selectById(updateValidate.getId());
        if (dictDataEntry == null) {
            return AjaxResult.failed("字典数据不存在");
        }
        dictDataEntry = updateValidate.toEntry(dictDataEntry);
        dictDataMapper.updateById(dictDataEntry);
        log.info("=============编辑字典数据成功=============");
        return AjaxResult.success("修改成功");
    }

    @Override
    public AjaxResult<Object> deleteDictData(String id) {
        // 查询字典类型是否存在
        SystemDictDataEntry dictDataEntry = dictDataMapper.selectById(id);
        if (dictDataEntry == null) {
            return AjaxResult.failed("字典数据不存在");
        }
        dictDataMapper.deleteById(id);
        log.info("=============删除字典数据成功=============");
        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult<Object> getDictDataDetail(String id) {
        SystemDictDataEntry dictDataEntry = dictDataMapper.selectById(id);
        return AjaxResult.success(dictDataEntry);
    }

}
