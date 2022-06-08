package backend.ssr.ddd.ssrblog.writer.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@NoArgsConstructor
@Getter
@IdClass(WriterId.class) // 복합키 식별자 클래스
public class Writer {

    @Id
    private Long postIdx;

    @Id
    private Long accountIdx;

    private Long realWriter;

    @Builder
    public Writer(Long postIdx, Long accountIdx, Long realWriter) {
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.realWriter = realWriter;
    }
}
