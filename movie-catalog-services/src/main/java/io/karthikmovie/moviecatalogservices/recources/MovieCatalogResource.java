package io.karthikmovie.moviecatalogservices.recources;

import io.karthikmovie.moviecatalogservices.modles.CtalogItem;
import io.karthikmovie.moviecatalogservices.modles.Movie;
import io.karthikmovie.moviecatalogservices.modles.Rating;
import io.karthikmovie.moviecatalogservices.modles.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private  WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CtalogItem> getCatalog(@PathVariable("userId") String userId){


        WebClient.Builder builder = WebClient.builder();

       UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+ userId, UserRating.class);

       return ratings.getUserRating().stream().map(rating -> {
                 //for each moie id, call movie info service and get details
                  Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                // put them all together

                   return new CtalogItem(movie.getName(), "test", rating.getRating());
               })
                .collect(Collectors.toList());



    }




      /* Movie movie = webClientBuilder.build()
                           .get()
                           .uri("http://localhost:8082/movies/" + rating.getMovieId())
                           .retrieve()
                           .bodyToMono(Movie.class)
                           .block();
                           */

}
