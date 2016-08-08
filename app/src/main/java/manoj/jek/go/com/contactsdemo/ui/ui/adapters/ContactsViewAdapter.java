package manoj.jek.go.com.contactsdemo.ui.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ContactsViewHolder> {
    private List<Contact> _contactSummaries;
    private Context _context;

    class ContactsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.contact_summary_pic)
        public ImageView _picView;
        @BindView(R.id.contact_summary_name)
        public TextView _nameView;
        @BindView(R.id.contact_charecter)
        public TextView _charecterView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ContactsViewAdapter(Context context, List<Contact> contactSummaryList) {
        Collections.sort(contactSummaryList);
        _contactSummaries = contactSummaryList;
        _context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(_context).inflate(R.layout.contact_summary_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Contact contact = _contactSummaries.get(position);
        holder._nameView.setText(Utils.capitalizeName(contact.getFirstName(), contact.getLastName()));
        Glide.with(_context).load(contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder).into(holder._picView);
        if(shouldShowChar(position)) {
            holder._charecterView.setText(getStartingChar(contact).toString());
        } else {
            holder._charecterView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return _contactSummaries.size();
    }

    public void updateContacts(List<Contact> summaryList) {
        Collections.sort(summaryList);
        _contactSummaries = summaryList;
        notifyDataSetChanged();
    }

    private Character getStartingChar(Contact contact) {
        return contact.getFirstName().toUpperCase().charAt(0);
    }

    private boolean shouldShowChar(int position)
    {
        Contact contact = _contactSummaries.get(position);
        if(position == 0 || !getStartingChar(contact).equals(getStartingChar(_contactSummaries.get(position-1)))) {
            return true;
        } else {
            return false;
        }
    }
}
