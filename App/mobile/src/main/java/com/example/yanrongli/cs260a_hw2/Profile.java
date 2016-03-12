package com.example.yanrongli.cs260a_hw2;

/**
 * Created by yanrongli on 2/27/16.
 */
public class Profile {
    private String userId;
    private String name;
    //private int iconID;
    private String title;
    private String party;
    private String email;
    private String website;
    private String twitterId;
    private String termEnd;
    private String tweet;
    private String imgURL = "http://www.freelanceme.net/Images/default%20profile%20picture.png";
    //private String details;

    public Profile(String userId, String name, String title, String party, String email, String website, String twitterId, String termEnd)
    {
        super();
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.party = party;
        this.email = email;
        this.website = website;
        this.twitterId = twitterId;
        this.termEnd = termEnd;
    }

    public String getUserId() {return userId; }

    public String getName() { return name; }

    public String getTitle() { return title; }

    public String getParty() { return party; }

    public String getEmail() { return email; }

    public String getWebsite(){
        return website;
    }

    public String getTwitterId() {return twitterId; }

    public String getTermEnd() {return termEnd; }

    public String getTweet() {return tweet; }

    public String getImgURL() {return imgURL; }

    public void setTweet(String tweet) {this.tweet = tweet; }

    public void setImgURL(String imgURL) {this.imgURL = imgURL; }

}
