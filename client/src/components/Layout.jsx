import { Outlet, useNavigate } from "react-router-dom";
import Nav from "./Navbar/Nav/Nav";
import { useContext, useEffect } from "react";
import { LoggedInUser } from "../contexts/LoggedInUser";

export default function Layout() {

  const loggedInUser = useContext(LoggedInUser)
  const navigator = useNavigate()

  useEffect(() => {
    if(loggedInUser){
      navigator("/collection");
    }
},[])

  return (
    <div className="bg-jeskai-dark h-full">
      <Nav />
      <Outlet />
    </div>
  );
}
