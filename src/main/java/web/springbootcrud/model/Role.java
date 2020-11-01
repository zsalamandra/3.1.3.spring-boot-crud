package web.springbootcrud.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

// Этот класс реализует интерфейс GrantedAuthority, в котором необходимо переопределить только один метод getAuthority() (возвращает имя роли).
// Имя роли должно соответствовать шаблону: «ROLE_ИМЯ», например, ROLE_USER.
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;


    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REFRESH)
    private Set<User> users;

    public Role(){}

    public Role(Long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.users = Set.of(owner);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
       return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
