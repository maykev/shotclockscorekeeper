package com.spartacus.solitude.match.list;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.spartacus.solitude.R;
import com.spartacus.solitude.model.Match;
import com.spartacus.solitude.model.Tournament;

public class MatchListActivity extends AppCompatActivity {
    public static final String TOURNAMENT_EXTRA = "TOURNAMENT_EXTRA";

    private SectionsPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        tournament = getIntent() == null ? null : (Tournament) getIntent().getParcelableExtra(TOURNAMENT_EXTRA);
        if (tournament == null) {
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setTitle(tournament.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchListFragment.newInstance(tournament.getId(), Match.STATUS_CREATED);
                case 1:
                    return MatchListFragment.newInstance(tournament.getId(), Match.STATUS_IN_PROGRESS);
                case 2:
                    return MatchListFragment.newInstance(tournament.getId(), Match.STATUS_FINISHED);
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Created";
                case 1:
                    return "In Progress";
                case 2:
                    return "Finished";
            }

            return null;
        }
    }
}
