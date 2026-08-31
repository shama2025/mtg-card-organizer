import { Outlet, useNavigate } from "react-router-dom";
import Nav from "./Navbar/Nav/Nav";
import { useContext, useEffect, useState } from "react";
import { LoggedInUser } from "../contexts/LoggedInUser";
import Collection from "./Collection/Collection/Collection";
import BrandingPage from "./BrandingPage/BrandingPage";
import { DisplayBrandingPage } from "../contexts/DisplayBrandingPage";

export default function Layout() {
  const displayBrandingPage = useContext(DisplayBrandingPage);

  const loggedInUser = useContext(LoggedInUser);

  return (
    <div className="bg-jeskai-dark h-screen">
      <Nav />
      {/* TODO: Use a react provider to manage the state of the branding page */}
      <div hidden={true}>
        <BrandingPage />
      </div>
      <Outlet />
    </div>
  );
}
