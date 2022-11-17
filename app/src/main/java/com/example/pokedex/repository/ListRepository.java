package com.example.pokedex.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pokedex.App;
import com.example.pokedex.api.APIClient;
import com.example.pokedex.database.PokemonListDAO;
import com.example.pokedex.model.PokemonList.PokemonCallback;
import com.example.pokedex.model.PokemonList.PokemonResponse;
import com.example.pokedex.util.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@ActivityScoped
public class ListRepository {

    //Injections
    @Inject
    APIClient apiClient;
    @Inject
    PokemonListDAO pokemonListDAO;

    //Declarations
    private final List<PokemonResponse> pokemonList = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static MutableLiveData<List<PokemonResponse>> pokemonListLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> progressBarLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> swipeRefreshLayoutLiveData = new MutableLiveData<>();
    private final static MutableLiveData<String> toastLiveData = new MutableLiveData<>();
    private final LoadingDialog loadingDialog = new LoadingDialog();

    public ListRepository() {
        getInjection();
    }

    public void fetchPokemonList(int offset) {
        progressBarLiveData.setValue(true);

        Observable<PokemonCallback> pokemonListAPIObservable = apiClient.observableFetchPokemonList(offset);
        DisposableObserver<PokemonCallback> pokemonListAPIObserver = getPokemonListAPIObserver(offset);

        Disposable disposableFetchData = pokemonListAPIObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(pokemonListAPIObserver);

        compositeDisposable.add(disposableFetchData);
    }

    private DisposableObserver<PokemonCallback> getPokemonListAPIObserver(int offset) {
        return new DisposableObserver<PokemonCallback>() {

            @Override
            public void onNext(@NonNull PokemonCallback pokemonCallback) {
                onResponseSuccess(pokemonCallback, offset);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onResponseFail(e, offset);
            }

            @Override
            public void onComplete() {

            }
        };

    }

    private void onResponseSuccess(PokemonCallback pokemonCallback, int offset) {
        pokemonList.clear();

        List<PokemonResponse> resultsList = pokemonCallback.getResults();

        for (int i = 0; i < resultsList.size(); i++) {
            PokemonResponse result = resultsList.get(i);

            String namePoke = result.getName();

            int id = result.getNumber();

            String urlPoke = result.getUrl().replaceFirst(".$", "").substring(33);

            pokemonList.add(new PokemonResponse(offset, id, namePoke, urlPoke));
        }

        progressBarLiveData.setValue(false);
        pokemonListLiveData.setValue(pokemonList);
        swipeRefreshLayoutLiveData.setValue(true);
        onInsertPokemonListIntoDatabase(pokemonList);
    }

    private void onResponseFail(Throwable e, int offset) {
        progressBarLiveData.setValue(false);

        if (pokemonListDAO.getPokemonList(offset).isEmpty()) {
            toastLiveData.setValue(e.getMessage());
        } else {
            pokemonListLiveData.setValue(pokemonListDAO.getPokemonList(offset));
            swipeRefreshLayoutLiveData.setValue(true);
            toastLiveData.setValue("");
        }
    }

    private void onInsertPokemonListIntoDatabase(List<PokemonResponse> pokemonList) {
        Observable<List<PokemonResponse>> observable = Observable.just(pokemonList);

        //Insert the pokemonList in to the Database
        Disposable disposableInsertData = observable
                .doOnNext(resultsResponses -> pokemonListDAO.insertPokemonList(resultsResponses))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableInsertData);
    }


    private void getInjection() {
        App.getAppComponent().injectMainRepository(this);
    }

    public static LiveData<List<PokemonResponse>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    public static LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public static LiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return swipeRefreshLayoutLiveData;
    }

    public static LiveData<String> getToastLiveData() {
        return toastLiveData;
    }

    public void resetValuesLiveData() {
        pokemonListLiveData.postValue(null);
        progressBarLiveData.postValue(null);
        swipeRefreshLayoutLiveData.postValue(null);
        toastLiveData.postValue(null);
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }

}
