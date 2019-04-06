# Ant
> 아파치 앤트(영어: Apache Ant)는 자바 프로그래밍 언어에서 사용하는 자동화된 소프트웨어 빌드 도구이다. 유닉스나 리눅스에서 사용되는 make와 비슷하나 자바언어로 구현되어 있어 자바 실행환경이 필요하며 자바 프로젝트들을 빌드하는데 표준으로 사용된다.  
> make와 눈에 띄는 가장 다른 부분은 빌드를 위한 환경구성을 XML 파일을 사용한다는 점이다. 기본적인 빌드 파일명은 build.xml 이다.  
> 로고는 이름(ANT)에 따라 개미 모양으로 만들어졌으나 이는 Another Neat Tool의 약어이다.  
> 최초 빌드 도구로서 제작되었으나 점점 많은 기능이 추가되면서 빌드와 배포, 유닛 테스트 등을 포함하는 통합 툴로서 발전되고 있다.  
[ANT-Manual](https://ant.apache.org/manual/index.html)  

## Build file

### 1. Project
> 프로젝트는 3개의 속성을 가지고 있다.  
> 1. name : 프로젝트의 이름. 
> 2. default : 타겟이 없을때 디폴트로 사용됨. 
> 3. basedir : 모든 수행이 되는 기본 디렉토리 경로   

```
<project name="MyProject" default="dist" basedir=".">
</project>
```
  
### 2. Targets
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

### Tasks
> tasks는 실행할수 있는 Code 이다.  
> 여러 속성 (또는 원하는 경우 인수)을 가질 수 있다. 속성의 값은 속성에 대한 참조를 포함 할 수 있습니다.  
[tasks-WYO](https://ant.apache.org/manual/develop.html#writingowntask)

```
# java
package com.mydomain;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class MyVeryOwnTask extends Task {
    private String msg;

    // The method executing the task
    public void execute() throws BuildException {
        System.out.println(msg);
    }

    // The setter for the "message" attribute
    public void setMessage(String msg) {
        this.msg = msg;
    }
}

=====================================

# ANT - example 1
<?xml version="1.0"?>

<project name="OwnTaskExample" default="main" basedir=".">
  <taskdef name="mytask" classname="com.mydomain.MyVeryOwnTask"/>

  <target name="main">
    <mytask message="Hello World! MyVeryOwnTask works!"/>
  </target>
</project>

=====================================

# ANT - example 2
<?xml version="1.0"?>

<project name="OwnTaskExample2" default="main" basedir=".">

  <target name="build" >
    <mkdir dir="build"/>
    <javac srcdir="source" destdir="build"/>
  </target>

  <target name="declare" depends="build">
    <taskdef name="mytask"
        classname="com.mydomain.MyVeryOwnTask"
        classpath="build"/>
  </target>

  <target name="main" depends="declare">
    <mytask message="Hello World! MyVeryOwnTask works!"/>
  </target>
</project>

```

### Properties
> properties는 빌드 프로세스를 사용자가 정의하거나 빌드 파일의 반복되는 문자열을 커스터마이징을 할수 있다.
[properties-manual](https://ant.apache.org/manual/Tasks/property.html)

```
<property name="foo.dist" value="dist"/>
```

### Example build file
```
<project name="MyProject" default="dist" basedir=".">
  <description>
    simple example build file
  </description>
  <!-- set global properties for this build -->
  <property name="src" location="src"/>
  <property name="build" location="build"/>
  <property name="dist" location="dist"/>

  <target name="init">
    <!-- Create the time stamp -->
    <tstamp/>
    <!-- Create the build directory structure used by compile -->
    <mkdir dir="${build}"/>
  </target>

  <target name="compile" depends="init"
        description="compile the source">
    <!-- Compile the Java code from ${src} into ${build} -->
    <javac srcdir="${src}" destdir="${build}"/>
  </target>

  <target name="dist" depends="compile"
        description="generate the distribution">
    <!-- Create the distribution directory -->
    <mkdir dir="${dist}/lib"/>

    <!-- Put everything in ${build} into the MyProject-${DSTAMP}.jar file -->
    <jar jarfile="${dist}/lib/MyProject-${DSTAMP}.jar" basedir="${build}"/>
  </target>

  <target name="clean"
        description="clean up">
    <!-- Delete the ${build} and ${dist} directory trees -->
    <delete dir="${build}"/>
    <delete dir="${dist}"/>
  </target>
</project>
```

### Token Filters
> 파일의 값을 치환해줌.
  
### Path-like Structures
> PATH- 그리고 CLASSPATH을 지정할수 있다.

```
<classpath>
  <pathelement path="${classpath}"/>
  <pathelement location="lib/helper.jar"/>
</classpath>
```