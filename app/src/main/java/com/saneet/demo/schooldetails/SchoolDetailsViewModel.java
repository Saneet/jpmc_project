package com.saneet.demo.schooldetails;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.saneet.demo.data.SchoolRepository;
import com.saneet.demo.models.SchoolDetailsModel;
import com.saneet.demo.models.ScoreDetailsModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SchoolDetailsViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<SchoolDetailsModel> mutableDetails = new MutableLiveData<>();
    private final MutableLiveData<ScoreDetailsModel> mutableScores = new MutableLiveData<>();
    public LiveData<SchoolDetailsModel> details = mutableDetails;
    public LiveData<ScoreDetailsModel> scores = mutableScores;

    public void initialize(SchoolRepository repository, String schoolId) {
        disposables.add(
                repository.getSchoolDetails(schoolId).subscribeOn(Schedulers.io()).observeOn(
                                AndroidSchedulers.mainThread())
                        .subscribe(mutableDetails::setValue,
                                throwable -> {
                                    Log.e("SchoolDetailsViewModel",
                                            "Error occurred while fetching details of school.",
                                            throwable);
                                }));

        disposables.add(repository.getScoreDetails(schoolId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mutableScores::setValue, throwable -> {
                    Log.e("SchoolDetailsViewModel",
                            "Error occurred while fetching scores for school.", throwable);
                }));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
