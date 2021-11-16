# Brooklyn
****Brooklyn_App_for_Buying_and_Selling_Used_Car****

## Table of Contents

1. 앱 설명 및 프로젝트 목표

2. 앱 주요 기능 및 시연
3. 앱 Tech Stack
4. 앱 구간별 들어간 Skill
5. 프로젝트를 진행하며 고민한 Technical Issue
6. 마무리

----------------------

## 1. 앱 설명 및 프로젝트 목표

### - ****Logo**** 

![pinterest_profile_image](https://user-images.githubusercontent.com/86999791/138602147-926bb76b-ffc5-4008-8646-e1194445057d.png)


### - ****App 이름**** 
    Brooklyn / 브루클린

### - ****제작 기간**** 
    21년 10월 14일 ~ 10월 27

### - ****App 간단 소개**** 
    Brooklyn은 차량에 관련한 물품을 유저끼리 거래하고, 자기 차량을 관리하는데에 도움을 주는 앱입니다.   
    유저들은 자기 차량에 필요한 물품을 살 수도 있고, 필요 없는 물품을 팔 수도 있습니다.
    또한 지도 검색을 통해 차량 수리센터나 주유소, 또한 어떤 장소든 찾아 정보를 볼 수 있습니다.

### - ****Project 목표**** 
    1. 당근마켓 서비스를 벤치마킹하였기 때문에 핵심적인 기능은 최대한 그에 못지 않은 서비스를 구현하는 것이 목표입니다.   

    2. 단순한 기능 구현 뿐 아니라 지속적인 성능 개선(유지보수)에도 적절한 코드 Architect를 목표로 하고 있습니다.

    3. 감에 의지하는 코드 스타일이 아닌 여러 이론적 토대위에서 올바른 코드를 작성하는 것이 목표 입니다.
    
-------------------------

## 2. 앱 주요 기능 및 시연

### ***1. 회원가입***

    1. 회원가입을 할 때 이메일과 비밀번호를 기입하지 않으면 가입을 못하도록 했습니다.
    2. 또한 비밀번호 재확인을 했을 때 맞지 않는 경우에도 가입을 하지 못하게 했습니다.
    3. 프로필 사진은 갤러리 / 카메라 두가지 버젼으로 설정할 수 있습니다.
    4. 닉네임은 필수사항으로 해놨습니다.
    5. 카메라로 사진을 찍어 프로필 이미지를 정할 수 있게 했습니다.
    6. 카메라를 사용하든 갤러리를 사용하든 각 기능에 필요한 권한을 승인하도록 체크 했습니다.
    7. 가입을 하면 자신이 설정한 프로필 이미지와 닉네임을 '내 공장' 페이지에서 확인할 수 있습니다.

![1 SignUp_Gallery](https://user-images.githubusercontent.com/86999791/138711836-e7a57e56-3e3f-4beb-a34c-a70e9489ce03.gif)
![2 SignUp_Camera](https://user-images.githubusercontent.com/86999791/138711853-87449977-ad97-49aa-a134-4f48aa7d57a9.gif)

### ***2. 로그인***

    1. 추후 SNS로그인 기능을 만들기 위해 로그인 방법을 따로 모으는 UI Controller를 구성했습니다.
    2. 이메일과 비밀번호를 입력하지 않으면 가입하지 못하도록 해놨습니다.
    3. 로그인을 하면 가입할 때, 혹은 업데이트한 프로필 이미지와 닉네임을 확인 할 수 있습니다.

![3 login](https://user-images.githubusercontent.com/86999791/138711864-8ab75077-fce2-4700-87ea-a9ee4bb33cee.gif)


### ***3. 프로필 변경 후 저장***

    1. 프로필은 언제든지 변경하고 업데이트 할 수 있습니다.
    2. 마찬가지로 갤러리 / 카메라 두 가지 방법으로 프로필을 업데이트 할 수 있습니다.

![4 UpdateProfile](https://user-images.githubusercontent.com/86999791/138711866-5afa1bfc-4c70-405e-925e-8676bcf60f15.gif)

### ***4. 아이템 등록 - 아이템 리스트에서 등록한 아이템 확인***

    1. 자신이 팔고싶은 물건을 등록할 수 있습니다.
    2. 이미지는 갤러리에서 복수로 선택할 수 있고, 카메라로 찍어 올릴 수도 있습니다.
    3. 업로드한 이미지를 클릭해서 지울 수 있습니다.
    4. 물건을 업로드 하면 '오늘의 매물' 페이지에서 확인할 수 있습니다.

![5  UploadArticle_CheckingList](https://user-images.githubusercontent.com/86999791/138714332-ffa2546a-9ebc-4565-8342-50f087fab89c.gif)

### ***5. 다른 사람 물건 상세페이지에서 채팅거래 누르고 채팅 리스트 확인***

    1. 관심이 있는 물건을 누르면 상세페이지로 이동해 정보를 볼 수 있습니다.
    2. 다른사람이 올린 이미지를 스와이프 형식으로 넘겨볼 수 있습니다.
    3. 물건을 사고 싶은 마음이 생기면 '판매자와 채팅하기' 버튼을 눌러 채팅방을 만들 수 있습니다.
    4. 채팅 목록에서 만들어진 채팅방을 확인할 수 있습니다.

![6  CheckArticles](https://user-images.githubusercontent.com/86999791/138714342-d0cf5721-3796-4007-b106-8494affc08ec.gif)

### ***6. 상대방과 실시간 채팅***

    1. 판매자와 실시간 채팅을 할 수 있습니다.

![7  Chating](https://user-images.githubusercontent.com/86999791/138714352-6253c807-d25a-4969-940d-9f9dab7a357e.gif)

### ***7. 지도에서 내 현 위치 클릭 - 지도에 근처 주유소 검색 후 검색기록 확인, 지우기***

    1. 지도를 열어 현재 나의 위치를 확인할 수 있습니다.
    2. 검색을 통해 알고자 하는 위치의 마커를 볼 수 있습니다.
    3. 검색한 검색어는 최근 검색어에 저장 됩니다.
    4. 최근 검색어는 원한다면 삭제할 수 있습니다.
    5. 위치 정보를 인터렉션한 위젯으로 확인할 수 있습니다.

![8  Location](https://user-images.githubusercontent.com/86999791/138714363-97c03c5e-3eac-4dab-ba22-acc07130f15c.gif)

-----------------------

## 3. 앱 Tech Stack

### ****- Skill****

- 🔤 Language
    - Kotlin
        
- 🏢 Architecture
    - MVVM

- 📚 Lybrary
    - Glide
    - ViewPager2
    - Retrofit2
    - Room

- 🔥 Firebase
    - Authentication
    - RealTime Database
    - Storage

- 💾 API
    - Google Map API
    - T-Map POI API

- ⚙️ ETC
    - AAC
    - Coroutine
    - Coordinator Layout

-------------------------------



## 4. 앱 구간별 들어간 Skill

### ***1. 회원가입 & 로그인 후 내 프로필 변경***

    브루클린 회원가입 (Firebase Authentication 이메일 로그인)

<img src="https://user-images.githubusercontent.com/86999791/138660752-cad63799-bdcc-45de-86ee-1b28c7ae2aed.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660754-889e4ce6-ee69-40f0-9806-e7def66be813.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660757-1f1d482a-145d-422f-b607-36ea294725ec.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    프로필 설정 (권한체크 - 갤러리, 카메라 기능)  

<img src="https://user-images.githubusercontent.com/86999791/138660762-10c1bf70-67f6-428d-a8af-d52ce4be6503.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660783-24e7791b-df48-4af1-aeee-abb6405ae055.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660786-931b518d-2c91-439c-8f67-a49f5e33143d.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">


    내 프로필 업데이트 (Firebase DB 저장)

<img src="https://user-images.githubusercontent.com/86999791/138660796-f57bf7b6-967d-48e9-b0dd-952891a655a8.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660810-48cb4403-4aba-4e31-a24e-0c46ce4699a0.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660812-8ee9bf41-c0a0-4cd6-8e36-a0910b878f81.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>


### ***2. 아이템 등록***

    아이템 등록 (갤러리 - 카메라 기능이용해 복수 이미지 업로드, Firebase DB 저장 )

<img src="https://user-images.githubusercontent.com/86999791/138660816-d96afb37-b826-453f-9116-b6286ce491ce.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660818-517e1879-41d8-4e69-883c-be470443c24b.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660826-c1d33161-d094-4bea-9d94-17ddb0c9fa0d.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    아이템 등록 확인 (Firebase DB 에서 아이템 가져오기)
<img src="https://user-images.githubusercontent.com/86999791/138660827-6b2d0946-c2e2-440a-9277-b8496498516f.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>


### ***3. 관심가는 다른 유저의 물품 상세보기***

    아이템 상세페이지 이동 (Firebase DB, ViewPager2)
<img src="https://user-images.githubusercontent.com/86999791/138660829-53a06c92-bb60-4afd-940f-ff9bcce5f40f.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660831-4664d020-3da0-40c7-b091-96c42c7c0f95.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

### ****4. 판매자와 채팅****    

    채팅방 생성 (Firebase DB)
<img src="https://user-images.githubusercontent.com/86999791/138660836-4b8dd1f6-946c-4b78-b595-afbb17e8ac7b.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660839-52e5fcb1-141c-473a-8bec-f4ccea0011cf.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    실시간 채팅 (Firebase DB)

<img src="https://user-images.githubusercontent.com/86999791/138660841-e385df96-38de-411c-ad99-2db0b692a8d8.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660849-8c2433cc-dc69-4713-bb2f-b4b79fd0933c.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">

</img><br/>


### ***5. 지도 검색***

    지도 (GoogleMap API, Retrofit2, 현재 위치 권한 체크)

<img src="https://user-images.githubusercontent.com/86999791/138660851-3226c74d-e86e-40d1-ad15-ba6986b9d0f8.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660855-eb040021-2f18-43c5-8616-87ab0a678393.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    위치 검색 후 최근 검색 확인 (T-Map POI API, Retrofit2, Room)

<img src="https://user-images.githubusercontent.com/86999791/138660860-0a4f10d6-edb6-4a0f-9dd6-9ee5f95dbe76.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660871-108687dc-aea1-47a0-8573-656b2d931a2c.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>


    검색한 위치 기반 POI 데이터 Google Map에 마커 찍고 정보 확인 
    (GoogleMap API, T-Map POI API, Retrofit2, CoordinatorLayout)

<img src="https://user-images.githubusercontent.com/86999791/138660869-98d49355-5c6b-4fe5-97ab-41e0bc819037.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660864-002f9f95-1654-4b8d-9beb-5e15c3456498.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    최근 검색어 삭제 (Room)

<img src="https://user-images.githubusercontent.com/86999791/138660871-108687dc-aea1-47a0-8573-656b2d931a2c.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138669687-ed92ff80-e17d-43ea-bdd3-35b16514c037.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>



### ***🔨 업데이트 예정 🔨***

    2. Kakao, Google을 이용한 SNS 로그인
    3. 아이템 상세페이지 Coordinator 디자인 적용해 좀더 인터렉션하게 확장
    4. 등록한 아이템 수정 기능


-----------------------

## 5. 프로젝트를 진행하며 고민한 Issue

- UI Controller는 UI 및 User 상호작용을 처리하는 로직만 관여하도록 노력 했습니다.   

- Repository Pattern을 사용한 데이터 플로우를 고민했습니다.   
- 여러가지 API를 사용하는 만큼 위화감 없이 보여질 수 있도록 디자인하는 것을 고민했습니다.
- 향후 개선 및 유지보수와 가독성을 위해 1 함수 1 로직 구현을 노력했습니다.


---------------------

## 6. 마무리

아직 업데이트할 부분이 남았지만 아쉬운점과 좋았던 점을 나열해보고자 한다.

두 가지 아쉬운 점은

리팩토링을 해보겠다고 UI Controller에 모든 코드를 넣고 나중에 MVVM 아키텍쳐를 주입해 고생을 많이 한 것과,   

완전히 혼자서 개발을 했다는 점이다. 혼자서 개발을 하니 피드백을 받기가 쉽지 않았고, 전적인 내 의견만 반영되어 코드의 방향성을 알기가 쉽지 않았다.   
다음 번에는 꼭 팀을 이루어 다른 사람들과 협업하는 형식으로 해보고 싶은 마음이다.

좋았던 점은 아쉬었던 점과 동일하게      
MVVM 아키텍쳐를 나중에 주입함으로 아키텍쳐의 중요성을 알게 되었다.

처음 앱을 개발할 당시에는 내가 알고있는 고급기술을 다 때려 넣으면 뭔가 대단한 것이 나올 줄 알았지만,    
가장 중요한 것은 언제 누가 내 코드를 보더라도 이해가 가능하고 협업이 가능한     
효율적이고 확장이 쉬운 코드여야 한다는 것이고,  그 근간이 바로 아키텍쳐에서 나온다는 사실을 알게되어 좋았다.

시작부터 끝까지 혼자 만들어본 앱이여서 그런지 고생도 많이 했지만 그만큼 애정이 많이 들어간 것 같다.      
개발 중간 중간 마다 지금까지 겪어보지 못했던 수많은 오류들과 버그, 기술적인 한계에 부딪혀 머리를 싸매고,        
잠도 못잘 때도 많았지만 그만큼 하루하루가 성장해가는 감사한 시간들이었다.

