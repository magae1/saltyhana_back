# 짭짤하나 백엔드 프로젝트

## :construction: 기능 개발 과정
1. feature 브랜치 생성
   ```shell
   git flow feature start <브랜치명>
   ```
   > 브랜치명은 개발하고자 하는 기능 이름을 하는게 정석이지만, 
   > 이름이 중복되면 원격 레포지토리에서 충돌이 발생할 수 있습니다. 
   > 따라서, 다른 사람이 사용하지 않을 것 같은 이름으로 정해주셔야 합니다.

2. 생성된 브랜치에서 기능 개발
   - 커밋은 최소한의 작업 단위로 여러개 만드셔도 됩니다.
   - 단, 커밋 메시지에 어떤 작업을 했는지 작성해주셔야 합니다. 예를 들면,
     - ✅ BankService에 대한 테스트 코드 작성
     - ♻️ BankRepository 리펙토링
     - 🏷️ BankAccountDTO 변경
     - 👔 BankAlgorithm 로직 추가
   - 기능 개발 중인 브랜치는 마음대로 푸시하셔도 됩니다. develop 브랜치에 전혀 영향이 가지 않습니다.

3. (선택) 불필요한 커밋들을 삭제하고 싶거나 커밋의 수를 줄이고 싶다면 squash를 이용해 커밋을 합칠 수도 있습니다.
   1. 합치고 싶은 첫번째 커밋 선택 후 우클릭 후 `Interactively Rebase from Here...`  
      ![squash1](/dev/images/squash1.png)
   2. 합치고 싶은 커밋들 선택 후 `SQUASH` 클릭  
      ![squash2](/dev/images/squash2.png)
   3. 커밋 메시지 수정 이후 `START REBASING` 클릭
      ![squash3](/dev/images/squash3.png)
   > 단, 작업 중인 feature 브랜치에서만 하셔야 합니다.

4. 개발이 끝나면 원격 레포지토리에 푸시
   - 브랜치가 원격 레포지토리에 올라간 적 없다면,
     ```shell
     git push --set-upstream
     ```
   - 브랜치가 원격 레포지토리에 올라가 있다면,
     ```shell
     git push
     ```
5. 이후, PR을 작성
6. PR이 승인되면 작업들을 develop 브랜치에 반영
   1. 로컬 develop 브랜치 최신화(원격 레포지토리로부터 풀)
      ```shell
      git pull origin develop
      ```
   2. feature 브랜치를 develop 브랜치에 병합
      ```shell
      git flow feature finish <브랜치명>
      ```
   3. develop 브랜치를 원격 레포지토리에 푸시
      ```shell
      git push origin develop
      ```

## :warning:  주의사항
1. 개발에 들어가기 전에 pre-commit을 설정할 것!
  
   - window에서 
     ```shell
     cp ./dev/pre-commit ./.git/hooks/pre-commit    
     ```
   - mac에서
     ```shell
     cp ./dev/pre-commit ./.git/hooks/pre-commit
     ```
     ```shell
     chmod +x ./.git/hooks/pre-commit
     ```

2. `Gitmoji Plus: Commit Button` 플러그인 추가할 것!
    
   [깃모지란?](https://gitmoji.dev/)

   [플러그인 자세히 보기](https://plugins.jetbrains.com/plugin/12383-gitmoji-plus-commit-button)
    
   1. `Settings` > `Plugins` > `MarketPlace` > `Gitmoji Plus: Commit Button`검색 후 설치
   
   2. 설치 완료 시 아래 화면처럼 버튼이 추가됨.
   ![gitmoji](/dev/images/gitmoji_plus.png)
    
3. git flow 설치할 것!
   
   [필독: git flow 가이드라인](https://danielkummer.github.io/git-flow-cheatsheet/index.ko_KR.html)
   
   - window에서 설치하기
     ```shell
     wget -q -O - --no-check-certificate https://raw.github.com/petervanderdoes/gitflow-avh/develop/contrib/gitflow-installer.sh install stable | bash
     ```
   - mac에서 설치하기
     ```shell
     brew install git-flow-avh
     ```
   설치 이후에 `git flow init`으로 시작

## :muscle: 도움될만한 설정들

1. 서버 실행시킬 때 자동으로 브라우저 실행하기
   
   > 인텔리제이는 자동적으로 빌드/실행 환경에 대해 설정하고 스크립트를 짜줍니다.
   이 자동으로 만들어진 스크립트를 추가로 설정할 수 있습니다.
   1. 빌드 실행 버튼 클릭 후 `:`  > `Edit...` 클릭
      ![build_env_set1](/dev/images/build_env_set1.png)
   2. `Before Launch` 옆에 `+` 버튼 클릭
      ![build_env_set1](/dev/images/build_env_set2.png)
   3. `Launch Web Browser` 버튼 클릭
      ![build_env_set1](/dev/images/build_env_set3.png)
   4. `http://localhost:9090/swagger-ui.html` 입력 후 Ok
      ![build_env_set1](/dev/images/build_env_set4.png)
   5. 서버 실행 시 swagger 페이지가 자동으로 실행
      ![build_env_set1](/dev/images/build_env_set5.png)

2. 깃모지가 바로 보이게 하기
   
    지금은 `:123:` 이런 형태로 이모지가 보이지만 unicode로 인코딩 형식을 바꾸면 에디터 상에서도 이모지를 볼 수 있습니다.
   1. `Settings` > `Gitmoji Plus: Commit Button` 설정 탭에서 `Use unicode emoji instead of text version` 체크

      ![gitmoji_set](/dev/images/gitmoji_set1.png)

   2. 깃 메시지에서 이모지가 보이기 시작

      ![gitmoji_set2](/dev/images/gitmoji_set2.png)

