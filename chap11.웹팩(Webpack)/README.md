# 웹팩 (Webpack)  
> 웹팩(Webpack 또는 webpack)은 오픈 소스 자바스크립트(JS) 모듈 번들러이다.  
> 웹팩은 의존성이 있는 모듈을 취하여 해당 모듈을 대표하는 정적 자산들을 생성한다.  
> 웹팩은 의존성을 취한 다음 의존성 그래프를 만듦으로써 웹 개발자들이 웹 애플리케이션 개발 목적을 위해 모듈 방식의 접근을 사용할 수 있게 도와준다.   
> 명령 줄을 통해서 사용할 수 있으며, "webpack.config.js"이라는 이름의 구성 파일을 사용하여 구성할 수 있다.  
> 프로젝트를 위해 로더, 플러그인 등을 정의할 수 있다. 
> https://github.com/webpack/webpack  

## 1. Introduce  
> webpack은 모듈 번들러이다.  
> 주 목적은 javascript 파일을 번들링 하는 것이지만, 거의 모든 리소스 또는 자원을  
> 변환, 번들링 또는 패키징 할 수 있다.  
> webpack은 ES5와 호환 되는 모든 브라우저를 지원함.  
     


## 1.1 Install  
```
# install with npm : 
npm install --save-dev webpack
npm install -D webpack webpack-cli

# install with yarn :
yarn add webpack --dev
yarn add webpack-cli --dev
```

## 1.2 엔트리/아웃풋  
> 웹팩은 여러개 파일을 하나의 파일로 합쳐주는 번들러(bundler)다.  

```
# webpack help
node_modules/.bin/webpack --help
--mode : Enable production optimizations or development hints.  
         [선택: "development", "production", "none"]
--entry : The entry point(s) of the compilation. [문자열]
--output, -o : The output path and file for compilation assets

# dist/main.js 번들 결과  
node_modules/.bin/webpack --mode development --entry ./src/app.js --output dist/main.js

# index.html 추가.
<script src="dist/main.js"></script>
```

> webpack의 설정파일의 경로를 지정하여 터미널에서 사용한 옵션을 코드로 구성  
> default file : webpack.config.js  

```
# webpack.config.js

const path = require('path');

module.exports = {
  mode: 'development',
  entry: {
    main: './src/app.js'
  },
  output: {
    filename: '[name].js',
    path: path.resolve('./dist'),
  },
}
mode : mode에 따라 기본 설정값이 다르고, 최적화를 할 수 있다.
       [none, development, production]
       

entry : 웹팩은 기본적으로 여러 개의 자바스크립트 모듈을 하나의 파일로 묶어내는 번들러이다.   
        따라서 웹팩은 다른 모듈을 사용하고 있는 최상위 자바스크립트 파일이 어디에 있는지 알아야 하며, 
        설정 파일에서 이를 Entry 속성으로 명시한다.

output : 번들링 결과에 파일을 만들어냄.   
         설정 파일의 Output 속성을 통해서 이 값을 다른 디렉터리와 파일로 변경할 수 있음.  
```


```
package.json

{
  "scripts": {
    "build": "./node_modules/.bin/webpack"
  }
}
```

> npm run build로 웹팩 작업 할수 있음


## 1.3 로더  
> 로더는 소스코드에 모듈로 적용되는 변환이다.  
> import로 사전 처리 될수 있다.   
> 로더는 파일을 다른 언어 (예 : TypeScript)에서 JavaScript로 변환하거나 인라인 이미지를 데이터 URL로로드 할 수 있다.   
> 로더를 사용하면 모듈에서 직접 CSS 파일도 import 시킬수 있다.  

```
# install loader with loader
npm install --save-dev css-loader
```