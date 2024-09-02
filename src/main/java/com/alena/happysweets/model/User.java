package com.alena.happysweets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;
//User Model
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "{errors.invalid_email}")
    private String email;
    @NotEmpty
    private String password;
    //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    /*CascadeType.ALL: Any operation (save, delete, update, etc.) performed on the User entity
    will also be applied to its associated Role entities.
    FetchType.EAGER: When a User entity is retrieved from the database,
    its associated Role entities will be fetched immediately.*/
    //@JoinTable(name = "user_role",
            //joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            //inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<Role> roles;

    //A copy constructor that creates a new User object based on an existing one.
    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }
    public User(){
    }
}
