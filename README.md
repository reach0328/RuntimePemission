# Runtime Permission & Content Resolver
런타임권한 체크 및 전화번호 데이터 불러오기

## 런타임 권한 설정
```java
    // 0. 요청코드 세팅
    private final int REQ_CODE = 100;

    // 1. 권한체크 (런타임 권한은 마시멜로우 이상 버전에서만 사용가능)
    @TargetApi(Build.VERSION_CODES.M) // Target 지정 애너테이션
    private void checkPermission(){
        // 1.1 런타임 권한체크
        if( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED
                ){
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.CALL_PHONE
                    , Manifest.permission.READ_CONTACTS};
            // 1.3 시스템에 권한요청
            requestPermissions(permArr, REQ_CODE);
        }else{
            loadData();
        }
    }

    // 2. 권한체크 후 콜백되는 함수 < 사용자가 확인후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE){
            // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 됬으면
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED ){
                // 2.2 프로그램 실행
                loadData();
            }else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_LONG).show();
                // 선택 : 1 종료, 2 권한체크 다시 물어보기
                finish();
            }
        }
    }
```

## 컨텐트 리졸버 사용
```java
    // 1. 데이터에 접근하기위해 ContentResolver 를 불러온다.
    ContentResolver resolver = context.getContentResolver();

    // 2. 데이터 컨테츠 URI 정의
    // 전화번호 URI : ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    // 주소록 URI : ContactsContract.Contacts.CONTENT_URI
    //       * 주소록에서 전호번호를 가져오면 주소당 여러개의 전화번호가 존재할 수 있다
    //         HAS_PHONE_NUMBER : 전화번호가 있는지 확인하는 상수
    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    // 3. 데이터에서 가져올 데이터 컬럼명을 String 배열에 담는다.
    //    데이터컬럼명은 Content Uri 의 패키지에 들어있다.
    String proj[] = {
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,   // 데이터의 아이디
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // 이름
        ContactsContract.CommonDataKinds.Phone.NUMBER        // 전화번호
    };

    // 4. Content Resolver 로 쿼리한 데이터를 Cursor 에 담는다.
    Cursor cursor = resolver.query(uri, proj, null, null, null);

    // 5. Cursor 에 담긴 데이터를 반복문을 돌면서 꺼낸다
    if(cursor != null){
        while(cursor.moveToNext()){
            Contact contact = new Contact();
            // 데이터 컬럼 인덱스 가져오기
            int idx = cursor.getColumnIndex(proj[1]); // 위에 선언된 proj 변수의 두번째 인자로 index를 찾는다
            // 실제 데이터 가져오기
            contact.name = cursor.getString(idx);

            // 세팅된 값을 ArrayList 에 담아준다
            datas.add(contact);
        }
        // 6. 처리 후 커서를 닫아준다
        cursor.close();
    }
```

---------------------------------------------


##**Permission**
![](http://cfile27.uf.tistory.com/image/231B3A3756C675731FD5BF)
> **권한 부여 방법**

>>**권한을 주는 위치**
 - manifests 에 있는 AndroidManifest.xml에 기술한다.
 - 권한은 <application> 태크 밖에 기술해야 한다.

>>**권한 주는 명령**
 - uses-permission android:name="android.permission."
 - "android.permission." 안에 권한을 기술한다.
 - 팝업 중에 선택하면 된다.
 - 팝업에서 마우스로 한번 클릭하면 Hint로 권한의 기능이 무엇인지 간략하세 확인해 볼 수 있다.

> 참고로 안드로이드 6.0 마시멜로우 부터는 앱을 설치할때는 그냥 설치하고
> 앱 사용중에 권한을 요하는 기능을 사용하게되면 권한이 필요합니다. 
> 권한을 주시겠습니까 라고 물어봅니다.
> 각각의 권한 하나하나씩 주거나 회수할 수 있도록 되어 있습니다.
> 혹시 권한을 부여해 주도록 팝업으로 알리지 않는다면
> 앱 관리자에 들어가서 해당 앱 선택하고 권한을 설정해 주셔야 합니다.

[참고 : TadPermission](http://gun0912.tistory.com/55)
![](http://cfile27.uf.tistory.com/image/25320A3356C6767F0C170D)



##**[Content Provider](http://androidhuman.com/279)**
![](http://cfile23.uf.tistory.com/image/1276BA044AFDAE1D186D66)
> 다른 어플리케이션에서 특정 어플리케이션의 데이터베이스에 직접 접근하는 것은 불가능합니다.

>> 컨텐트 프로바이더는 어플리케이션 내의 데이터베이스를 다른 어플리케이션이 사용할 수 있는 "통로"를 제공해줍니다. 이 과정에서 컨텐트 프로바이더를 통해 외부 어플리케이션이 접근할 수 있는 범위를 정해줄 수 있어, "공유할 것만 공유하는" 것이 가능합니다.

![](http://cfile5.uf.tistory.com/image/1178C5014AFDAE6A083D75)
> 컨텐트 리졸버 및 컨텐트 프로바이더를 통한 데이터베이스 접근
>> 컨텐트 프로바이더의 주소는 컨텐트 프로바이더를 생성할 때 지정하며, URI(Uniform Resource Identifier) 형식으로 구성되어 있습니다. URI라는 단어 자체가 좀 생소할지도 모르겠습니다. 하지만 어렵게 생각할 것은 없습니다. URI는 우리가 인터넷 상의 자원의 주소를 표시할 때 쓰는 URL(Uniform Resource Locator)의 상위 개념으로, 어떠한 자원의 위치를 표기하기 위한 형식입니다.
