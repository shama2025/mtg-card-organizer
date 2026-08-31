import React, { useState } from "react";
import { EllipsisVertical } from "lucide-react";
import { NavLink } from "react-router-dom";
import BinderBurgerMenu from "../BinderBurgerMenu/BinderBurgerMenu";

export default function BinderNameCard({
  binder,
  setBinderToEdit,
  setDisplayBinderModal,
  setIsEdit,
}) {
  const [displayBinderBurgerMenu, setDisplayBinderBurgerMenu] = useState(true);

  return (
    <div>
      <div className="flex flex-row">
        <NavLink
          className="
        hover:scale-105
         hover:cursor-pointer
         "
          to={`/collection/binder/${binder.deckId}`}
          target="blank"
        >
          {binder.name}
        </NavLink>
        <span>
          <EllipsisVertical
            className="w-5 h-5 hover:cursor-pointer hover:scale-110"
            onClick={(event) => {
              event.stopPropagation();
              setDisplayBinderBurgerMenu(
                displayBinderBurgerMenu ? false : true,
              );
              setBinderToEdit(binder);
            }}
          />
          <div hidden={displayBinderBurgerMenu}>
            <BinderBurgerMenu
              setDisplayBinderModal={setDisplayBinderModal}
              setIsEdit={setIsEdit}
            />
          </div>
        </span>
      </div>
    </div>
  );
}
