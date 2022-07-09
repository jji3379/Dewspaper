package backend.ssr.ddd.ssrblog.post.service;

import backend.ssr.ddd.ssrblog.account.domain.entity.Account;
import backend.ssr.ddd.ssrblog.account.domain.repository.AccountRepository;
import backend.ssr.ddd.ssrblog.account.dto.AccountResponse;
import backend.ssr.ddd.ssrblog.comment.domain.entity.Comment;
import backend.ssr.ddd.ssrblog.comment.domain.repository.CommentRepository;
import backend.ssr.ddd.ssrblog.common.Exception.CustomException;
import backend.ssr.ddd.ssrblog.common.Exception.ErrorCode;
import backend.ssr.ddd.ssrblog.post.domain.entity.Post;
import backend.ssr.ddd.ssrblog.post.domain.entity.QPost;
import backend.ssr.ddd.ssrblog.post.domain.repository.PostRepository;
import backend.ssr.ddd.ssrblog.post.dto.PostRequest;
import backend.ssr.ddd.ssrblog.post.dto.PostResponse;
import backend.ssr.ddd.ssrblog.writer.domain.entity.QWriter;
import backend.ssr.ddd.ssrblog.writer.domain.entity.Writer;
import backend.ssr.ddd.ssrblog.writer.domain.repository.WriterRepository;
import backend.ssr.ddd.ssrblog.writer.dto.WriterResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final WriterRepository writerRepository;

    private final AccountRepository accountRepository;

    private final CommentRepository commentRepository;

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 페이징 처리된 게시물 리스트 조회
     * - 공개 설정 (privated = "N")
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostList(Pageable pageable, String term) {
        Page<Post> postList = null;
        List<PostResponse> postResponseList = new ArrayList();
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

        for (Post post : postList) {
            postResponseList.add(post.toResponse(getCoWriterInfo(post)));
        }

        Page<PostResponse> pagePostResponseList = new PageImpl<>(postResponseList, pageable, postList.getTotalElements());

        return pagePostResponseList;
    }

    /**
     * pk 가 postIdx 인 게시물 조회
     * - 삭제 여부 (deleted = "N")
     * - 조회시 조회수 증가
     */
    @Transactional
    public PostResponse getPost(Long postIdx) {
        Post getPost = postRepository.findPostByPostIdxAndDelYn(postIdx,"N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.updateBoardCount();
        postRepository.save(getPost);

        return getPost.toResponse(getCoWriterInfo(getPost));
    }

    /**
     * 게시물 등록
     */
    @Transactional
    public PostResponse savePost(PostRequest postRequest) {
        Post save = postRepository.save(postRequest.toEntity());
        List<AccountResponse> accountResponseList = new ArrayList<>();
        if (postRequest.getCoWriter().getAccountIdx().isEmpty()) {
            Writer writer = new Writer();
            writer.addRealWriter(save.getPostIdx(), postRequest.getCoWriter().getRealWriter(), postRequest.getCoWriter().getRealWriter());
            writerRepository.save(writer);

            save.addCowriter(writer);
        } else {
            for (Long account : postRequest.getCoWriter().getAccountIdx()) {
                Account coWriterInfo = accountRepository.findById(account).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
                accountResponseList.add(coWriterInfo.toResponse());

                Writer writer = new Writer();
                writer.addCoWriter(save.getPostIdx(), account, postRequest.getCoWriter().getRealWriter());
                writerRepository.save(writer);
                save.addCowriter(writer);
            }
        }

        WriterResponse writerResponse = new WriterResponse();
        Account account = accountRepository.findById(postRequest.getCoWriter().getRealWriter())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
        writerResponse.addWriterResponse(accountResponseList, account);

        return save.toResponse(writerResponse);
    }

    /**
     * 게시물 수정
     * pk 가 postIdx 인 게시물 수정
     * - 삭제 여부 (deleted = "N")
     */
    @Transactional
    public PostResponse updatePost(Long postIdx, PostRequest postRequest) {
        Post getPost = postRepository.findPostByPostIdxAndDelYn(postIdx, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        getPost.update(postRequest);

        List<Writer> beforeWriterInfo = writerRepository.findByPostIdxAndDelYn(getPost, "N");
        for (Writer writer : beforeWriterInfo) {
            // 새로운 작성자에 기존 작성자가 포함되어 있지 않다면
            if (!postRequest.getCoWriter().getAccountIdx().contains(writer.getAccountIdx().getAccountIdx())) {
                writer.delete();
            }
        }

        for (Long writer : postRequest.getCoWriter().getAccountIdx()) {
            Writer updateCoWriter = new Writer();
            updateCoWriter.addCoWriter(postIdx, writer, postRequest.getCoWriter().getRealWriter());
            writerRepository.save(updateCoWriter);
        }

        postRepository.save(getPost);

        return getPost.toResponse(getCoWriterInfo(getPost));
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

        // 게시글 삭제시 같이 작성한 사람들도 제거된다.
        List<Writer> postWriterInfo = writerRepository.findByPostIdxAndDelYn(getPost, "N");
        for (Writer writer : postWriterInfo) {
            writer.delete();
        }

        // 게시글 삭제시 댓글도 함께 삭제된다.
        List<Comment> postCommentList = commentRepository.findByPostIdxAndDelYn(getPost, "N");
        for (Comment comment : postCommentList) {
            comment.delete();
        }

        getPost.delete();

        postRepository.save(getPost);
    }

    /**
     * post 에 속해있는 writer 의 정보 조회
     */
    public WriterResponse getCoWriterInfo(Post post) {
        // 함께 작성한 사람을 찾고
        List<Writer> postWriterInfo = writerRepository.findByPostIdxAndDelYn(post, "N");
        // 작성한 사람의 정보를 꺼낸다.
        // 같이 작성한 사람은 다수 이므로 작성한 사람의 정보를 담을 리스트를 생성하고
        List<AccountResponse> coWriterList = new ArrayList<>();
        for (Writer writer : postWriterInfo) {
            // 같이 작성한 사람의 존재 유무를 확인 후 정보를 꺼낸다.
            Account coWriter = accountRepository.findById(writer.getAccountIdx().getAccountIdx())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));
            // 꺼낸 정보를 coWriterList 에 담아준다.
            coWriterList.add(coWriter.toResponse());
        }

        Account realWriter = accountRepository.findById(postWriterInfo.get(0).getRealWriter().getAccountIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT));

        WriterResponse writerResponse = new WriterResponse();
        writerResponse.addWriterResponse(coWriterList, realWriter);

        return writerResponse;
    }

    /**
     * 회원이 작성한 글 목록
     */
    public Page<PostResponse> getMyPosts(Pageable pageable, Account accountIdx, String display) {
        QWriter qWriter = QWriter.writer;

        BooleanBuilder builder = new BooleanBuilder();

        if (display.equals("public")) {
            builder.and(qWriter.postIdx.privated.eq("N"));
        } else if (display.equals("private")) {
            builder.and(qWriter.postIdx.privated.eq("Y"));
        }

        QueryResults<Post> pagePostList = jpaQueryFactory.select(qWriter.postIdx).distinct()
                .from(qWriter)
                .where(qWriter.realWriter.eq(accountIdx)
                        .and(qWriter.delYn.eq("N"))
                        .and(builder) // public or private
                ).orderBy(qWriter.postIdx.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post : pagePostList.getResults()) {
            postResponseList.add(post.toResponse(getCoWriterInfo(post)));
        }

        return new PageImpl<>(postResponseList, pageable, pagePostList.getTotal());
    }

    public Page<PostResponse> getMyPostsOrTagList(Pageable pageable, Account accountIdx, String tab) {
        QWriter qWriter = QWriter.writer;

        Page<Writer> myPostsList = writerRepository.findByAccountIdxAndRealWriterAndDelYnOrderByCreateDateDesc(pageable, accountIdx ,accountIdx, "N");

        QueryResults<Post> pageTagList = jpaQueryFactory.select(qWriter.postIdx).distinct()
                .from(qWriter)
                .where(qWriter.accountIdx.accountIdx.eq(accountIdx.getAccountIdx())
                        .and(qWriter.delYn.eq("N"))
                        .and(qWriter.realWriter.ne(accountIdx))
                ).orderBy(qWriter.postIdx.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<PostResponse> postResponseList = new ArrayList<>();
        Page<PostResponse> pagePostResponseList = null;

        if (tab.equals("tag")) { // 태그된 경우
            for (Post post : pageTagList.getResults()) {
                postResponseList.add(post.toResponse(getCoWriterInfo(post)));
            }

            pagePostResponseList = new PageImpl<>(postResponseList, pageable, pageTagList.getTotal());
        } else if (tab.equals("post")){ // 내가 작성한 글
            for (Writer writer : myPostsList) {
                Post postIdx = writer.getPostIdx();
                postResponseList.add(postIdx.toResponse(getCoWriterInfo(postIdx)));
            }

            pagePostResponseList = new PageImpl<>(postResponseList, pageable, myPostsList.getTotalElements());
        }

        return pagePostResponseList;
    }
}
