package com.spartacus.solitude.tournament;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.SolitudeService;
import com.spartacus.solitude.model.Tournament;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.match.create.CreateMatchActivity;
import com.spartacus.solitude.match.list.MatchListActivity;
import com.spartacus.solitude.utils.SubscriptionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class TournamentListViewModel extends ViewModel {

    public interface View {
        void showFetchError();
    }

    private final ObservableArrayList<ItemViewModel> items;
    private final SolitudeService service;
    private final View view;

    private Subscription subscription;
    private boolean refreshing;

    public TournamentListViewModel(View view) {
        this.service = SolitudeApp.getInstance().getService();
        this.items = new ObservableArrayList<>();
        this.view = view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshTournaments();
    }

    public void onRefresh() {
        refreshTournaments();
    }

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    private void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }

    private void refreshTournaments() {
        SubscriptionUtils.unsubscribe(subscription);

        setRefreshing(true);
        subscription = service.listTournaments()
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<Tournament>, List<ItemViewModel>>() {
                    @Override
                    public List<ItemViewModel> call(List<Tournament> tournaments) {
                        List<ItemViewModel> viewModels = new ArrayList<>();
                        for (Tournament tournament : tournaments) {
                            viewModels.add(new ItemViewModel(tournament));
                        }
                        return viewModels;
                    }
                })
                .subscribe(new Subscriber<List<ItemViewModel>>() {
                    @Override
                    public void onCompleted() {
                        setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefreshing(false);
                        view.showFetchError();
                    }

                    @Override
                    public void onNext(List<ItemViewModel> tournamentViewModels) {
                        items.clear();
                        items.addAll(tournamentViewModels);
                    }
                });
    }

    @Override
    public void onDestroy() {
        SubscriptionUtils.unsubscribe(subscription);
    }

    public ObservableArrayList<ItemViewModel> getItems() {
        return items;
    }

    public static class ItemViewModel extends BaseObservable {
        public final Tournament tournament;

        public ItemViewModel(Tournament tournament) {
            this.tournament = tournament;
        }

        @Bindable
        public String getName() {
            return tournament.getName();
        }

        public void onClick(Context context) {
            Intent intent = new Intent();

            if (tournament.isFullyConnected()) {
                intent.setClass(context, MatchListActivity.class)
                        .putExtra(MatchListActivity.TOURNAMENT_EXTRA, tournament);

            } else {
                intent.setClass(context, CreateMatchActivity.class)
                        .putExtra(CreateMatchActivity.TOURNAMENT_EXTRA, tournament);

            }

            context.startActivity(intent);
        }
    }
}
