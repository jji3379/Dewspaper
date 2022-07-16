package backend.ssr.ddd.ssrblog.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "refresh-token 이 유효하지 않습니다"),
    DUPLICATE_FRIEND(BAD_REQUEST, "이미 친구 상태입니다"),
    DUPLICATE_FRIEND_REQUEST(BAD_REQUEST, "이미 친구 요청을 보낸 상태입니다"),
    INVALID_WRITER(BAD_REQUEST, "작성자와 일치하지 않습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    EXPIRE_TOKEN(UNAUTHORIZED, "토큰이 만료 되었습니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    NOT_FOUND_ACCOUNT(NOT_FOUND, "요청하신 회원의 정보가 존재하지 않습니다"),
    NOT_FOUND_REQUESTER(NOT_FOUND, "친구 신청 회원의 정보가 존재하지 않습니다"),
    NOT_FOUND_ACCEPTER(NOT_FOUND, "친구 수락 회원의 정보가 존재하지 않습니다"),
    NOT_FOUND_ALARM(NOT_FOUND, "삭제할 알람이 존재하지 않습니다"),
    NOT_FOUND_POST(NOT_FOUND, "요청하신 게시물이 존재하지 않습니다"),
    NOT_FOUND_COMMNET(NOT_FOUND, "요청하신 댓글이 존재하지 않습니다"),
    NOT_FOUND_FRIEND_REQUEST(NOT_FOUND, "요청하신 친구요청이 존재하지 않습니다"),
    NOT_FOUND_WRITER(NOT_FOUND, "요청하신 작성자가 존재하지 않습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다");

    private final HttpStatus httpStatus;
    private final String detail;
}