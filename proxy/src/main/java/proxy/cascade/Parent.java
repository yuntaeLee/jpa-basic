package proxy.cascade;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    /**
     * Parent와 Child의 Life cycle이 유사할 때
     * 단일 Entity에만 종속적일 때 사용
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }
}
