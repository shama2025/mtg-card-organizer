import React, { useState } from "react";

export default function FilterCards({ cardList, setCardList }) {
  const [sortBy, setSortBy] = useState("");
  const [sortOrder, setSortOrder] = useState("asc");

  function handleSort(property, order) {
    if (!property) return;

    const cardListCopy = [...cardList];
    const sorted = cardListCopy.sort((a, b) => {
      if (property === "setName") {
        const valA = a?.set?.name ?? "";
        const valB = b?.set?.name ?? "";
        if (order === "desc") {
          return valB.localeCompare(valA);
        }
        return valA.localeCompare(valB);
      } else if (property === "artistName") {
        const valA = a[property] ?? "";
        const valB = b[property] ?? "";
        if (order === "desc") {
          return valB.localeCompare(valA);
        }
        return valA.localeCompare(valB);
      } else if (property === "manaCost") {
        const valA = a?.manaCost?.cmc ?? "";
        const valB = b?.manaCost?.cmc ?? "";
        if (order === "desc") {
          return valB - valA;
        }
        return valA - valB;
      } else if (property === "manaColor") {
        const valA = getManaColorWeights(a);
        const valB = getManaColorWeights(b);
        if (order === "desc") {
          return valB - valA;
        }
        return valA - valB;
      }
    });

    function getManaColorWeights(card) {
      const color = card?.manaColor?.colors;
      let weight = 0;
      if (color?.length == 0) {
        // colorless cards
        return (weight = 6);
      } else if (color?.length > 1) {
        // multi-color cards
        return (weight = 7);
      } else {
        switch (color[0]) {
          case "W":
            return 1; // White
          case "U":
            return 2; // Blue
          case "B":
            return 3; // Black
          case "R":
            return 4; // Red
          case "G":
            return 5; // Green
          default:
            return 6; // Fall back
        }
      }
    }

    setCardList(sorted);
  }

  return (
    <div>
      <select
        onChange={(e) => {
          const property = e.target.value;
          setSortBy(property);
          handleSort(property, sortOrder);
        }}
      >
        <option value="">Select a value</option>
        <option value="setName">Set</option>
        <option value="artistName">Artist</option>
        <option value="manaColor">Color</option>
        <option value="manaCost">Converted Mana Cost</option>
      </select>

      <select
        onChange={(e) => {
          const newOrder = e.target.value;
          setSortOrder(newOrder);
          handleSort(sortBy, newOrder);
        }}
      >
        <option value="asc">Ascending</option>
        <option value="desc">Descending</option>
      </select>
    </div>
  );
}
