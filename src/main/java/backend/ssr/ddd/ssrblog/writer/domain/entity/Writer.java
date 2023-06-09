package backend.ssr.ddd.ssrblog.writer.domain.entity;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.common.TimeEntity.BaseTimeEntity;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.dto.WriterRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@IdClass(WriterId.class) // 복합키 식별자 클래스
public class Writer extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private Post postIdx;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_idx")
    private Account accountIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_writer")
    private Account realWriter;

    @Column(name = "del_yn")
    private String delYn = "N";

    public void delete(){
        this.delYn="Y";
    }

    @Builder
    public Writer(Post postIdx, Account accountIdx, Account realWriter, String delYn) {
        this.postIdx = postIdx;
        this.accountIdx = accountIdx;
        this.realWriter = realWriter;
        this.delYn = delYn;
    }

    public Writer addCoWriter(Long postIdx, Long accountIdx, Long realWriter) {
        this.postIdx = Post.builder().postIdx(postIdx).build();
        this.accountIdx = Account.builder().accountIdx(accountIdx).build();
        this.realWriter = Account.builder().accountIdx(realWriter).build();

        return this;
    }

    public Writer addRealWriter(Long postIdx, Long accountIdx, Long realWriter) {
        this.postIdx = Post.builder().postIdx(postIdx).build();
        this.accountIdx = Account.builder().accountIdx(accountIdx).build();
        this.realWriter = Account.builder().accountIdx(realWriter).build();

        return this;
    }
}
