package jm.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users231")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String Name;

    @Column(name = "adress")
    private String Adress;

    @Column(name = "email")
    private String Email;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String Name, String Adress, String Email) {
        this.Name = Name;
        this.Adress = Adress;
        this.Email = Email;
    }

    public User(long id, String Name, String Adress, String Email) {
        this.id = id;
        this.Name = Name;
        this.Adress = Adress;
        this.Email = Email;
    }

    public User(long id, String name, String adress, String email, String login, String password) {
        this.id = id;
        this.Name = name;
        this.Adress = adress;
        this.Email = email;
        this.login = login;
        this.password = password;
    }

    public User(String name, String adress, String email, String login, String password) {
        this.Name = name;
        this.Adress = adress;
        this.Email = email;
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        this.Adress = adress;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

}
