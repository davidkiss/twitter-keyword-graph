package com.kaviddiss.keywords.service;

import com.kaviddiss.keywords.domain.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by david on 2014-09-16.
 */
@Service
public class TwitterStreamIngester implements InitializingBean, StreamListener {

    private static final boolean PROCESS_FRIENDS = false;

    @Inject
    private Twitter twitter;
    @Inject
    private GraphService graphService;
    @Inject
    private ThreadPoolTaskExecutor taskExecutor;
    private BlockingQueue<Tweet> queue = new ArrayBlockingQueue<Tweet>(20);

    public void run() {
        List<StreamListener> listeners = new ArrayList<>();
        listeners.add(this);
        twitter.streamingOperations().sample(listeners);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < taskExecutor.getMaxPoolSize(); i++) {
            taskExecutor.execute(new TweetProcessor(graphService, queue));
        }

        run();
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
//            queue.offer(tweet);
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
