// Handles http requests
import userUrl from "./userUrl";

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

export async function loginUser(user) {
  try {
    // Test User: test@test.com password 1234
    const response = await fetch(`${userUrl}/login`, {
      method: "POST",
      headers: {
        Authorization: JSON.stringify(user),
      },
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
