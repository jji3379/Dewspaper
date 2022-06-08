package backend.ssr.ddd.ssrblog.writer.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WriterId implements Serializable { // writer 복합키 매핑
    private Long postIdx;
    private Long accountIdx;
}
