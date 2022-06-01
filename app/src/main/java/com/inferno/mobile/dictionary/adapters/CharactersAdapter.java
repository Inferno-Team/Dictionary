package com.inferno.mobile.dictionary.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inferno.mobile.dictionary.R;
import com.inferno.mobile.dictionary.databinding.CharacterItemBinding;

import java.util.ArrayList;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharacterHolder> {
    private final Context context;
    private final ArrayList<Character> characters;
    private AdapterItemClick<Character> onClickListener;

    public CharactersAdapter(Context context, ArrayList<Character> characters) {
        this.context = context;
        this.characters = characters;
    }


    public void setOnClickListener(AdapterItemClick<Character> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacterHolder(LayoutInflater.from(context).
                inflate(R.layout.character_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        holder.binding.setCharacter(characters.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null)
                onClickListener.onClick(characters.get(holder.getAdapterPosition())
                        , holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class CharacterHolder extends RecyclerView.ViewHolder {
        CharacterItemBinding binding;

        public CharacterHolder(View itemView) {
            super(itemView);
            binding = CharacterItemBinding.bind(itemView);
        }
    }
}
