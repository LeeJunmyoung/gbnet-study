# 바벨

## istall 
```
npm install -D @babel/core @babel/cli
npm install -D @babel/plugin-transform-block-scoping
npm install -D @babel/plugin-transform-arrow-functions
npm install -D @babel/plugin-transform-strict-mode
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
```