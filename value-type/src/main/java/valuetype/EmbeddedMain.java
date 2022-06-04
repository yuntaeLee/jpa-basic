package valuetype;

import valuetype.embedded.Address;
import valuetype.embedded.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EmbeddedMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

//            Member member = new Member();
//            member.setUsername("Lee");
//            member.setHomeAddress(new Address("city", "street", "123-123"));
//            member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));
//
//            em.persist(member);


            /**
             * 임베디드 타임 같은 타입을 여러 엔티티에서 공유하면 위험함
             *
             * 객체 타입의 한계
             * - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
             * - 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다.
             * - 자바 기본 타입에 값을 대입하면 값을 복사한다.
             * - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
             * - 객체의 공유 참조는 피할 수 없다.
             *
             * 불변 객체 활용
             * - 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
             * - 값 타입은 불변 객체(immutable object)로 설계해야 함
             * - 불변 객체: 생성 시점 이후 절대 값을 변경할 수 없는 객체
             * - 생성자로만 값을 설정하고 수정자(setter)를 만들지 않으면 됨
             * - 참고: Integer, String은 자바가 제공하는 대표적인 불변 객체
             */
            Address address = new Address("city", "street", "123-123");
            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setHomeAddress(address);
            em.persist(member1);

            Address copyAddress = new Address(address.getCity(), address.getCity(), address.getZipcode());
            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(copyAddress);
            em.persist(member2);

            // 값 변경 불가능
//            member1.getHomeAddress().setCity("newCity");

            // 값을 통으로 변경
            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
            member1.setHomeAddress(newAddress);


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
