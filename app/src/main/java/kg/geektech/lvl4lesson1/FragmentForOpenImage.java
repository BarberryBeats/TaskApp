package kg.geektech.lvl4lesson1;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.net.URI;

import kg.geektech.lvl4lesson1.databinding.FragmentForOpenImageBinding;
import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;

public class FragmentForOpenImage extends Fragment {
    private FragmentForOpenImageBinding binding;
    private Prefs prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForOpenImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void InitListener(){
getParentFragmentManager().setFragmentResultListener("rk_image", getViewLifecycleOwner(), new FragmentResultListener() {
    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        String a = result.getString("photoFull");
        Glide.with(binding.photoAfter).load(prefs.getImage()).circleCrop().into(binding.photoAfter1);
    }
});
    }
}