package com.spartacus.solitude.match.create;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.SolitudeService;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchRequest;
import com.spartacus.solitude.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


public class CreateMatchViewModel extends ViewModel {

    public final ObservableList<PlayerViewModel> players = new ObservableArrayList<>();

    private final SolitudeService service;
    private final int tournamentId;
    private Subscription refreshPlayersSubscription;
    private Subscription createGameSubscription;
    private final View view;

    private int playerOneIndex;
    private int playerTwoIndex;

    private boolean isMatchCreating = false;

    public interface View {
        void showStartMatchDialog(Match match);
        void showPlayerFetchError();
        void showCreateMatchError();
    }

    public CreateMatchViewModel(View view, int tournamentId) {
        this.view = view;
        this.tournamentId = tournamentId;
        this.service = SolitudeApp.getInstance().getService();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshPlayers();
    }

    @Bindable
    public boolean isMatchCreating() {
        return isMatchCreating;
    }

    @Bindable
    public boolean isValid() {
        if (playerOneIndex == playerTwoIndex) {
            return false;
        }

        if (playerTwoIndex < 0 || playerTwoIndex >= players.size()) {
            return false;
        }

        if (playerOneIndex < 0 || playerOneIndex >= players.size()) {
            return false;
        }

        return true;
    }

    public void onPlayerOneSelected(int position) {
        this.playerOneIndex = position;
        notifyChange();
    }

    public void onPlayerTwoSelected(int position) {
        this.playerTwoIndex = position;
        notifyChange();
    }

    public void onCreateMatch() {
        if (createGameSubscription != null && !createGameSubscription.isUnsubscribed()) {
            createGameSubscription.unsubscribe();
        }

        Player playerOne = players.get(playerOneIndex).player;
        Player playerTwo = players.get(playerTwoIndex).player;

        this.isMatchCreating = true;
        notifyChange();

        MatchRequest request = new MatchRequest.Builder()
                .setPlayers(playerOne, playerTwo)
                .setTournamentId(tournamentId)
                .build();

        createGameSubscription = service.createMatch(request)
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Match>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showCreateMatchError();
                        isMatchCreating = false;
                        notifyChange();
                    }

                    @Override
                    public void onNext(Match match) {
                        view.showStartMatchDialog(match);
                        isMatchCreating = false;
                        notifyChange();
                    }
                });
    }

    public void refreshPlayers() {
        if (refreshPlayersSubscription != null && !refreshPlayersSubscription.isUnsubscribed()) {
            refreshPlayersSubscription.unsubscribe();
        }

        refreshPlayersSubscription = service.listPlayers(tournamentId)
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<Player>, List<PlayerViewModel>>() {
                    @Override
                    public List<PlayerViewModel> call(List<Player> players) {
                        List<PlayerViewModel> viewModels = new ArrayList<>();
                        for (Player player : players) {
                            viewModels.add(new PlayerViewModel(player));
                        }
                        return viewModels;
                    }
                })
                .subscribe(new Subscriber<List<PlayerViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showPlayerFetchError();
                    }

                    @Override
                    public void onNext(List<PlayerViewModel> players) {
                        CreateMatchViewModel.this.players.clear();
                        CreateMatchViewModel.this.players.addAll(players);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (refreshPlayersSubscription != null && !refreshPlayersSubscription.isUnsubscribed()) {
            refreshPlayersSubscription.unsubscribe();
        }

        if (createGameSubscription != null && !createGameSubscription.isUnsubscribed()) {
            createGameSubscription.unsubscribe();
        }
    }
}
