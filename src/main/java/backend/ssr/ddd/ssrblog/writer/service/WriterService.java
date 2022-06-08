package backend.ssr.ddd.ssrblog.writer.service;

import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriterService {

    private final WriterRepository writerRepository;
}
