package br.kleberf65.scrapping.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    private String title;
    private String poster;
    private String season;
    private String studio;
    private String audio;
    private String episodesCount;
    private String status;
    private String launchDay;
    private String year;
    private String synopsis;
    private String rating;
    private String urlDetails;
    private List<Anime> relatedAnimes = new ArrayList<>();
    private List<Episode> episodes = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
}