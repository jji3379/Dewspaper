package backend.ssr.ddd.ssrblog.writer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@ApiModel(description = "함께 작성한 동료")
public class WriterDto {
    @ApiModelProperty(value = "함께한 동료", example = "[8, 11, 14]", position = 1)
    private List<Long> accountIdx;
    @ApiModelProperty(value = "실제 작성자", example = "33", position = 2)
    private Long realWriter;

    @Builder
    public WriterDto(List<Long> accountIdx, Long realWriter) {
        this.accountIdx = accountIdx;
        this.realWriter = realWriter;
    }
}
