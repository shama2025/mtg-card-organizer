import { createContext } from "react";

export const JwtToken = createContext(localStorage.getItem("jwt_token"));
