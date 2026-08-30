import { use, useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./Layout";
import SignUpForm from "./UserForms/SignUpForm/SignUpForm";
import LoginForm from "./UserForms/LoginForm/LoginForm";
import Collection from "./Collection/Collection/Collection";
import { CollectionId } from "../contexts/CollectionId";
import PageNotFound from "./404Page/PageNotFound";

export default function AppRouter() {
  const [loggedInUser, setLoggedInUser] = useState(
    JSON.parse(localStorage.getItem("user")),
  );

  const [collectionId, setCollectionId] = useState(() => {
    return localStorage.getItem("collection_id") || null;
  });

  const routes = createBrowserRouter([
    {
      path: "/",
      element: <Layout setLoggedInUser={setLoggedInUser} />,
      errorElement: <PageNotFound />,
      children: [
        {
          path: "users",
          children: [
            {
              path: "signup",
              element: <SignUpForm setLoggedInUser={setLoggedInUser} />,
            },
            {
              path: "login",
              element: <LoginForm setLoggedInUser={setLoggedInUser} />,
            },
          ],
        },
        {
          path: "collection",
          element: loggedInUser ? (
            <Collection />
          ) : (
            <LoginForm setLoggedInUser={setLoggedInUser} />
          ),
        },
      ],
    },
  ]);

  return (
    <CollectionId.Provider value={{ collectionId, setCollectionId }}>
      <RouterProvider router={routes} />
    </CollectionId.Provider>
  );
}
