package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // inner 생략 가능
            String query1 = "select m from Member m inner join m.team t";
            List<Member> result1 = em.createQuery(query1, Member.class)
                    .getResultList();

            // outer 생략 가능
            String query2 = "select m from Member m left outer join m.team t";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();


            // 세타 조인
            String query3 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .getResultList();

            // ON 절
            // ex) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
            String query4 = "select m, t from Member m left join m.team t on t.name = 'A'";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();

            // 연관관계 없는 엔티티 외부 조인
            // ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
            String query5 = "select m, t from Member m left join Team t on m.username = t.name";
            List<Member> result5 = em.createQuery(query5, Member.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }

        emf.close();
    }
}
