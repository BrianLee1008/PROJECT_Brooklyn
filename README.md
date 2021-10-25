# Brooklyn
****Brooklyn_App_for_Buying_and_Selling_Used_Car****

## Table of Contents

1. 앱 설명 및 프로젝트 목표

2. 앱 주요 기능
3. 앱 시연
4. 앱 Tech Stack
5. 앱 Work Flow
6. 프로젝트를 진행하며 고민한 Technical Issue
7. 마무리

----------------------

## 1. 앱 설명 및 프로젝트 목표

### - ****Logo**** 

![pinterest_profile_image](https://user-images.githubusercontent.com/86999791/138602147-926bb76b-ffc5-4008-8646-e1194445057d.png)


### - ****App 이름**** 
    Brooklyn / 브루클린

### - ****제작 기간**** 
    21년 10월 14일 ~ 현재

### - ****App 간단 소개**** 
    Brooklyn은 차량에 관련한 물품을 유저끼리 거래하고, 자기 차량을 관리하는데에 도움을 주는 앱입니다.   
    유저들은 자기 차량에 필요한 물품을 살 수도 있고, 필요 없는 물품을 팔 수도 있습니다.
    또한 차량 수리센터나 스토어, 주유소를 찾아 정보를 볼 수 있습니다.   
    마지막으로 자기 차량에 대한 관리, 즉 유지비나 소모품 관리를 할 수도 있습니다.

### - ****Project 목표**** 
    1. 당근마켓, 마이현대 서비스를 벤치마킹하였기 때문에 핵심적인 기능은 최대한 그에 못지않은 서비스를 구현하는 것이 목표입니다.   

    2. 단순한 기능 구현 뿐 아니라 지속적인 성능 개선(유지보수)에도 적절한 코드 Architect를 목표로 하고 있습니다.

    3. 감에 의지하는 코드 스타일이 아닌 여러 이론적 토대위에서 올바른 코드를 작성하는 것이 목표 입니다.
    

    
--------------------



## 2. 앱 주요 기능

### ***1. 회원가입 & 로그인 후 내 프로필 변경***

    브루클린 회원가입 (Firebase Authentication 이메일 로그인)

<img src="https://user-images.githubusercontent.com/86999791/138660752-cad63799-bdcc-45de-86ee-1b28c7ae2aed.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660754-889e4ce6-ee69-40f0-9806-e7def66be813.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660757-1f1d482a-145d-422f-b607-36ea294725ec.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
</img><br/>

    프로필 설정 (권한체크 - 갤러리, 카메라 기능)  

<img src="https://user-images.githubusercontent.com/86999791/138660762-10c1bf70-67f6-428d-a8af-d52ce4be6503.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660767-44efae4d-75e7-43d3-b06f-ae7bbd3b3195.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <!-- <img src="https://user-images.githubusercontent.com/86999791/138660771-431a9e15-9a88-437c-b3f8-d58a8c7a4550.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> --> <img src="https://user-images.githubusercontent.com/86999791/138660775-3f2b9650-62b5-444a-b01d-c80312479a59.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660777-02c4062f-0de2-48cb-ac5f-9aeaf1640c92.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <!-- <img src="https://user-images.githubusercontent.com/86999791/138660780-d1888ce5-2b2d-49be-be22-c8c8ec0370f6.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> --> <img src="https://user-images.githubusercontent.com/86999791/138660783-24e7791b-df48-4af1-aeee-abb6405ae055.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660786-931b518d-2c91-439c-8f67-a49f5e33143d.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">


    내 프로필 업데이트 (Firebase DB 저장)

<img src="https://user-images.githubusercontent.com/86999791/138660793-aca9881d-dc47-4296-a7d3-3a148189c66a.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660796-f57bf7b6-967d-48e9-b0dd-952891a655a8.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660799-b2bad8f2-2e26-4f06-b7d0-1867203fc023.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660810-48cb4403-4aba-4e31-a24e-0c46ce4699a0.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck"> <img src="https://user-images.githubusercontent.com/86999791/138660812-8ee9bf41-c0a0-4cd6-8e36-a0910b878f81.jpg" width="288px" height="640px" title="px(픽셀) 크기 설정" alt="RubberDuck">
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

    1. 현대 Open API 이용해 손쉬운 차량 관리
    2. Kakao, Google을 이용한 SNS 로그인
    3. 아이템 상세페이지 Coordinator 디자인 적용해 좀더 인터렉션하게 확장
    4. 등록한 아이템 수정 기능

-------------------------

## 3. 앱 시연

..GIF 로 앱 시연 -> 설명

1. 회원가입, 로그인 (권한)

2. 프로필 변경 후 저장

3. 아이템 등록 - 아이템 리스트에서 등록한 아이템 확인

4. 다른 사람 아이템 상세페이지에서 채팅거래 누르고 채팅 리스트 확인

5. 상대방과 실시간 채팅

6. 지도에서 내 현 위치 클릭 - 지도에 근처 주유소 검색 후 검색기록 확인, 지우기

7. 지도에서 정비소 검색 후 마커 확인, 정비소 세부 사항은 BottomSheet에서 확인

### ***🔨 업데이트 예정 🔨***

9. 내 아이템 상세 페이지에서 뷰페이지 이미지 확인 후 아이템 수정. 저장


---------------------

## 4. 앱 Tech Stack

### ****- Skill****

- 🔤 Language
    - Kotlin
        
- 🏢 Architecture
    - MVVM
    - Google Architecture

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
    - Hyundai API

- ⚙️ ETC
    - AAC
    - Coroutine
    - UI Custom - Constraint Layout / Coordinator Layout
    - Lifecycle

-------------------

## 5. 앱 Work Flow

데이터 흐름 그림으로 설명

-----------------------

## 6. 프로젝트를 진행하며 고민한 Issue

- UI Controller는 UI 및 User 상호작용을 처리하는 로직만 관여하도록 노력 했습니다.   

- Repository Pattern을 사용한 데이터 플로우를 고민했습니다.   
- 여러가지 API를 사용하는 만큼 위화감 없이 보여질 수 있도록 디자인하는 것을 고민했습니다.
- 향후 개선 및 유지보수와 가독성을 위해 1 함수 1 로직 구현을 노력했습니다.


---------------------

## 7. 마무리

혼자서, 그리고 시작부터 끝까지 처음으로 만들어본 앱이여서 그런지 고생도 많이 했지만 그만큼 애정이 많이 들어간 것 같다.      
개발 중간 중간 마다 지금까지 겪어보지 못했던 수많은 오류들과 버그, 기술적인 한계에 부딪혀 머리를 싸매고,        
잠도 못잘 때도 많았지만 그만큼 하루하루가 성장해가는 감사한 시간들이었다.
