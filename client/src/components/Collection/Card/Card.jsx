import React from "react";

export default function Card({ card }) {
  return (
    <div>
      <img src={card.imgPath[0].normal} className="h-45" />
    </div>
  );
}
