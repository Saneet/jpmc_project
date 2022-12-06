package com.saneet.demo.network;

import static com.saneet.demo.network.Constants.ARG_LIMIT;
import static com.saneet.demo.network.Constants.ARG_OFFSET;
import static com.saneet.demo.network.Constants.ARG_WHERE;
import static com.saneet.demo.network.Constants.COL_ID;

import com.saneet.demo.models.SchoolDetailsModel;
import com.saneet.demo.models.SchoolPreviewModel;
import com.saneet.demo.models.ScoreDetailsModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("s3k6-pzi2.json?$SELECT=dbn,school_name,grades2018,neighborhood&$order=school_name")
    Single<List<SchoolPreviewModel>> searchSchools(
            @Query(ARG_WHERE) String searchQuery,
            @Query(ARG_LIMIT) int limit,
            @Query(ARG_OFFSET) int offset
    );

    @GET("s3k6-pzi2.json?$SELECT=dbn,school_name,grades2018,neighborhood&$order=school_name")
    Single<List<SchoolPreviewModel>> fetchAllSchoolsList(
            @Query(ARG_LIMIT) int limit,
            @Query(ARG_OFFSET) int offset
    );

    @GET("f9bf-2cp4.json")
    Single<List<ScoreDetailsModel>> fetchScoresForSchool(
            @Query(COL_ID) String schoolId
    );

    @GET("s3k6-pzi2.json")
    Single<List<SchoolDetailsModel>> fetchSchoolDetails(
            @Query(COL_ID) String schoolId
    );
}
