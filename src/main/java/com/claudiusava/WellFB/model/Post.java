package com.claudiusava.WellFB.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column()
    private String description;

    @Column(nullable = false)
    private String date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

    @ManyToMany
    private List<User> likedBy;

    @OneToOne
    private Upload uploadFile;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Comment> comments;


}
