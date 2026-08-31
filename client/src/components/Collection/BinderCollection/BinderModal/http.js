import binderUrl from "../../../url/binderUrl";

export async function deleteBinder(binderId, loggedInUser) {
  try {
    debugger
    const response = await fetch(`${binderUrl}/deck/${binderId}`, {
      method: "DELETE",
      headers: {
        Authorization: JSON.stringify(loggedInUser),
      },
    });
    if (response.ok) {
      return {
        isDeleted: true,
        errors: null,
      };
    } else {
      const payload = await response.json();
      return {
        isDeleted: false,
        errors: payload,
      };
    }
  } catch (errors) {
    return {
      isDeleted: true,
      errors: errors,
    };
  }
}
