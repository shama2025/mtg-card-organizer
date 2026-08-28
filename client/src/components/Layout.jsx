import { Outlet } from "react-router-dom";
import Collection from "./Collection/Collection/Collection";

export default function Layout({
  collectionId,
  loggedInUser,
  setLoggedInUser,
}) {
  return (
    <div className="bg-jeskai-dark h-full">
      <Collection collectionId={collectionId} loggedInUser={loggedInUser} />
      <Outlet />
    </div>
  );
}
