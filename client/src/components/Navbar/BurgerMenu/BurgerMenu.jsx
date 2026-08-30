import React, { useContext } from "react";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { NavLink, useNavigate } from "react-router-dom";

export default function BurgerMenu() {
  const loggedInUser = useContext(LoggedInUser);
  const navigator = useNavigate();

  function handleLogOut() {
    localStorage.removeItem("user");
    localStorage.removeItem("collection_id");
    navigator("/users/login");
  }

  return (
    <div className="bg-jeskai-white-surface z-50 rounded-md">
      <div
        className="text-jeskai-white-surface border-jeskai-white-border
       border rounded-2xl bg-jeskai-dark p-3 w-auto h-auto"
      >
        <ul>
          {loggedInUser != null ? (
            <li>
              <button
                onClick={handleLogOut}
                className="hover:border-b-jeskai-red-light hover:scale-105 p-1"
              >
                Log Out
              </button>
              <div>
                <NavLink to="/collection">Collection</NavLink>
              </div>
            </li>
          ) : (
            <div className="">
              <div className="hover:border-b-jeskai-red-light hover:scale-105 p-1">
                <NavLink to="/users/login">Login</NavLink>
              </div>
              <div className="hover:border-b-jeskai-red-light hover:scale-105 p-1">
                <NavLink to="/users/signup">Sign Up</NavLink>
              </div>
            </div>
          )}
        </ul>
      </div>
    </div>
  );
}
