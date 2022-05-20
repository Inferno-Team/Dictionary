package com.inferno.mobile.dictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.inferno.mobile.dictionary.R;
import com.inferno.mobile.dictionary.databinding.WordItemBinding;
import com.inferno.mobile.dictionary.models.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    private final Context context;
    private final ArrayList<Word> words;
    private AdapterItemClick<Word> onItemClickListener;


    public WordAdapter(Context context, ArrayList<Word> words) {
        this.context = context;
        this.words = words;
    }

    public void addNewWords(List<Word> newWords) {
        int lastIndex = words.size() - 1;
        words.addAll(newWords);
        notifyItemRangeInserted(lastIndex, newWords.size());
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WordHolder(LayoutInflater.from(context)
                .inflate(R.layout.word_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {
        Word word = words.get(position);
        holder.binding.setWord(word);
        if (word.isLiked())
            holder.binding.likeBtn.setImageResource(R.drawable.ic_heart_fill);
        else holder.binding.likeBtn.setImageResource(R.drawable.ic_heart);

        holder.binding.likeBtn.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onClick(word, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setOnItemClickListener(AdapterItemClick<Word> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class WordHolder extends RecyclerView.ViewHolder {
        WordItemBinding binding;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            binding = WordItemBinding.bind(itemView);
        }
    }
}
