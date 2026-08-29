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
      };
    } else {
      debugger;
      const payload = await response.json();
      return {
        errors: payload.message,
      };
    }
  } catch (errors) {
    return {
      errors: errors,
    };
  }
}
