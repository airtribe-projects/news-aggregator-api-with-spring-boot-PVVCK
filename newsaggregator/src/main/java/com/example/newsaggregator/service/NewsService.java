package com.example.newsaggregator.service;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.newsaggregator.model.NewsApiArticle;
import com.example.newsaggregator.model.NewsArticle;
import com.example.newsaggregator.model.NewsArticleResponse;
import com.example.newsaggregator.repo.NewsArticleRepository;

import jakarta.transaction.Transactional;

@Service
public class NewsService {

    @Autowired
    private NewsArticleRepository newsArticleRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${newsapi.base-url}")
    private String newsApiUrl;

    @Value("${newsapi.api-key}")
    private String apiKey;

    // Fetch news based on user preferences
    @Transactional(rollbackOn = Exception.class)
    @Cacheable(value = "Cache_News_Preferences", key = "#category")
    public List<NewsArticle> fetchNewsBasedOnPreferences(String category) {
        try {
            String url = newsApiUrl + "?category=" + category + "&country=us&apiKey=" + apiKey;
            
            NewsArticleResponse response = restTemplate.getForObject(url, NewsArticleResponse.class);

            List<NewsArticle> savedArticles = new ArrayList<>();

            if (response != null && response.getArticles() != null) {
                for (NewsApiArticle apiArticle : response.getArticles()) {
                    NewsArticle article = new NewsArticle();
                    article.setTitle(apiArticle.getTitle());
                    article.setDescription(apiArticle.getDescription());
                    article.setUrl(apiArticle.getUrl());
                    article.setImageUrl(apiArticle.getUrlToImage());
                    article.setSource(apiArticle.getSource() != null ? apiArticle.getSource().getName() : null);
                    article.setContent(apiArticle.getContent());
                    article.setPublishedAt(parseDate(apiArticle.getPublishedAt()));

                    NewsArticle saved = newsArticleRepository.save(article);
                    savedArticles.add(saved);
                }
            }

            return savedArticles;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching news from API: " + e.getStatusCode(), e);
        }
    }


    private Date parseDate(String publishedAt) {
        try {
            OffsetDateTime odt = OffsetDateTime.parse(publishedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return Date.from(odt.toInstant());
        } catch (Exception e) {
            return new Date();
        }
    }


    // Search news based on a keyword
        public List<NewsArticle> searchNews(String keyword) {
            try {
                String searchUrl = UriComponentsBuilder.fromHttpUrl(newsApiUrl.replace("/top-headlines", "/everything"))
                        .queryParam("q", keyword)
                        .queryParam("apiKey", apiKey)
                        .toUriString();

                NewsArticleResponse response = restTemplate.getForObject(searchUrl, NewsArticleResponse.class);

                List<NewsArticle> searchedArticles = new ArrayList<>();

                if (response != null && response.getArticles() != null) {
                    for (NewsApiArticle apiArticle : response.getArticles()) {
                        NewsArticle article = mapNewsApiArticleToNewsArticle(apiArticle);
                        NewsArticle saved = newsArticleRepository.save(article);
                        searchedArticles.add(saved);
                    }
                }

                return searchedArticles;
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new RuntimeException("Error searching news: " + e.getStatusCode(), e);
            }
        }
        
        private NewsArticle mapNewsApiArticleToNewsArticle(NewsApiArticle apiArticle) {
            NewsArticle article = new NewsArticle();
            article.setTitle(apiArticle.getTitle());
            article.setDescription(apiArticle.getDescription());
            article.setUrl(apiArticle.getUrl());
            article.setImageUrl(apiArticle.getUrlToImage());
            article.setSource(apiArticle.getSource() != null ? apiArticle.getSource().getName() : null);
            article.setContent(apiArticle.getContent());
            article.setPublishedAt(parseDate(apiArticle.getPublishedAt()));
            return article;
        }


    // Mark an article as read
        @Transactional(rollbackOn = Exception.class)
    public void markAsRead(Long id) {
        Optional<NewsArticle> articleOpt = newsArticleRepository.findById(id);
        if (articleOpt.isPresent()) {
            NewsArticle article = articleOpt.get();
            article.setRead(true);
            newsArticleRepository.save(article);
        }
    }

    // Mark an article as favorite
        
    @Transactional(rollbackOn = Exception.class)
    public void markAsFavorite(Long id) {
        Optional<NewsArticle> articleOpt = newsArticleRepository.findById(id);
        if (articleOpt.isPresent()) {
            NewsArticle article = articleOpt.get();
            article.setFavorite(true);
            newsArticleRepository.save(article);
        }
    }

    // Get all read articles
    @Transactional(rollbackOn = Exception.class)
	@Cacheable(value = "Cache_News_AllReadArticles", key = "'allReadArticles'")
    public List<NewsArticle> getReadArticles() {
        return newsArticleRepository.findByRead(true);
    }

    // Get all favorite articles
    @Transactional(rollbackOn = Exception.class)
   	@Cacheable(value = "Cache_News_AllFavArticles", key = "'allFavArticles'")
    public List<NewsArticle> getFavoriteArticles() {
        return newsArticleRepository.findByFavorite(true);
    }
}
