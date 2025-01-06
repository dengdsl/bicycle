package com.bicycle.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.user.SystemUserEntry;
import com.bicycle.validate.user.SystemUserSearchValidate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户管理员dao接口
 * */
public interface SystemUserMapper extends BaseMapper<SystemUserEntry> {

    /**
     * 获取用户列表，需要分页
     * */
    List<SystemUserEntry> userList(@Param("pageNo") Integer pageNo,
                                   @Param("pageSize") Integer pageSize,
                                   @Param("userSearchValidate") SystemUserSearchValidate userSearchValidate);

    /**
     * 获取满足条件的用户数量
     * */
    Long userCount(@Param("userSearchValidate") SystemUserSearchValidate userSearchValidate);
    /**
     * 插入用户角色
     * */
    int insertRoleList(@Param("userId") Long userId,
                       @Param("roleIds") List<Long> roleIds);

    /**
     * 插入用户部门
     * */
    int insertDeptList(@Param("userId") Long userId,
                       @Param("deptIds") List<Long> deptIds);

    /**
     * 删除用户角色
     * */
    @Delete("delete from sys_user_role where user_id = #{userId}")
    void deleteRoleList(@Param("userId") Long userId);

    /**
     * 删除用户部门
     */
    @Delete("delete from sys_dept_user where user_id = #{userId} ")
    void deleteDeptList(@Param("userId") Long userId);

    /**
     * 获取用户角色id列表
     * */
    @Select("select distinct role_id from sys_user_role where user_id = #{userId}")
    List<Long> getRoleIdList(@Param("userId") Long userId);

    /**
     * 获取用户部门id列表
     * */
    @Select("select distinct dept_id from sys_dept_user where user_id = #{userId}")
    List<Long> getDeptIdList(@Param("userId") Long userId);
}
