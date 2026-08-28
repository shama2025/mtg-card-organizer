import { use, useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "./Layout";
import SignUpForm from "./UserForms/SignUpForm/SignUpForm";
import LoginForm from "./UserForms/LoginForm/LoginForm";
import Collection from "./Collection/Collection/Collection";

export default function AppRouter() {
  const [loggedInUser, setLoggedInUser] = useState(
    JSON.parse(localStorage.getItem("user")),
  );
  const [collectionId, setCollectionId] = useState(loggedInUser.collectionId);

  const routes = createBrowserRouter([
    {
      path: "/",
      element: (
        <Layout
          collectionId={collectionId}
          loggedInUser={loggedInUser}
          setLoggedInUser={setLoggedInUser}
        />
      ),
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
            <Collection
              collectionId={collectionId}
              loggedInUser={loggedInUser}
            />
          ) : (
            <LoginForm setLoggedInUser={setLoggedInUser} />
          ),
        },
      ],
    },
  ]);

  return <RouterProvider router={routes}></RouterProvider>;
}
