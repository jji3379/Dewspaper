package backend.ssr.ddd.ssrblog.post.service;

import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 페이징 처리된 게시물 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<Post> getPostList() {

        return postRepository.findAll();
    }

    @Transactional
    public Post getPost(Long postIdx) {
        Post getPost = postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 게시물이 존재하지 않습니다."));

        getPost.updateBoardCount();

        return postRepository.save(getPost);
    }

    @Transactional
    public Post savePost(PostRequest postRequest) {

        return postRepository.save(postRequest.toEntity());
    }

    @Transactional
    public Post updatePost(Long postIdx, PostRequest postRequest) {
        Post getPost = postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 게시물이 존재하지 않습니다.")
                );

        getPost.update(postRequest);

        return postRepository.save(getPost);
    }

    @Transactional
    public void deletePost(Long postIdx) {
        Post getPost = postRepository.findById(postIdx)
                .orElseThrow(() -> new IllegalArgumentException("요청하신 게시물이 존재하지 않습니다."));

        getPost.delete();
    }

}
