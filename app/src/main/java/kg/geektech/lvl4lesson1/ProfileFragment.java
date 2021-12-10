package kg.geektech.lvl4lesson1;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;
import kg.geektech.lvl4lesson1.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private FragmentProfileBinding binding;
    private static int RESULT_LOAD_IMAGE = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        binding.editphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent guess = new Intent(Intent.ACTION_GET_CONTENT);
                Bundle bundle = new Bundle();
                Bitmap image4 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add);
                bundle.putParcelable("image", image4);
                guess.putExtras(bundle);*/

                Intent cameraIntent = new Intent(Intent.ACTION_PICK);
                cameraIntent.setType("image/*");
                startActivityForResult(cameraIntent, 1000);

            }

        });
        binding.imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.roundImageViewBody.setImageBitmap(image);
            /**/
        }
    }
}