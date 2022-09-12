package br.kleberf65.scrapping.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Episode {
    private String title;
    private String urlDetails;

    private String backdrop;
    private List<Option> options = new ArrayList<>();

}