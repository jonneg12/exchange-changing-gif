package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.model.GifModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GifServiceImpl implements GifService{

    private final GiphyService giphy;

    @Autowired
    public GifServiceImpl(GiphyService giphy) {
        this.giphy = giphy;
    }

    @Override
    public GifModel getGif(String q) {
        return giphy.getRandomGif(q);
    }
}
