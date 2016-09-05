package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
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
import manoj.jek.go.com.contactsdemo.ui.ui.custom.SearchBar;
import manoj.jek.go.com.contactsdemo.ui.ui.custom.TextListener;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static android.view.View.GONE;

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
    @BindView(R.id.toolbar)
    public Toolbar _toolbar;
    @BindView(R.id.contact_search)
    public SearchBar _searchBar;

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
        initView();
        initSubscription();
    }

    private void initView() {
        _adapter = new ContactsViewAdapter(this, new ArrayList<Contact>());
        _adapter.setClickListener(new ContactsViewAdapter.ContactClickListener() {
            @Override
            public void onContactClicked(Contact contact, View rootView) {
                ContactInfoActivity.launch(contact, ContactsListActivity.this,
                        rootView.findViewById(R.id.contact_summary_pic), rootView.findViewById(R.id.contact_summary_name));
            }
        });
        _recyclerView.setAdapter(_adapter);
        //Need this to get recylcer view to work. Android is frustrating.
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.contact_list_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsListActivity.this, NewContactActivity.class));
            }
        });
        _fastScroller.setRecyclerView(_recyclerView);
        _recyclerView.addOnScrollListener(_fastScroller.getOnScrollListener());
        _fastScroller.setSectionIndicator((SectionTitleIndicator)findViewById(R.id.fast_scroller_section_title_indicator));
        _searchBar.addTextListener(new TextListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                _adapter.getFilter().filter(charSequence);
            }
        });
        setSupportActionBar(_toolbar);
    }

    private void initSubscription() {
        _subscription = _single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Contact>>() {
                    @Override
                    public void onSuccess(List<Contact> value) {
                        findViewById(R.id.progress_wheel).setVisibility(GONE);
                        _adapter.updateContacts(value);
                        _recyclerView.setAdapter(_adapter);
                        _recyclerView.invalidate();
                        if(value.size() == 0) {
                            findViewById(R.id.empty_view).setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(ContactsListActivity.this, "got error!!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        findViewById(R.id.progress_wheel).setVisibility(GONE);
                        showErrorView();
                    }
                });
    }

    private void showErrorView() {
        _errorView.setVisibility(View.VISIBLE);
        _retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _errorView.setVisibility(GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            findViewById(R.id.contact_search).setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
