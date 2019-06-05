import React from 'react';
import './App.css';


function ImgItem(url) {
const a = url['url']
  return (
    
      <img className='ImgList--item' src={a}></img>
    
  );
}

export default ImgItem;
