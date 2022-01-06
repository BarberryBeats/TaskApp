package kg.geektech.lvl4lesson1;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import kg.geektech.lvl4lesson1.databinding.FragmentHomeBinding;
import kg.geektech.lvl4lesson1.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private FragmentProfileBinding binding;
    private static int RESULT_LOAD_IMAGE = 1;
    private boolean proverka = false;
    private boolean proverka2 = false;
    private boolean isImageFitToScreen;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Prefs prefs = new Prefs(getContext());
        putPhoto(prefs);
        initListeners();
        if (!prefs.isNameEntered().equals(""))
            binding.editName.setText(prefs.isNameEntered());
        proverka2 = true;
        if (!prefs.getImage().equals("")) {
            Glide.with(binding.roundImageView).load(prefs.getImage()).circleCrop().into(binding.roundImageViewBody);
            proverka = true;
            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
            if(signInAccount != null){
                binding.editName.setText(signInAccount.getDisplayName());
            }
        }
        saveInfo(prefs);
    }

    private void putPhoto(Prefs prefs) {

    }


    private void initListeners() {
        binding.editphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(Intent.ACTION_PICK);
                cameraIntent.setType("image/*");
                startActivityForResult(cameraIntent, 1000);

            }
        });
        getParentFragmentManager().setFragmentResultListener("rk_name", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String a = result.getString("name");
                String b = result.getString("email");
                binding.editName.setText(a);
                binding.editMail.setText(b);
            }
        });
        binding.roundImageViewBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(requireContext());
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                Bundle bundle = new Bundle();
                bundle.putString("photoFull", prefs.getImage());
                getParentFragmentManager().setFragmentResult("rk_image", bundle);
                navController.navigate(R.id.fragmentForOpenImage, bundle);
            }
        });

        binding.imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigateUp();
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlert();

            }
        });
    }

    private void saveInfo(Prefs prefs) {
        binding.editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                prefs.saveNameState(s.toString());
            }
        });
        binding.editName.setText(prefs.isNameEntered());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && null != data) {
            Prefs prefs = new Prefs(getContext());
            Uri selectedImage = data.getData();
            binding.roundImageViewBody.setImageURI(selectedImage);
            prefs.saveImage(selectedImage);
        }
    }

    /*public static String encodeToBase64(Bitmap image) {
        Context context = null;
        Prefs prefs = new Prefs(context.getApplicationContext());
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }*/
    public void save() {
        Prefs prefs = new Prefs(getContext());
        String saveResult = binding.editName.getText().toString();
        prefs.saveNameState(saveResult);
    }
    private void setAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Вы уверены что хотите выйти?");
        builder.setMessage("при выходе приложением нельзя будет пользоваться.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                dialog.dismiss();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigateUp();
                navController.navigate(R.id.loginFragment);
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
}