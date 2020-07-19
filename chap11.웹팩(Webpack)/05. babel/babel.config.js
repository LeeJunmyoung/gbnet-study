module.exports = {
    presets: [
        [
            //'./mypreset.js'
            '@babel/preset-env',
            {
                targets: {
                    chrome: '79',
                    ie: '11' 
                }
            } // 크롬 79 와 ie 11을 지원하는 코드로 변환한다.
        ]
    ]
}