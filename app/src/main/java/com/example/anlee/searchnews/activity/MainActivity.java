package com.example.anlee.searchnews.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.anlee.searchnews.R;
import com.example.anlee.searchnews.adapter.ArticleAdapter;
import com.example.anlee.searchnews.api.ArticleApi;
import com.example.anlee.searchnews.model.Article;
import com.example.anlee.searchnews.model.SearchRequest;
import com.example.anlee.searchnews.model.SearchResult;
import com.example.anlee.searchnews.utils.RetrofitUtils;
import com.victor.loading.rotate.RotateLoading;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class MainActivity extends AppCompatActivity {
    private static SearchRequest searchRequest;
    private static SearchRequest stateSearchRequest;
    private static ArticleApi articleApi;
    private SearchView searchView;
    private ArticleAdapter articleAdapter;

    private EditText edtDateTime;
    private Spinner spinnerOrder;
    private CheckBox checkArt;
    private CheckBox checkFashionStyle;
    private CheckBox checkSport;

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;

    @BindView(R.id.pbLoading)
    RotateLoading pbLoading;

    @BindView(R.id.pbLoadMore)
    RotateLoading pbLoadMore;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    private interface Listener {
        void onResult(SearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpView();
        setUpApi();
        if (savedInstanceState == null) {
            search();
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setUpView() {
        articleAdapter = new ArticleAdapter(this);
        rvArticle.setAdapter(articleAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, VERTICAL);
        rvArticle.setLayoutManager(layoutManager);
        // Set up toolbar.
        toolBar.setTitle(R.string.search_news);
        setSupportActionBar(toolBar);
        // Set up load more feature
        articleAdapter.setListener(new ArticleAdapter.Listener() {
            @Override
            public void onLoadMore() {
                searchMore();
            }
        });
        setUpPullToRefresh();
    }

    private void setUpPullToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setUpApi() {
        searchRequest = new SearchRequest();
        articleApi = RetrofitUtils.create().create(ArticleApi.class);
    }

    private void search() {
        pbLoading.start();
        searchRequest.resetPage();
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                articleAdapter.setData(searchResult.getArticles());
//                rvArticle.scrollToPosition(0);
            }
        });
    }

    // Load more data
    private void searchMore() {
        searchRequest.nextPage();
        pbLoadMore.start();
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                articleAdapter.appendData(searchResult.getArticles());
            }
        });
    }

    private void fetchArticles(final Listener listener) {
        if (isNetworkAvailable() && isOnline()) {
            articleApi.search(searchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    if (response.body() != null) {
                        listener.onResult(response.body());
                    }
                    handleComplete();
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "So sorry, please wait a minute...!!", Toast.LENGTH_SHORT).show();
                    fetchArticles(listener);
                    Log.e("MainActivity", t.getMessage());
                }
            });
        } else {
            handleComplete();
            Toast.makeText(this, "NO INTERNET !!", Toast.LENGTH_LONG).show();
        }
    }

    private void handleComplete() {
        pbLoading.stop();
        pbLoadMore.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setUpSearchView(menu.findItem(R.id.action_search));
        return super.onCreateOptionsMenu(menu);
    }

    // Bind search text
    private void setUpSearchView(MenuItem item) {
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRequest.setQuery(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            showSortMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    //Show Sort Dialog
    private void showSortMenu() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setSearchRequest();
                        search();
                    }
                })
                .customView(R.layout.sort_dialog, true)
                .cancelable(true)
                .build();
        setUpSortMenu(dialog);
        dialog.show();
    }

    //Set values to search request.
    private void setSearchRequest() {
        if (edtDateTime != null && edtDateTime.getText().length() > 0) {
            searchRequest.setBeginDate(edtDateTime.getText().toString());
        }

        if (spinnerOrder != null) {
            searchRequest.setOrder(spinnerOrder.getSelectedItem().toString());
        }

        if (checkArt.isChecked()) {
            searchRequest.setHasArts(true);
        } else {
            searchRequest.setHasArts(false);
        }

        if (checkFashionStyle.isChecked()) {
            searchRequest.setHasFashionAndStyle(true);
        } else {
            searchRequest.setHasFashionAndStyle(false);
        }

        if (checkSport.isChecked()) {
            searchRequest.setHasSport(true);
        } else {
            searchRequest.setHasSport(false);
        }
    }

    private void setUpSortMenu(MaterialDialog dialog) {
        stateSearchRequest = searchRequest;
        setupDateTime(dialog);
        setUpSpinnerOrder(dialog);

        checkArt = (CheckBox) dialog.findViewById(R.id.checkArts);
        if (stateSearchRequest != null && stateSearchRequest.isHasArts()) {
            checkArt.setChecked(true);
        }
        checkFashionStyle = (CheckBox) dialog.findViewById(R.id.checkFashionStyle);
        if (stateSearchRequest != null && stateSearchRequest.isHasFashionAndStyle()) {
            checkFashionStyle.setChecked(true);
        }
        checkSport = (CheckBox) dialog.findViewById(R.id.checkSports);
        if (stateSearchRequest != null && stateSearchRequest.isHasSport()) {
            checkSport.setChecked(true);
        }
    }

    private void setUpSpinnerOrder(MaterialDialog dialog) {
        spinnerOrder = (Spinner) dialog.findViewById(R.id.spinnerOrder);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(dialog.getContext(),
                R.array.sort_order, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(priorityAdapter);
        spinnerOrder.setSelection(0);
        if (stateSearchRequest != null && stateSearchRequest.getOrder().equalsIgnoreCase("oldest")) {
            spinnerOrder.setSelection(1);
        }
    }

    private void setupDateTime(MaterialDialog dialog) {
        edtDateTime = (EditText) dialog.findViewById(R.id.edtDateTime);
        if (stateSearchRequest.getBeginDate() != null) {
            edtDateTime.setText(stateSearchRequest.getBeginDate());
        }
        edtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    public void openDatePicker() {
        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        edtDateTime.setText(formatDate(year, monthOfYear, dayOfMonth));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(now);
        dpd.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private String formatDate(int year, int monthOfYear, int dayOfMonth) {
        String day = "" + dayOfMonth;
        int m = monthOfYear + 1;
        String month = "" + m;
        if (m < 10) {
            month = "0" + month;
        }
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        }
        String date = day + "/" + month + "/" + year;
        return date;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        articleAdapter.setState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<Article> stateArticleList = articleAdapter.getStateList(savedInstanceState);
        articleAdapter.setData(stateArticleList);
    }

}
