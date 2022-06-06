package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NamedQuery {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);

            em.persist(member1);

            em.flush();
            em.clear();

            /**
             * Named 쿼리 - 정적 쿼리
             * - 미리 정의해서 이름을 부여해두고 사용하는 JPQL
             * - 정적 쿼리
             * - annotation, XML에 정의
             * - 애플리케이션 로딩 시점에 초기화 후 재사용
             * - 애플리케이션 로딩 시점에 쿼리를 검증
             *
             * Named 쿼리 환경에 따른 설정
             * - XML이 항상 우선권을 가진다.
             * - 애플리케이션 운영 환경에 따라 다른 XML을 배포할 수 있다.
             */

            // annotation named query
            List<Member> result1 = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "member1")
                    .getResultList();

            for (Member member : result1) {
                System.out.println("member = " + member);
            }

            tx.commit();
        } catch (Exception e) {
            e.getStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
