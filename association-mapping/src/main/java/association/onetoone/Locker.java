package association.onetoone;

import javax.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    @Column(name = "LOCKER_NAME")
    private String name;

    @OneToOne(mappedBy = "locker")
    private User user;
}
