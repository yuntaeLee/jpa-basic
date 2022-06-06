package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.*;
import java.util.List;

public class FunctionMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(team);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.setTeam(team);

            em.persist(member1);
            em.persist(member2);

            em.flush();
            em.clear();

            /**
             * JPQL 기본 함수
             * - concat
             * - substring
             * - trim
             * - lower, upper
             * - length
             * - locate
             * - abs, sqrt, mod
             * - size, index (JPA 용도)
             */
            // concat
            String query1 = "select concat('a', 'b') from Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();
            for (String s : result1) {
                System.out.println("s = " + s);
            }

            // substring
            String query2 = "select substring(m.username, 2, 3) from Member m";
            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();
            for (String s : result2) {
                System.out.println("s = " + s);
            }

            // locate
            String query3 = "select locate('de', 'abcdefg') from Member m";
            List<Integer> result3 = em.createQuery(query3, Integer.class)
                    .getResultList();
            for (Integer integer : result3) {
                System.out.println("integer = " + integer);
            }

            // size
            String query4 = "select size(t.members) from Team t";
            List<Integer> result4 = em.createQuery(query4, Integer.class)
                    .getResultList();

            for (Integer integer : result4) {
                System.out.println("integer = " + integer);
            }


            /**
             * 사용자 정의 함수
             * - 하이버네이트는 사용전에 방언에 추가해야 한다.
             * -- 사용하는 DB 방언을 상속 받고, 사용자 정의 함수를 등록한다.
             */
            // 한줄에 출력
            String query5 = "select function('group_concat', m.username) from Member m";
            List<String> result5 = em.createQuery(query5, String.class)
                    .getResultList();

            for (String s : result5) {
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
