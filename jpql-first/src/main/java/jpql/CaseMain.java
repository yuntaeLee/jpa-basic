package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CaseMain {

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
            member.setUsername(null);
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 기본 CASE 식
            String query1 = "select " +
                                "case when m.age <= 10 then '학생요금' " +
                                "     when m.age >= 60 then '경로요금' " +
                                "     else '일반요금' " +
                                "end " +
                           "from Member m";

            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();

            for (String s : result1) {
                System.out.println("s = " + s);
            }

            // 단순 CASE 식
            String query2 = "select " +
                                "case t.name " +
                                    "when 'teamA' then '인센티브 110%' " +
                                    "when 'teamB' then '인센티브 120%' " +
                                    "else '인센티브 150%' " +
                                "end " +
                            "from Team t";

            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : result2) {
                System.out.println("s = " + s);
            }

            // 사용자 이름이 없으면 이름 없는 회원을 반환
            String query3 = "select coalesce(m.username, '이름 없는 회원') from Member m ";
            List<String> result3 = em.createQuery(query3, String.class)
                    .getResultList();

            for (String s : result3) {
                System.out.println("s = " + s);
            }

            // 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름을 반환
            String query4 = "select nullif(m.username, '관리자') from Member m ";
            List<String> result4 = em.createQuery(query4, String.class)
                    .getResultList();

            for (String s : result4) {
                System.out.println("s = " + s);
            }

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
