package com.saneet.demo.schoollist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.saneet.demo.data.SchoolRepository;
import com.saneet.demo.models.SchoolPreviewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SchoolListViewModel extends ViewModel {
    private static final int DEFAULT_NUMBER_OF_ITEMS_TO_FETCH = 15;

    private final MutableLiveData<List<SchoolPreviewModel>> mutableSchoolList =
            new MutableLiveData<>();
    private final MutableLiveData<Boolean> mutableShowLoading = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    public LiveData<List<SchoolPreviewModel>> schoolsList = mutableSchoolList;
    public LiveData<Boolean> clearAndShowLoading = mutableShowLoading;
    private SchoolRepository repository;
    private int numberOfItemsToFetch = DEFAULT_NUMBER_OF_ITEMS_TO_FETCH;
    private int currentOffset = 0;
    private volatile boolean isFetching = false;
    private String searchTerm = null;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private volatile boolean moreItemsAvailable = true;

    public void setRepository(SchoolRepository repository) {
        this.repository = repository;
    }

    public void setSearchTerm(String term) {
        if (term != null && term.isEmpty()) {
            term = null;
        }

        if (!Objects.equals(searchTerm, term)) {
            searchTerm = term;
            startNewSearch();
        }
    }

    private void startNewSearch() {
        currentOffset = 0;
        disposables.clear();
        isFetching = false;
        mutableShowLoading.setValue(true);
        fetchNext();
    }

    public synchronized void fetchNext() {
        if (isFetching) return;

        isFetching = true;

        Single<List<SchoolPreviewModel>> observable;
        if (searchTerm != null)
            observable = repository.searchSchools(searchTerm, numberOfItemsToFetch, currentOffset);
        else
            observable = repository.getSchoolsList(numberOfItemsToFetch, currentOffset);

        disposables.add(observable.subscribeOn(Schedulers.from(executor))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelList -> {
                    synchronized (this) {
                        if (modelList.size() == 0) {
                            moreItemsAvailable = false;
                        } else {
                            currentOffset += modelList.size() + 1;
                            boolean itemsFinished = modelList.size() < numberOfItemsToFetch;
                            moreItemsAvailable = !itemsFinished;
                            mutableSchoolList.setValue(modelList);
                            mutableShowLoading.setValue(false);
                            isFetching = false;
                        }
                    }
                }, throwable -> {
                    Log.e(this.getClass().getSimpleName(), "Error while fetching models",
                            throwable);
                }));

    }

    public boolean getMoreItemsAvailable() {
        return moreItemsAvailable;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
