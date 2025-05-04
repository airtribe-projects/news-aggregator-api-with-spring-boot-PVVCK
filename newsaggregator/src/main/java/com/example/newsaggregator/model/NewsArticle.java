package com.example.newsaggregator.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 1000)
    private String description;
    private String url;
    private String imageUrl;
    private String source;
    private String content;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    private boolean read = false;      // To mark the article as read or not
    private boolean favorite = false;  // To mark the article as a favorite
}
