package com.bestSite.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "overviews")
@NoArgsConstructor
@Data
public class Overview extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(columnDefinition = "TEXT", name = "text")
    private String text;

    @Column(name = "image")
    private String image;

    @Column(name = "rating")
    private int rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "overview_id")
    private List<Comment> comments;

    public Overview(String title, String image, String description, String text ) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.text = text;
    }
}
