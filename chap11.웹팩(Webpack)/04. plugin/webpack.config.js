const path                            = require('path');
const webpack                         = require('webpack');
const { CleanWebpackPlugin }          = require('clean-webpack-plugin');
const banner                          = require('./banner');
const ConsoleLogOnBuildWebpackPlugin  = require('./ConsoleLogOnBuildWebpackPlugin');
const HtmlWebpackPlugin               = require('html-webpack-plugin');
const MiniCssExtractPlugin            = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin         = require("optimize-css-assets-webpack-plugin");
const TerserPlugin                    = require('terser-webpack-plugin');

const mode = 'production';
module.exports = {
  mode: mode,
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
      use: [mode === 'production' 
              ? MiniCssExtractPlugin.loader  
              : 'style-loader', 'css-loader'],
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
  optimization: { minimize: true, 
                  minimizer: [
                    new OptimizeCSSAssetsPlugin({}), 
                    new TerserPlugin({ 
                      terserOptions: {
                        output: {
                          comments: false,
                        },
                      },
                      extractComments: {
                        filename: (file) => {
                          return 'banner.txt';
                        },
                        banner: () => { return banner } 
                      } 
                    }) 
                  ]
                },
  plugins: [
    new ConsoleLogOnBuildWebpackPlugin(),
    new webpack.BannerPlugin(banner),
    new HtmlWebpackPlugin({
      template: './src/index.html', // 템플릿 경로
      templateParameters: { // 파라매터 변수
        env: mode == 'development' ? '(개발 ver)' : '', 
      },
      minify: mode === 'production' ? { 
        collapseWhitespace: true, // 빈칸 제거 
        removeComments: true, // 주석 제거 
      } : false,
      hash: true, // 정적 파일을 파라미터값에 해쉬값 추가
    }),
    new CleanWebpackPlugin(),
    mode === 'production' 
      ? new MiniCssExtractPlugin({filename: `[name].css`})
      : null
  ]
}