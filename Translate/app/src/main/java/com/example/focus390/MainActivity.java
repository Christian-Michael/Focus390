package com.example.focus390;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Original sentence structure
    private EditText inputText;
    private String originalText;

    //Translated sentence structure
    private TextView transText;
    private String translatedText;

    Button translateButton;
    Translate translate;
    String getCountry;

    //Access the country selected
    String countrySelected;
    final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("plzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://serpapi.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDisplayStates();
        languageBar();

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTranslateService();
                translate();
            }
        });

        // create an instance of the ApiService
        GoogleAPIService apiService = retrofit.create(GoogleAPIService.class);
        // make a request by calling the corresponding method
        Single<ImageResult> testResult = apiService.getImageData("apple");
        testResult.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<ImageResult>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(ImageResult result) {
                    ImageView imageView = findViewById(R.id.my_image_view);
                    Glide.with(getApplicationContext()).load(result.getResults().get(0).getOriginal()).into(imageView);

                    System.out.println(";laskjdflfkdsjafdslja;ksdlfjka;fdsljk;asdfaljkdfsjlk;dsfjlk;sdjlk;");
                    System.out.println(result);
                }
                public void onError(Throwable e) {
                    System.out.println(e.getMessage());
                }
            });
    }

    //Sets the stage of the interactable objects
    public void initDisplayStates(){
        inputText = findViewById(R.id.inputText);
        transText = findViewById(R.id.transText);
        translateButton = findViewById(R.id.translateButton);
    }

    //Translates text
    public void translate() {
        //Get input text to be translated
        originalText = inputText.getText().toString();
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage(getCountry), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        transText.setText(translatedText);
    }

    //Gives JSON key to GOOGLE
    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    //Sets the size of the languageBar and creates the Country List
    public void languageBar(){
        Spinner menu = findViewById(R.id.countries);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(menu);

            // Set popupWindow height to 500px
            popupWindow.setHeight(650);
        }

        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<CharSequence> countryList = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        countryList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(countryList);
        menu.setOnItemSelectedListener(this);

        countrySelected = getCountry;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getCountry = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}