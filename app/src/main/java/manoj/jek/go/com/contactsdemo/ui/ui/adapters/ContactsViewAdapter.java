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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.activities.ContactInfoActivity;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.models.ContactSummary;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;

public class ContactsViewAdapter extends RecyclerView.Adapter<ContactsViewAdapter.ContactsViewHolder> implements SectionIndexer, Filterable {

    private List<Contact> _contactSummaries;
    private List<Contact> _resultContactSummaries;
    private Context _context;
    private CharSequence [] _sections;
    private Map<CharSequence, Integer> _sectionPosMap;
    private String _searchString;

    @Override
    public Object[] getSections() {
        return _sections;
    }

    private void initSections() {
        CharSequence arr[] = new String[27];
        arr[0] = "star";
        char c = 'A';
        while(c <= 'Z') {
            arr[c- 'A' + 1] = String.valueOf(c);
            c++;
        }
        _sections = arr;
        for(int i = 0; i< _resultContactSummaries.size(); i++) {
            CharSequence charSequence = getCharecterToShow(i);
            _sectionPosMap.put(charSequence, i);
        }
    }

    @Override
    public int getPositionForSection(int i) {
        CharSequence charSequence = _sections[i];
        return _sectionPosMap.get(charSequence);
    }

    @Override
    public int getSectionForPosition(int i) {
        if(i < 0) {
            return 0;
        }
        if(i >= _resultContactSummaries.size()) {
            return getSectionForPosition(_resultContactSummaries.size() - 1);
        }
        CharSequence startingChar = getStartingChar(_resultContactSummaries.get(i));
        int secVal=0;
        for(CharSequence value: _sections) {
            if(value.equals(startingChar)) {
                return secVal;
            }
            secVal++;
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                _resultContactSummaries.clear();
                for(Contact contact: _contactSummaries) {
                    if (matches(contact, charSequence)) {
                        _resultContactSummaries.add(contact);
                    }
                }
                _searchString = charSequence.toString();
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                initSections();
                notifyDataSetChanged();
            }
        };
    }

    private boolean matches(Contact contact, CharSequence searchString) {
        if(matches(contact.getFirstName(), searchString) || matches(contact.getLastName(), searchString)
                || matches(contact.getNumber(), searchString)) {
            return true;
        }
        return false;
    }

    private boolean matches(String value, CharSequence searchString) {
        if(value != null && value.toLowerCase().contains(searchString.toString().toLowerCase())) return true;
        return false;
    }

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
        _resultContactSummaries = new ArrayList<>(contactSummaryList);
        _contactSummaries = new ArrayList<>(contactSummaryList);
        _context = context;
        _sectionPosMap = new HashMap<>();
        _searchString = "";
        initSections();
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(_context).inflate(R.layout.contact_summary_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        final Contact contact = _resultContactSummaries.get(position);
        Glide.with(_context).load(contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder).into(holder._picView);
        holder._charecterView.setText(getCharecterToShow(position));
        holder._rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactInfoActivity.launch(contact,  _context);
            }
        });
        String name = Utils.capitalizeName(contact.getFirstName(), contact.getLastName());
        holder._nameView.setText(Utils.colorPartialString(name, _searchString, _context.getResources().getColor(R.color.red)));
    }

    @Override
    public int getItemCount() {
        return _resultContactSummaries.size();
    }

    public void updateContacts(List<Contact> summaryList) {
        Collections.sort(summaryList);
        _contactSummaries = new ArrayList<>(summaryList);
        _resultContactSummaries = new ArrayList<>(summaryList);
        notifyDataSetChanged();
    }

    private String getStartingChar(Contact contact) {
        return String.valueOf(contact.getFirstName().toUpperCase().charAt(0));
    }

    private CharSequence getCharecterToShow(int position) {
        Contact contact = _resultContactSummaries.get(position);
        if(position == 0) {
            return (contact.getIsFavorite() ? getStarIcon() : getStartingChar(contact));
        } else {
            Contact prevContact = _resultContactSummaries.get(position - 1);
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
