# 짭짤하나 백엔드 프로젝트

## :warning:  주의사항
1. 개발에 들어가기 전에 pre-commit을 설정할 것!
  
   - window에서 
     ```
     cp ./dev/pre-commit ./.git/hooks/pre-commit    
     ```
   - mac에서
     ```
     cp ./dev/pre-commit ./.git/hooks/pre-commit
     ```
     ```
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
     ```
     wget -q -O - --no-check-certificate https://raw.github.com/petervanderdoes/gitflow-avh/develop/contrib/gitflow-installer.sh install stable | bash
     ```
   - mac에서 설치하기
     ```
     brew install git-flow-avh
     ```
   설치 이후에 `git flow init`으로 시작