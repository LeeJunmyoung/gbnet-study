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