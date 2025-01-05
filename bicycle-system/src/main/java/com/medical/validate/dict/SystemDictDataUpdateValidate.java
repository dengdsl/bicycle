package com.medical.validate.dict;

import com.medical.entry.dict.SystemDictDataEntry;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemDictDataUpdateValidate implements Serializable {

    @NotNull(message = "字典数据id必填")
    private Long id;

    @NotNull(message = "字典类型必填")
    private String dictType;

    @NotNull(message = "数据名称必填")
    private String name;

    @NotNull(message = "数据值必填")
    private String value;

    @NotNull(message = "数据状态必填")
    private Integer state;

    private String remark;

    @NotNull(message = "数据排序必填")
    private Integer sort;

    public SystemDictDataEntry toEntry(SystemDictDataEntry dictDataEntry){
        dictDataEntry.setDictType(this.getDictType());
        dictDataEntry.setName(this.getName());
        dictDataEntry.setValue(this.getValue());
        dictDataEntry.setState(this.getState());
        dictDataEntry.setRemark(this.getRemark());
        dictDataEntry.setSort(this.getSort());
        return dictDataEntry;
    }

}
