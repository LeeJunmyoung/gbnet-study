import React from 'react';
import './Header.css';
import instagram from './instagram.png';

function Header() {
  return (
    <div className="Header">
      <div className="Header__flex">
        <div className="Header__flex--left Header__flex--item">
            <img src={instagram}></img>
        </div>
        <div className="Header__flex--center  Header__flex--item">
            <input type="text" className="search" placeholder="검색"></input>
        </div>
        <div className="Header__flex--right  Header__flex--item">
            <button className='Header__flex--login__button'>로그인</button>
            <span className='Header__flex--sign__span'>가입하기</span>
        </div>
      </div>
    </div>
  );
}

export default Header;
