package com.inferno.mobile.dictionary.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inferno.mobile.dictionary.R;
import com.inferno.mobile.dictionary.activities.WordActivity;

import java.util.ArrayList;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharacterHolder> {
    private final Context context;
    private final ArrayList<Character> characters;

    public CharactersAdapter(Context context, ArrayList<Character> characters) {
        this.context = context;
        this.characters = characters;
    }



    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CharacterHolder(inflater.inflate(R.layout.character_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        Character character = characters.get(position);
        holder.character.setText(String.valueOf(character));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WordActivity.class);
                intent.putExtra("character", character);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public static class CharacterHolder extends RecyclerView.ViewHolder {
        TextView character;

        public CharacterHolder(View itemView) {
            super(itemView);
            character = itemView.findViewById(R.id.character);
        }
    }
}
