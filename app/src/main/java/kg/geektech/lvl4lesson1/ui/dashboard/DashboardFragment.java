package kg.geektech.lvl4lesson1.ui.dashboard;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import kg.geektech.lvl4lesson1.App;
import kg.geektech.lvl4lesson1.News;
import kg.geektech.lvl4lesson1.R;
import kg.geektech.lvl4lesson1.databinding.FragmentDashboardBinding;
import kg.geektech.lvl4lesson1.ui.home.NewsAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private NewsAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter(requireContext());
        getData1();
    }

    private void getData() {
        FirebaseFirestore.getInstance().collection("news")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<News> list = queryDocumentSnapshots.toObjects(News.class);
                adapter.addItems(list);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void getData1(){
       FirebaseFirestore.getInstance().collection("news").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   QuerySnapshot document = task.getResult();
                   if(document.isEmpty()){
                       binding.textNoData.setVisibility(View.VISIBLE);
                       binding.imageError.setVisibility(View.VISIBLE);
                       binding.btnNoData.setVisibility(View.VISIBLE);
                       binding.progressBar.setVisibility(View.INVISIBLE);
                   }else {
                       FirebaseFirestore.getInstance().collection("news")
                               .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                               if (DashboardFragment.this.isVisible()){
                                   List<News> list = queryDocumentSnapshots.toObjects(News.class);
                                   adapter.addItems(list);
                                   binding.progressBar.setVisibility(View.INVISIBLE);
                               }
                               }

                       });
                   }
               }
           }
       });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
        initListeners();
    }

    private void initListeners() {
        binding.btnNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnNoData.setVisibility(View.INVISIBLE);
                binding.imageError.setVisibility(View.INVISIBLE);
                binding.textNoData.setVisibility(View.INVISIBLE);
                getData1();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                adapter.sort1();
                return true;
            case R.id.item3:
                adapter.sort2();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}