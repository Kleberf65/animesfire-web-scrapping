package br.kleberf65.scrapping.responses;

import br.kleberf65.scrapping.models.Anime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeDetailsResponse {

    private boolean success;
    private String message;
    private Anime anime;

}