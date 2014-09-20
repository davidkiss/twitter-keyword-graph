package com.kaviddiss.keywords.service;

import com.kaviddiss.keywords.domain.Connected;
import com.kaviddiss.keywords.domain.Keyword;
import com.kaviddiss.keywords.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by david on 2014-09-18.
 */
@Service
public class GraphService {
    @Inject
    private KeywordRepository keywordRepository;

    @Transactional
    public Connected connectWords(String str1, String str2) {
//        System.out.println(str1+"->"+str2);
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
        word.count = word.count + 1;
        word = keywordRepository.save(word);
        return word;
    }
}
