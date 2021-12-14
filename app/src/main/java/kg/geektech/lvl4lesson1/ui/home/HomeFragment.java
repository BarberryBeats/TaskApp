package kg.geektech.lvl4lesson1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.w3c.dom.Text;

import kg.geektech.lvl4lesson1.EditNewsFragment;
import kg.geektech.lvl4lesson1.News;
import kg.geektech.lvl4lesson1.OnItemClickListener;
import kg.geektech.lvl4lesson1.R;
import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;
import kg.geektech.lvl4lesson1.databinding.ItemNewsBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter(getContext());
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                getItem(position);
                News news = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", news);
                getParentFragmentManager().setFragmentResult("key1", bundle);

               openFragment2();
                getParentFragmentManager().setFragmentResultListener("ed_news", getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e("TAG", "onFragmentResult: ");
                        News news = (News) result.getSerializable("edit_news");
                        Log.d("Home", "text = " + news.getTitle());
                        adapter.changeItem(news, position);

                    }
                });
            }

            @Override
            public void onLongClick(int position) {
                setAlert(position);
            }
        });

    }

    private void getItem(int position) {
        getParentFragmentManager().setFragmentResultListener("ed_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                News news = (News) result.getSerializable("edit_news");
                adapter.changeItem(news, position);
            }
        });
    }

    private void setAlert(int position) {
        mySimpleDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_alert);
        builder.setMessage(R.string.title_message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                adapter.notifyItemRemoved(position);
                adapter.removelist(position);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void mySimpleDialog() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("TAG", "onFragmentResult: ");

                News news = (News) result.getSerializable("news");
                Log.d("Home", "text = " + news.getTitle());
                adapter.addItem(news);
            }

        });


        initList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initList() {

        binding.recyclerView.setAdapter(adapter);

    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.newsFragment);
    }

    private void openFragment2() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.editNewsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}