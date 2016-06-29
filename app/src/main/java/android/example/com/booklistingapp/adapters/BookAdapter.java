package android.example.com.booklistingapp.adapters;

import android.content.Context;
import android.example.com.booklistingapp.R;
import android.example.com.booklistingapp.models.Book;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class BookAdapter extends BaseAdapter {

    Context context;
    List<Book> booksList;

    public BookAdapter(Context context, List<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @Override
    public int getCount() {
        return booksList.size();
    }

    @Override
    public Object getItem(int position) {
        return booksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = parent.inflate(context, R.layout.book_row, null);

        Book book = (Book) getItem(position);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(book.getName());

        TextView tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        tvAuthor.setText(book.getAuthor());

        ImageView ivCover = (ImageView) view.findViewById(R.id.ivCover);
        Picasso.with(context).load(book.getImageUrl()).into(ivCover);

        return view;
    }
}