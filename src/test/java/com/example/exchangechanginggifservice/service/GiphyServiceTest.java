package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.exception.GifException;
import com.example.exchangechanginggifservice.feign.GiphyApi;
import com.example.exchangechanginggifservice.model.ExchangeRateChangeStatus;
import com.example.exchangechanginggifservice.model.GifModel;
import com.example.exchangechanginggifservice.model.GiphyModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GiphyServiceTest {

    @Value("${service.giphy.api-key}")
    private String apiKey;

    @MockBean
    private GiphyApi api;

    @Autowired
    private GiphyService service;

    @Test
    void testGetGifs() {
        String q = ExchangeRateChangeStatus.INCREASED.getQ();
        List<GifModel> mockedGifs = new ArrayList<>();
        mockedGifs.add(new GifModel("id1", "https://test.com"));
        Mockito.when(api.getGifs(apiKey, q)).thenReturn(new GiphyModel(mockedGifs));
        List<GifModel> gifs = service.getGifs(q);
        Assert.isTrue(gifs.equals(mockedGifs), "Should be equal");
    }

    @Test
    void testGetRandomGif() {
        String q = ExchangeRateChangeStatus.INCREASED.getQ();
        List<GifModel> mockedGifs = new ArrayList<>();
        mockedGifs.add(new GifModel("id1", "https://test1api.com"));
        mockedGifs.add(new GifModel("id2", "https://test2api.com"));
        mockedGifs.add(new GifModel("id3", "https://test3api.com"));
        mockedGifs.add(new GifModel("id4", "https://test4api.com"));
        Mockito.when(api.getGifs(apiKey, q)).thenReturn(new GiphyModel(mockedGifs));
        final GifModel randomGif = service.getRandomGif(q);
        Assert.isTrue(mockedGifs.contains(randomGif), "RandomGif will be one from gifs");
    }

    @Test
    void testGetRandomGifThrowException() {
        String q = ExchangeRateChangeStatus.INCREASED.getQ();
        Assertions.assertThrows(GifException.class,
                () -> service.getRandomGif(q));
    }
}