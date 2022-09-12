package br.kleberf65.scrapping.responses;

import br.kleberf65.scrapping.models.Episode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeResponse {

    private boolean success;
    private String message;
    private Episode episode;

}