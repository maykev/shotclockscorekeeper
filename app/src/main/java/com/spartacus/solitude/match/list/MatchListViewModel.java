package com.spartacus.solitude.match.list;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.SolitudeService;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.model.Match;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MatchListViewModel extends ViewModel {

    public final ObservableArrayList<ItemViewModel> items;

    private final SolitudeService service;
    private final int tournamentId;
    private final String matchStatus;
    private Subscription subscription;
    private boolean refreshing;
    private final View view;

    public interface View {
        void showMatchFetchError();
        void showMatchDialog(Match match);
    }

    public MatchListViewModel(View view, int tournamentId, String matchStatus) {
        this.view = view;
        this.tournamentId = tournamentId;
        this.items = new ObservableArrayList<>();
        this.matchStatus = matchStatus;
        this.service = SolitudeApp.getInstance().getService();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMatches();
    }

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    private void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }

    public void onRefresh() {
        refreshMatches();
    }

    private void refreshMatches() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        setRefreshing(true);
        subscription = service.listMatches(tournamentId, matchStatus)
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<Match>, List<ItemViewModel>>() {
                    @Override
                    public List<ItemViewModel> call(List<Match> matches) {
                        List<ItemViewModel> viewModels = new ArrayList<>();
                        for (Match match : matches) {
                            viewModels.add(new ItemViewModel(match));
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
                        view.showMatchFetchError();
                    }

                    @Override
                    public void onNext(List<ItemViewModel> matchItemViewModels) {
                        items.clear();
                        items.addAll(matchItemViewModels);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public class ItemViewModel extends BaseObservable {
        public final Match match;

        public ItemViewModel(Match match) {
            this.match = match;
        }

        public String getName() {
            return match.getPlayers().get(0).getName() + " vs " + match.getPlayers().get(1).getName();
        }

        public void onClick() {
            view.showMatchDialog(match);
        }
    }
}
