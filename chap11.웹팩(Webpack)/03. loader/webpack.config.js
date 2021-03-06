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
  module: {
    rules: [{
        test: /\.js$/, // .js 확장자로 끝나는 모든 파일에 적용
        use: [path.resolve('./src/myloader.js')] // 커스텀 로더
    },{
      test: /\.css$/, // .css 확장자로 끝나는 모든 파일 
      use: ['style-loader', 'css-loader'], // style-loader, css-loader를 적용한다. style-loader는 앞에 적용.
    },{
      test: /\.jpg$/,
      use: {
        loader: 'url-loader', // url 로더를 설정한다
        options: {
          publicPath: './dist/', // file-loader와 동일
          mimetype: false,
          name: '[name].[ext]?[hash]', // file-loader와 동일
          limit: 500000 // 5kb 미만 파일만 data url로 처리 
        }
      }
    }],
  }
}