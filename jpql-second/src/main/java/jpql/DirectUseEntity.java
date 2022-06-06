package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class DirectUseEntity {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            em.persist(member2);

            em.flush();
            em.clear();

            // 엔티티 직접 사용
            String query = "select count(m) from Member m";
            Long result = em.createQuery(query, Long.class)
                    .getSingleResult();
            System.out.println("result = " + result);

            // 엔티티 id 직접 사용
            String query1 = "select count(m.id) from Member m";
            Long result1 = em.createQuery(query1, Long.class)
                    .getSingleResult();
            System.out.println("result1 = " + result1);


            // 엔티티를 파라미터로 전달
            String query2 = "select m from Member m where m = :member";
            Member result2 = em.createQuery(query2, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("result2 = " + result2);


            // 식별자를 직접 전달
            String query3 = "select m from Member m where m.id = :memberId";
            Member result3 = em.createQuery(query3, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();
            System.out.println("findMember = " + result3);


            // 엔티티 직접 사용 -외래 키 값
            String query4 = "select m from Member m where m.team = :team";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : result4) {
                System.out.println("member = " + member);
            }

            String query5 = "select m from Member m where m.team.id = :teamId";
            Member result5 = em.createQuery(query5, Member.class)
                    .setParameter("teamId", teamA.getId())
                    .getSingleResult();
            System.out.println("result5 = " + result5);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
