package association;

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

    @ManyToOne
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
