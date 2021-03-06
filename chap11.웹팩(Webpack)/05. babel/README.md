# 바벨
  
  
> 바벨은 다양한 브라우저에서 돌아가는 어플리케이션을 만들어 준다.  
> 바벨의 코어는 파싱과 출력을 담당하고 변환은 플러그인에서 처리한다.  
> 여러개의 플러그인을 모아놓은 세트를 프리셋이라 하며, 
> 바벨이 변환하지 못하는 코드는 폴리필로 변환한다.  

## istall 
```
npm install -D @babel/core @babel/cli
npm install -D @babel/plugin-transform-block-scoping
npm install -D @babel/plugin-transform-arrow-functions
npm install -D @babel/plugin-transform-strict-mode
npm install -D babel-loader
```  

## run
```
npx babel app.js
```

## 프리셋
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

# install @babel/preset-env
npm install -D @babel/preset-env
```

## 폴리필
```
# app.js
new Promise();

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


# babel build
npx babel app.js --out-file src/app.js

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
