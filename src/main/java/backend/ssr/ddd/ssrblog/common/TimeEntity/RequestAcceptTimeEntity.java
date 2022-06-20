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

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class RequestAcceptTimeEntity {

    @CreatedDate
    @Column(name = "request_date_time", updatable = false)
    @ApiModelProperty(value = "요청 날짜")
    private LocalDateTime requestDateTime;

    @LastModifiedDate
    @Column(name = "accepted_date_time", updatable = true)
    @ApiModelProperty(value = "수락 날짜")
    private LocalDateTime acceptedDateTime;
}
