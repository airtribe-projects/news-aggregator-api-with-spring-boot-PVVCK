package com.example.newsaggregator.model;

import lombok.Data;
import java.util.List;

@Data
public class NewsArticleResponse {
    private String status;
    private int totalResults;
    private List<NewsApiArticle> articles;
}