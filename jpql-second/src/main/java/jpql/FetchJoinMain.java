package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            /**
             * Fetch join
             * - SQL join 종류 X
             * - JPQL 에서 성능 최적화를 위해 제공하는 기능
             * - 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
             * - join fetch 명령어 사용
             */

            /**
             * Entity fetch join
             * - 회원을 조회하면서 연관된 팀도 함께 조회 (SQL 한번에)
             * - SQL을 보면 회원 뿐만 아니라 팀(t.*)도 함께 select
             * -- [JPQL]: select m from Member m join fetch m.team
             * -- [SQL]: SELECT M.*, T.* FROM MEMBER M
             *           INNER JOIN TEAM T ON M.TEAM_ID = T.ID
             */
            String query1 = "select m from Member m join fetch m.team";
            List<Member> result1 = em.createQuery(query1, Member.class)
                    .getResultList();

            for (Member member : result1) {
                System.out.println("username = " + member.getUsername() + ", team = " + member.getTeam().getName());

                // join fetch 사용 안할 시
                // member1, teamA (SQL)
                // Persistence context 에 teamA 가 올라감
                // member2, teamA (1차 캐시)
                // member3, teamB (SQL)
            }


            /**
             * 컬렉션 fetch join
             * - 일대다 관계
             * -- [JPQL]: select t from Team t join fetch t.members where t.name = 'teamA'
             * -- [SQL]: SELECT T.*, M.* FROM TEAM T
             *           INNER JOIN MEMBER M ON T.ID = M.TEAM_ID
             *           WHERE T.NAME = 'teamA'
             *
             * fetch join과 distinct
             * - SQL의 distinct는 중복된 결과를 제거하는 명령
             * - JPQL의 distinct는 2가지 기능 제공
             * -- 1. SQL의 distinct 를 추가
             * -- 2. 애플리케이션에서 엔티티 중복 제거
             */
            String query2 = "select distinct t from Team t join fetch t.members";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            for (Team team : result2) {
                System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());

                for (Member member : team.getMembers()) {
                    System.out.println("--> member = " + member);
                }
            }

            /**
             * fetch join과 일반 join의 차이
             * - JPQL은 결과를 반환할 때 연관관계 고려 X
             * - 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
             * - 여기서 팀 엔티티만 조회하고, 회원 엔티티는 조회 X
             * - fetch join을 사용할 때만 연관된 엔티티도 함께 조회 (즉시 로딩)
             * - fetch join은 객체 그래프를 SQL 한번에 조회하는 개념
             */

            /**
             * fetch join의 특징과 한계
             * - fetch join 대상에는 별칭을 줄 수 없다.
             *  -- 하이버네이트는 가능, 가급적 사용 X
             *
             * - 둘 이상의 컬렉션 fetch join 할 수 없다.
             * - 컬렉션을 fetch join 하면 페이징 API(setFirstResult, setMaxResults) 를 사용할 수 없다.
             *  -- 일대일, 다대일 같은 단일 값 연관 필드들은 fetch join 해도 페이징 가능
             *  -- 하이버네이트는 경고 로그를 남기고 메모리에서 페이징 (매우 위험)
             *
             * - 연관된 엔티티들은 SQL 한 번으로 조회 - 성능 최적화
             * - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
             *   -- @OneToMany(fetch = FetchType.LAZY) -> 글로벌 로딩 전략
             *
             * - 실무에서 글로벌 로딩 전략은 모두 지연 로딩
             * - 최적화가 필요한 곳은 fetch join 적용
             */

            /**
             * - 필드 설정
             * --> Team class의 members 필드에 @BatchSize(size = 100) 을 적용해 N + 1 문제를 해결
             *
             * - 글로벌 설정
             * persistence.xml
             * --> <property name="hibernate.default_batch_fetch_size" value="100"/>
             */
            String query3 = "select t from Team t";
            List<Team> result3 = em.createQuery(query3, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result3.size() = " + result3.size());

            for (Team team : result3) {
                System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());

                for (Member member : team.getMembers()) {
                    System.out.println("--> member = " + member);
                }
            }

            /**
             * fetch join 정리
             * - 모든 것을 fetch join 으로 해결할 수는 없음
             * - fetch join 은 객체 그래프를 유지할 때 사용하면 효과적
             * - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
             *   fetch join 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
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
