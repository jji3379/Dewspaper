package backend.ssr.ddd.ssrblog.writer.dto;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@ApiModel(description = "함께 작성한 동료 응답 정보")
public class WriterResponse {
    @ApiModelProperty(value = "함께한 동료", example = "[8, 11, 14]", position = 1)
    private List<AccountResponse> coWriterInfo;

    @ApiModelProperty(value = "실제 작성자", example = "11", position = 2)
    private AccountResponse realWriterInfo;

    @Builder
    public WriterResponse(List<AccountResponse> coWriterInfo, Account realWriter) {
        this.coWriterInfo = coWriterInfo;
        this.realWriterInfo = realWriter.toResponse();
    }

    public WriterResponse addWriterResponse(List<AccountResponse> accountResponseList, Account realWriter) {
        this.coWriterInfo = accountResponseList;
        this.realWriterInfo = realWriter.toResponse();

        return this;
    }
}
