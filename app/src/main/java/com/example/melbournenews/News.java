package com.example.melbournenews;

import java.util.Date;

/**
 * A {@link News} object contains information related to a single news item.
 * Each list item on the main screen displays relevant text and information about the story.
 *
 * The title of the article and the name of the section that it belongs to are required field.
 *
 * If available, author name and date published should be included.
 */

public class News {

    /** Title of the News Article */
    private String titleOfArticle;

    /** Name of the sections this article belongs to */
    private String sectionName;

    /** Name of the author of this article */
    private String authorName;

    /** Date on which the article was published */
    private String publicationDate;

    /** Web URL for the article */
    private String webURL;

    /** Thumbnail for the article */
    private String thumbnail;


    public News(String titleOfArticle, String sectionName, String authorName, String publicationDate, String webURL, String thumbnail) {
        this.titleOfArticle = titleOfArticle;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.publicationDate = publicationDate;
        this.webURL = webURL;
        this.thumbnail = thumbnail;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitleOfArticle() {
        return titleOfArticle;
    }

    /**
     * Returns the section name of the article.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Returns the name of the author of the article.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Returns the publication date of the article.
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Returns the web URL for the article
     */
    public String getWebURL(){
        return webURL;
    }

    /**
     * Returns the thumbnail for the article
     */
    public String getThumbnail(){
        return thumbnail;
    }
}
