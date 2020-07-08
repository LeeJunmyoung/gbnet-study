const pluginName = 'ConsoleLogOnBuildWebpackPlugin';

class ConsoleLogOnBuildWebpackPlugin {
  apply(compiler) {
    compiler.hooks.run.tap(pluginName, compilation => {
      console.log('The webpack build process is starting!!!');
    });

    // compiler.plugin() 함수로 후처리한다
    compiler.plugin('emit', (compilation, callback) => { 
      const source = compilation.assets['main.js'].source();
      console.log(source);
      callback();
    })
  }
}

module.exports = ConsoleLogOnBuildWebpackPlugin;