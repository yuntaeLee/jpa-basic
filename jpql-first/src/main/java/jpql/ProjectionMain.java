package jpql;

import jpql.dto.MemberDTO;
import jpql.entity.Member;
import jpql.entity.Team;
import jpql.entity.valuetype.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class ProjectionMain {

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

            em.flush();
            em.clear();

            // 엔티티 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            em.flush();
            em.clear();

            // 임베디드 타입 프로젝션
            em.createQuery("select o.address from Order o", Address.class)
                            .getResultList();

            /**
             * 스칼라 타입 프로젝션, distinct 로 중복 제거
             *
             * * new 명령어로 조회 *
             * - 단순 값을 DTO로 바로 조회
             * - 패키지 명을 포함한 전체 클래스 명 입력
             * - 순서와 타입이 일치하는 생성자 필요
             */

            // 1. Query 타입으로 조회
//            List resultList = em.createQuery("select distinct m.username, m.age from Member m")
//                    .getResultList();
//            Object o = resultList.get(0);
//            Object[] resultObj = (Object[]) o;

            // 2. Object[] 타입으로 조회
//            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
//                    .getResultList();
//            Object[] resultObj = resultList.get(0);

            // new 명령어로 조회
            List<MemberDTO> resultList = em.createQuery("select new jpql.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());

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
