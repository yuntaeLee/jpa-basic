package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BulkOperation {

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

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(20);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(30);
            em.persist(member3);

            /**
             * 벌크 연산
             * - 재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면?
             * - JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
             * -- 1. 재고가 10개 미만인 상품을 리스트로 조회한다.
             * -- 2. 상품 엔티티의 가격을 10% 증가한다.
             * -- 3. 트랜잭션 커밋 시점에 변경감지가 동작한다.
             * - 변경된 데이터가 100건이라면 100번의 UPDATE SQL 실행
             */

            /**
             * 벌크 연산 예제
             * - 쿼리 한 번으로 여러 테이블 로우 변경 (엔티티)
             * - executeupdate()의 결과는 영향받은 엔티티 수 반환
             * - UPDATE, DELETE 지원
             * - INSERT (insert into .. select, 하이버네이트 지원)
             */

            // query가 나가기 때문에 em.flush() 자동 호출
            String query1 = "update Product p " +
                            "set p.price = p.price * 1.1 " +
                            "where p.stockAmount < :stockAmount";
            int updateCount1 = em.createQuery(query1)
                    .setParameter("stockAmount", 10)
                    .executeUpdate();


            // 모든 사람의 나이를 20살로 변경
            int updateCount2 = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + updateCount2);

            em.clear(); // 영속성 컨텍스트 초기화 한 후 DB 에서 새롭게 조회

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());

            /**
             * 벌크 연산 주의
             * - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
             * -- 방법 1. 벌크 연산을 먼저 실행
             * -- 방법 2. 벌크 연산 수행 후 영속성 컨텍스트 초기화 (em.clear() 사용)
             * ---- 쿼리가 나가기 때문에 em.flush()는 자동 수행
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
