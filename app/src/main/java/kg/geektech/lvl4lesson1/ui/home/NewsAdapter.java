package kg.geektech.lvl4lesson1.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kg.geektech.lvl4lesson1.News;
import kg.geektech.lvl4lesson1.OnItemClickListener;
import kg.geektech.lvl4lesson1.R;
import kg.geektech.lvl4lesson1.databinding.ItemNewsBinding;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private ArrayList<News> list;
    private TextView title, time;
    private OnItemClickListener onItemClickListener;
    private Context context;



    public NewsAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(News news) {
        list.add(0, news);
        Log.e("TAG", "addItem: ");
notifyItemInserted(0);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public News getItem(int position) {
        return list.get(position);
    }

    public void removelist(int position) {
        list.remove(position);
    }

    public void changeItem(News news) {
        int index = list.indexOf(news);
        list.set(index,news);
        notifyItemChanged(index);
    }
    public void obnovit() {
        notifyDataSetChanged();
    }
    public String getItem1(int position) {
        return list.get(position).toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            title = itemView.findViewById(R.id.textTitle);
            time = itemView.findViewById(R.id.textTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public void onBind(News news) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd  HH:mm:ss");
            String a = sdf.format(news.getCreatedAt());
            title.setText(news.getTitle());
           time.setText(a);
            if (getAdapterPosition() % 2 == 1) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            }
        }
    }
}
