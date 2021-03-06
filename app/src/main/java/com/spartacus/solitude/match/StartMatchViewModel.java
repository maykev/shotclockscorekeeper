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
import com.spartacus.solitude.utils.SubscriptionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StartMatchViewModel extends ViewModel {

    public final AdapterView.OnItemSelectedListener tableChangeListener;
    public final ArrayAdapter<Integer> tableAdapter;
    private final Match match;
    private final Listener listener;
    private final SolitudeService service;

    private Subscription refreshTablesSubscription;
    private Subscription startGameSubscription;

    private boolean isTablesRefreshing;
    private boolean isMatchStarting;
    public int tableIndex = -1;

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
        notifyPropertyChanged(BR.tablesRefreshing);
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
                tableIndex = position;
                notifyPropertyChanged(BR.valid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tableIndex = -1;
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
        SubscriptionUtils.unsubscribe(refreshTablesSubscription);

        setTablesRefreshing(true);

        refreshTablesSubscription = service.listTables(match.getTournamentId())
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
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
                        List<Integer> newTables = new ArrayList<>(tables);

                        if (Match.STATUS_IN_PROGRESS.equals(match.getStatus()) && match.getTable() != null) {
                            newTables.remove(match.getTable());
                            newTables.add(0, match.getTable());
                        }

                        tableAdapter.clear();
                        tableAdapter.addAll(newTables);
                    }
                });
    }

    public void onStartGame(View view) {
        SubscriptionUtils.unsubscribe(startGameSubscription);

        setMatchStarting(true);

        final int table = tableAdapter.getItem(tableIndex);

        MatchUpdate matchUpdate = new MatchUpdate.Builder()
                .setStatus(Match.STATUS_IN_PROGRESS)
                .setTable(table)
                .setPlayerScore(match.getPlayerOne(), match.getPlayerOne().getGamesOnTheWire())
                .setPlayerScore(match.getPlayerTwo(), match.getPlayerTwo().getGamesOnTheWire())
                .build();

        startGameSubscription = service.updateMatch(match.getId(), matchUpdate)
                .subscribeOn(SolitudeApp.getInstance().getBackgroundScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        setMatchStarting(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onToast("Table already taken");
                        refreshTables();
                        setMatchStarting(false);
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

        SubscriptionUtils.unsubscribe(startGameSubscription, refreshTablesSubscription);
    }

    @Bindable
    public boolean isValid() {
        return tableIndex != -1;
    }
}
