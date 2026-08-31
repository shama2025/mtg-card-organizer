import React, { useState } from "react";

export default function BinderBurgerMenu({ setDisplayBinderModal, setIsEdit }) {
  return (
    <div className="absolute bg-jeskai-white-surface z-50 rounded-md">
      <div
        className="text-jeskai-white-surface border-jeskai-white-border
       border-0.5 rounded-md bg-jeskai-dark p-3 w-auto h-auto"
      >
        <ul>
          <li>
            <button
              className="hover:scale-105 hover:border-b-2
             hover:border-jeskai-red-light"
              onClick={() => {
                setDisplayBinderModal(false);
                setIsEdit(true);
              }}
            >
              Edit
            </button>
          </li>
          <li>
            <button
              className="hover:scale-105 hover:border-b-2
             hover:border-jeskai-red-light"
              onClick={() => {
                setDisplayBinderModal(false);
                setIsEdit(false);
              }}
            >
              Delete
            </button>
          </li>
        </ul>
      </div>
    </div>
  );
}
