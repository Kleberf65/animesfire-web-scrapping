package br.kleberf65.scrapping.responses;

import java.util.ArrayList;
import java.util.List;

import br.kleberf65.scrapping.models.Anime;
import br.kleberf65.scrapping.models.Episode;
import br.kleberf65.scrapping.models.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomePageResponse {

    private boolean success;
    private String message;

    private List<Genre> genres = new ArrayList<>();
    private List<Anime> releaseAnimes = new ArrayList<>();
    private List<Episode> lastEpisodes = new ArrayList<>();
    private List<Anime> weekAnimes = new ArrayList<>();
    private List<Anime> lastAnimes = new ArrayList<>();

}