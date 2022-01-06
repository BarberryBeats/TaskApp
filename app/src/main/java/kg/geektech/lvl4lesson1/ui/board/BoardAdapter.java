package kg.geektech.lvl4lesson1.ui.board;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import kg.geektech.lvl4lesson1.Prefs;
import kg.geektech.lvl4lesson1.R;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private String[] titles = new String[]{"Привет", "Hello", "おい"};
    private String[] discription = new String[]{russian(), english(), japan()};
    private int[] drawable = new int[]{R.raw.ic_hi, R.raw.smile, R.raw.news};
    private Context context;


    private String english() {
        String a = "Application for news.\n" +
                "Record your news only in our application!";
        return a;
    }

    private String france() {
        String a = "Demande de nouvelles.\n" +
                "Enregistrez vos actualités uniquement dans notre application !";
        return a;
    }

    private String japan() {
        String a = "ニュースの申し込み。\n" +
                "私たちのアプリケーションでのみあなたのニュースを記録してください！";
        return a;
    }

    private NavController navController;

    public BoardAdapter(NavController navController) {
        this.navController = navController;
    }

    private String russian() {
        String a = "Приложение для новостей.\n" +
                "Записывайте свои новости только в нашем приложении !";
        return a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDesc;
        private Button btnStart;
        private LottieAnimationView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            btnStart = itemView.findViewById(R.id.btnStart);
            textDesc = itemView.findViewById(R.id.textDesc);
            image = itemView.findViewById(R.id.imageview);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Prefs(context).saveBoardState();
                    navController.navigateUp();
                }
            });
        }

        public void onBind(int position) {

            textTitle.setText(titles[position]);
            image.setImageResource(drawable[position]);
            textDesc.setText(discription[position]);
            image.setAnimation(drawable[position]);
            if (position == titles.length - 1) {
                btnStart.setVisibility(View.VISIBLE);
            } else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }
    }
}
