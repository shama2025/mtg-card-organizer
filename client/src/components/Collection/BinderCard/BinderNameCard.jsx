import React from "react";
import { EllipsisVertical } from "lucide-react";
import { NavLink } from "react-router-dom";

export default function BinderNameCard({ binder }) {
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
              console.log("Clicked Ellipsis");
            }}
          />
        </span>
      </div>
    </div>
  );
}
