package com.bestSite.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "overviews")
@Data
public class Overview extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "rating")
    private int rating;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User author;

}
