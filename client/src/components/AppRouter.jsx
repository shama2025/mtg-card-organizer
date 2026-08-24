import { useState } from "react";
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Layout from "./Layout";
import SignUpForm from "./UserForms/SignUpForm/SignUpForm";
import LoginForm from "./UserForms/LoginForm/LoginForm";

export default function AppRouter() {
  const [loggedInUser, setLoggedInUser] = useState(
    JSON.parse(localStorage.getItem("user")),
  );


  const routes = createBrowserRouter([
    {
      path: "/",
      element: (
        <Layout loggedInUser={loggedInUser} setLoggedInUser={setLoggedInUser} />
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
        }
      ],
    },
  ]);

  return <RouterProvider router={routes}></RouterProvider>;
}
