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
> 엔트리포인트를 시작으로 연결되어 었는 모든 모듈을 하나로 합쳐서 결과물을 만드는 것이 웹팩의 역할이다.   
> 자바스크립트 모듈 뿐만 아니라 스타일시트, 이미지 파일까지도 모듈로 제공해 주기 때문에 일관적으로 개발할 수 있다.  
     
  
  
### 1.1 Install  
```
# install with npm : 
npm install --save-dev webpack
npm install -D webpack webpack-cli

# install with yarn :
yarn add webpack --dev
yarn add webpack-cli --dev
```
  
### 1.2 엔트리/아웃풋  
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
  
  
### 1.3 로더  
> 로더는 소스코드에 모듈로 적용되는 변환이다.  
> import로 사전 처리 될수 있다.   
> 로더는 파일을 다른 언어 (예 : TypeScript)에서 JavaScript로 변환하거나 인라인 이미지를 데이터 URL로로드 할 수 있다.   
> 로더를 사용하면 모듈에서 직접 CSS 파일도 import 시킬수 있다.  
  
```
# install loader with loader
npm install --save-dev css-loader
```
  
#### 1.3.1 css-loader
> style sheet도 import 구문으로 불러올수 있다.
  
```
# install css-loader
npm install -D css-loader

# webpack.config.js css-loader module 추가
module.exports = {
  module: {
    rules: [{
      test: /\.css$/, // .css 확장자로 끝나는 모든 파일 
      use: ['css-loader'], // css-loader를 적용한다 
    }]
  }
}
```
  
#### 1.3.2 style-loader
> 자바스크립트로 변경된 스타일을 동적으로 돔에 추가한다.  
> css를 번들링하기 위해서는 css-loader와 style-loader를 함께 사용한다.  
  
```
# install style-loader
npm install -D style-loader

# webpack.config.js style-loader module 추가
module.exports = {
  module: {
    rules: [{
      test: /\.css$/,
      use: ['style-loader', 'css-loader'], // style-loader를 앞에 추가한다 
    }]
  }
}
```
  
#### 1.3.3 file-loader
> css 뿐만 아니라 소스 코드에서 사용하는 모든 파일을 모듈로 변환한다.  
> css에서 url경로에 이미지를 file-loader를 이용해서 처리한다.  
  
```
# install file-loader
npm install -D file-loader

module.exports = {
  module: {
    rules: [{
      test: /\.png$/, // .png 확장자로 마치는 모든 파일
      loader: 'file-loader',
      options: {
        publicPath: './dist/', // prefix를 아웃풋 경로로 지정 
        name: '[name].[ext]?[hash]', // 파일명 형식 
      }
    }]
  }
}
```
  
#### 1.3.4 url-loader
> 사용하는 이미지 개수가 많다면 request가 많아져서 부담이 갈 수 있다.  
> Base64로 인코딩하여 Data URI Scheme 형식으로 문자열 형태로 소스에 자동으로 넣어준다.  
> file-loader나 url-loader 둘중의 하나만 사용해야한다.
  
```
# install url-loader
npm install -D url-loader

module.exports = {
  module: {
    {
      test: /\.png$/,
      use: {
        loader: 'url-loader', // url 로더를 설정한다
        options: {
          publicPath: './dist/', // file-loader와 동일
          name: '[name].[ext]?[hash]', // file-loader와 동일
          limit: 5000 // 5kb 미만 파일만 data url로 처리 
        }
      }
    }
  }
}
```
  
  
### 1.4 플러그인  
> 웹팩 플러그인은 apply 메서드를 가지는 자바스크립트 object이다.  
> apply 메서드는 웹팩 컴파일러로 부터 호출되어 전체 컴파일러 라이프 사이클에 접근할 수 있다.    
  
#### 1.4.1 커스텀 플러그인  
```
const pluginName = 'ConsoleLogOnBuildWebpackPlugin';

class ConsoleLogOnBuildWebpackPlugin {
  apply(compiler) {
    compiler.hooks.run.tap(pluginName, compilation => {
      console.log('The webpack build process is starting!!!');
    });
  }
}

module.exports = ConsoleLogOnBuildWebpackPlugin;


const MyPlugin = require('./ConsoleLogOnBuildWebpackPlugin');

module.exports = {
  plugins: [
    new ConsoleLogOnBuildWebpackPlugin(),
  ]
}
```
  
#### 1.4.2 banner plugin
```
# banner.js
const childProcess = require('child_process');

module.exports = function banner() {
  const commit = childProcess.execSync('git rev-parse --short HEAD')
  const user = childProcess.execSync('git config user.name')
  const log = childProcess.execSync('git log --pretty=format:"%h - %an, %ar : %s" -5')
  const date = new Date().toLocaleString();
  
  return (
    `commitVersion: ${commit}` +
    `Build Date: ${date}\n` +
    `Author: ${user} \n` +
    `log history \n` +
    `${log}`
  );
}

# webpack.config.js
const path = require('path');
const webpack = require('webpack');
const banner = require('./banner');

module.exports = {
  mode: 'development',
  entry: {
    main: './src/app.js'
  },
  output: {
    filename: '[name].js',
    path: path.resolve('./dist'),
  },
  plugins: [
    new webpack.BannerPlugin(banner)
  ]
}

```
  
#### 1.4.3 HtmlWebpackPlugin
> css, js 를 html 에 자동 번들링 하도록 도와준다.  
```
# install html-webpack-plugin
npm i --save-dev html-webpack-plugin

# webpack.config.js
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports {
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html', // 템플릿 경로
      templateParameters: { // 파라매터 변수
        env: process.env.NODE_ENV === 'development' ? '(개발 ver)' : '', 
      },
    })
  ]
}
```
  
#### 1.4.4 CleanWebpackPlugin
> 빌드시 이전 빌드파일을 지워주는 플러그인.  
```
# install CleanWebpackPlugin
npm install -D clean-webpack-plugin

# webpack.config.js
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  plugins: [
    new CleanWebpackPlugin(),
  ]
}
```
  
#### 1.4.5 MiniCssExtractPlugin
> css파일을 별도로 파일로 변환해서 만들어주는 플러그인.  
```
# install MiniCssExtractPlugin
npm install -D mini-css-extract-plugin

# webpack.config.js
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
  module: {
    rules: [{
      test: /\.css$/,
      use: [
        process.env.NODE_ENV === 'production' 
        ? MiniCssExtractPlugin.loader  
        : 'style-loader', 'css-loader'
      ],
    }]
  },
  plugins: [
    ...(
      process.env.NODE_ENV === 'production' 
      ? [ new MiniCssExtractPlugin({filename: `[name].css`}) ]
      : []
    ),
  ],
}
```
  
  
# 바벨 (Babel)

## 2. Introduce  
> ES5 이상의 코드를 하위 버전으로 바꿔주는 역할을 함.  
> IE나 구버전 브라우저에서 최신 자바스크립트 코드를 작동하도록 변환해준다.  
> 바벨은 파싱과 출력만을 담당한다.

### 2.1 Install
```
# babel install
npm install -D @babel/core @babel/cli

# app.js
const alert = msg => console.log(msg);

# babel cmd
npx babel app.js

# 단순히 출력만 된다.
```

### 2.2 빌드

1. 파싱 - 추상구문트리로 변환하는 단계
```
while b ≠ 0
if a > b
a := a − b
else
b := b − a
return a
```
![추상구문트리_AST](./추상구문트리.png)   

2. 변환 - 추상구문트리를 조건에 맞게 변환  
3. 출력 - 변환된걸 출력  

### 2.3 커스텀 플러그인  
  
```
# custom-plugin.js
module.exports = function customPlugin() {
  return {
    visitor: {
      VariableDeclaration(path) {
        console.log('VariableDeclaration() kind:', path.node.kind);
        if(path.node.kind === 'const' || path.node.kind === 'let') {
          path.node.kind = 'var';
        }
      }  
    }
  }
}

# npx babel app.js --plugins ./custom-plugin.js
```

### 2.4 플러그인  
```
# block scoping plugin install
# const, let 처럼 블록 스코프 변수를 var로 변환해줌.
npm install -D @babel/plugin-transform-block-scoping

# arrow function plugin install
# 람다식을 es5에 맞게 변환해줌.
npm install -D @babel/plugin-transform-arrow-functions

# strict mode plugin install  
# ES5에 추가된 엄격 모드, 묵인했던 자바스크립트의 에러들의 메시지를 발생시킴.   
npm install -D @babel/plugin-transform-strict-mode

npx babel app.js \
--plugins @babel/plugin-transform-block-scoping \
--plugins @babel/plugin-transform-arrow-functions
```
  
#### 2.4.1 babel.config.js  
> webpack.config.js 처럼 바벨도 babel.config.js 를 이용해서 바벨 설정을 할 수 있다.  
  
```
module.exports = {
  plugins: [
    "@babel/plugin-transform-block-scoping",
    "@babel/plugin-transform-arrow-functions",
    "@babel/plugin-transform-strict-mode", 
  ]
}

npx babel app.js
```

### 2.5 프리셋  
> 여러가지 플러그인을 배열형식으로 관리 할 수 있다.  

  
```
# mypreset.js
module.exports = function mypreset() {
  return {
    plugins: [
      "@babel/plugin-transform-arrow-functions",
      "@babel/plugin-transform-block-scoping",
      "@babel/plugin-transform-strict-mode",
    ],
  }
}

# babel.config.js
module.exports = {
  presets: [
    './mypreset.js'
  ],
}
```

> 일반적으로 사용하는 프리셋
```
# preset-env
# preset-flow
# preset-react
# preset-typescript

npm install -D @babel/preset-env

# babel.config.js
module.exports = {
  presets: [
    [
      '@babel/preset-env',
      {
        targets: {
          chrome: '79',
          ie: '11' 
        }
      } // 크롬 79 와 ie 11을 지원하는 코드로 변환한다.
    ]
  ]
}
```

### 2.6 폴리필
> 폴리필(polyfill)은 웹 개발에서 기능을 지원하지 않는 웹 브라우저 상의 기능을 구현하는 코드를 뜻한다.  
> Promise 같은 경우는 위에 프리셋으로 변환 할수 없다.  
> 이럴 경우 폴리필을 사용해 변환한다.  

```
# babel.config.js 
module.exports = {
    presets: [
        [
            //'./mypreset.js'
            '@babel/preset-env'
            , {
                targets: {
                    chrome: '79',
                    ie: '11' 
                },
                useBuiltIns: 'usage', // 폴리필 사용 방식 지정
                                      // 'usage': 실제 소스코드에서 사용하여 필요한 폴리필만 포함하도록 트랜스폼한다.
                                      // 'entry': 지정된 환경에서 필요한 폴리필은 일단 포함하도록 트랜스폼한다.
                corejs: { // 폴리필 버전 지정
                  version: 2
                }
            }
        ]
    ]
}

# webpack.config.js
module: {
  rules: [
      {
          test: /\.js$/,
          exclude: /node_modules/,
          loader: 'babel-loader', // 바벨 로더를 추가한다 
          query: {
            presets: ['@babel/preset-env']
          }
      }
  ],
}

# babel build
npx babel app.js --out-file app.build.js
```
  
> webpack.config.js 에 babel-loader를 추가하여 빌드할수 있다.  
```
# webpack.config.js
module: {
  rules: [
      {
          test: /\.js$/,
          exclude: /node_modules/,
          loader: 'babel-loader', // 바벨 로더를 추가한다 
          query: {
            presets: ['@babel/preset-env']
          }
      }
  ],
}
```  

   
# 린트 (Lint)