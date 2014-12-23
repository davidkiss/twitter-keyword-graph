package com.kaviddiss.keywords.service;

import com.kaviddiss.keywords.domain.*;
import com.kaviddiss.keywords.repository.KeywordRepository;
import com.kaviddiss.keywords.repository.TweetRepository;
import com.kaviddiss.keywords.repository.ProfileRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2014-09-18.
 */
@Service
public class GraphService {
    @Inject
    private KeywordRepository keywordRepository;
    @Inject
    private TweetRepository tweetRepository;
    @Inject
    private ProfileRepository profileRepository;

    public Connected connectWords(String str1, String str2) {
        Keyword word1 = createOrGetKeyword(str1);
        Keyword word2 = createOrGetKeyword(str2);
        Connected connected = createOrGetConnected(word1, word2);
        return connected;
    }

    private Connected createOrGetConnected(Keyword word1, Keyword word2) {
        Connected connected = keywordRepository.getRelationshipBetween(word1, word2, Connected.class, "Connected");
        if (connected == null) {
            connected = keywordRepository.createRelationshipBetween(word1, word2, Connected.class, "Connected");
        }
        connected.setCount(connected.getCount() + 1);
        return connected;
    }

    private Keyword createOrGetKeyword(String str) {
        str = str.toLowerCase();
        Keyword word = keywordRepository.findByWord(str);
        if (word == null) {
            word = new Keyword();
            word.word = str;
        }
        word = keywordRepository.save(word);
        return word;
    }

    public Tweet createTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile createProfile(String screenName) {
        return profileRepository.save(new Profile(screenName));
    }

    public Tag connectTweetWithTag(Tweet tweet, String word) {
        Keyword keyword = new Keyword(word);
        keyword = keywordRepository.save(keyword);
        Tag tag = tweetRepository.createRelationshipBetween(tweet, keyword, Tag.class, "Tag");
        return tag;
    }

    public Mention connectTweetWithMention(Tweet tweet, Profile mention) {
        return tweetRepository.createRelationshipBetween(tweet, mention, Mention.class, "Mention");
    }

    public Author connectTweetWithAuthor(Tweet tweet, Profile author) {
        return tweetRepository.createRelationshipBetween(tweet, author, Author.class, "Author");
    }

    public List<Map> findTopKeywords() {
        return keywordRepository.findTopKeywords();
    }

    public List<Keyword> findRelevantKeywords(String word) {
        Keyword keyword = keywordRepository.findByWord(word);
        return keyword == null ? new ArrayList<Keyword>() : keywordRepository.findRelevantKeywords(keyword.getId());
    }
}
