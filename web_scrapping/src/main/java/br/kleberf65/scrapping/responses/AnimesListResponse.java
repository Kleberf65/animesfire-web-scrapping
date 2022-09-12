package br.kleberf65.scrapping.responses;

import java.util.ArrayList;
import java.util.List;

import br.kleberf65.scrapping.models.Anime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimesListResponse {

    private boolean success;
    private String message;
    private String lastPage;
    private List<Anime> animes = new ArrayList<>();

}