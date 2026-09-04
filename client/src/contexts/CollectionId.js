import { createContext, useContext } from "react";

export const CollectionId = createContext(null);

export const useCollectionId = () => useContext(CollectionId);
