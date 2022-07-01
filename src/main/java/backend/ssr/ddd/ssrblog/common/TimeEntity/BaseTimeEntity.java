package backend.ssr.ddd.ssrblog.common.TimeEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
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

    @CreatedDate // 등록된 시간을 기록해주는 Annotation
    @Column(name = "create_date", updatable = false) // 등록시간은 처음에만 발생하기 때문에 updatable = false 로 한다.
    @ApiModelProperty(value = "등록 시간")
    private LocalDateTime createDate;

    @LastModifiedDate // 마지막에 수정된 시간을 기록해주는 Anotation
    @Column(name = "update_date", updatable = true) // 수정시간은 수정 가능해야하 하기에 default 값인 updatable = true 로 한다.
    @ApiModelProperty(value = "수정 시간")
    private LocalDateTime updateDate;
}
