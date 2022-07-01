package backend.ssr.ddd.ssrblog.writer.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.common.TimeEntity.BaseTimeEntity;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.dto.WriterRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@IdClass(WriterId.class) // 복합키 식별자 클래스
public class Writer extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Post postIdx;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Account accountIdx;

    private Long realWriter;

    private String delYn = "N";

    @Builder
    public Writer(Post postIdx, Account accountIdx, Long realWriter, String delYn) {
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.realWriter = realWriter;
        this.delYn = delYn;
    }

    public Writer add(Post postIdx, WriterRequest account) {
        this.postIdx = postIdx;
        this.accountIdx = Account.builder().accountIdx(account.getAccountIdx()).build();

        return this;
    }
}
