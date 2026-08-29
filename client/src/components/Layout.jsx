import { Outlet } from "react-router-dom";
import Collection from "./Collection/Collection/Collection";
import LoginForm from "./UserForms/LoginForm/LoginForm";
import { useContext } from "react";
import { LoggedInUser } from "../contexts/LoggedInUser";

export default function Layout({ setLoggedInUser }) {
  const loggedInUser = useContext(LoggedInUser);
  return (
    <div className="bg-jeskai-dark h-full">
      {loggedInUser ? (
        <>
          <Collection />
          <Outlet />
        </>
      ) : (
        <>
          <LoginForm setLoggedInUser={setLoggedInUser} />
          <Outlet />
        </>
      )}
    </div>
  );
}
