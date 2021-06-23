package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.model.GifModel;

import java.util.List;

public interface GiphyService {

    List<GifModel> getGifs(String q);

    GifModel getRandomGif(String q);
}
