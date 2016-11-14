package com.abnd.maso.news;

/**
 * Created by mariosoberanis on 11/5/16.
 * Article item from news feed the guardian
 */

public class GuardianArticle {
    private String mSectionName;
    private String mWebPublicationDate;
    private String mWebTitle;
    private String mWebUrl;

    protected GuardianArticle(String mSectionName, String mWebPublicationDate, String mWebTitle, String mWebUrl) {

        this.mSectionName = mSectionName;
        this.mWebPublicationDate = mWebPublicationDate;
        this.mWebTitle = mWebTitle;
        this.mWebUrl = mWebUrl;
    }

    protected String getSectionName() {
        return mSectionName;
    }

    protected String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    protected String getWebTitle() {
        return mWebTitle;
    }

    protected String getWebUrl() {
        return mWebUrl;
    }

}
