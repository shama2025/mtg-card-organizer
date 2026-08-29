import { Outlet } from "react-router-dom";
import Collection from "./Collection/Collection/Collection";
import LoginForm from "./UserForms/LoginForm/LoginForm";
import { useContext } from "react";
import { LoggedInUser } from "../contexts/LoggedInUser";

export default function Layout() {
  return (
    <div className="bg-jeskai-dark h-full">
      <Outlet />
    </div>
  );
}
