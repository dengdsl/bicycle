package com.medical.validate.dict;

import com.medical.entry.dict.SystemDictEntry;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SystemDictCreateValidate  implements Serializable {

    @NotNull
    private String dictName;

    @NotNull
    private String dictType;

    @NotNull
    private Integer dictStatus;

    @NotNull
    private String dictRemark;

    public SystemDictEntry toEntry(SystemDictEntry dictEntry){
        dictEntry.setDictName(this.getDictName());
        dictEntry.setDictType(this.getDictType());
        dictEntry.setDictStatus(this.getDictStatus());
        dictEntry.setDictRemark(this.getDictRemark());
        dictEntry.setCreateTime(new Date());
        dictEntry.setUpdateTime(new Date());
        return dictEntry;
    }

}
