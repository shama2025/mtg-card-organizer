import { React, useContext, useEffect, useState } from "react";
import { fetchCollection } from "./http";
import CardInfo from "../CardInfoContainer/CardInfo";
import AddCard from "../AddCard/AddCard";
import AddCardModal from "../AddCardModal/AddCardModal";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { CollectionId } from "../../../contexts/CollectionId";

export default function Collection() {
  const loggedInUser = useContext(LoggedInUser);
  const collectionId = useContext(CollectionId);

  const [collection, setCollection] = useState(undefined);
  const [card, setCard] = useState(undefined);
  const [errors, setErrors] = useState([]);
  const [isAddCardModalVisible, setAddCardModalVisible] = useState(false);

  useEffect(function () {
    async function handleFecthCollection() {
      const response = await fetchCollection(collectionId, loggedInUser);
      if (response.collection) {
        setCollection(response.collection);
      } else {
        setErrors(response.errors);
      }
    }
    handleFecthCollection();
  }, []);

  if (!collection) {
    return (
      <div className="w-full max-w-7xl mx-auto p-6 md:p-8 min-h-screen">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
            <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-3">
              Collection Overview
            </h2>
            <p className="text-sm text-slate-300">
              Total Cards: <span className="font-semibold text-white">{0}</span>
            </p>
          </div>
          <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
              <p className="text-gray-400 font-semibold">
                No cards in your collection. Would you like to add one?
              </p>
              <AddCard setAddCardModalVisible={setAddCardModalVisible} />
              <div hidden={!isAddCardModalVisible}>
                <AddCardModal
                  setAddCardModalVisible={setAddCardModalVisible}
                  setCollection={setCollection}
                  collection={collection}
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
          setCollection={setCollection}
          collection={collection}
        />
      </div>
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
          <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-3">
            Collection Overview
          </h2>
          <p className="text-sm text-slate-300">
            Total Cards:{" "}
            <span className="font-semibold text-white">
              {collection.length}
            </span>
          </p>
        </div>

        <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            <AddCard setAddCardModalVisible={setAddCardModalVisible} />
            {collection.map((card) => (
              <div
                key={card.id}
                onMouseOver={() => setCard(card)}
                className="transition-transform hover:-translate-y-1 cursor-pointer"
              >
                <div className="relative inline-block overflow-hidden rounded-lg transition-transform duration-200 hover:scale-105 hover:-translate-y-1 cursor-pointer">
                  <div className="absolute z-10 bg-jeskai-blue-light text-jeskai-dark font-bold text-xs px-2 py-0.5 rounded-full shadow-md">
                    {card?.quantity}
                  </div>
                  <img
                    src={card?.imgPath?.[0]?.large || card?.imgPath}
                    alt={card?.name}
                    className="w-full h-auto block"
                  />
                </div>
              </div>
            ))}
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
