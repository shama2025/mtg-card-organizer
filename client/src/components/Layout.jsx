import { Outlet } from "react-router-dom";
import Nav from "./Navbar/Nav/Nav";

export default function Layout() {
  return (
    <div className="bg-jeskai-dark h-full">
      <Nav />
      <Outlet />
    </div>
  );
}
