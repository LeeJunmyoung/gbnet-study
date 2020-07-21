const path                            = require('path');

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
}