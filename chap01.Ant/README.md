# Ant
> 아파치 앤트(영어: Apache Ant)는 자바 프로그래밍 언어에서 사용하는 자동화된 소프트웨어 빌드 도구이다. 유닉스나 리눅스에서 사용되는 make와 비슷하나 자바언어로 구현되어 있어 자바 실행환경이 필요하며 자바 프로젝트들을 빌드하는데 표준으로 사용된다.  
> make와 눈에 띄는 가장 다른 부분은 빌드를 위한 환경구성을 XML 파일을 사용한다는 점이다. 기본적인 빌드 파일명은 build.xml 이다.  
> 로고는 이름(ANT)에 따라 개미 모양으로 만들어졌으나 이는 Another Neat Tool의 약어이다.  
> 최초 빌드 도구로서 제작되었으나 점점 많은 기능이 추가되면서 빌드와 배포, 유닛 테스트 등을 포함하는 통합 툴로서 발전되고 있다.  
[ANT-Manual](https://ant.apache.org/manual/index.html)  

## Build file

### 1. project
> 프로젝트는 3개의 속성을 가지고 있다.  
> 1. name : 프로젝트의 이름. 
> 2. default : 타겟이 없을때 디폴트로 사용됨. 
> 3. basedir : 모든 수행이 되는 기본 디렉토리 경로   

```
<project name="MyProject" default="dist" basedir=".">
</project>
```
  
### 2. targets
> 타겟은 다른 타겟에 의존적일수 있다.  
> 먼저 컴파일 한 후 배포 패키지를 빌드 할 수 있으므로 배포 타겟은 컴파일 타겟에 따라 달라집니다. Ant는 이러한 의존성을 해결합니다.
> 의존성 관계를 맺어서 순서대로 진행할수 있도록 해줌.
[targets-manual](https://ant.apache.org/manual/targets.html)

```
<target name="A"/>
<target name="B" depends="A"/>
<target name="C" depends="B"/>
<target name="D" depends="C,B,A"/>
# Call-Graph:  A → B → C → D

=====================================

<target name="myTarget" depends="myTarget.check" if="myTarget.run">
    <echo>Files foo.txt and bar.txt are present.</echo>
</target>

<target name="myTarget.check">
    <condition property="myTarget.run">
        <and>
            <available file="foo.txt"/>
            <available file="bar.txt"/>
        </and>
    </condition>
</target>
# Call-Graph:  myTarget.check → maybe(myTarget)

=====================================

<target name="create-directory-layout">
   ...
</target>
<extension-point name="ready-to-compile"
              depends="create-directory-layout"/>
<target name="compile" depends="ready-to-compile">
   ...
</target>
# Call-Graph:  create-directory-layout → 'empty slot' → compile

=====================================

<target name="generate-sources"
        extensionOf="ready-to-compile">
   ...
</target>
<target name="create-directory-layout">
   ...
</target>
<extension-point name="ready-to-compile"
              depends="create-directory-layout"/>
<target name="compile" depends="ready-to-compile">
   ...
</target>
# Call-Graph:  create-directory-layout → generate-sources  → compile

```
