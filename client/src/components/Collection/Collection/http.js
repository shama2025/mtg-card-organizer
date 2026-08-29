import collectionUrl from "../collectionUrl";

export async function fetchCollection(collectionId, loggedInUser) {
  try {
    const response = await fetch(`${collectionUrl}/${collectionId}`, {
      method: "GET",
      headers: {
        Authorization: JSON.stringify(loggedInUser),
      },
    });
    if (response.ok) {
      const payload = await response.json();
      return {
        collection: payload,
      };
    } else {
      const payload = await response.json();
      return {
        errors: payload,
      };
    }
  } catch (errors) {
    return {
      errors,
    };
  }
}

export async function editCardCount(
  quantity,
  cardId,
  collectionId,
  loggedInUser,
) {
  try {
    const response = await fetch(
      `${collectionUrl}/${collectionId}/card/${cardId}`,
      {
        method: "PUT",
        headers: {
          Authorization: JSON.stringify(loggedInUser),
          "content-type": "application/json",
        },
        body: JSON.stringify({ quantity }),
      },
    );
    if (response.ok) {
      // return quantity with undefined errors
      return {
        cardId: cardId,
        errors: undefined,
      };
    } else {
      // return errors with undefined quantity
      const payload = await response.json();
      return {
        cardId: undefined,
        errors: payload.message,
      };
    }
  } catch (errors) {
    // return errors with undefined quantity
    return {
      cardId: undefined,
      errors: errors,
    };
  }
}

export async function deleteCard(cardId, collectionId, loggedInUser) {
  try {
    const response = await fetch(
      `${collectionUrl}/${collectionId}/card/${cardId}`,
      {
        method: "DELETE",
        headers: {
          Authorization: JSON.stringify(loggedInUser),
          "content-type": "application/json",
        },
      },
    );
    if (response.ok) {
      // return quantity with undefined errors
      return {
        cardId: cardId,
        errors: undefined,
      };
    } else {
      // return errors with undefined quantity
      const payload = await response.json();
      return {
        cardId: cardId,
        errors: payload.message,
      };
    }
  } catch (errors) {
    // return errors with undefined quantity
    return {
      cardId: cardId,
      errors: errors,
    };
  }
}
