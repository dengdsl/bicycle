package com.bicycle.validate.dict;

import com.bicycle.entry.dict.SystemDictEntry;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SystemDictUpdateValidate  implements Serializable {

    private Long id;

    private String dictName;

    private String dictType;

    private Integer dictStatus;

    private String dictRemark;

    public SystemDictEntry toEntry(SystemDictEntry dictEntry){
        dictEntry.setDictName(this.getDictName());
        dictEntry.setDictType(this.getDictType());
        dictEntry.setDictStatus(this.getDictStatus());
        dictEntry.setDictRemark(this.getDictRemark());
        dictEntry.setUpdateTime(new Date());
        return dictEntry;
    }
}
