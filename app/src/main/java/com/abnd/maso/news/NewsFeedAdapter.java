package com.abnd.maso.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by mariosoberanis on 11/5/16.
 */

public class NewsFeedAdapter extends ArrayAdapter<GuardianArticle> {

    public NewsFeedAdapter(Context context, List<GuardianArticle> guardianArticles) {
        super(context, 0, guardianArticles);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Find current article position in the array
        GuardianArticle article = getItem(position);

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        }

        TextView sectionName = (TextView) convertView.findViewById(R.id.section_name);
        sectionName.setText(article.getSectionName());

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(article.getWebTitle());

        TextView articleDate = (TextView) convertView.findViewById(R.id.web_publication_date);
        // We  format the date
        String formatArticleDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(article.getWebPublicationDate());
            formatArticleDate = date.toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        articleDate.setText(formatArticleDate);

        return convertView;
    }
}

