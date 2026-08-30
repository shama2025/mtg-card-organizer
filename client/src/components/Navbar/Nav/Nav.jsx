import React, { useState } from "react";
import mtgIcon from "../../assets/mtg-logo.png";
import BurgerMenu from "../BurgerMenu/BurgerMenu";

export default function Nav() {
  const [displayBurgerMenu, setDisplayBurgerMenu] = useState(true);

  return (
    <div className="flex items-center w-full shadow-black">
      <div
        onClick={() => setDisplayBurgerMenu(displayBurgerMenu ? false : true)}
        className="hover:cursor-pointer"
      >
        <img
          src={mtgIcon}
          alt="MTG PlanesWalker logo"
          className=" w-18.75 h-12.5 left-1 "
        />
        <div hidden={displayBurgerMenu} className="z-999 absolute">
          <BurgerMenu />
        </div>
      </div>
    </div>
  );
}
