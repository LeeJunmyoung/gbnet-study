import React from 'react';
import './App.css';

function ProfileMenu() {
  return (
    <div className="ProfileMenu">
        <div className="ProfileMenu--item">
            <span className='ProfileMenu--item--img1'></span>
            게시물
        </div>
        <div className="ProfileMenu--item">
            <span className='ProfileMenu--item--img2'></span>
            IGTV
        </div>
        <div className="ProfileMenu--item">
            <span className='ProfileMenu--item--img3'></span>
            태그됌
        </div>
    </div>
  );
}

export default ProfileMenu;
