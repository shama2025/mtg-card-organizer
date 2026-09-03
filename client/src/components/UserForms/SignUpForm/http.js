import userUrl from "../../url/userUrl";

export async function createUser(user) {
  try {
    const response = await fetch(`${userUrl}/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    });

    // returns [user obj, errors]
    if (response.ok) {
      const user = await response.json();
      return {
        user,
      };
    }
    const errors = await response.json();
    return {
      errors,
    };
  } catch (errors) {
    return {
      errors,
    };
  }
}
