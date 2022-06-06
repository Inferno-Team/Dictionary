package com.inferno.mobile.dictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.inferno.mobile.dictionary.R;
import com.inferno.mobile.dictionary.adapters.AdapterItemClick;
import com.inferno.mobile.dictionary.adapters.CharactersAdapter;
import com.inferno.mobile.dictionary.adapters.GridSpacingItemDecoration;
import com.inferno.mobile.dictionary.databinding.CharactersLayoutBinding;
import com.inferno.mobile.dictionary.models.Word;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.characters_layout);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        RecyclerView characters = findViewById(R.id.characters);
        characters.addItemDecoration(new
                GridSpacingItemDecoration(3, 50, true));
        new JsonLoaderThread(progressBar,characters,this).start();
   /*     binding.searchFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, WordActivity.class);
            intent.putExtra("character", '-');
            startActivity(intent);
        });
*/
    }

    public static class JsonLoaderThread extends Thread {
       private final ProgressBar progressBar;
       private final RecyclerView characters;
        private final Context context;
        static ArrayList<Word> words;
        private CharactersAdapter charactersAdapter;
        static Map<Character, ArrayList<Word>> wordsMap;

        public JsonLoaderThread(ProgressBar progressBar, RecyclerView characters,
                                Context context) {
            this.progressBar = progressBar;
            this.characters = characters;
            this.context = context;
            charactersAdapter = new CharactersAdapter(context, new ArrayList<>());
        }


        private String getJsonDataFromAsset(Context context) {
            StringBuilder json = new StringBuilder();
            try {
                InputStream is = context.getAssets().open("dictionary.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = reader.readLine())!=null){
                    json.append(line).append("\n");
                }
//                json.append(new String(buffer, StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.fillInStackTrace();
            }
            return json.toString();
        }

        private ArrayList<Word> convertJsonToWords(String json) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Word>>(){

            }.getType();
            return gson.fromJson(json, type);
        }

        @Override
        public void run() {
            super.run();
            ((MainActivity) context).runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                characters.setVisibility(View.GONE);

            });
            String json = getJsonDataFromAsset(context);
            words = toLowerCase(convertJsonToWords(json));

            wordsMap = reshapeData();
            ((MainActivity) context).runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                characters.setVisibility(View.VISIBLE);
                ArrayList<Character> _characters = new ArrayList<>(wordsMap.keySet());
                Collections.sort(_characters);

                charactersAdapter = new CharactersAdapter(context, _characters);

                charactersAdapter.setOnClickListener((item, pos) -> {
                    Intent intent = new Intent(context, WordActivity.class);
                    intent.putExtra("character", item);
                    context.startActivity(intent);
                });
                characters.setAdapter(charactersAdapter);
                String msg = "تم اكتشاف " + words.size() + " كلمة";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            });
//            interrupt();
        }

        private ArrayList<Word> toLowerCase(ArrayList<Word> list) {
            for (Word word : list)
                word.setEng(word.getEng().toLowerCase());
            return list;
        }


        Map<Character, ArrayList<Word>> reshapeData() {
            Map<Character, ArrayList<Word>> data = new HashMap<>();
            for (Word word : words) {
                char c = word.getEng().toCharArray()[0];
                if (!Character.isAlphabetic(c)) continue;
                c = Character.toUpperCase(c);
                ArrayList<Word> value = data.get(c);
                if (value == null) {
                    value = new ArrayList<>();
                    data.put(c, value);
                }

                value.add(word);
            }
            return data;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.open_liked) {
            Intent intent = new Intent(this, WordActivity.class);
            intent.putExtra("character", '*');
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}