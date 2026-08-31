import binderUrl from "../../url/binderUrl";

export async function fetchBinder(binderId, loggedInUser) {
  try {
    const response = await fetch(`${binderUrl}/deck/${binderId}`, {
      method: "GET",
      headers: {
        Authorization: JSON.stringify(loggedInUser),
      },
    });
    if (response.ok) {
      const payload = await response.json();
      return {
        binder: payload,
        errors: null,
      };
    } else {
      const payload = await response.json();
      return {
        binder: null,
        errors: payload.message,
      };
    }
  } catch (errors) {
    return {
      binder: null,
      errors,
    };
  }
}

export async function editCardCount(quantity, cardId, binderId, loggedInUser) {
  try {
    const response = await fetch(
      `${binderUrl}/deck/${binderId}/card/${cardId}`,
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

export async function deleteCard(cardId, binderId, loggedInUser) {
  try {
    const response = await fetch(
      `${binderUrl}/deck/${binderId}/card/${cardId}`,
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
