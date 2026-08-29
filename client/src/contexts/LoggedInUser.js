import { createContext } from "react";

export const LoggedInUser = createContext(
  JSON.parse(localStorage.getItem("user")),
);
