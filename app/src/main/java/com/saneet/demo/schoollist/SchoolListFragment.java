package com.saneet.demo.schoollist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.saneet.demo.DemoApplication;
import com.saneet.demo.R;
import com.saneet.demo.data.SchoolRepository;
import com.saneet.demo.models.SchoolPreviewModel;
import com.saneet.demo.schooldetails.SchoolDetailsFragment;

import javax.inject.Inject;

interface RecyclerViewEventListener {
    void onRecyclerViewItemClick(int position);
    void onScrolledToLoadItem();
}

public class SchoolListFragment extends Fragment implements RecyclerViewEventListener {
    private final SchoolListAdapter schoolListAdapter = new SchoolListAdapter(this);
    @Inject
    public SchoolRepository repository;
    private SchoolListViewModel viewModel;
    private ProgressBar progressBar;

    public static SchoolListFragment newInstance() {
        return new SchoolListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((DemoApplication) requireActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SchoolListViewModel.class);
        viewModel.setRepository(repository);
    }

    @Override
    public void onStart() {
        super.onStart();
        assignButtonClickListeners();
        initRecyclerView();
        viewModel.schoolsList.observe(this, schoolPreviewModels -> {
            schoolListAdapter.addItems(schoolPreviewModels, viewModel.getMoreItemsAvailable());
        });
        progressBar = requireView().findViewById(R.id.progress_bar);
        viewModel.clearAndShowLoading.observe(this, value -> {
            if (value) {
                schoolListAdapter.clearList();
                progressBar.setVisibility(View.VISIBLE);
            } else
                progressBar.setVisibility(View.GONE);
        });
        viewModel.fetchNext();
    }

    private void assignButtonClickListeners() {
        TextInputEditText searchText = requireView().findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.setSearchTerm(searchText.getText().toString().trim());
                        return true;
                    }
                    return false;
                });
        requireView().findViewById(R.id.clear_button).setOnClickListener(
                v -> {
                    searchText.setText(null);
                    viewModel.setSearchTerm(null);
                });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = requireView().findViewById(R.id.school_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(schoolListAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        SchoolPreviewModel school = schoolListAdapter.getItemAtIndex(position);
        Log.i(this.getClass().getSimpleName(), "Clicked school: " + school.getName());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        SchoolDetailsFragment.newInstance(school.getId(), school.getName()),
                        "details")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onScrolledToLoadItem() {
        viewModel.fetchNext();
    }

    @Override
    public void onDestroyView() {
        RecyclerView recyclerView = requireView().findViewById(R.id.school_list);
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }
}
