import { Outlet } from "react-router-dom";

export default function Layout({ loggedInUser, setLoggedInUser }) {
  return (
    <div className="bg-gray-800 h-screen">
      <Outlet />
    </div>
  );
}
