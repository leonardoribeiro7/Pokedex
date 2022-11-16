package com.example.pokedex.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pokedex.R;
import com.example.pokedex.fragments.PokemonListFragmentDirections.NavigateToPokemonDetailsFragment;
import com.example.pokedex.repository.ListRepository;
import com.example.pokedex.adapter.PokemonListAdapter;
import com.example.pokedex.databinding.FragmentPokemonListBinding;
import com.example.pokedex.viewModel.ListViewModel;

import org.jetbrains.annotations.NotNull;

public class PokemonListFragment extends Fragment implements PokemonListAdapter.OnItemClickListener {

    private FragmentPokemonListBinding fragmentPokemonListBinding;
    private PokemonListAdapter adapter;
    private ListRepository repository;
    private ListViewModel viewModel;
    private int offset;
    private boolean checking0, checking1, checking2;
    private boolean loading = true;
    private boolean isScrolledToEnd;
    private final String STATE_OFFSET = "Current Offset";
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";


    //View Declarations
    private ImageView arrowBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ListRepository();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPokemonListBinding = FragmentPokemonListBinding.inflate(getLayoutInflater());
        setUpRecyclerView();
        arrowBack.setOnClickListener(v -> {
            scrollUp(fragmentPokemonListBinding.getRoot());
        });

        viewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);

        return fragmentPokemonListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMoreOnRecyclerView();
        pullToRefresh();
        updateDataForUI();
        updateProgressBarForUI();
        updateSwipeRefreshLayoutForUI();
        updateToastForUI();


        if (savedInstanceState == null) {
            checking0 = true;
            checking1 = true;
            checking2 = true;
            offset = 0;
            fetchPokemonList(offset);
        } else {
            offset = savedInstanceState.getInt(STATE_OFFSET);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_OFFSET, offset);
    }

    private void setUpRecyclerView() {
        arrowBack = fragmentPokemonListBinding.scrollUpFab;
        fragmentPokemonListBinding.pokemonListRv.setHasFixedSize(true);
        adapter = new PokemonListAdapter(requireActivity(), this, new PokemonListAdapter.PokemonDiff());
        fragmentPokemonListBinding.pokemonListRv.setAdapter(adapter);
    }

    private void fetchPokemonList(int offset) {
        repository.fetchPokemonList(offset);
    }

    private void loadMoreOnRecyclerView() {
        fragmentPokemonListBinding.pokemonListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // dy > 0 = check for scroll down
                    if (loading && !recyclerView.canScrollVertically(1)) {
                        loading = false;
                        offset += 20;
                        fetchPokemonList(offset);
                    }
                }
            }
        });
    }

    public void scrollUp(View view) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) fragmentPokemonListBinding.pokemonListRv.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.smoothScrollToPosition(fragmentPokemonListBinding.pokemonListRv, null, 0);
        }
    }


    private void pullToRefresh() {
        fragmentPokemonListBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.clearAllOldData();
            offset = 0; // the offset need to be reset to 0 bcz
            fetchPokemonList(offset); // the loadMoreOnRecyclerView method stored the value of the offset from last time scrolled
        });
    }


    private void updateDataForUI() {
        viewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemonList -> {
            if (pokemonList != null) {
                adapter.refreshPokemonList(pokemonList);
            }
        });
    }

    private void updateProgressBarForUI() {
        viewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (checking0 && aBoolean != null) {
                if (aBoolean) {
                    fragmentPokemonListBinding.listProgressbar.setVisibility(View.VISIBLE);
                } else {
                    fragmentPokemonListBinding.listProgressbar.setVisibility(View.GONE);
                }
            } else {
                checking0 = true;
            }
        });
    }

    private void updateSwipeRefreshLayoutForUI() {
        viewModel.getSwipeRefreshLayoutLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking1 && aBoolean) {
                    loading = true;
                    fragmentPokemonListBinding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    checking1 = true;
                }
            }
        });
    }

    private void updateToastForUI() {
        viewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (string != null) {
                if (checking2) {
                    if (string.isEmpty()) {
                        Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
                    } else {
                        offset -= 20;
                        loading = true;
                        fragmentPokemonListBinding.swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checking2 = true;
                }
            }
        });
    }

    private void navigateToDetail(String name, String image) {
        NavigateToPokemonDetailsFragment action = PokemonListFragmentDirections.navigateToPokemonDetailsFragment(name, image);
        Navigation.findNavController(requireView()).navigate(action);
    }


    @Override
    public void onClick(String namePoke, String imagePoke) {
        navigateToDetail(namePoke, imagePoke);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.getDisposableToUnsubscribe();
    }
}