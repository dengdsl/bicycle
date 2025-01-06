package com.bicycle.mapper.bicycle;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.bicycle.BicycleEntry;

import java.util.List;

/**
 * 自行车数据库操作
 * */
public interface BicycleMapper extends BaseMapper<BicycleEntry> {

    int insertList(List<BicycleEntry> list);
}

