package backend.ssr.ddd.ssrblog.writer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriterRequest {
    private Long postIdx;
    private Long accountIdx;
    private Long realWriter;

    @Builder
    public WriterRequest(Long postIdx, Long accountIdx, Long realWriter) {
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.realWriter = realWriter;
    }
}
