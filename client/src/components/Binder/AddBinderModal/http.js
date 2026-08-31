import binderUrl from "../../url/binderUrl";

export async function addBinder(binder, collectionId, loggedInUser) {
  try {
    const response = await fetch(
      `${binderUrl}/collection/${collectionId.collectionId}/deck`,
      {
        method: "POST",
        headers: {
          Authorization: JSON.stringify(loggedInUser),
          "content-type": "application/json",
        },
        body: JSON.stringify(binder),
      },
    );
    if (response.ok) {
      const payload = await response.json();
      return {
        newBinder: payload,
        errors: undefined,
      };
    } else {
      const payload = await response.json();
      return {
        newBinder: undefined,
        errors: payload,
      };
    }
  } catch (errors) {
    return {
      newBinder: undefined,
      errors: errors || "Network error.",
    };
  }
}
