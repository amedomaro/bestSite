package com.bestSite.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "page")
@NoArgsConstructor
@Data
public class Page extends BaseEntity{

    @NotBlank(message = "Username should not be empty")
    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(columnDefinition = "TEXT", name = "text1")
    private String text1;

    @Column(columnDefinition = "TEXT", name = "text2")
    private String text2;
}
