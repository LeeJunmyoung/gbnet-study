# Lint

```
npm install -D eslint

$ npx eslint --init

√ How would you like to use ESLint? · problems
√ What type of modules does your project use? · esm
√ Which framework does your project use? · none
√ Does your project use TypeScript? · No / Yes
√ Where does your code run? · browser
√ What format do you want your config file to be in? · JavaScript

# index.js
var foo = bar;;

# npx eslint index.js --fix실행시 
# ;; -> ;
# foo라는 변수가 사용되지 않았다.
# bar가 선언되어있지 않다라는 에러를 출력함.
var foo = bar;
```

