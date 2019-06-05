import React from 'react';
import './App.css';
import ProfileBody from './ProfileBody';
import ProfileMenu from './ProfileMenu';
import ImgList from './ImgList';

function Body() {
  return (
    <div className="Body">
      <ProfileBody></ProfileBody>
      <ProfileMenu></ProfileMenu>
      <ImgList></ImgList>
    </div>
  );
}

export default Body;
