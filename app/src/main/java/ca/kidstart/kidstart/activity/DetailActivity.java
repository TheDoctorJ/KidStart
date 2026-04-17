package ca.kidstart.kidstart.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.kidstart.kidstart.R;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage, btnBack;
    private TextView tvCategory, tvTitle, tvPrice, tvLocation, tvAge, tvRating, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        btnBack = findViewById(R.id.btnBack);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvAge = findViewById(R.id.tvDetailAge);
        tvRating = findViewById(R.id.tvDetailRating);
        tvDescription = findViewById(R.id.tvDetailDescription);

        int imageRes = getIntent().getIntExtra("imageRes", 0);
        String category = getIntent().getStringExtra("category");
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String location = getIntent().getStringExtra("location");
        String age = getIntent().getStringExtra("age");
        String rating = getIntent().getStringExtra("rating");
        String description = getIntent().getStringExtra("description");

        if (imageRes != 0) {
            ivDetailImage.setImageResource(imageRes);
        }

        tvCategory.setText(category);
        tvTitle.setText(title);
        tvPrice.setText(price);
        tvLocation.setText(location);
        tvAge.setText(age);
        tvRating.setText(rating);
        tvDescription.setText(description);

        btnBack.setOnClickListener(v -> finish());
    }
}