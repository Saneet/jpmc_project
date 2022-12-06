package com.saneet.demo.data;

import static com.saneet.demo.network.Constants.COL_NAME;

import com.saneet.demo.models.SchoolDetailsModel;
import com.saneet.demo.models.SchoolPreviewModel;
import com.saneet.demo.models.ScoreDetailsModel;
import com.saneet.demo.network.NetworkService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class SchoolRepository {
    private final NetworkService networkService;

    @Inject
    public SchoolRepository(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Single<List<SchoolPreviewModel>> getSchoolsList(int numOfItems, int offset) {
        return networkService.fetchAllSchoolsList(numOfItems, offset);
    }

    public Single<List<SchoolPreviewModel>> searchSchools(String searchTerm,
                                                          int numOfItems,
                                                          int offset) {
        return networkService.searchSchools(createNameSearchQuery(searchTerm), numOfItems, offset);
    }

    public Single<SchoolDetailsModel> getSchoolDetails(String id) {
        return networkService.fetchSchoolDetails(id).flatMap(
                schoolDetailsModels -> {
                    if (schoolDetailsModels.size() == 0) {
                        return Single.error(new Exception("No schools returned from server."));
                    }
                    return Single.just(schoolDetailsModels.get(0));
                });
    }

    public Single<ScoreDetailsModel> getScoreDetails(String id) {
        return networkService.fetchScoresForSchool(id).flatMap(
                scoreDetailsModels -> {
                    if (scoreDetailsModels.size() == 0) {
                        return Single.error(new Exception("No scores returned from server."));
                    }
                    return Single.just(scoreDetailsModels.get(0));
                }
        );
    }

    private String createNameSearchQuery(String name) {
        return COL_NAME + " like '%" + name + "%'";
    }

}
