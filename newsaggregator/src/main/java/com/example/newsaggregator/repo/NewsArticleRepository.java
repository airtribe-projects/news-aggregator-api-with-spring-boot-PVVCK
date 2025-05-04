package com.example.newsaggregator.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.newsaggregator.model.NewsArticle;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByRead(boolean read);
    List<NewsArticle> findByFavorite(boolean favorite);
}