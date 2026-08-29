import { createContext } from "react";

export const CollectionId = createContext(
  JSON.parse(localStorage.getItem("collection_id")),
);
