package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class PathExpressionMain {

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
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(20);
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            /**
             * 경로 표현식
             * - 상태 필드(state field): 단순히 값을 저장하기 위한 필드(ex: m.username)
             * -- 경로 탐색의 끝, 탐색 X
             *
             * - 연관 필드(association field): 연관관계를 위한 필드
             * -- 단일값 연관 필드: @ManyToOne, @OneToOne, 대상이 엔티티(ex: m.team)
             * ---- 단일값 연관 경로: 묵시적 내부 조인(inner join) 발생, 탐색 O
             *
             * -- 컬렉션 값 연관 필드: @OneToMany, @ManyToMany, 대상이 컬렉션(ex: m.orders)
             * ---- 컬렉션 값 연관 경로: 묵시적 내부 조인 발생, 탐색 X
             * ------ from 절에서 명시적 조인을 통해 별칠을 얻으면 별칭을 통해 탐색 가능
             */
            // 상태 필드
            String query1 = "select m.username from Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();


            // 단일 값 연관 경로 (묵시적 내부조인)
            String query2 = "select m.team from Member m";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            String query3 = "select m.team.name from Member m";
            List<String> result3 = em.createQuery(query3, String.class)
                    .getResultList();

            // 컬렉션 값 연관 경로 (탐색 X)
            String query4 = "select t.members from Team t";
            List result4 = em.createQuery(query4, Collection.class)
                    .getResultList();
            for (Object o : result4) {
                System.out.println("o = " + o);
            }

            /**
             * 경로 탐색을 사용한 묵시적 조인 시 주의사항
             * - 항상 내부 조인
             * - 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야 함
             * - 경로 탐색은 주로 select, where 절에서 사용하지만 묵시적 조인으로 인해
             *   SQL의 from (join) 절에 영향을 줌
             */
            // from 절에서 명시적 조인을 통해 별칠을 얻으면 별칭을 통해 탐색 가능
            String query5 = "select m.username from Team t join t.members m";
            List<String> result5 = em.createQuery(query5, String.class)
                    .getResultList();
            for (String s : result5) {
                System.out.println("s = " + s);
            }

            /**
             * 실무
             * - 묵시적 조인 대신에 명시적 조인 사용
             * - 조인은 SQL 튜닝에 중요 포인트
             * - 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움
             */

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
