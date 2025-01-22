package com.bicycle.validate.bicycle;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExportValidate implements Serializable {

    @NotNull(message = "ids参数缺失")
    List<String> ids;
}
