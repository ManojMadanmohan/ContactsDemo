package manoj.jek.go.com.contactsdemo.ui.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
import manoj.jek.go.com.contactsdemo.ui.activities.ContactInfoActivity;
import manoj.jek.go.com.contactsdemo.ui.activities.ContactsListActivity;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import rx.Observable;
import rx.subjects.PublishSubject;

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
        @BindView(R.id.contact_summary_root)
        public View _rootView;

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
        final Contact contact = _contactSummaries.get(position);
        holder._nameView.setText(Utils.capitalizeName(contact.getFirstName(), contact.getLastName()));
        Glide.with(_context).load(contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder).into(holder._picView);
        holder._charecterView.setText(getCharecterToShow(position));
        holder._rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactInfoActivity.launch(contact,  _context);
            }
        });
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

    private String getStartingChar(Contact contact) {
        return String.valueOf(contact.getFirstName().toUpperCase().charAt(0));
    }

    private CharSequence getCharecterToShow(int position) {
        Contact contact = _contactSummaries.get(position);
        if(position == 0) {
            return (contact.getIsFavorite() ? "star" : getStartingChar(contact));
        } else {
            Contact prevContact = _contactSummaries.get(position - 1);
            if(!getStartingChar(contact).equals(getStartingChar(prevContact))) {
                return getStartingChar(contact);
            } else {
                return "";
            }
        }
    }

    private SpannableString getStarIcon() {
        SpannableString ss = new SpannableString("");
        Drawable d = _context.getResources().getDrawable(R.drawable.ic_star_rate);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
