package persistencecontext1;

import entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속 상태
            Member member = new Member();
            member.setId(1L);
            member.setName("memberA");

            // 영속 상태
            em.persist(member);

            // 준영속 상태: member entity 를 persistence context 에서 분리
            em.detach(member);

            // 삭제
            em.remove(member);

            // transaction 이 commit 된 후 쓰기 지연 SQL 저장소에서 SQL 쿼리가 flush 된다.
           tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.clear();
        }

        emf.close();
    }
}
