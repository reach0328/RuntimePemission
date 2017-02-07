package com.android.jh.runtimepermission;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class DataLoader {
    //맴버변수 선언시 초기화(생성자에서 해도되지만)

    private ArrayList<Contact> datas = new ArrayList<>();
    private Context context;
    public  DataLoader (Context context) {
        this.context = context;
    }
    public ArrayList<Contact> get() {
        return datas;
    }

    public void load() {
        //주소록에 접근 하기 위해 ContentResolver를 불러온다
        ContentResolver resolver = context.getContentResolver();
        // 주소록에 가져올 데이터 컬럼명을 정의
        String projections[] = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, //데이터의 아이디
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // 이름
                ContactsContract.CommonDataKinds.Phone.NUMBER  // 전화번호
        };
        // Resolver로 쿼리한 데이터를 커서에 담는다
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI//데이터 주소
                , projections     // 가져올 데이터 컬럼명 배열
                , null            // 조건절
                , null            // 지정된 컬럼 명과 매핑되는 실제 조건값
                , null              // 정렬
                 );
        // 커서에 넘어온 데이터가 있다면 반복문을 돌면서 Datas에 담아준다
        if(cursor != null){
            while(cursor.moveToNext()){
                Contact contact = new Contact();

                // 커서의 컬럼 인덱스를 가져온 후
                int idx = cursor.getColumnIndex(projections[0]);
                // 컬럼 인덱스에 해당하는 타입에 맞게 값을 꺼내서 세팅한다
                contact.setId(cursor.getInt(idx));
                // 반복
                idx = cursor.getColumnIndex(projections[1]);
                contact.setName(cursor.getString(idx));
                idx = cursor.getColumnIndex(projections[2]);
                contact.addNumber(cursor.getString(idx));

                datas.add(contact);
            }
            //중요 : 사용 후 close 를 호출 하지 않으면 메모리 누수가 발생
            cursor.close();
        }

    }
}
