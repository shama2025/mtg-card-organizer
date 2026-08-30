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
