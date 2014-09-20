package com.kaviddiss.keywords.controller;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * Created by david on 2014-09-16.
 */
@Controller
public class TwitterController {

    private Twitter twitter;

    @Inject
    public TwitterController(Twitter twitter) {
        this.twitter = twitter;
    }

    @RequestMapping("/{handle}/friends")
    @ResponseBody
    public CursoredList<TwitterProfile> findProfile(@PathVariable("handle") String handle) {
        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends(handle);
        return friends;
    }
}
