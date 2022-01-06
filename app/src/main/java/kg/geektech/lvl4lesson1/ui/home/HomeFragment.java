package kg.geektech.lvl4lesson1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import kg.geektech.lvl4lesson1.App;
import kg.geektech.lvl4lesson1.News;
import kg.geektech.lvl4lesson1.OnItemClickListener;
import kg.geektech.lvl4lesson1.R;
import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<News> list = App.getInstance().getDatabase().newsDao().getAll();
        adapter = new NewsAdapter(getContext());
        adapter.addItems(list);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                List<News> list = App.getInstance().getDatabase().newsDao().getAll();
                adapter.addItems(list);
                return true;
            case R.id.item3:
                List<News> list2 = App.getInstance().getDatabase().newsDao().getAllSortedByTitle();
                adapter.addItems(list2);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setAlert(int position) {
        mySimpleDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_alert);
        builder.setMessage(R.string.title_message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                News news = adapter.getItem(position);
                dialog.dismiss();
                App.getInstance().getDatabase().newsDao().delete(news);
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
        create();
        setHasOptionsMenu(true);
    }

    private void create() {
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                News news = adapter.getItem(position);
                openFragment(news);
            }

            @Override
            public void onLongClick(int position) {
                setAlert(position);
            }
        });

    }

    private void initListener() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(null);
            }
        });
        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                News news = (News) result.getSerializable("news");
                adapter.addItem(news);
            }

        });

        getParentFragmentManager().setFragmentResultListener("rk_news_update", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                News news = (News) result.getSerializable("news");
                adapter.changeItem(news);
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

    private void openFragment(News news) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", news);
        navController.navigate(R.id.newsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}