package com.kaviddiss.keywords.controller;

import com.kaviddiss.keywords.domain.Keyword;
import com.kaviddiss.keywords.service.GraphService;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2014-09-16.
 */
@Controller
public class TwitterController {

    private final Twitter twitter;
    private final GraphService graphService;

    @Inject
    public TwitterController(Twitter twitter, GraphService graphService) {
        this.twitter = twitter;
        this.graphService = graphService;
    }

    @RequestMapping("/{handle}/friends")
    @ResponseBody
    public CursoredList<TwitterProfile> findFriends(@PathVariable("handle") String handle) {
        // return friends using the Spring Social Twitter framework:
        return twitter.friendOperations().getFriends(handle);
    }

    @RequestMapping("/keywords/relevants/{word}")
    @ResponseBody
    public Iterable<Keyword> findRelevantKeywords(@PathVariable("word") String word) {
        return graphService.findRelevantKeywords(word);
    }

    @RequestMapping("/keywords/top")
    @ResponseBody
    public List<Map> findTopKeywords() {
        return graphService.findTopKeywords();
    }
}
