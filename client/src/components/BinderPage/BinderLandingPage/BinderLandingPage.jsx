import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchBinder } from "./http";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import CardInfo from "../../Collection/CardInfoContainer/CardInfo";
import AddCard from "../../Collection/AddCard/AddCard";
import AddCardModal from "../../Collection/AddCardModal/AddCardModal";

export default function BinderLandingPage() {
  const { binderId } = useParams("binderId");
  const [isAddCardModalVisible, setAddCardModalVisible] = useState(false);
  const [binderQuantity, setCollectionQuantity] = useState(0);
  const [card, setCard] = useState({});
  const [binder, setBinder] = useState({});
  const [binderErrors, setBinderErrors] = useState([]);

  const loggedInUser = useContext(LoggedInUser);

  useEffect(function () {
    async function handleFetchBinder() {
      const { binder, errors } = await fetchBinder(binderId, loggedInUser);
      if (errors) {
        setBinderErrors(errors);
      } else if (binder) {
        setBinder(binder);
      }
    }
    handleFetchBinder();
  }, []);

  if (!binder) {
    return (
      <div className="w-full max-w-7xl mx-auto p-6 md:p-8 min-h-screen">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
            <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-3">
              Binder Overview
            </h2>
            <p className="text-sm text-slate-300">
              Total Cards: <span className="font-semibold text-white">{0}</span>
            </p>
            <div
              className="lg:col-span-3 bg-jeskai-card
         text-jeskai-white-pure p-4 rounded-xl border
          border-slate-700 shadow-lg
          mt-4
          "
            ></div>
          </div>
          <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
              <p className="text-gray-400 font-semibold">
                No cards in your binder. Would you like to add one?
              </p>
              <AddCard setAddCardModalVisible={setAddCardModalVisible} />
              {binderErrors.length > 0 ? (
                <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
                  <ErrorList errors={binderErrors} />
                </div>
              ) : (
                <></>
              )}
              <div hidden={!isAddCardModalVisible}>
                <AddCardModal
                  setAddCardModalVisible={setAddCardModalVisible}
                  setBinder={setBinder}
                  binder={binder}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full max-w-7xl mx-auto p-6 md:p-8 min-h-screen">
      <div hidden={!isAddCardModalVisible}>
        <AddCardModal
          setAddCardModalVisible={setAddCardModalVisible}
          setBinder={setBinder}
          binder={binder}
        />
      </div>
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
          <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-3">
            {binder?.name || "Unamed Binder"}
          </h2>
          <p className="text-sm text-slate-300">
            Total Cards:{" "}
            <span className="font-semibold text-white">{binderQuantity}</span>
          </p>
          <div>
            {binderErrors.length > 0 ? (
              <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
                <ErrorList errors={binderErrors} />
              </div>
            ) : (
              <></>
            )}
          </div>
        </div>
        <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
          {binderErrors.length > 0 ? (
            <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
              <ErrorList errors={binderErrors} />
            </div>
          ) : (
            <></>
          )}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            {binder?.cardList?.length > 0 ? (
              binder.cardList.map((card) => (
                <div
                  key={card.id}
                  onMouseOver={() => setCard(card)}
                  className="transition-transform hover:-translate-y-1 cursor-pointer"
                >
                  <div className="relative inline-block overflow-hidden rounded-lg transition-transform duration-200 hover:scale-105 hover:-translate-y-1 cursor-pointer">
                    <div className="absolute z-10 bg-jeskai-blue-light text-jeskai-dark font-bold text-xs px-2 py-0.5 rounded-full shadow-md">
                      {card?.quantity}
                    </div>
                    <div
                      className="absolute z-10
                   hover:bg-gray-500
                    hover:scale-115 
                    border-jeskai-blue 
                    top-5 right-1.5 
                    bg-gray-500/50
                     text-jeskai-dark 
                     font-bold text-xs 
                     px-1 py-0.5 
                     rounded-md 
                     shadow-md
                     "
                      onClick={() => handleCardCount(card, true)}
                    >
                      <Plus />
                    </div>
                    <div
                      className="absolute z-10
                   hover:bg-gray-500 hover:scale-115
                    border-jeskai-blue top-15 right-1.5
                     bg-gray-500/50 text-jeskai-dark 
                     font-bold text-xs px-1 py-0.5 
                     rounded-md shadow-md"
                      onClick={() => handleCardCount(card, false)}
                    >
                      <Minus />
                    </div>
                    <img
                      src={card?.imgPath?.[0]?.large || card?.imgPath}
                      alt={card?.name}
                      className="w-full h-auto block"
                    />
                  </div>
                </div>
              ))
            ) : (
              <div>
                <AddCard setAddCardModalVisible={setAddCardModalVisible} />
              </div>
            )}
          </div>
        </div>

        <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg sticky top-20">
          <h3 className="text-md font-semibold text-jeskai-red-light mb-3 border-b border-slate-700 pb-2">
            Card Details
          </h3>
          <CardInfo card={card} />
        </div>
      </div>
    </div>
  );
}
