export async function fetchModelChat(message) {
  try {
    debugger;
    const encodedString = encodeURIComponent(message);
    const response = await fetch(
      `http://localhost:8080/api/ollama/${encodedString}`,
    );
    if (response.ok) {
      const payload = await response.text();
      return {
        modelResponse:
          payload || "Could not parse model text. Please try again.",
        errors: null,
      };
    } else {
      const payload = await response.json();
      return {
        modelResponse: null,
        errors: payload.message,
      };
    }
  } catch (errors) {
    return {
      modelResponse: null,
      errors: errors.message,
    };
  }
}
