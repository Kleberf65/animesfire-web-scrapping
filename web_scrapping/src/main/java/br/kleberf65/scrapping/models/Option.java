package br.kleberf65.scrapping.models;

import br.kleberf65.scrapping.enums.VideoQuality;
import br.kleberf65.scrapping.enums.VideoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private String link;
    private VideoQuality videoQuality;
    private VideoType videoType;

}