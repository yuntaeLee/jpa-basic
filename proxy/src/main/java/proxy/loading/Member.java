package proxy.loading;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    /**
     * 실무에선 가급적 지연 로딩만 사용
     * 즉시 로딩을 적용하면 예상치 못한 SQL이 발생
     * 즉시로딩은 JPQL에서 N+1의 문제를 일으킨다.
     * @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
     * @OneToMany, @ManyToMany는 기본이 지연로딩
     */
    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩: 프록시로 조회
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    /**
     * 연관관계 편의 메서드
     */
    //양방향 mapping 시 순수 객체 상태를 고려해서 항상 양쪽에 값을 셋팅해준다.
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
