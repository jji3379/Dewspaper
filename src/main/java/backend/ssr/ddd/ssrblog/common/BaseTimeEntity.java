package backend.ssr.ddd.ssrblog.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // dateTime 값에 접근하기 위한 Getter
@MappedSuperclass // 속성을 공유하는 클래스, 진짜 JPA 상속은 X
@EntityListeners(AuditingEntityListener.class) // 이벤트 기반 동작
public class BaseTimeEntity {

    @LastModifiedDate // 마지막에 수정된 시간을 기록해주는 Anotation
    @Column(name = "date_time", updatable = true)
    @ApiModelProperty(value = "작성 일시")
    private LocalDateTime dateTime;
}
