package association.mappedsuperclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑정보를 모으는 역할
 * 조회 검색 불가
 */
@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
