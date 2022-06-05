package jpql;

import jpql.entity.Member;

import javax.persistence.*;
import java.util.List;

public class BasicMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            /**
             * TypeQuery: 반환 타입이 명확할 때 사용
             * Query: 반환 타입이 명확하지 않을 때 사용
             */
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            /**
             * 결과 조회 API
             * - getResultList(): 결과가 하나 이상일 때, 리스트 반환
             * -- 결과가 없으면 빈 리스트 반환
             *
             * - getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
             * -- 결과가 없으면: javax.persistence.NoResultException
             * -- 둘 이상이면: javax.persistence.NonUniqueResultException
             */
            List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            TypedQuery<Member> query = em.createQuery("select m from Member m where m.id = 1", Member.class);
            Member singleResult = query.getSingleResult();
            System.out.println("singleResult = " + singleResult);

            /**
             * 파라미터 바인딩
             */
            Member result = em.createQuery("select m from Member m where m.username =: username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("result = " + result);


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
