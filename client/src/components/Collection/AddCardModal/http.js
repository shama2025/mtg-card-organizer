import collectionUrl from "../collectionUrl";

export async function addCardToCollection(name, collectionId, loggedInUser) {
  try {
    const response = await fetch(`${collectionUrl}/${collectionId}`, {
      method: "POST",
      headers: {
        Authorization: JSON.stringify(loggedInUser),
        "content-type": "application/json",
      },
      body: JSON.stringify({ name }),
    });
    if (response.ok) {
      const payload = await response.json();
      return {
        card: payload,
        errors: undefined
      };
    } else {
      const payload = await response.json();
      return {
        card: undefined,
        errors: payload.message || "Failed to add card. Make sure card doesn't already exist.",
      };
    }
  } catch (errors) {
    return {
      card: undefined,
      errors: errors || "Network error.",
    };
  }
}
