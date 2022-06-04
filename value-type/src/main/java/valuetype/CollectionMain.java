package valuetype;

import valuetype.collection.AddressEntity;
import valuetype.collection.User;
import valuetype.embedded.Address;
import valuetype.embedded.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class CollectionMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /**
             * 값 타입 저장
             * 값 타입 컬렉션은 영속성 전에(Cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.
             */
            User user = new User();
            user.setUsername("user1");
            user.setHomeAddress(new Address("homeCity", "street", "123-123"));

            user.getFavoriteFoods().add("치킨");
            user.getFavoriteFoods().add("족발");
            user.getFavoriteFoods().add("피자");

            user.getAddressHistory().add(new AddressEntity("old1", "street", "123-123"));
            user.getAddressHistory().add(new AddressEntity("old2", "street", "123-123"));

            em.persist(user);

            em.flush();
            em.clear();

            /**
             * 값 타입 조회
             * 값 타입 컬렉션도 지연 로딩 전략 사용
             */
            System.out.println("============= START =============");
//            User findUser = em.find(User.class, user.getId());
//
//            List<Address> addressHistory = findUser.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address.getCity() = " + address.getCity());
//            }
//
//            Set<String> favoriteFoods = findUser.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }

            /**
             * 값 타입 수정
             */
            User findUser = em.find(User.class, user.getId());

            // 사이드 이팩트 발생 가능성
//            findUser.getHomeAddress().setCity("newCity");
            Address oldAddress = findUser.getHomeAddress();
            findUser.setHomeAddress(new Address("newCity", oldAddress.getStreet(), oldAddress.getZipcode()));

            // 치킨 -> 한식
            findUser.getFavoriteFoods().remove("치킨");
            findUser.getFavoriteFoods().add("한식");

            /**
             * 값 타입 컬렉션의 제약사항
             * - 값 타입은 엔티티와 다르게 식별자 개념이 없다
             * - 값은 변경하면 추적이 어렵다.
             * - 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
             * - 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
             * - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 함: null 입력X, 중복 저장X
             *
             * -- 결론 : 사용 X
             *
             * 값 타입 컬렉션 대안
             * - 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
             * - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
             * - 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
             * EX) AddressEntity
             */
            // old1 -> newCity1
//            findUser.getAddressHistory().remove(new Address("old1", "street", "123-123"));
//            findUser.getAddressHistory().add(new Address("newCity1", "street", "123-123"));

            /**
             * 정리
             * - 엔티티 타입의 특징
             * -- 식별자 O
             * -- 생명 주기 관리
             * -- 공유
             *
             * - 값 타입의 특징
             * -- 식별자 X
             * -- 생명 주기를 엔티티에 의존
             * -- 공유하지 않는 것이 안전 (복사해서 사용)
             * -- 불변 객체로 만드는 것이 안전
             *
             * ! 값 타입은 정말 값 타입이라 판단될 때만 사용
             * ! 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
             * ! 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티
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
