package backend.ssr.ddd.ssrblog.post.service;

import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 페이징 처리된 게시물 리스트 조회
     * - 공개 설정 (privated = "N")
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional(readOnly = true)
    public List<Post> getPostList(Pageable pageable) {

        return postRepository.findAllByPrivatedAndDeleted(pageable, "N", "N");
    }

    /**
     * pk 가 postIdx 인 게시물 조회
     * - 삭제 여부 (deleted = "N")
     * - 조회시 조회수 증가
     */
    @Transactional
    public Post getPost(Long postIdx) {
        Post getPost = postRepository.findPostByPostIdxAndDeleted(postIdx,"N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.updateBoardCount();

        return postRepository.save(getPost);
    }

    /**
     * 게시물 등록
     */
    @Transactional
    public Post savePost(PostRequest postRequest) {

        return postRepository.save(postRequest.toEntity());
    }

    /**
     * 게시물 수정
     * pk 가 postIdx 인 게시물 수정
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional
    public Post updatePost(Long postIdx, PostRequest postRequest) {
        Post getPost = postRepository.findPostByPostIdxAndDeleted(postIdx, "N")
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
        Post getPost = postRepository.findPostByPostIdxAndDeleted(postIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.delete();
    }

}
