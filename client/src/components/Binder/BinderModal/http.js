import binderUrl from "../../url/binderUrl";

export async function deleteBinder(binderId, jwtToken) {
  try {
    const response = await fetch(`${binderUrl}/deck/${binderId}`, {
      method: "DELETE",
      headers: {
        Authorization: JSON.stringify(jwtToken),
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
