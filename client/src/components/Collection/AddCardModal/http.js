import { jsx } from "react/jsx-runtime";
import binderUrl from "../../url/binderUrl";
import collectionUrl from "../../url/collectionUrl";

export async function addCardToCollection(name, collectionId, jwtToken) {
  try {
    const response = await fetch(`${collectionUrl}/${collectionId}`, {
      method: "POST",
      headers: {
        Authorization: JSON.stringify(jwtToken),
        "content-type": "application/json",
      },
      body: JSON.stringify({ name }),
    });
    if (response.ok) {
      const payload = await response.json();
      return {
        card: payload,
        errors: undefined,
      };
    } else {
      const payload = await response.json();
      return {
        card: undefined,
        errors:
          payload.message ||
          "Failed to add card. Make sure card doesn't already exist.",
      };
    }
  } catch (errors) {
    return {
      card: undefined,
      errors: errors || "Network error.",
    };
  }
}

export async function addCardToBinder(name, binder, binderId, jwtToken) {
  try {
    const response = await fetch(`${binderUrl}/deck/${binderId}/card/${name}`, {
      method: "POST",
      headers: {
        Authorization: JSON.stringify(jwtToken),
        "content-type": "application/json",
      },
      body: JSON.stringify(binder),
    });
    if (response.ok) {
      const payload = await response.json();
      return {
        card: payload,
        errors: undefined,
      };
    } else {
      const payload = await response.json();
      return {
        card: undefined,
        errors: payload.message || payload,
      };
    }
  } catch (errors) {
    return {
      card: undefined,
      errors: errors || "Network error.",
    };
  }
}
