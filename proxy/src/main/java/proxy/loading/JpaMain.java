package proxy.loading;

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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("hello");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();
            
            /**
             * em.getReference(): 데이터 베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
             * 해당 객체를 사용할 시점에 query가 나감
             *
             * 프록시의 특징
             * 1. 프록시 객체는 처음 사용할 때 한 번만 초기화
             * 2. 프록시 객체를 통해서 실제 엔티티에 접근 가능
             * 3. 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의 (instance of 사용)
             * 4. 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference() 를 호출해도 실제 엔티티 반환
             * 5. 영속성 컨텍스트의 도움을 받을 수 없느 ㄴ준영속 상태일 때, 프록시를 초기화하면 문제 발생
             */
//            Member m = em.getReference(Member.class, member.getId());
//            System.out.println("m.getClass() = " + m.getClass());
//            System.out.println("m.getName() = " + m.getName());
//            System.out.println("m.getId) = " + m.getId());

            /**
             * 프록시 특징 4
             */
//            Member fm = em.find(Member.class, member.getId());
//            System.out.println("fm.getClass() = " + fm.getClass());
//
//            Member rm = em.getReference(Member.class, member.getId());
//            System.out.println("rm.getClass() = " + rm.getClass());

            /**
             * JPA 는 하나의 Transaction 내에서 객체의 동일성을 보장하기 때문에
             * find 호출시에도 proxy 객체를 출력
             */
//            Member refMember = em.getReference(Member.class, member.getId());
//            System.out.println("refMember.getClass() = " + refMember.getClass());
//
//            Member m = em.find(Member.class, member.getId());
//            System.out.println("m.getClass() = " + m.getClass());
//
//            System.out.println("refMember == m: " + (refMember == m));

            /**
             * 프록시 특징 5
             */
//            Member refMember = em.getReference(Member.class, member.getId());
//            System.out.println("refMember.getClass() = " + refMember.getClass()); // Proxy

//            em.close();

//            System.out.println("refMember.getName() = " + refMember.getName());

            /**
             * 프록시 확인
             */
//            Member refMember = em.getReference(Member.class, member.getId());
//
//            // 프록시 인스턴스의 초기화 여부 확인
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
//            refMember.getName();
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
//
//            // 프록시 클래스 확인
//            System.out.println("refMember.getClass().getName() = " + refMember.getClass());
//
//            // 프록시 강제 초기화
//            Hibernate.initialize(refMember);

            /**
             * 지연로딩
             */
//            Member m = em.find(Member.class, member.getId());
//            System.out.println("m.getClass() = " + m.getClass());
//            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass()); // Proxy
//
//            System.out.println("=====================");
//            m.getTeam().getName(); // 초기화
//            System.out.println("=====================");
//
//            // 같이 조회 하려면 fetch join 사용
//            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
//                    .getResultList();

            /**
             * 즉시로딩 문제점 (즉시로딩은 JPQL에서 N+1의 문제를 일으킨다.)
             * 추가 쿼리가 N개 많큼 나감
             *    SQL: select * from Member
             *    SQL: select * from Team where TEAM_ID = xxx
             *
             */
//            List<Member> members = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

}
