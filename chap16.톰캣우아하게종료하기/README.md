# Tomcat Graceful Shutdown


### 기존 톰캣 재기동시 (스크립트 사용)
```
#!/bin/bash

# 1. 웹서버 shutdown
/path/shutdown.sh
sleep 3
# ps -elf | grep service | grep java | grep -v grep | awk '{print $4}' | xargs kill -9

# 2. 웹서버 patch
/path/ant.sh

# 4. 웹서버 restart
sleep 3
/path/startup.sh

# slack
sleep 1
/path/notice-complete-deploy.sh "WEB"

# tomcat check
tail -f /path/catalina.out
```



### ./shutdown.sh
```
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# -----------------------------------------------------------------------------
# Stop script for the CATALINA Server
# -----------------------------------------------------------------------------

# Better OS/400 detection: see Bugzilla 31132
os400=false
case "`uname`" in
OS400*) os400=true;;
esac

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
EXECUTABLE=catalina.sh

# Check that target executable exists
if $os400; then
  # -x will Only work on the os400 if the files are:
  # 1. owned by the user
  # 2. owned by the PRIMARY group of the user
  # this will not work if the user belongs in secondary groups
  eval
else
  if [ ! -x "$PRGDIR"/"$EXECUTABLE" ]; then
    echo "Cannot find $PRGDIR/$EXECUTABLE"
    echo "The file is absent or does not have execute permission"
    echo "This file is needed to run this program"
    exit 1
  fi
fi

exec "$PRGDIR"/"$EXECUTABLE" stop "$@"
```


### catalina.sh
```
elif [ "$1" = "stop" ] ; then

  shift

  SLEEP=5
  if [ ! -z "$1" ]; then
    echo $1 | grep "[^0-9]" >/dev/null 2>&1
    if [ $? -gt 0 ]; then
      SLEEP=$1
      shift
    fi
  fi

  FORCE=0
  if [ "$1" = "-force" ]; then
    shift
    FORCE=1
  fi

  if [ ! -z "$CATALINA_PID" ]; then
    if [ -f "$CATALINA_PID" ]; then
      if [ -s "$CATALINA_PID" ]; then
        kill -0 `cat "$CATALINA_PID"` >/dev/null 2>&1
        if [ $? -gt 0 ]; then
          exit 1
        fi
      else
        echo "PID file is empty and has been ignored."
      fi
    else
      echo "\$CATALINA_PID was set but the specified file does not exist. Is Tomcat running? Stop aborted."
      exit 1
    fi
  fi

  eval "\"$_RUNJAVA\"" $JAVA_OPTS \
    -D$ENDORSED_PROP="\"$JAVA_ENDORSED_DIRS\"" \
    -classpath "\"$CLASSPATH\"" \
    -Dcatalina.base="\"$CATALINA_BASE\"" \
    -Dcatalina.home="\"$CATALINA_HOME\"" \
    -Djava.io.tmpdir="\"$CATALINA_TMPDIR\"" \
    org.apache.catalina.startup.Bootstrap "$@" stop

  # stop failed. Shutdown port disabled? Try a normal kill.
  if [ $? != 0 ]; then
    if [ ! -z "$CATALINA_PID" ]; then
      echo "The stop command failed. Attempting to signal the process to stop through OS signal."
      kill -15 `cat "$CATALINA_PID"` >/dev/null 2>&1
    fi
  fi

  if [ ! -z "$CATALINA_PID" ]; then
    if [ -f "$CATALINA_PID" ]; then
      while [ $SLEEP -ge 0 ]; do
        kill -0 `cat "$CATALINA_PID"` >/dev/null 2>&1
        if [ $? -gt 0 ]; then
          rm -f "$CATALINA_PID" >/dev/null 2>&1
          if [ $? != 0 ]; then
            if [ -w "$CATALINA_PID" ]; then
              cat /dev/null > "$CATALINA_PID"
              # If Tomcat has stopped don't try and force a stop with an empty PID file
              FORCE=0
            else
              echo "The PID file could not be removed or cleared."
            fi
          fi
          echo "Tomcat stopped."
          break
        fi
        if [ $SLEEP -gt 0 ]; then
          sleep 1
        fi
        if [ $SLEEP -eq 0 ]; then
          echo "Tomcat did not stop in time."
          if [ $FORCE -eq 0 ]; then
            echo "PID file was not removed."
          fi
          echo "To aid diagnostics a thread dump has been written to standard out."
          kill -3 `cat "$CATALINA_PID"`
        fi
        SLEEP=`expr $SLEEP - 1 `
      done
    fi
  fi
```


### kill 시그널
```
> kill -l

1) SIGHUP	 2) SIGINT	 3) SIGQUIT	 4) SIGILL	 5) SIGTRAP
6) SIGABRT	 7) SIGBUS	 8) SIGFPE	 9) SIGKILL	10) SIGUSR1
11) SIGSEGV	12) SIGUSR2	13) SIGPIPE	14) SIGALRM	15) SIGTERM
16) SIGSTKFLT	17) SIGCHLD	18) SIGCONT	19) SIGSTOP	20) SIGTSTP
21) SIGTTIN	22) SIGTTOU	23) SIGURG	24) SIGXCPU	25) SIGXFSZ
26) SIGVTALRM	27) SIGPROF	28) SIGWINCH	29) SIGIO	30) SIGPWR
31) SIGSYS	34) SIGRTMIN	35) SIGRTMIN+1	36) SIGRTMIN+2	37) SIGRTMIN+3
38) SIGRTMIN+4	39) SIGRTMIN+5	40) SIGRTMIN+6	41) SIGRTMIN+7	42) SIGRTMIN+8
43) SIGRTMIN+9	44) SIGRTMIN+10	45) SIGRTMIN+11	46) SIGRTMIN+12	47) SIGRTMIN+13
48) SIGRTMIN+14	49) SIGRTMIN+15	50) SIGRTMAX-14	51) SIGRTMAX-13	52) SIGRTMAX-12
53) SIGRTMAX-11	54) SIGRTMAX-10	55) SIGRTMAX-9	56) SIGRTMAX-8	57) SIGRTMAX-7
58) SIGRTMAX-6	59) SIGRTMAX-5	60) SIGRTMAX-4	61) SIGRTMAX-3	62) SIGRTMAX-2
63) SIGRTMAX-1	64) SIGRTMAX
```
> kill 시그널 값을 주지 않았을 경우에는 default 로 SIGTERM(15) 시그널이 날라간다.


### kill -9 를 쓰면 안되는 이유

[1]
프로세스 종료의 의미로 사용하는 signal (INT, HUP, TERM 등)을 받으면 사용했던 리소스(파일, 소켓, DB 연결 등)를 닫고 저장하는 cleanup 함수를 작성하고 이를 시그널 핸들러로 등록한다.

유닉스의 표준상 handler를 등록할 수 없는 2개의 시그널이 있는데 바로 SIGKILL(9)과 SIGSTOP(19) 이며 `kill-9` 명령어는 KILL signal을 보내겠다는 의미이다.

즉, `kill -9`로 signal을 보내면 개발자가 구현한 종료 함수가 호출되지 않고 즉시 프로세스가 종료되어 버리므로 데이터가 유실되거나 리소스가 제대로 안 닫히는 문제가 발생할 수 있다.

[2]
아직 처리중인 클라이언트가 남아 있거나 등의 여러 가지 이유로 톰캣을 구동한 Java VM이 깨끗하게 종료되지 않는 경우가 있다.

보통 이런 경우는 Server Port를 사용하는 쓰레드(Thread)는 종료되었지만 Connector 쓰레드는 종료되지 않아 shutdown.sh로 종료할 수 없다.

이럴 때는 직업 pid를 얻어서 kill명령어로 시그널을 전송해서 종료시켜야 한다. 이 때 KILL(9) 시그널을 전송하는데 KILL 시그널은 프로세스가 종료 루틴을 제대로 호출하지 못할 수 있으므로 좋은 방법은 아니다.


### gracefulShutdown.sh
```
#!/bin/sh

if [ -z "$1" ]
then
    echo "사용법 : gracefulShutdown process"
    exit
fi

target=$1
loop=1
limitLoop=30

ps -ef | grep $target | grep -v grep | grep -v vi | grep -v sh | awk '{ print $2 }' | \
while read PID
do
    echo "[$PID] 프로세스에 종료 신호를 전송합니다."
    kill -15 $PID
done

while [ $loop -le $limitLoop ]
do
    PID_LIST=(`ps -ef | grep $target | grep -v grep | grep -v vi | grep -v sh | awk '{ print $2 }'`)

    if [ ${#PID_LIST[@]} = 0 ]
    then
        echo "프로세스가 완전히 종료되었습니다."
        exit
    else
        for pid in "${PID_LIST[@]}"
        do
            echo "[$loop/$limitLoop] $pid 프로세스 종료를 기다리는중입니다."
        done

        loop=$(( $loop + 1 ))
        sleep 1
        continue
    fi
done

echo "!!! 대기시간이 만료되었습니다. 강제종료를 시도합니다. !!!"
ps -ef | grep $target | grep -v grep | grep -v vi | grep -v sh | awk '{ print $2 }' | \
while read PID
do
    echo "[$PID] 프로세스에 강제종료 신호를 전송합니다."
    kill -9 $PID
done
```

### 우아하게 죽여보자
```
> gracefulShutdown.sh service

[648] 프로세스에 종료 신호를 전송합니다.
[1/30] 648 프로세스 종료를 기다리는중입니다.
프로세스가 완전히 종료되었습니다.
```



## 참고
[팀장님 발표 자료]
[Unix, Linux에서 Kill 명령어로 안전하게 프로세스 종료 시키는 방법](https://www.lesstif.com/system-admin/unix-linux-kill-12943674.html)
[위키백과, kill](https://ko.wikipedia.org/wiki/Kill)
[KLDP array](http://wiki.kldp.org/HOWTO/html/Adv-Bash-Scr-HOWTO/arrays.html)
[awk](https://zetawiki.com/wiki/%EB%A6%AC%EB%88%85%EC%8A%A4_awk)
[read](https://zetawiki.com/wiki/%EB%A6%AC%EB%88%85%EC%8A%A4_read)
[ps](https://zetawiki.com/wiki/%EB%A6%AC%EB%88%85%EC%8A%A4_ps)
