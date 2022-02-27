package com.example.slider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private SliderAdapter adapter;
    private ArrayList<SliderModal> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        data.add(new SliderModal("1", "100"));
        list = findViewById(R.id.recycler);
        list.setHasFixedSize(true);
        adapter = new SliderAdapter(data);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    private class SliderAdapter extends RecyclerView.Adapter<SliderViewHolder> {
        ArrayList<SliderModal> data;

        public SliderAdapter(ArrayList<SliderModal> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.slider_viewholder, parent, false);
            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SliderViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.left.setText(data.get(position).getLeft());
            holder.right.setText(data.get(position).getRight());
            holder.slider.setValue(Float.valueOf("100"));
            holder.slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onStartTrackingTouch(@NonNull Slider slider) {
                    holder.delete.setVisibility(View.VISIBLE);
                }

                @SuppressLint("RestrictedApi")
                @Override
                public void onStopTrackingTouch(@NonNull Slider slider) {
                    holder.delete.setVisibility(View.GONE);
                    float value = slider.getValue();
                    if(data.size() == 1 && value == 0.0f || (value == 100.00f)) {

                    } else if(value == 0.0f && position == 0) {
                        data.get(position+1).setLeft("1");
                        data.remove(position);
                    } else if (value == 0.0f) {
                        int right = Integer.parseInt(data.get(position-1).getRight());
                        int l = Integer.parseInt(data.get(position).getLeft());
                        int r = Integer.parseInt(data.get(position).getRight());
                        data.get(position-1).setRight(String.valueOf(right + r-l+1));
//                        adapter.notifyItemChanged(position-1);
                        data.remove(position);
//                        adapter.notifyItemRemoved(position-1);
                    } else {
                        int right = Integer.parseInt(holder.right.getText().toString());
                        int left = Integer.parseInt(holder.left.getText().toString());
                        if(right - left <= 5 ){
                            Toast.makeText(MainActivity.this, "Can't divide further", Toast.LENGTH_SHORT).show();
                        } else {
                            int diff = right - left;
                            value = value < 10 ? 10 : value;
                            int result = (int) value*diff/100;
                            if (result < 2) {
                                Toast.makeText(MainActivity.this, "Can't divide further", Toast.LENGTH_SHORT).show();
                            } else {
                                int r = right - result;
                                int l = r+1;
                                data.get(position).setRight(String.valueOf(r));
                                if (position == data.size()-1) {
                                    data.add(position+1, new SliderModal(String.valueOf(l), "100"));
                                } else {
                                    data.add(position+1, new SliderModal(String.valueOf(l), String.valueOf(Integer.parseInt(data.get(position+1).getLeft())-1)));
                                }
                            }

                        }

//                        adapter.notifyItemChanged(position);
//                        adapter.notifyItemInserted(position+1);
                    }
                    slider.setValue(100.00f);
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class SliderViewHolder extends RecyclerView.ViewHolder {
        TextView left, right;
        Slider slider;
        ImageButton delete;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);
            slider = itemView.findViewById(R.id.slider);
            delete = itemView.findViewById(R.id.left_img);
        }
    }
}
