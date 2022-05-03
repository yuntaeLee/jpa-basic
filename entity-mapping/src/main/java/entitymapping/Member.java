package entitymapping;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "MEMBER_SEO_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 1)
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    private Integer age;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Lob
    private String description;

    /**
     * 필드 매핑 X
     * 데이터 베이스에 저장 X, 조회 X
     * 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때 사용용
     */
    @Transient
    private int temp;
}
