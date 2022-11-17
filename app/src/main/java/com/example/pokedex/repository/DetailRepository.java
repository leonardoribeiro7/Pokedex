package com.example.pokedex.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pokedex.App;
import com.example.pokedex.api.APIClient;
import com.example.pokedex.database.PokemonInfoDAO;
import com.example.pokedex.model.PokemonInfo.AbilityResponse;
import com.example.pokedex.model.PokemonInfo.PokemonInfoResponse;
import com.example.pokedex.model.PokemonInfo.TypesResponse;
import com.example.pokedex.util.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailRepository {

    //Injections
    @Inject
    APIClient apiClient;
    @Inject
    PokemonInfoDAO pokemonInfoDAO;

    //Declarations
    private final List<TypesResponse> typesResponses = new ArrayList<>();
    private final List<AbilityResponse> abilityResponses = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static MutableLiveData<PokemonInfoResponse> pokemonInfoLiveData = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> progressBarLiveData = new MutableLiveData<>();
    private final static MutableLiveData<String> toastLiveData = new MutableLiveData<>();

    public DetailRepository() {
        getInjection();
    }

    public void fetchPokemonInfo(String namePoke) {
        progressBarLiveData.setValue(true);

        Observable<PokemonInfoResponse> pokemonInfoAPIObservable = apiClient.observableFetchPokemonInfo(namePoke);
        DisposableObserver<PokemonInfoResponse> pokemonInfoAPIObserver = getPokemonInfoAPIObserver(namePoke);

        Disposable disposableFetchData = pokemonInfoAPIObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(pokemonInfoAPIObserver);

        compositeDisposable.add(disposableFetchData);
    }

    private DisposableObserver<PokemonInfoResponse> getPokemonInfoAPIObserver(String namePoke) {
        return new DisposableObserver<PokemonInfoResponse>() {


            @Override
            public void onNext(@NonNull PokemonInfoResponse pokemonInfoResponse) {
                onResponseSuccess(pokemonInfoResponse, namePoke);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onResponseFail(e, namePoke);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void onResponseSuccess(PokemonInfoResponse pokemonInfoResponse, String namePoke) {

        abilityResponses.clear();
        typesResponses.clear();

        //Get name of types Pokemon and Color Types
        List<TypesResponse> typesList = pokemonInfoResponse.getTypes();

        List<AbilityResponse> abilityList = pokemonInfoResponse.getAbility();

        for (int i = 0, a = 0; i < typesList.size() && a < abilityList.size(); i++, a++) {
            TypesResponse type = typesList.get(i);

            AbilityResponse ability = abilityList.get(i);

            String abilityName = ability.getAbility().getName();

            String nameType = type.getType().getName();

            abilityResponses.add(new AbilityResponse(abilityName));

            typesResponses.add(new TypesResponse(nameType));
        }

        PokemonInfoResponse pokemonInfo = new PokemonInfoResponse(namePoke, typesResponses, abilityResponses);

        progressBarLiveData.setValue(false);
        pokemonInfoLiveData.setValue(pokemonInfo);
        onInsertPokemonInfoIntoDatabase(pokemonInfo);
    }

    private void onResponseFail(Throwable e, String namePoke) {
        progressBarLiveData.setValue(false);

        if (pokemonInfoDAO.getPokemonInfo(namePoke) == null) {
            toastLiveData.setValue(e.getMessage());
        } else {
            pokemonInfoLiveData.setValue(pokemonInfoDAO.getPokemonInfo(namePoke));
            toastLiveData.setValue("");
        }
    }

    private void onInsertPokemonInfoIntoDatabase(PokemonInfoResponse pokemonInfo) {
        Observable<PokemonInfoResponse> observable = Observable.just(pokemonInfo);

        Disposable disposableInsertData = observable
                .doOnNext(pokemonInfoAPI -> pokemonInfoDAO.insertPokemonInfo(pokemonInfoAPI))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableInsertData);
    }

    private void getInjection() {
        App.getAppComponent().injectDetailRepository(this);
    }

    public static LiveData<PokemonInfoResponse> getPokemonInfoLiveData() {
        return pokemonInfoLiveData;
    }

    public static LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public static LiveData<String> getToastLiveData() {
        return toastLiveData;
    }

    public void resetValuesLiveData() {
        pokemonInfoLiveData.postValue(null);
        progressBarLiveData.postValue(null);
        toastLiveData.postValue(null);
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }
}
