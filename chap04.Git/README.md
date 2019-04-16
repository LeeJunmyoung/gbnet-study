# Git
> 깃(Git)은 컴퓨터 파일의 변경사항을 추적하고 여러 명의 사용자들 간에 해당 파일들의 작업을 조율하기 위한 분산 버전 관리 시스템이다.

## GIT 구조
```
.git/hooks/             <- 클라이언트나 서버에 훅을 넣어두는 곳 ex)pre-push < push 이전에 작동하는 스크립트.
    /info/              <- .gitignore 처럼 무시할 파일의 패턴을 저장하는곳
    /logs/   
    /objects/           <- 기록을 담고있는 폴더
    /COMMIT_EDITMSG     <- 마지막 커밋 메시지
    /config/            <- 설정정보.
    /description        
    /FETCH_HEAD         <- 원격 저장소 정보.
    /HEAD               <- 현재 브랜치 정보
    /index              <- staging area 정보 저장.
    /ORIG_HEAD          <- 이전상태
```

## local-operations
![git-operations](./git_operations.png)


## Repository
![Git_workflow](./Git_workflow.png)
[Git-Workflow-Ref](https://sselab.de/lab2/public/wiki/sselab/index.php?title=Git#Fetch_URL_for_project)

## Remote Repository
```
git -bare init
```

## Config
```
git config --global user.name "LeeJunmyoung"
git config --global user.email "leejm1@gbnet.kr"

git config --list
```

## Project 생성
```
git init
# .git 파일생성 확인.

git remote add origin <Repository url>
git remote -v
# 원격저장소확인

git pull <name> <branch>
git branch --set-upstream-to=origin/<branch> master
# branch 원격 repository 연동.

git clone <url>
# 원격 저장소에서 가져오기.
```

## status & add & commit
![file_status](./file_status.png)
```
git status
# 파일의 상태를 확인할수 있다.

git add 
# 새로운 파일을 추적 할 수 있다. 이미 Tracked상태라면 staged 상태를 확인할수 있다.

git commit 
# 변경사항 커밋하기.
```

## reset & revert
```
git reset <시점 혹은 파일>
# --hard : 지정한 시점으로.
# --mixed : 지정한 시점 이후 변경된 파일 add 전 (default)
# --soft : 지정한 시점 이후 변경된 파일 add 후 commit 전.
# reset 이전으로 되돌리기 git reset --hard ORIG_HEAD

git revert <시점>
# 복구 시점 이후에 커밋이 많지 않거나 merge 커밋이 없는경우.
# reset과 달리 기존 이력이 사라지지 않음.
# revert한 시점에 이력을 무력화 시킬수 있음.
```

## merge & rebase
```
# 합쳐질 브랜치에서 command

git merge <브랜치명>
# 분기가 합쳐짐.

git rebase <브랜치명>
# 1개의 라인으로 표시.
```

## conflict
```
1. 충동나는 파일 commit 
2. conflict 확인후 소스 수정
3. git commit
```
