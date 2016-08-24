package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.features.ContactsFeature;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.models.ContactSummary;
import manoj.jek.go.com.contactsdemo.ui.network.ContactsService;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import manoj.jek.go.com.contactsdemo.ui.ui.adapters.ContactsViewAdapter;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class ContactsListActivity extends AppCompatActivity {

    private ContactsService _restClient;
    private Single<List<Contact>> _single;
    private Subscription _subscription;
    private ContactsViewAdapter _adapter;

    @BindView(R.id.recycler_view)
    public RecyclerView _recyclerView;
    @BindView(R.id.error_view)
    public View _errorView;
    @BindView(R.id.retry)
    public Button _retryButton;
    @BindView(R.id.fast_scroller)
    public VerticalRecyclerViewFastScroller _fastScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _restClient = Utils.getContactsService();
        _single = Single.fromCallable(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                if(_adapter == null || _adapter.getItemCount() == 0) findViewById(R.id.progress_wheel).setVisibility(View.VISIBLE);
                return ContactsFeature.getInstance().fetchAllContactsSync(ContactsListActivity.this);
            }
        });

        _adapter = new ContactsViewAdapter(this, new ArrayList<Contact>());
        _recyclerView.setAdapter(_adapter);
        //Need this to get recylcer view to work. Android is frustrating.
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initSubscription();
        findViewById(R.id.contact_list_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsListActivity.this, NewContactActivity.class));
            }
        });
        _fastScroller.setRecyclerView(_recyclerView);
        _recyclerView.addOnScrollListener(_fastScroller.getOnScrollListener());
        _fastScroller.setSectionIndicator((SectionTitleIndicator)findViewById(R.id.fast_scroller_section_title_indicator));
    }

    private void initSubscription() {
        _subscription = _single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Contact>>() {
                    @Override
                    public void onSuccess(List<Contact> value) {
                        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                        _adapter.updateContacts(value);
                        _recyclerView.setAdapter(_adapter);
                        _recyclerView.invalidate();
                        if(value.size() == 0) {
                            findViewById(R.id.empty_view).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(ContactsListActivity.this, "got error!!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                        showErrorView();
                    }
                });
    }

    private void showErrorView() {
        _errorView.setVisibility(View.VISIBLE);
        _retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _errorView.setVisibility(View.GONE);
                initSubscription();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        _subscription.unsubscribe();
        super.onStop();
    }

}
