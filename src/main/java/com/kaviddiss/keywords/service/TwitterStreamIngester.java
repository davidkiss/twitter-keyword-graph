package com.kaviddiss.keywords.service;

import com.kaviddiss.keywords.domain.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.Tweet;
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

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");
    private static final boolean PROCESS_FRIENDS = false;
    private static final boolean USE_HASHTAG = true;

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

    private void processTweet(Tweet tweetEntity) {
        String lang = tweetEntity.getLanguageCode();
        String text = tweetEntity.getText();
        if (!"en".equals(lang)) {
            return;
        }

        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        Set<String> hashtags = new HashSet<>();
        while (matcher.find()) {
            String handle = matcher.group();
            hashtags.add(handle.substring(1));
        }

        if (hashtags.isEmpty()) {
            return;
        }

        com.kaviddiss.keywords.domain.Tweet tweet = new com.kaviddiss.keywords.domain.Tweet(tweetEntity.getFromUser(), tweetEntity.getText(), tweetEntity.getCreatedAt(), tweetEntity.getLanguageCode());
        tweet = graphService.createTweet(tweet);
        text = text.replaceAll("[\\s,\"]", " ").replaceAll("[ ]{2,}", " ");
        System.out.println("Tweet: " + text);
        if (tweetEntity.getEntities() != null) {
            System.out.println("Mentions: " + tweetEntity.getEntities().getMentions().size());
            for (MentionEntity mentionEntity : tweetEntity.getEntities().getMentions()) {
                Profile profile = new Profile(mentionEntity.getScreenName());
                profile = graphService.createProfile(profile);

                graphService.connectTweetWithMention(tweet, profile);
            }
        }

        String[] words = USE_HASHTAG ? hashtags.toArray(new String[hashtags.size()]) : text.split("[ ]+");
        System.out.println("Word count: " + words.length);
        for (String word : words) {
            graphService.connectTweetWithTag(tweet, word);
        }
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
            processTweet(tweet);
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
