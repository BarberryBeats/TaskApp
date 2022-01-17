package kg.geektech.lvl4lesson1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;
import kg.geektech.lvl4lesson1.databinding.FragmentNewsBinding;
import kg.geektech.lvl4lesson1.ui.home.NewsAdapter;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;
    private News news;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        news = (News) requireArguments().getSerializable("news");
        if (news != null) binding.editText.setText(news.getTitle());
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                save();
                binding.btnSave.setEnabled(false);

            }
        });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }

    private void save() {
        Bundle bundle = new Bundle();
        String text = binding.editText.getText().toString();
        if (news == null){
            news = new News(text, System.currentTimeMillis());
            bundle.putSerializable("news", news);
            getParentFragmentManager().setFragmentResult("rk_news", bundle);
            App.getInstance().getDatabase().newsDao().insert(news);
            addToFirestore();
        }else{
            news.setTitle(text);
            bundle.putSerializable("news", news);
            getParentFragmentManager().setFragmentResult("rk_news_update", bundle);
            App.getInstance().getDatabase().newsDao().update(news);
        }

    }

    private void addToFirestore() {
        FirebaseFirestore.getInstance().collection("news").add(news).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                  if (task.isSuccessful()){
                      binding.progressBar.setVisibility(View.INVISIBLE);
                      binding.btnSave.setEnabled(true);
                      Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                  } else {
                      binding.progressBar.setVisibility(View.INVISIBLE);
                      binding.btnSave.setEnabled(true);
                      Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                  }
                close();
            }
        });
    }
}