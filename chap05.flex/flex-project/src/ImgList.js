import React from 'react';
import './App.css';
import ImgItem from './ImgItem';

const imgList = {
    "image":[
        {
            id:1,
            imgUrl:'/img/1.jpg'
        },
        {
            id:2,
            imgUrl:'/img/2.jpg'
        },
        {
            id:3,
            imgUrl:'/img/3.jpg'
        },
        {
            id:4,
            imgUrl:'/img/4.jpg'
        },
        {
            id:5,
            imgUrl:'/img/5.jpg'
        },
        {
            id:6,
            imgUrl:'/img/6.png'
        },
        {
            id:7,
            imgUrl:'/img/7.jpg'
        },
        {
            id:8,
            imgUrl:'/img/8.jpg'
        },
        {
            id:9,
            imgUrl:'/img/9.png'
        }
        
    ]
}

function ImgList() {
  return (
    <React.Fragment>
        <div className="ImgList">
            <ImgItem url={imgList.image[0].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[1].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[2].imgUrl}></ImgItem>
        </div>
        <div className="ImgList">
            <ImgItem url={imgList.image[3].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[4].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[5].imgUrl}></ImgItem>
        </div>
        <div className="ImgList">
            <ImgItem url={imgList.image[6].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[7].imgUrl}></ImgItem>
            <ImgItem url={imgList.image[8].imgUrl}></ImgItem>
        </div>
    </React.Fragment>
  );
}

export default ImgList;
