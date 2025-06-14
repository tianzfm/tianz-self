package com.fremont.tianz;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FrequencyConfig {
    /**
     * 请求总数
     */
    @NotNull(message = "totalRequests cannot be null")
    @Min(value = 1, message = "totalRequests must be greater than or equal to 1")
    private Integer totalRequests;

    /**
     * 持续时间
     */
    @NotNull(message = "duration cannot be null")
    @Min(value = 1, message = "duration must be greater than or equal to 1")
    private Integer duration;
}
