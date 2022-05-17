package association;

import association.inheritance.Item;
import association.inheritance.Movie;
import association.manytoone.Member;
import association.manytoone.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /*try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getName() = " + m.getName());
            }

            *//**
             * 양방향 mapping 주의점
             *//*
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.changeTeam(team); // **
            em.persist(member);

//            em.flush();
//            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            System.out.println("=============");
            for (Member m : members) {
                System.out.println("m = " + m.getName());
            }
            System.out.println("=============");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
        */


        /*
        try {

            Movie movie = new Movie();
            movie.setDirector("aaa");
            movie.setActor("bbb");
            movie.setName("ccc");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            Item findMovie = em.find(Item.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            tx.commit();
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
        */

        try {

            Member member = new Member();
            member.setName("member1");
            member.setCreatedBy("Lee");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

            em.flush();
            em.clear();

            tx.commit();
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

}
