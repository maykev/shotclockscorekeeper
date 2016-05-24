package com.spartacus.solitude.match;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.spartacus.solitude.BR;
import com.spartacus.solitude.R;
import com.spartacus.solitude.SolitudeApp;
import com.spartacus.solitude.SolitudeService;
import com.spartacus.solitude.databinding.ViewModel;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.MatchUpdate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StartMatchViewModel extends ViewModel {

    public final AdapterView.OnItemSelectedListener tableChangeListener;
    public final ArrayAdapter<Integer> tableAdapter;
    private final Match match;
    private final Listener listener;
    private final SolitudeService service;
    private Subscription subscription;

    private boolean isTablesRefreshing;
    private boolean isMatchStarting;
    public int table = -1;

    @Bindable
    public boolean isMatchStarting() {
        return isMatchStarting;
    }

    public void setMatchStarting(boolean isMatchStarting) {
        this.isMatchStarting = isMatchStarting;
        notifyPropertyChanged(BR.matchStarting);
    }

    @Bindable
    public boolean isTablesRefreshing() {
        return isTablesRefreshing;
    }

    public void setTablesRefreshing(boolean isTablesRefreshing) {
        this.isTablesRefreshing = isTablesRefreshing;
        notifyPropertyChanged(BR.matchCreating);
    }

    public interface Listener {
        void onStartMatch(Match match, int table);
        void onToast(String toast);
    }

    public StartMatchViewModel(Context context, Listener listener, final Match match) {
        this.match = match;
        this.listener = listener;
        this.service = SolitudeApp.getInstance().getService();
        this.tableAdapter = new ArrayAdapter<>(context, R.layout.item_table);
        this.tableChangeListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table = tableAdapter.getItem(position);
                notifyPropertyChanged(BR.valid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                table = -1;
                notifyPropertyChanged(BR.valid);
            }
        };

        refreshTables();
    }

    @Bindable
    public String getPlayerOneName() {
        return match.getPlayerOne().getName();
    }

    @Bindable
    public String getPlayerTwoName() {
        return match.getPlayerTwo().getName();
    }

    private void refreshTables() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        setTablesRefreshing(true);

        subscription = service.listTables(match.getTournamentId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(new Subscriber<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        setTablesRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setTablesRefreshing(false);
                    }

                    @Override
                    public void onNext(List<Integer> tables) {
                        tableAdapter.clear();
                        tableAdapter.addAll(tables);
                    }
                });
    }

    public void onStartGame(View view) {
        setMatchStarting(true);

        MatchUpdate matchUpdate = new MatchUpdate.Builder()
                .setMatchFinished(false)
                .setPlayerScore(match.getPlayerOne(), match.getPlayerOne().getGamesOnTheWire())
                .setPlayerScore(match.getPlayerTwo(), match.getPlayerTwo().getGamesOnTheWire())
                .setTable(table)
                .build();


        service.updateMatch(match.getId(), matchUpdate)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        setMatchStarting(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setMatchStarting(false);
                        listener.onToast("Table already taken");
                        refreshTables();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        listener.onStartMatch(match, table);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Bindable
    public boolean isValid() {
        return table != -1;
    }
}
