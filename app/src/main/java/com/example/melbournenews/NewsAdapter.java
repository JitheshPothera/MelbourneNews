package com.example.melbournenews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position from the list of News
        News currentNews = getItem(position);

        // Get the Text view for the title of news article and assign title value to the view
        TextView newsTitle = convertView.findViewById(R.id.news_title);
        newsTitle.setText(currentNews.getTitleOfArticle());

        // Get the Text view for the section name of news article and assign value to the view
        TextView sectionName = convertView.findViewById(R.id.section_name);
        sectionName.setText(currentNews.getSectionName());

        // Get the Text view for the Author name of the article and assign value to the view
        TextView authorName = convertView.findViewById(R.id.author_name);
        authorName.setText(currentNews.getAuthorName());

        // Get the Text view for the publication date of the article and assign value to the view
        TextView publicationDate = convertView.findViewById(R.id.publication_date);
        publicationDate.setText(formatDate(currentNews.getPublicationDate()));

        // Get the news image view and set the image from the thumbnail url
        new DownloadImageFromInternet((ImageView) convertView.findViewById(R.id.news_image))
                .execute(currentNews.getThumbnail());


        return convertView;
    }

    /**
     * Method to split the date value from UTC date format
     */
    private String formatDate(String str) {
        String[] parts = str.split("T");
        return parts[0];
    }


    /**
     * Method to download the image from the Internet
     */
    private static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
