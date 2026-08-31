import { createContext, useContext } from "react";

export const DisplayBrandingPage = createContext(
  localStorage.getItem("displayBrandingPage") || false,
);
