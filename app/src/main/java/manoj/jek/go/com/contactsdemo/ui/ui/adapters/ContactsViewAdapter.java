package manoj.jek.go.com.contactsdemo.ui.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.models.ContactSummary;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ContactsViewHolder> {
    private List<ContactSummary> _contactSummaries;
    private Context _context;

    class ContactsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.contact_summary_pic)
        public ImageView _picView;
        @BindView(R.id.contact_summary_name)
        public TextView _nameView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ContactsViewAdapter(Context context, List<ContactSummary> contactSummaryList) {
        _contactSummaries = contactSummaryList;
        _context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(_context).inflate(R.layout.contact_summary_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        ContactSummary summary = _contactSummaries.get(position);
        holder._nameView.setText(summary.getFirst_name()+" "+summary.getLast_name());
        Glide.with(_context).load(summary.getProfile_pic()).placeholder(R.drawable.contacts_placeholder).into(holder._picView);
    }

    @Override
    public int getItemCount() {
        return _contactSummaries.size();
    }

    public void updateContacts(List<ContactSummary> summaryList) {
        _contactSummaries = summaryList;
        notifyDataSetChanged();
    }
}
