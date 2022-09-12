package br.kleberf65.scrapping.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.kleberf65.scrapping.enums.ListType;
import br.kleberf65.scrapping.enums.VideoQuality;
import br.kleberf65.scrapping.enums.VideoType;
import br.kleberf65.scrapping.models.Anime;
import br.kleberf65.scrapping.models.Episode;
import br.kleberf65.scrapping.models.Genre;
import br.kleberf65.scrapping.models.Option;
import br.kleberf65.scrapping.responses.AnimeDetailsResponse;
import br.kleberf65.scrapping.responses.AnimesListResponse;
import br.kleberf65.scrapping.responses.EpisodeResponse;
import br.kleberf65.scrapping.responses.HomePageResponse;

public class JsoupUtils {

    static final String BASE_URL = "https://animefire.net";

    public static HomePageResponse getHomePageResponse() {
        HomePageResponse homePageResponse = new HomePageResponse();
        try {
            Document html = Jsoup.connect(BASE_URL).get();

            //GENRES
            List<Genre> genres = getListGenres(html.selectXpath(".//div[@class=\"dropdown-menu dropdown-menu-fk dmGenero\"]"));
            homePageResponse.setGenres(genres);

            //LIST OF ANIMES
            Elements selectXpath = html.selectXpath(".//div[@class=\"row mx-2\"]");

            //RELEASE ANIMES
            List<Anime> releaseAnimes = getListAnimes(selectXpath.get(0).selectXpath(".//article[@class=\"containerAnimes\"]"));
            homePageResponse.setReleaseAnimes(releaseAnimes);
            System.out.println("RELEASE ANIMES " + releaseAnimes.size());

            //WEEK ANIMES
            List<Anime> weekAnimes = getListAnimes(selectXpath.get(1).selectXpath(".//article[@class=\"containerAnimes\"]"));
            homePageResponse.setWeekAnimes(weekAnimes);
            System.out.println("WEEK ANIMES " + weekAnimes.size());

            //LAST ANIMES
            List<Anime> lastAnimes = getListAnimes(selectXpath.get(2).selectXpath(".//article[@class=\"containerAnimes\"]"));
            homePageResponse.setLastAnimes(lastAnimes);
            System.out.println("LAST ANIMES " + lastAnimes.size());


            //LAST EPISODES
            List<Episode> lastEpisodes = new ArrayList<>();
            for (Element element : html.selectXpath(".//article[@class=\"card cardUltimosEps\"]")) {
                Episode episode = new Episode();

                //EPISODE TITLE
                episode.setTitle(element.select("a").attr("href"));
                //EPISODE BACKDROP
                episode.setBackdrop(element.select("a img.card-img-top.lazy.imgAnimesUltimosEps").attr("data-src"));
                //EPISODE URL DETAILS
                episode.setUrlDetails(element.attr("href"));

                lastEpisodes.add(episode);
            }

            homePageResponse.setLastEpisodes(lastEpisodes);

            homePageResponse.setSuccess(true);

        } catch (Exception e) {
            homePageResponse.setSuccess(false);
            homePageResponse.setMessage(e.getMessage());
        }
        return homePageResponse;
    }

    public static AnimesListResponse getAnimesListResponse(ListType listType, String urlDetails, int page) {
        AnimesListResponse animesListResponse = new AnimesListResponse();
        try {
            String urlRequest = formatStringToList(listType, urlDetails, page);
            Document html = Jsoup.connect(urlRequest).get();

            String lastPage = (html.select("ul.pagination li.page-item.firLasLi a.page-link").attr("href"));
            Elements elements = html.select("div.col-6.col-sm-4.col-md-3.col-lg-2.mb-1.minWDanime.divCardUltimosEps");
            List<Anime> animesList = getListAnimes(elements);
            animesListResponse.setSuccess(true);
            animesListResponse.setLastPage(lastPage);
            animesListResponse.setAnimes(animesList);
        } catch (Exception e) {
            animesListResponse.setSuccess(false);
            animesListResponse.setMessage(e.getMessage());
        }
        return animesListResponse;
    }

    public static AnimeDetailsResponse getAnimeDetailsResponse(String urlDetails) {
        AnimeDetailsResponse animeDetailsResponse = new AnimeDetailsResponse();
        try {
            Anime anime = new Anime();
            Document html = Jsoup.connect(urlDetails).get();
            //TITLE
            Element elementTitle = html.selectXpath(".//h1[@class=\"quicksand400 mt-2 mb-0\"]").first();
            if (elementTitle != null) {
                anime.setTitle(elementTitle.text());
            }

            //POSTER
            String animePoster = html.select("div.sub_animepage_img").select("img").attr("data-src");
            anime.setPoster(animePoster);

            Elements elementsAnimeInfo = html.selectXpath(".//div[@class=\"animeInfo\"]");
            //GENRES
            try {
                for (Element element : elementsAnimeInfo.get(0).getAllElements()) {
                    anime.getGenres().add(new Genre(
                            element.select("a").text(),
                            element.select("a").attr("href")
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //SEASON
            anime.setSeason(getTextFromIndexAnimeInfo(elementsAnimeInfo, 1));
            //STUDIO
            anime.setStudio(getTextFromIndexAnimeInfo(elementsAnimeInfo, 2));
            //AUDIO
            anime.setAudio(getTextFromIndexAnimeInfo(elementsAnimeInfo, 3));
            //EPISODES COUT
            anime.setEpisodesCount(getTextFromIndexAnimeInfo(elementsAnimeInfo, 4));
            //STATUS
            anime.setStatus(getTextFromIndexAnimeInfo(elementsAnimeInfo, 5));
            //LAUNCH DAY
            anime.setLaunchDay(getTextFromIndexAnimeInfo(elementsAnimeInfo, 6));
            //YEAR
            anime.setYear(getTextFromIndexAnimeInfo(elementsAnimeInfo, 7));

            //SYNOPSIS
            Element elementSynopsis = html.selectXpath(".//div[@class=\"d-inline-block\"]").first();
            if (elementSynopsis != null) {
                anime.setSynopsis(elementSynopsis.select("span.spanAnimeInfo").text());
            }
            //EPISODES
            Elements elementsEpisodes = html.select("div.div_video_list a");

            List<Episode> episodes = new ArrayList<>();
            for (Element element : elementsEpisodes) {

                Episode episode = new Episode();

                String episodeTitle = element.text();
                String episodeUrlDetails = element.attr("href");

                episode.setTitle(episodeTitle);
                episode.setUrlDetails(episodeUrlDetails);

                episodes.add(episode);

            }
            anime.setEpisodes(episodes);

            //RELATED ANIMES
            anime.setRelatedAnimes(getListAnimes(html.selectXpath(".//article[@class=\"containerAnimes\"]")));

            animeDetailsResponse.setSuccess(true);
            animeDetailsResponse.setAnime(anime);

        } catch (Exception e) {
            animeDetailsResponse.setSuccess(false);
            animeDetailsResponse.setMessage(e.getMessage());
        }
        return animeDetailsResponse;
    }

    public static EpisodeResponse getEpisodeResponse(Episode episode) {
        EpisodeResponse episodeResponse = new EpisodeResponse();
        try {

            Document html = Jsoup.connect(episode.getUrlDetails()).get();
            Element elementTitle = html.selectXpath(".//h1[@itemprop=\"name\"]").first();
            if (elementTitle != null) {
                episode.setTitle(elementTitle.text());
            }

            Element elementBackdrop = html.selectXpath("//*[@id=\"div_video\"]/meta[2]").first();
            if (elementBackdrop != null)
                episode.setBackdrop(elementBackdrop.attr("content"));

            Element elementLink = html.selectXpath("//*[@id=\"main_div_video\"]/div[3]/iframe").first();
            if (elementLink != null) {
                Option option = new Option(elementLink.attr("src"), VideoQuality.empty, VideoType.blogger);
                episode.setOptions(Collections.singletonList(option));

            } else {
                elementLink = html.selectXpath("//*[@id=\"my-video\"]").first();
                if (elementLink != null) {
                    String requestLink = elementLink.attr("data-video-src");
                    html = Jsoup.connect(requestLink).ignoreContentType(true).get();
                    JSONObject object = new JSONObject(html.text());
                    try {
                        List<Option> options = new ArrayList<>();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String videoLink = obj.optString("src");
                            String label = obj.optString("label");

                            options.add(new Option(videoLink, VideoQuality.fromLabel(label), VideoType.mp4));

                        }
                        episode.setOptions(options);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            episodeResponse.setSuccess(true);
            episodeResponse.setEpisode(episode);

        } catch (IOException | JSONException e) {
            episodeResponse.setSuccess(false);
            episodeResponse.setMessage(e.getMessage());
        }
        return episodeResponse;
    }

    private static List<Genre> getListGenres(Elements elementsGenres) {
        List<Genre> genres = new ArrayList<>();
        try {
            for (Element element : elementsGenres.select("div.divDMLinks a.dropdown-item")) {
                String genreTitle = element.text();
                String genreUrlDetails = element.attr("href");
                genres.add(new Genre(genreTitle, genreUrlDetails));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }

    private static List<Anime> getListAnimes(Elements elements) {
        List<Anime> animes = new ArrayList<>();
        try {
            for (Element it : elements) {
                Anime anime = new Anime();
                anime.setTitle(it.select("a div.text-block h3.animeTitle").text());

                String animePoster = it.select("a img.card-img-top.lazy.imgAnimes").attr("data-src");
                if (animePoster.isEmpty()) {
                    animePoster = it.select("a img.img-fluid.lazy.imgAnimes").attr("data-src");
                }
                anime.setPoster(animePoster);

                anime.setRating(it.select("a div.text-block1 span.horaUltimosEps").text());
                anime.setUrlDetails(it.select("a").attr("href"));
                animes.add(anime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return animes;
    }

    private static String getTextFromIndexAnimeInfo(Elements elements, int index) {
        try {
            return elements.get(index).select("span.spanAnimeInfo").text();
        } catch (Exception e) {
            return "";
        }
    }

    private static String formatStringToList(ListType listType, String urlDetails, int page) {
        String urlFormat = urlDetails.endsWith("/") ? String.valueOf(page) : "/" + page;
        switch (listType) {
            default:
                return urlDetails.concat(urlFormat);
            case animesSearch:
                return BASE_URL.concat("/pesquisar/").concat(urlDetails) + "/" + page;
            case animesRelease:
                return BASE_URL.concat("/em-lancamento/") + page;
            case animesTop:
                return BASE_URL.concat("/top-animes/") + page;
            case animesLeg:
                return BASE_URL.concat("/lista-de-animes-legendados/") + page;
            case animesDub:
                return BASE_URL.concat("/lista-de-animes-dublados/") + page;
            case moviesDub:
                return BASE_URL.concat("/lista-de-filmes-dublados/") + page;
            case moviesLeg:
                return BASE_URL.concat("/lista-de-filmes-legendados/") + page;
            case seasonAutumn:
                return BASE_URL.concat("/temporada/outono/") + page;
            case seasonWinter:
                return BASE_URL.concat("/temporada/inverno/") + page;
            case seasonPrimavera:
                return BASE_URL.concat("/temporada/primavera/") + page;
            case seasonSummer:
                return BASE_URL.concat("/temporada/verao/") + page;
        }
    }

}
