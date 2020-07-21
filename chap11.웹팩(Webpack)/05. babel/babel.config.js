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