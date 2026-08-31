import { React, useContext, useEffect, useState } from "react";
import {
  deleteCard,
  editCardCount,
  fetchBinders,
  fetchCollection,
} from "./http";
import CardInfo from "../CardInfoContainer/CardInfo";
import AddCard from "../AddCard/AddCard";
import AddCardModal from "../AddCardModal/AddCardModal";
import { LoggedInUser } from "../../../contexts/LoggedInUser";
import { CollectionId } from "../../../contexts/CollectionId";
import { Plus, Minus, SquarePlus } from "lucide-react";
import ErrorList from "../../ErrorList/ErrorList";
import BinderNameCard from "../../Binder/BinderCard/BinderNameCard";
import BinderModal from "../../Binder/BinderModal/BinderModal";
import AddBinder from "../../Binder/AddBinder/AddBinder";
import AddBinderModal from "../../Binder/AddBinderModal/AddBinderModal";
import SearchBar from "../../UtilityComponents/SearchForCard/SearchBar";

export default function Collection() {
  const [collection, setCollection] = useState(undefined);
  const [card, setCard] = useState(undefined);
  const [collectionErrors, setCollectionErrors] = useState([]);
  const [isAddCardModalVisible, setAddCardModalVisible] = useState(false);
  const [collectionQuantity, setCollectionQuantity] = useState(0);
  const [binders, setBinders] = useState([]);
  const [binderErrors, setBinderErrors] = useState([]);
  const [displayBinderModal, setDisplayBinderModal] = useState(true);
  const [isEdit, setIsEdit] = useState(false);
  const [binderToEdit, setBinderToEdit] = useState({});
  const [displayBinderCardModal, setDisplayBinderCardModal] = useState(true);

  const loggedInUser = useContext(LoggedInUser);
  const collectionId = useContext(CollectionId).collectionId;

  function handleCardCount(cardToUpdate, isQuantityIncreased) {
    const updatedCard = {
      ...cardToUpdate,
      quantity: cardToUpdate.quantity + (isQuantityIncreased ? 1 : -1),
    };

    if (updatedCard.quantity > 0) {
      // Edit existing card count
      const { cardId, errors } = editCardCount(
        updatedCard.quantity,
        updatedCard.id,
        collectionId,
        loggedInUser,
      );
      if (errors) {
        setCollectionErrors(errors);
        return;
      }

      const updatedCollection = collection.map((c) =>
        c.id === updatedCard.id ? updatedCard : c,
      );
      setCollection(updatedCollection);
      calculateCollectionQuantity(updatedCollection);
    } else if (updatedCard.quantity === 0) {
      // Delete card completely
      const { cardId, errors } = deleteCard(
        updatedCard.id,
        collectionId,
        loggedInUser,
      );
      if (errors) {
        setCollectionErrors(errors);
        return;
      }

      const updatedCollection = collection.filter(
        (c) => c.id !== updatedCard.id,
      );
      setCollection(updatedCollection);
      calculateCollectionQuantity(updatedCollection);
    }
  }

  function calculateCollectionQuantity(currentCollection) {
    let sum = 0;
    currentCollection.forEach((card) => {
      sum += card.quantity;
    });
    setCollectionQuantity(sum);
  }

  useEffect(
    function () {
      async function handleFecthCollection() {
        const response = await fetchCollection(collectionId, loggedInUser);
        if (response.collection) {
          setCollection(response.collection);
          calculateCollectionQuantity(response.collection);
        } else {
          setCollectionErrors(response.errors);
        }
      }
      handleFecthCollection();
    },
    [collectionId, loggedInUser],
  );

  useEffect(
    function () {
      async function handleFetchBinders() {
        const { binders, errors } = await fetchBinders(
          collectionId,
          loggedInUser,
        );
        if (errors) {
          setBinderErrors(errors);
        } else if (binders) {
          setBinders(binders);
        }
      }
      handleFetchBinders();
    },
    [collectionId, loggedInUser, binders],
  );

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
            <div
              className="lg:col-span-3 bg-jeskai-card
         text-jeskai-white-pure p-4 rounded-xl border
          border-slate-700 shadow-lg
          mt-4
          "
            >
              <p className="text-jeskai-white-pure text-lg">Binders</p>
              <p>No binders in your collection. Would you like to add one?</p>
            </div>
          </div>
          <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
              <p className="text-gray-400 font-semibold">
                No cards in your collection. Would you like to add one?
              </p>
              <AddCard setAddCardModalVisible={setAddCardModalVisible} />
              {collectionErrors.length > 0 ? (
                <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
                  <ErrorList errors={collectionErrors} />
                </div>
              ) : (
                <></>
              )}
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
              {collectionQuantity}
            </span>
          </p>
          <div
            className="lg:col-span-3 bg-jeskai-card
         text-jeskai-white-pure p-4 rounded-xl border
          border-slate-700 shadow-lg
          mt-4
          "
          >
            <AddBinder setDisplayBinderCardModal={setDisplayBinderCardModal} />

            {binderErrors.length > 0 ? (
              <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
                <ErrorList errors={binderErrors} />
              </div>
            ) : (
              <></>
            )}
            {binders.map((binder, elementId) => {
              return (
                <div
                  key={elementId}
                  className="hover:border-b-2 hover:scale-105 hover:border-jeskai-red-dark"
                >
                  <div>
                    <BinderNameCard
                      binder={binder}
                      setBinderToEdit={setBinderToEdit}
                      setDisplayBinderModal={setDisplayBinderModal}
                      setIsEdit={setIsEdit}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
        <div className="relative lg:col-span-6 bg-jeskai-white-border p-4 rounded-xl border border-slate-300 shadow-md">
          {collectionErrors.length > 0 ? (
            <div className="fixed inset-0 z-100 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
              <ErrorList errors={collectionErrors} />
            </div>
          ) : (
            <></>
          )}
          <div hidden={displayBinderModal}>
            <BinderModal
              isEdit={isEdit}
              binderToEdit={binderToEdit}
              setDisplayBinderModal={setDisplayBinderModal}
              binders={binders}
              setBinders={setBinders}
            />
          </div>
          <div hidden={displayBinderCardModal}>
            <AddBinderModal
              setDisplayBinderCardModal={setDisplayBinderCardModal}
              setBinders={setBinders}
              binders={binders}
            />
          </div>
          <SearchBar cardList={collection} setCardList={setCollection} />
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
