package com.saneet.demo.schooldetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.saneet.demo.DemoApplication;
import com.saneet.demo.R;
import com.saneet.demo.data.SchoolRepository;
import com.saneet.demo.models.SchoolDetailsModel;
import com.saneet.demo.models.ScoreDetailsModel;

import javax.inject.Inject;

public class SchoolDetailsFragment extends Fragment {
    private final static String ARG_SCHOOL_ID = "school_id";
    private final static String ARG_SCHOOL_NAME = "school_name";
    @Inject
    SchoolRepository repository;
    private SchoolDetailsViewModel viewModel;

    public static SchoolDetailsFragment newInstance(String schoolId, String schoolName) {
        SchoolDetailsFragment fragment = new SchoolDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SCHOOL_ID, schoolId);
        bundle.putString(ARG_SCHOOL_NAME, schoolName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school_details, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((DemoApplication) requireActivity().getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SchoolDetailsViewModel.class);
        String schoolId = null;
        if (getArguments() != null)
            schoolId = getArguments().getString(ARG_SCHOOL_ID);

        if (schoolId == null)
            requireActivity().getSupportFragmentManager().popBackStack();

        viewModel.initialize(repository, schoolId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.school_name)).setText(
                requireArguments().getString(ARG_SCHOOL_NAME));

        viewModel.details.observe(getViewLifecycleOwner(), this::assignDetails);
        viewModel.scores.observe(getViewLifecycleOwner(), this::assignScores);
    }

    private void assignScores(ScoreDetailsModel scores) {
        getTV(R.id.math).setText(String.format("%d", scores.getMath()));
        getTV(R.id.reading).setText(String.format("%d", scores.getReading()));
        getTV(R.id.writing).setText(String.format("%d", scores.getWriting()));
        getTV(R.id.sat).setText(String.format("%d", scores.getTotalSatTakers()));
    }

    private void assignDetails(SchoolDetailsModel details) {
        getTV(R.id.address).setText(
                String.format("%s, %s, %s", details.getAddress1(), details.getCity(),
                        details.getPostCode()));
        getTV(R.id.phone).setText(details.getPhone());
        getTV(R.id.website).setText(details.getWebsite());
        getTV(R.id.email).setText(details.getEmail());
    }

    private TextView getTV(@IdRes int resId) {
        return requireView().findViewById(resId);
    }
}
