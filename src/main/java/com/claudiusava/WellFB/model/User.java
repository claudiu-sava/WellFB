package com.claudiusava.WellFB.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.text.SimpleDateFormat;
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

    @OneToOne
    private Avatar avatar;

    @ManyToMany
    private List<User> followedBy;

    @ManyToMany
    private List<User> follows;

    private Boolean isOnline;

    @Column(nullable = false)
    private String creationDate = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

}
