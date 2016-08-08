package manoj.jek.go.com.contactsdemo.ui.activities;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.models.ContactSummary;
import manoj.jek.go.com.contactsdemo.ui.network.ContactsService;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import manoj.jek.go.com.contactsdemo.ui.ui.adapters.ContactsViewAdapter;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactsListActivity extends AppCompatActivity {

    private ContactsService _restClient;
    private Single<List<Contact>> _single;
    private Subscription _subscription;
    private ContactsViewAdapter _adapter;

    @BindView(R.id.recycler_view)
    public RecyclerView _recyclerView;

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
                return _restClient.getAllContacts().execute().body();
            }
        });

        _adapter = new ContactsViewAdapter(this, new ArrayList<Contact>());
        _recyclerView.setAdapter(_adapter);
        //Need this to get recylcer view to work. Android is frustrating.
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_list, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        _subscription = _single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Contact>>() {
                    @Override
                    public void onSuccess(List<Contact> value) {
                        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                        _recyclerView.setAdapter(new ContactsViewAdapter(ContactsListActivity.this, value));
                        _recyclerView.invalidate();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(ContactsListActivity.this, "got error!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStop() {
        _subscription.unsubscribe();
        super.onStop();
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
}
