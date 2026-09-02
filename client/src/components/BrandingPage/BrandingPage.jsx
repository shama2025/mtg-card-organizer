import React from "react";
import mtgLogo from "../assets/mtg-logo.png";
import parse from "html-react-parser";
import { NavLink } from "react-router-dom";
import { DisplayBrandingPage } from "../../contexts/DisplayBrandingPage";
import { useContext } from "react";

export default function BrandingPage() {
  const items = [
    '<i className="ms ms-b text-sm text-[#150b00]"></i>',
    '<i className="ms ms-r text-sm text-jeskai-red"></i>',
    '<i className="ms ms-g text-sm text-[#00733e]"></i>',
    '<i className="ms ms-w text-sm text-jeskai-white-pure"></i>',
    '<i className="ms ms-u text-sm text-jeskai-blue"></i>',
  ];

  const total = items.length;
  const radius = 128; // Half of parent's width

  return (
    <div className="flex items-center justify-center">
      <div className="relative top-15 flex flex-col items-center justify-center">
        <div className="relative top-10">
          <div className="z-1000 text-6xl text-jeskai-white-surface relative top-17 flex items-center justify-center">
            ManaVault
          </div>
          <img src={mtgLogo} className="w-113.5 h-87.5 p-10" alt="MTG Logo" />
        </div>
        <div className="relative z-999">
          <NavLink
            className="text-jeskai-white-surface 
      hover:cursor-pointer
      border
      p-1
      m-3
      rounded-md
      "
            to="/users/signup"
            onClick={() => localStorage.setItem("displayBrandingPage", true)}
          >
            Sign Up
          </NavLink>
        </div>
        {items.map((item, index) => {
          const angle = (360 / total) * index;
          return (
            <div
              key={index}
              className="z-0 absolute rounded-full flex items-center justify-center transform"
              style={{
                transform: `rotate(${angle + 55}deg) translate(${radius}px) rotate(-${angle + 55}deg)`,
              }}
            >
              {parse(item)}
            </div>
          );
        })}
      </div>
    </div>
  );
}
