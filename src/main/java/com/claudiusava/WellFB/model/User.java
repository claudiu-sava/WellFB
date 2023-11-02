package com.claudiusava.WellFB.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Role> roles;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> posts;


    public static String getLoggedUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
