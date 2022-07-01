package backend.ssr.ddd.ssrblog.writer.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import backend.ssr.ddd.ssrblog.writer.dto.WriterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WriterService {

    private final WriterRepository writerRepository;

    public void saveWriter(Post postIdx, List<WriterRequest> coworker) {
        for (WriterRequest account : coworker) {
            Writer writer = new Writer();
            writer.add(postIdx, account);
            writerRepository.save(writer);
        }
    }
}
