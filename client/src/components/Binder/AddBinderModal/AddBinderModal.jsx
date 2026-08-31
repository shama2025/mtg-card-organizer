import React, { useContext, useState } from "react";
import { Bars } from "react-loader-spinner";
import { CircleX, Forward } from "lucide-react";
import ErrorList from "../../ErrorList/ErrorList";
import { addBinder } from "./http";
import { CollectionId } from "../../../contexts/CollectionId";
import { LoggedInUser } from "../../../contexts/LoggedInUser";

export default function AddBinderModal({
  setDisplayBinderCardModal,
  setBinders,
  binders,
}) {
  const initBinder = {
    id: 0,
    name: "",
    cardCount: 0,
    dateCreated: new Date().toLocaleDateString("fr-CA"),
    dateUpdated: new Date().toLocaleDateString("fr-CA"),
    cardList: [],
  };

  const [binder, setBinder] = useState(initBinder);

  const [errors, setErrors] = useState([]);
  const [isSpinnerHidden, setIsSpinnerHidden] = useState(true);

  const collectionId = useContext(CollectionId);
  const loggedInUser = useContext(LoggedInUser);

  async function handleSubmit(event) {
    event.preventDefault();
    setIsSpinnerHidden(false);
    const { newBinder, errors } = await addBinder(
      binder,
      collectionId,
      loggedInUser,
    );
    if (newBinder) {
      setIsSpinnerHidden(true);
      window.open(
        `/collection/binder/${newBinder.deckId}`,
        "_blank",
        "noopener,noreferrer",
      );
      setBinder(initBinder);
      setDisplayBinderCardModal(true);
      setBinders([...binders, newBinder]);
    } else {
      setIsSpinnerHidden(true);
      setErrors([errors]);
      return;
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
            onClick={() => setDisplayBinderCardModal(true)}
          >
            <CircleX />
          </div>
          <div className="pb-1">
            <label>Create new Binder Name</label>
          </div>
          <div>
            <input
              type="text"
              placeholder="Budget All Stars"
              className="border m-1 rounded-2xl p-2"
              name="name"
              value={binder.name}
              onChange={(event) => {
                setBinder({
                  ...binder,
                  [event.target.name]: event.target.value,
                });
              }}
              required
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
