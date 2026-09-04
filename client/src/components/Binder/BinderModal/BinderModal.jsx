import React, { use, useContext, useEffect, useState } from "react";
import { CircleX } from "lucide-react";
import { Bars } from "react-loader-spinner";
import ErrorList from "../../ErrorList/ErrorList";
import { deleteBinder } from "./http";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { useNavigate } from "react-router-dom";
import { JwtToken } from "../../../contexts/JwtToken";

export default function BinderModal({
  isEdit,
  binderToEdit,
  setDisplayBinderModal,
  binders,
  setBinders,
}) {
  const [errors, setErrors] = useState([]);
  const [isSpinnerHidden, setIsSpinnerHidden] = useState(true);
  const [binder, setBinder] = useState(undefined);

  const navigator = useNavigate();
  const loggedInUser = useContext(LoggedInUser);
  const jwtToken = useContext(JwtToken)

  useEffect(() => {
    if (binderToEdit?.name) {
      setBinder(binderToEdit);
    }
  }, [binderToEdit]);

  async function handleSubmit(event) {
    event.preventDefault();
    if (isEdit) {
      // Call edit function
    } else {
      setIsSpinnerHidden(false);
      const { isDeleted, errors } = await deleteBinder(binder.deckId, jwtToken);
      if (isDeleted) {
        const updatedBinders = binders.filter((c) => c.id !== binderToEdit.id);
        setBinders(updatedBinders);
        setIsSpinnerHidden(true);
        setDisplayBinderModal(true);
      } else if (errors) {
        // Display errors
        setIsSpinnerHidden(true);
        setErrors([errors]);
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
            onClick={() => setDisplayBinderModal(true)}
          >
            <CircleX />
          </div>
          <div className="pb-1">
            <label>
              {isEdit ? "Edit Deck Name" : "Do you want to delete this deck?"}
            </label>
          </div>
          <div className="flex flex-col">
            <input
              type="text"
              readOnly={!isEdit}
              className="border m-1 rounded-2xl p-2"
              value={binder?.name || "No name rendered"}
              onChange={(e) => {
                setBinder(e.target.value);
              }}
            />
            <button
              type="submit"
              className="hover:scale-105 hover:cursor-pointer"
            >
              {isEdit ? "Submit" : "Delete"}
            </button>
          </div>
          <div></div>
          <ErrorList errors={errors} />
        </form>
      </div>
    </div>
  );
}
