package com.android.jh.runtimepermission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 리사이클러 뷰 구현순서
 * 1. 홀더에 사용하는 위젯을 모두 정의한다
 * 2. getitemcount에 데이터 갯수 전달
 * 3. onCreateViewHolder 에서 뷰 아이템 생성
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder>{
    ArrayList<Contact> datas;
    Context context;
    Intent intent;
    public RecyclerAdapter(ArrayList<Contact> datas, Context context){
        this.datas = datas;
        this.context= context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Contact contact = datas.get(position);
        holder.txtNo.setText(contact.getId()+"");
        holder.txtName.setText(contact.getName());
        holder.txtNum.setText(contact.getNumber().get(0));

    }
    private void callPhone(String number){
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView txtNo,txtName,txtNum;
        ImageButton btn_Call;

        public Holder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView);
            txtNo = (TextView)itemView.findViewById(R.id.txtNo);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            txtNum = (TextView)itemView.findViewById(R.id.txtNum);
            btn_Call = (ImageButton)itemView.findViewById(R.id.btn_Call);

           btn_Call.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            callPhone(txtNum.getText().toString());
                        } else {
                            callPhone(txtNum.getText().toString());
                        }
                    }
                }
            });
        }

    }
}
