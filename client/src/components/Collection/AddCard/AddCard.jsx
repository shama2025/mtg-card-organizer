import React from "react";
import { SquarePlus } from "lucide-react";

export default function AddCard({ setAddCardModalVisible }) {
  return (
    <div
      className="flex flex-col justify-center items-center text-gray-400 hover:cursor-pointer 
    hover:shadow-lg hover:shadow-jeskai-red-light hover:rounded-2xl hover:scale-105"
      onClick={() => setAddCardModalVisible(true)}
    >
      <SquarePlus className="w-20 h-20" />
      <div className="pt-3 font-semibold">
        <p>Add a Card</p>
      </div>
    </div>
  );
}
