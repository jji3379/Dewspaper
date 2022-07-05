package backend.ssr.ddd.ssrblog.post.service;

import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final WriterRepository writerRepository;

    /**
     * 페이징 처리된 게시물 리스트 조회
     * - 공개 설정 (privated = "N")
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostList(Pageable pageable, String term) {
        Page<Post> postList = null;
        LocalDateTime now = LocalDateTime.now();

        if (term.equals("weekly")) {
            LocalDateTime week = now.minusWeeks(1);
            postList = postRepository.findAllByPrivatedAndDelYnAndCreateDateBetween(pageable, "N", "N", week, now);
        } else if (term.equals("monthly")) {
            LocalDateTime month = now.minusMonths(1);
            postList = postRepository.findAllByPrivatedAndDelYnAndCreateDateBetween(pageable, "N", "N", month, now);
        } else {
            postList = postRepository.findAllByPrivatedAndDelYn(pageable, "N", "N");
        }

        return postList;
    }

    /**
     * pk 가 postIdx 인 게시물 조회
     * - 삭제 여부 (deleted = "N")
     * - 조회시 조회수 증가
     */
    @Transactional
    public Post getPost(Long postIdx) {
        Post getPost = postRepository.findPostByPostIdxAndDelYn(postIdx,"N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.updateBoardCount();

        return postRepository.save(getPost);
    }

    /**
     * 게시물 등록
     */
    @Transactional
    public Post savePost(PostRequest postRequest) {
        Post save = postRepository.save(postRequest.toEntity());

        for (Long account : postRequest.getCowriter().getAccountIdx()) {
            Writer writer = new Writer();
            writer.add(save.getPostIdx(), account, postRequest.getCowriter().getRealWriter());
            writerRepository.save(writer);
        }

        return save;
    }

    /**
     * 게시물 수정
     * pk 가 postIdx 인 게시물 수정
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional
    public Post updatePost(Long postIdx, PostRequest postRequest) {
        Post getPost = postRepository.findPostByPostIdxAndDelYn(postIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.update(postRequest);

        return postRepository.save(getPost);
    }

    /**
     * 게시물 삭제
     * pk 가 postIdx 인 게시물 삭제
     * - 삭제 여부 (deleted = "N")
     * - 삭제시 deleted = "Y" 으로 update
     */
    @Transactional
    public void deletePost(Long postIdx) {
        Post getPost = postRepository.findPostByPostIdxAndDelYn(postIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.delete();

        postRepository.save(getPost);
    }
}
