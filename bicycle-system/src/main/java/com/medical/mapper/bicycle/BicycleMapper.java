package com.medical.mapper.bicycle;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.entry.bicycle.BicycleEntry;

import java.util.List;

/**
 * 自行车数据库操作
 * */
public interface BicycleMapper extends BaseMapper<BicycleEntry> {

    int insertList(List<BicycleEntry> list);
}

