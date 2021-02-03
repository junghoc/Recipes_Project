package com.webproject.food.main.recipes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webproject.food.BoardInfoActivity;
import com.webproject.food.R;
import com.webproject.food.vo.BoardVO;

import java.util.ArrayList;

public class BoardRecipesAdapter extends RecyclerView.Adapter<BoardRecipesAdapter.ViewHolder>{

    private ArrayList<BoardVO> items;
    private Context context;

    public BoardRecipesAdapter(ArrayList<BoardVO> items){
        this.items = items;
    }//생성자

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipes_board,parent,false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }//아이템 뷰로 사용할 xml inflate 시킴

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //viewHolder 객체
        holder.tv_item_b_name.setText(items.get(position).getTitle());
        holder.tv_item_m_name.setText(items.get(position).getM_name());
        holder.tv_item_b_like.setText(items.get(position).getLike());
        //클릭이벤트
        holder.item_recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BoardInfoActivity.class);
                i.putExtra("idx", items.get(position).getIdx());
                context.startActivity(i);
            }
        });

    }//뷰 안에 필요한 정보 채움

    @Override
    public int getItemCount() {//data의 전체 크기
        Log.i("TEST", "Size : " + items.size() );
        return items.size();
    }

    //ViewHolder 클래스
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_item_b_name;
        public TextView tv_item_m_name;
        public TextView tv_item_b_like;
        public LinearLayout item_recipes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //파라미터 찾기
            tv_item_b_name = itemView.findViewById(R.id.tv_item_b_name);
            tv_item_m_name = itemView.findViewById(R.id.tv_item_m_name);
            tv_item_b_like = itemView.findViewById(R.id.tv_item_b_like);
            item_recipes = itemView.findViewById(R.id.item_recipes);
        }
    }

}
