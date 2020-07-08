const path = require('path');
const webpack = require('webpack');
const ConsoleLogOnBuildWebpackPlugin = require('./ConsoleLogOnBuildWebpackPlugin');
const banner = require('./banner');
const HtmlWebpackPlugin = require('html-webpack-plugin');

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
      test: /\.css$/, // .css 확장자로 끝나는 모든 파일 
      use: ['style-loader', 'css-loader'], // style-loader, css-loader를 적용한다. style-loader는 앞에 적용.
    },{
      test: /\.jpg$/,
      use: {
        loader: 'url-loader', // url 로더를 설정한다
        options: {
          publicPath: './', // file-loader와 동일
          mimetype: false,
          name: '[name].[ext]?[hash]', // file-loader와 동일
          limit: 5000 // 5kb 미만 파일만 data url로 처리 
        }
      }
    }],
  },
  plugins: [
    new ConsoleLogOnBuildWebpackPlugin(),
    new webpack.BannerPlugin(banner),
    new HtmlWebpackPlugin({
      template: './src/index.html', // 템플릿 경로
      templateParameters: { // 파라매터 변수
        env: process.env.NODE_ENV === 'development' ? '(개발 ver)' : '', 
      },
    })
  ]
}