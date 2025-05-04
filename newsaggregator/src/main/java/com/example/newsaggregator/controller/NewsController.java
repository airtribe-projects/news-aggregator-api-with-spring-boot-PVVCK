package com.example.newsaggregator.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.newsaggregator.model.NewsArticle;
import com.example.newsaggregator.response.APIResponse;
import com.example.newsaggregator.service.NewsService;
import com.example.newsaggregator.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/news")
public class NewsController {

	 @Autowired
	    private NewsService newsService;

	    @Autowired
	    private UserService userService;

	    // Get news preferences for the logged-in user
	    @GetMapping("/preferences")
	    public ResponseEntity<APIResponse> getNews(@RequestParam String category) {
	        List<NewsArticle> articles = newsService.fetchNewsBasedOnPreferences(category);
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(articles);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(articles);
	    }


	    // Update news preferences for the logged-in user
	    @PutMapping("/update-preferences")
	    public ResponseEntity<APIResponse> updatePreferences(@Valid @RequestBody List<String> preferences) {
	        userService.updateUserPreferences(preferences);
	        
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData("Preferences updated successfully");
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok();
	    }

	    // Get news articles based on user preferences
	    @GetMapping("/news-by-preference")
	    public ResponseEntity<APIResponse> getNewsByCategory(@RequestParam String category) {
	        List<NewsArticle> newsArticles = newsService.fetchNewsBasedOnPreferences(category);
	        
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(newsArticles);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(newsArticles);
	    }

	    // Search news articles based on a keyword
	    @GetMapping("/search/{keyword}")
	    public ResponseEntity<APIResponse> searchNews(@PathVariable String keyword) {
	        List<NewsArticle> newsArticles = newsService.searchNews(keyword);
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(newsArticles);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(newsArticles);
	    }

	    // Mark a news article as read
	    @PostMapping("/{id}/read")
	    public ResponseEntity<APIResponse> markAsRead(@PathVariable Long id) {
	        newsService.markAsRead(id);
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData("Article marked as read");
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok();
	    }

	    // Mark a news article as favorite
	    @PostMapping("/{id}/favorite")
	    public ResponseEntity<APIResponse> markAsFavorite(@PathVariable Long id) {
	        newsService.markAsFavorite(id);
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData("Article marked as favorite");
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok();
	    }

	    // Get all read news articles
	    @GetMapping("/read")
	    public ResponseEntity<APIResponse> getReadArticles() {
	        List<NewsArticle> readArticles = newsService.getReadArticles();
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(readArticles);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(readArticles);
	    }

	    // Get all favorite news articles
	    @GetMapping("/favorites")
	    public ResponseEntity<APIResponse> getFavoriteArticles() {
	        List<NewsArticle> favoriteArticles = newsService.getFavoriteArticles();
	        
	        APIResponse apiResponse = new APIResponse();
    		apiResponse.setSuccess(true);	
    		apiResponse.setTimestamp(LocalDateTime.now());
    		apiResponse.setData(favoriteArticles);
    		apiResponse.setErrorMessage(null);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//	        return ResponseEntity.ok(favoriteArticles);
	    }

}
