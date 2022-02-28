package com.bestSite.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "overviews")
@Data
public class Overview extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(columnDefinition = "TEXT", name = "text")
    private String text;

    @Column(name = "image")
    private String image = "https://mdbootstrap.com/img/Photos/Others/img%20(28).jpg";

    @Column(name = "rating")
    private int rating;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User author;

    public Overview(){
    }

    public Overview(String title, String image, String description, String text ) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.text = text;
    }
}
