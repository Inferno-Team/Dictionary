package com.inferno.mobile.dictionary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.inferno.mobile.dictionary.activities.WordActivity;
import com.inferno.mobile.dictionary.models.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikedWordSavor {
    private final static String fileName = "liked.words";
    private final static String wordsField = "words";

    public static void addWord(Context context, int index) {
        Set<String> set = getWordsSet(context);
        set.add(String.valueOf(index));
        getEditor(context)
                .putStringSet(wordsField, set)
                .apply();
    }

    public static void removeWord(Context context,int index){
        Set<String> set = getWordsSet(context);
        set.remove(String.valueOf(index));
        getEditor(context)
                .putStringSet(wordsField, set)
                .apply();
    }


    public static ArrayList<Integer> getWords(Context context) {
        Set<String> set = getWordsSet(context);
        ArrayList<Integer> ids = new ArrayList<>();
        for (String s : set) {
            ids.add(Integer.parseInt(s));
        }
        return ids;
    }

    private static Set<String> getWordsSet(Context context) {
        Set<String> set = getShared(context).getStringSet(wordsField, null);
        return set != null ? set : new HashSet<>();
    }


    private static SharedPreferences getShared(Context context) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getShared(context).edit();
    }

    public static void checkLikedWords(Context context, List<Word> newWords) {
        Set<String> ids = getWordsSet(context);
        for (Word word : newWords)
            word.setLiked(ids.contains(String.valueOf(word.getId())));

    }

    public static boolean isWordLiked(Context context,Word word){
        return getWordsSet(context).contains(String.valueOf(word.getId()));
    }
}
