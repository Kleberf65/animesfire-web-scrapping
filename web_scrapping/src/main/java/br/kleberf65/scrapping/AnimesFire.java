package br.kleberf65.scrapping;

import com.google.gson.Gson;

import br.kleberf65.scrapping.enums.ListType;
import br.kleberf65.scrapping.models.Episode;
import br.kleberf65.scrapping.responses.AnimeDetailsResponse;
import br.kleberf65.scrapping.responses.AnimesListResponse;
import br.kleberf65.scrapping.responses.EpisodeResponse;
import br.kleberf65.scrapping.responses.HomePageResponse;
import br.kleberf65.scrapping.utilities.AnimesFireCallback;
import br.kleberf65.scrapping.utilities.JsoupUtils;
import br.kleberf65.scrapping.utilities.TaskRunner;

public class AnimesFire {

    public static void getHomePage(AnimesFireCallback<HomePageResponse> callback) {
        TaskRunner.getInstance().executeCallable(JsoupUtils::getHomePageResponse, callback::onResult);
    }

    public static void getAnimesList(AnimesFireCallback<AnimesListResponse> callback, ListType listType,  int page) {
        TaskRunner.getInstance().executeCallable(() -> JsoupUtils.getAnimesListResponse(listType, "", page), callback::onResult);
    }

    public static void getAnimesList(AnimesFireCallback<AnimesListResponse> callback, ListType listType, String text, int page) {
        TaskRunner.getInstance().executeCallable(() -> JsoupUtils.getAnimesListResponse(listType, text, page), callback::onResult);
    }

    public static void getAnimeDetails(AnimesFireCallback<AnimeDetailsResponse> callback, String urlDetails) {
        TaskRunner.getInstance().executeCallable(() -> JsoupUtils.getAnimeDetailsResponse(urlDetails), callback::onResult);
    }

    public static void getEpisodeOptions(AnimesFireCallback<EpisodeResponse> callback, Episode episode) {
        TaskRunner.getInstance().executeCallable(() -> JsoupUtils.getEpisodeResponse(episode), callback::onResult);
    }

    public static String objToString(Object object){
        return new Gson().toJson(object);
    }
}
