package com.example.newsaggregator.model;

import lombok.Data;

@Data
public class NewsApiArticle {

	  private Source source;
	    private String author;
	    private String title;
	    private String description;
	    private String url;
	    private String urlToImage;
	    private String publishedAt;
	    private String content;

	    @Data
	    public static class Source {
	        private String id;
	        private String name;
	    }
}
