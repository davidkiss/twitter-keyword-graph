package com.kaviddiss.keywords.service;

import com.kaviddiss.keywords.domain.Profile;
import org.springframework.social.twitter.api.MentionEntity;
import org.springframework.social.twitter.api.Tweet;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by david on 2014-12-19.
*/
public class TweetProcessor implements Runnable {
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");
    private static final boolean USE_HASHTAG = true;

    private GraphService graphService;
    private final BlockingQueue<Tweet> queue;

    public TweetProcessor(GraphService graphService, BlockingQueue<Tweet> queue) {
        this.graphService = graphService;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tweet tweet = queue.take();
//                System.out.printf("Text: %s%n", tweet.getText());
                processTweet(tweet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        text = text.replaceAll("[\\s,\"@]", " ").replaceAll("[ ]{2,}", " ");
        int mentions = 0;
        if (tweetEntity.getEntities() != null) {
            mentions = tweetEntity.getEntities().getMentions().size();
            for (MentionEntity mentionEntity : tweetEntity.getEntities().getMentions()) {
                Profile profile = new Profile(mentionEntity.getScreenName());
                profile = graphService.createProfile(profile);

                graphService.connectTweetWithMention(tweet, profile);
            }
        }

        String[] words = USE_HASHTAG ? hashtags.toArray(new String[hashtags.size()]) : text.split("[ ]+");
        for (String word : words) {
            graphService.connectTweetWithTag(tweet, word);
        }
        System.out.printf("%d - %d - %s%n", mentions, words.length, text);
    }
}
