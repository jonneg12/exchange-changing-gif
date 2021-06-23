package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.exception.GifException;
import com.example.exchangechanginggifservice.feign.GiphyApi;
import com.example.exchangechanginggifservice.model.GifModel;
import com.example.exchangechanginggifservice.model.GiphyModel;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class GiphyServiceImpl implements GiphyService {

    private GiphyApi giphyApi;

    @Value("${service.giphy.api-key}")
    private String apiKey;

    @Autowired
    public GiphyServiceImpl(GiphyApi giphyApi) {
        this.giphyApi = giphyApi;
    }

    @Override
    public List<GifModel> getGifs(String q) {
        try {
            GiphyModel giphyModel = giphyApi.getGifs(apiKey, q);
            if(giphyModel == null) {
                log.error("Giphy model is null");
                throw new GifException("g03", "Giphy model is null");

            }
            log.info("Got gifs: {}", giphyModel.getData());
            final List<GifModel> gifs = giphyModel.getData();
            if (gifs.isEmpty()) {
                log.error("Gifs list is empty error");
                throw new GifException("g02", "Got empty gif list from api");
            }
            return gifs;
        } catch (FeignException exception) {
            log.error("Feign error ", exception);
            throw new GifException("g01", "Couldn't get data from giphy");
        }
    }

    @Override
    public GifModel getRandomGif(String q) {
        final List<GifModel> gifs = getGifs(q);
        final GifModel randomGif = gifs.get(new Random().nextInt(gifs.size()));
        log.info("Got random gif: {}", randomGif);
        return randomGif;
    }
}
