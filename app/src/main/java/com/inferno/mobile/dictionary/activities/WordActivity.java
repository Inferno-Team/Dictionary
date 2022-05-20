package com.inferno.mobile.dictionary.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inferno.mobile.dictionary.R;
import com.inferno.mobile.dictionary.adapters.PaginationListener;
import com.inferno.mobile.dictionary.adapters.WordAdapter;
import com.inferno.mobile.dictionary.databinding.ActivityMainBinding;
import com.inferno.mobile.dictionary.models.Word;
import com.inferno.mobile.dictionary.utils.LikedWordSavor;

import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PaginationListener listener;
    private ArrayList<Word> words;
    private WordAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        char character = getIntent().getCharExtra("character", 'A');
        if (character != '-' && character != '*') {
            words = MainActivity.JsonLoaderThread.wordsMap.get(character);

        } else {
            if (character == '-')
                words = MainActivity.JsonLoaderThread.words;
            else words = loadLikedWords();
        }
        adapter = new WordAdapter(this, new ArrayList<>());
        adapter.setOnItemClickListener((item, pos) -> {
            if (LikedWordSavor.isWordLiked(this, item)) {
                LikedWordSavor.removeWord(this, item.getId());
                item.setLiked(false);
            } else {
                LikedWordSavor.addWord(this, item.getId());
                Toast.makeText(this,
                        "new word added to favourite : [ " + item.getEng() + " ]"
                        , Toast.LENGTH_LONG).show();
                item.setLiked(true);
            }
            adapter.notifyItemChanged(pos);
        });
        listener = new PaginationListener();

        listener.setCurrentPage(0);
        listener.setLoading(true);
        listener.setLastPage(false);

        loadMore(0);
        listener.setLoadMore(currentPage -> {
            listener.setLoading(true);
            Log.d("JsonLoaderThread", "setLoadMore: page #" + currentPage);
            loadMore(currentPage);
        });
        binding.words.addOnScrollListener(listener);
        binding.words.setAdapter(adapter);
    }

    private ArrayList<Word> loadLikedWords() {
        ArrayList<Word> words = new ArrayList<>();
        ArrayList<Integer> ids = LikedWordSavor.getWords(this);
        ArrayList<Word> allWords = MainActivity.JsonLoaderThread.words;
        for (int id : ids)
            words.add(getWordById(allWords, id));
        return words;
    }

    private Word getWordById(ArrayList<Word> words, int id) {
        for (Word word : words)
            if (word.getId() == id) return word;
        return null;
    }

    void loadMore(int page) {
        listener.setLoading(false);
        int wordCount = listener.getPageItemCount();
        int startIndex = page * wordCount;
        int lastIndex = (page + 1) * wordCount;
        if (lastIndex >= words.size())
            lastIndex = words.size() - 1;
        if (words.size() == 0)
            return;
        Log.d("JsonLoaderThread", "loadMore: page #" + page);
        Log.d("JsonLoaderThread", "loadMore: startIndex :" + startIndex + " , lastIndex : " + lastIndex);
        List<Word> newWords;
        if (startIndex == lastIndex)
            newWords = words;
        else newWords = words.subList(startIndex, lastIndex);
        LikedWordSavor.checkLikedWords(this, newWords);
        adapter.addNewWords(newWords);



        listener.setLastPage((page + 1) * wordCount >= words.size());
        listener.setCurrentPage(page + 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        char character = getIntent().getCharExtra("character", 'A');
        if (character == '-') {
            search.setIconifiedByDefault(false);
            search.requestFocus();
        }
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchWord(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void searchWord(String text) {
        if (text.equals("")) {
            binding.words.setAdapter(adapter);
            loadMore(0);
            binding.words.addOnScrollListener(listener);
        } else {
            binding.words.removeOnScrollListener(listener);
            ArrayList<Word> searchWords = new ArrayList<>();

            for (Word word : words) {
                if (word.getEng().startsWith(text) || word.getAr().startsWith(text)) {
                    searchWords.add(word);
                }
            }
            WordAdapter searchAdapter = new WordAdapter(this,searchWords);
            searchAdapter.setOnItemClickListener((item, pos) -> {
                if (LikedWordSavor.isWordLiked(this, item)) {
                    LikedWordSavor.removeWord(this, item.getId());
                    item.setLiked(false);
                } else {
                    LikedWordSavor.addWord(this, item.getId());
                    Toast.makeText(this,
                            "new word added to favourite : [ " + item.getEng() + " ]"
                            , Toast.LENGTH_LONG).show();
                    item.setLiked(true);
                }
                searchAdapter.notifyItemChanged(pos);
            });
            binding.words.swapAdapter(searchAdapter, true);
        }
    }

}
