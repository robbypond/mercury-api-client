package com.robbypond.mercury.data;

import lombok.Data;

@Data
public class MercuryArticle {
    private String title;
    private String content;
    private String date_published;
    private String lead_image_url;
    private String dek;
    private String url;
    private String domain;
    private String excerpt;
    private int word_count;
    private String direction;
    private int total_pages;
    private int rendered_pages;
    private String next_page_url;
}
