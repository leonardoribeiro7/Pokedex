package com.example.pokedex.fragments;

import static com.example.pokedex.util.Constants.OFFLINE_MODE;
import static com.example.pokedex.util.Constants.STATE_OFFSET;

import android.os.Bundle;

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

import com.example.pokedex.fragments.PokemonListFragmentDirections.NavigateToPokemonDetailsFragment;
import com.example.pokedex.repository.ListRepository;
import com.example.pokedex.adapter.PokemonListAdapter;
import com.example.pokedex.databinding.FragmentPokemonListBinding;
import com.example.pokedex.util.LoadingDialog;
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
    private final LoadingDialog loadingDialog = new LoadingDialog();


    //View Declarations
    private ImageView arrowBack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ListRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPokemonListBinding = FragmentPokemonListBinding.inflate(getLayoutInflater());
        setUpRecyclerView();
        arrowBack.setOnClickListener(v -> scrollUp(fragmentPokemonListBinding.getRoot()));

        viewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);

        return fragmentPokemonListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //calling the functions
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
        //setting the adapter and the arrowBack button
        arrowBack = fragmentPokemonListBinding.scrollUpFab;
        fragmentPokemonListBinding.pokemonListRv.setHasFixedSize(true);
        adapter = new PokemonListAdapter(requireActivity(), this, new PokemonListAdapter.PokemonDiff());
        fragmentPokemonListBinding.pokemonListRv.setAdapter(adapter);
    }

    //Function to get the list of pokemon using repository IO thread functions
    private void fetchPokemonList(int offset) {
        repository.fetchPokemonList(offset);
    }

    private void loadMoreOnRecyclerView() {
        //scroll listener if the user passes the 20 pokemon
        fragmentPokemonListBinding.pokemonListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // dy > 0 = check for scroll down
                    if (loading && !recyclerView.canScrollVertically(1)) {
                        //starts the loading
                        loadingDialog.displayLoading(requireContext(), false);
                        loading = false;
                        //adds more 20 pokemon
                        offset += 20;
                        //does the request
                        fetchPokemonList(offset);
                    }
                }
            }
        });
    }

    public void scrollUp(View view) {
        //scrollUP_fab function to get back to the top of the list
        LinearLayoutManager layoutManager = (LinearLayoutManager) fragmentPokemonListBinding.pokemonListRv.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.smoothScrollToPosition(fragmentPokemonListBinding.pokemonListRv, null, 0);
        }
    }


    private void pullToRefresh() {
        //refresh the page function
        fragmentPokemonListBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            //clears the adapter
            adapter.clearAllOldData();
            offset = 0; // resets the offset
            fetchPokemonList(offset); // makes the request again
        });
    }


    private void updateDataForUI() {
        //if the list changes it refreshes
        viewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemonList -> {
            if (pokemonList != null) {
                adapter.refreshPokemonList(pokemonList);
            }
        });
    }

    private void updateProgressBarForUI() {
        //observable for the progressbar
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
        //if the checking1 equals true it doesn't refresh if its false refreshes and sets the checking1 true
        viewModel.getSwipeRefreshLayoutLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking1 && aBoolean) {
                    loading = true;
                    fragmentPokemonListBinding.swipeRefreshLayout.setRefreshing(false);
                    loadingDialog.hideLoading();
                } else {
                    checking1 = true;
                }
            }
        });
    }

    private void updateToastForUI() {
        // if the user tries scroll down the app without using internet
        viewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (string != null) {
                if (checking2) {
                    if (string.isEmpty()) {
                        //sends the message of offlineMode
                        Toast.makeText(requireActivity(), OFFLINE_MODE, Toast.LENGTH_SHORT).show();
                    } else {
                        //doesn't do the request for more pokemon
                        offset -= 20;
                        loading = true;
                        fragmentPokemonListBinding.swipeRefreshLayout.setRefreshing(false);
                        loadingDialog.hideLoading();
                        Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checking2 = true;
                }
            }
        });
    }

    private void navigateToDetail(String name, String image) {
        /*Navigate one fragment to other using the Navigation Resource
         and it passes the name and the image using navigation Args
         */
        NavigateToPokemonDetailsFragment action = PokemonListFragmentDirections.navigateToPokemonDetailsFragment(name, image);
        Navigation.findNavController(requireView()).navigate(action);
    }


    @Override
    public void onClick(String namePoke, String imagePoke) {
        //if the users clicks on the pokemon it navigates and passes the data of the same
        navigateToDetail(namePoke, imagePoke);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.getDisposableToUnsubscribe();
    }
}