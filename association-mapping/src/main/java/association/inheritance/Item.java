package association.inheritance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // DTYPE column 생성
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 자동으로 DTYPE 생성
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Setter
@ToString
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;
    private int price;
}
