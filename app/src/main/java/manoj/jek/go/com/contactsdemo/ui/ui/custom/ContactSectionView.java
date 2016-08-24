package manoj.jek.go.com.contactsdemo.ui.ui.custom;


import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

public class ContactSectionView extends SectionTitleIndicator{

    public ContactSectionView(Context context) {
        super(context);
    }

    public ContactSectionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ContactSectionView(Context context, AttributeSet attributeSet, int defStylAtr) {
        super(context, attributeSet, defStylAtr);
    }

    @Override
    public void setSection(Object object) {
        CharSequence charSequence = (CharSequence) object;
        setTitleText(charSequence.toString());
    }
}
