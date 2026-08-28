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
