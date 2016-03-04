package com.example.yanrongli.cs260a_hw2;

/**
 * Created by yanrongli on 2/27/16.
 */
public class Profile {
    private String name;
    private int iconID;
    private String party;
    private String email;
    private String website;
    private String tweet;
    //private String details;

    public Profile(String name, int iconID, String party, String email, String website, String tweet)
    {
        super();
        this.name = name;
        this.iconID = iconID;
        this.party = party;
        this.email = email;
        this.website = website;
        this.tweet = tweet;
    }

    public String getName(){
        return name;
    }

    public int getIconID(){
        return iconID;
    }

    public String getParty(){
        return party;
    }

    public String getEmail(){
        return email;
    }

    public String getWebsite(){
        return website;
    }

    public String getTweet(){
        return tweet;
    }

}
