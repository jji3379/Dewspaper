package backend.ssr.ddd.ssrblog.writer.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriterService {

    private final WriterRepository writerRepository;

    public void deleteWriter(Post postIdx, Account accountIdx) {
        Writer writer = writerRepository.findByPostIdxAndAccountIdxAndDelYn(postIdx, accountIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WRITER));

        writer.delete();

        writerRepository.save(writer);
    }

}

