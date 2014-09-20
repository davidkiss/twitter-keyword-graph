package com.kaviddiss.keywords.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by david on 2014-09-16.
 */
@Service
public class TwitterStreamIngester implements InitializingBean, StreamListener {

    private static final Pattern HANDLE_PATTERN = Pattern.compile("#\\w+");
    private static final boolean PROCESS_FRIENDS = false;
    private static final boolean USE_HANDLES = true;

    @Inject
    private Twitter twitter;
    @Inject
    private GraphService graphService;

    public void run() {
        List<StreamListener> listeners = new ArrayList<>();
        listeners.add(this);
        twitter.streamingOperations().sample(listeners);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        run();
    }

    private void processTweet(String lang, String text) {
        if (!"en".equals(lang)) {
            return;
        }

        Matcher matcher = HANDLE_PATTERN.matcher(text);
        Set<String> handles = new HashSet<>();
        while (matcher.find()) {
            String handle = matcher.group();
            handles.add(handle.substring(1));
        }

        if (handles.isEmpty()) {
            return;
        }

        text = text.replaceAll("[\\s,\"]", " ").replaceAll("[ ]{2,}", " ");
        System.out.println("Tweet: " + text);
        String[] words = USE_HANDLES ? handles.toArray(new String[handles.size()]) : text.split("[ ]+");
//        words = handles.toArray(new String[handles.size()]);
        int counter = 0;
        System.out.println("Word count: " + words.length);
        for (int i = 0; i < words.length - 1; i++) {
            for (int j = i + 1; j < words.length; j++) {
                try {
                    graphService.connectWords(words[i], words[j]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                counter++;
            }
        }
        System.out.println("New connections: " + counter);
    }

    @Override
    public void onTweet(Tweet tweet) {
        if (PROCESS_FRIENDS) {
            tweet.getFromUser();
            CursoredList<Long> followerIds = twitter.friendOperations().getFollowerIds(tweet.getFromUserId());
            for (Long followerId : followerIds) {
                graphService.connectWords(String.valueOf(tweet.getFromUserId()), String.valueOf(followerId));
            }
        } else {
            processTweet(tweet.getLanguageCode(), tweet.getText());
        }
    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
    }
}
