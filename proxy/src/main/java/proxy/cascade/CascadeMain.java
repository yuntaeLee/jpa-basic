package proxy.cascade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CascadeMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

//            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            /**
             * 영속성 전이
             * cascade = CascadeType.ALL
             * 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
             * 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
             */
            em.persist(parent);

            em.flush();
            em.clear();

            /**
             * 고아 객체
             * 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
             * orphanRemoval = true
             *
             * 주의점: 특정 Entity 하나에만 종속적일 때 사용
             *
             * 영속성 전이 + 고아 객체
             * 1. 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음
             * 2. 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용용
             */
            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildren().remove(0);

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
