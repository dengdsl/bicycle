package com.bicycle.validate.dict;

import com.bicycle.entry.dict.SystemDictDataEntry;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemDictDataCreateValidate  implements Serializable {

    @NotNull(message = "dictType不能为空")
    private String dictType;

    @NotNull(message = "数据名称不能为空")
    private String name;

    @NotNull(message = "数据值不能为空")
    private String value;

    @NotNull(message = "数据状态不能为空")
    private Integer state;

    private String remark;

    @NotNull(message = "排序不能为空")
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
