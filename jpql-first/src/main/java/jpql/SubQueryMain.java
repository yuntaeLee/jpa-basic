package jpql;

import jpql.entity.Member;
import jpql.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubQueryMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * JPA 서브 쿼리 한계
             * - JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
             * - SELECT 절도 가능(하이버네이트에서 지원)
             * - FROM 절의 서브 쿼리는 현재 JPQL 에서 불가능
             * -- 조인으로 풀 수 있으면 풀어서 해결
             */

            // 나이가 평균보다 많은 회원
            String query1 = "select m from Member m " +
                    "where m.age > (select avg(m2.age) from Member m2)";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();


            // teamA 소속인 회원
            String query2 = "select m from Member m " +
                    "where exists (select t from m.team t where t.name = 'teamA')";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();


            // 전체 상품 각각의 재고보다 주문량이 많은 주문들
            String query3 = "select o from Order o " +
                    "where o.orderAmount > All(select p.stockAmount from Product p";
            List<Order> result3 = em.createQuery(query3, Order.class).getResultList();

            // 어떤 팀이든 팀에 소속된 회원
            String query4 = "select m from Member m " +
                    "where m.team = ANY(select t from Team t)";
            List<Member> result4 = em.createQuery(query4, Member.class).getResultList();


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
