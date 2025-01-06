package com.bicycle.vo.user;

import com.bicycle.entry.user.SystemUserEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SystemUserVo implements Serializable {

    private Long id;

    private String account;

    private String nickname;

    private List<Long> deptIds;

    private List<Long> roleIds;

    private String avatar;

    private Integer userState;

    private String email;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public SystemUserVo toEntry(SystemUserEntry userEntry){
        this.id = userEntry.getId();
        this.account = userEntry.getAccount();
        this.nickname = userEntry.getUsername();
        this.avatar = userEntry.getAvatar();
        this.userState = userEntry.getUserState();
        this.email = userEntry.getEmail();
        this.remark = userEntry.getRemark();
        this.createTime = userEntry.getCreateTime();
        this.updateTime = userEntry.getUpdateTime();
        return this;
    }
}
