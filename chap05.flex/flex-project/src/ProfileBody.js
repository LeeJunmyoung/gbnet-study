import React from 'react';
import './App.css';
import profileImg from './img/solo.jpg'
import chkImg from './img/f01fcb405c10.png'

function ProfileBody() {
  return (
    <div className="ProfileBody">
        <div className='ProfileBody__left'>
            <img src={profileImg}/>
        </div>
        <div className='ProfileBody__right'>
            <div className='ProfileBody__right__top'>
                <h2>Savannah_SoLo</h2>
                <span ></span>
                <button className='common__button'>팔로우</button>
            </div>
            <div className='ProfileBody__right__middle'>
                <div className='ProfileBody__right__middle--item'>게시물 <span className='bold'>1234</span></div>
                <div className='ProfileBody__right__middle--item'>팔로워 <span className='bold'>686천</span></div>
                <div className='ProfileBody__right__middle--item'>팔로우 <span className='bold'>1234</span></div>
            </div>
            <div className='ProfileBody__right__bottom'>
                <div className='ProfileBody__right__bottom--item'><span>아프리카 초원의 동물들을 공포에 떨게 한 솔로부대❤❤</span></div>
                <div className='ProfileBody__right__bottom--item'># 위협적인 존재</div>
                <div className='ProfileBody__right__bottom--item'># 여친을 못만난 백수</div>
            </div>
        </div>
        
      
    </div>
  );
}

export default ProfileBody;
