import React, { useContext, useState } from "react";
import { CircleX, Forward } from "lucide-react";
import { CollectionId } from "../../../contexts/CollectionId";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { addCardToBinder, addCardToCollection } from "./http";
import ErrorList from "../../ErrorList/ErrorList";
import { Bars } from "react-loader-spinner";

export default function AddCardModal({
  setAddCardModalVisible,
  collection,
  setCollection,
  setBinderCardList,
  binder,
}) {
  const collectionId = useContext(CollectionId);
  const loggedInUser = useContext(LoggedInUser);

  const [cardName, setCardName] = useState("");
  const [errors, setErrors] = useState([]);
  const [isSpinnerHidden, setIsSpinnerHidden] = useState(true);

  async function handleSubmit(event) {
    event.preventDefault();
    setIsSpinnerHidden(false);

    if (binder) {
      const { card, errors } = await addCardToBinder(
        cardName,
        binder,
        binder.deckId,
        loggedInUser,
      );
      setIsSpinnerHidden(true);
      if (errors) {
        setErrors([errors]);
        return;
      }
      if (card && card.id) {
        debugger;
        setBinderCardList((prevCardList) => [...prevCardList, card]);
        setCardName("");
        setAddCardModalVisible(false);
      } else {
        setErrors(["Received invalid card data from server."]);
      }
    }
    if (collection) {
      const { card, errors } = await addCardToCollection(
        cardName,
        collectionId.collectionId,
        loggedInUser,
      );
      setIsSpinnerHidden(true);
      if (errors) {
        setErrors([errors]);
        return;
      }
      if (card && card.id) {
        setCollection((prevCollection) => [...prevCollection, card]);
        setCardName("");
        setAddCardModalVisible(false);
      } else {
        setErrors(["Received invalid card data from server."]);
      }
    }
  }

  return (
    <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
      <div
        className="text-jeskai-white-surface border-jeskai-white-border
       border rounded-2xl bg-jeskai-dark p-3 w-auto h-auto"
      >
        <div
          hidden={isSpinnerHidden}
          className="z-999 items-center flex justify-center fixed inset-0 backdrop-blur-2xl"
        >
          <Bars />
        </div>
        <form onSubmit={handleSubmit}>
          <div
            className="hover:cursor-pointer"
            onClick={() => setAddCardModalVisible(false)}
          >
            <CircleX />
          </div>
          <div className="pb-1">
            <label>Enter MTG Card Name</label>
          </div>
          <div>
            <input
              type="text"
              placeholder="Rhystic Study"
              value={cardName}
              onChange={(event) => setCardName(event.target.value)}
              className="border m-1 rounded-2xl p-2"
            />
            <button
              type="submit"
              className="hover:scale-105 hover:cursor-pointer"
            >
              <Forward />
            </button>
          </div>
          <div></div>
          <ErrorList errors={errors} />
        </form>
      </div>
    </div>
  );
}
