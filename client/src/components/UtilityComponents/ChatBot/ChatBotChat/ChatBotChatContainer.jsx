import React, { useRef, useState } from "react";
import Chat from "../Chat/Chat";
import { fetchModelChat } from "./http";

export default function ChatBotChatContainer() {
  const [chatList, setChatList] = useState([]);
  const [message, setMessage] = useState("");
  const endRef = useRef(null);
  const initialChat = {
    user: message,
    ai: null,
  };
  async function handleChatBotResponse(event) {
    event.preventDefault();
    const query = message;
    setMessage("");
    setChatList([...chatList, initialChat]);
    const { modelResponse, errors } = await fetchModelChat(message);
    const chat = {
      user: query,
      ai: modelResponse || errors,
    };
    setChatList([...chatList, chat]);
  }

  return (
    <div className="flex flex-col h-80 w-64 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
      <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-2 shrink-0">
        ChatBot
      </h2>
      <div className="flex-1 min-h-0 overflow-y-auto pr-1 space-y-2">
        {chatList.length > 0 ? (
          chatList.map((chat, index) => (
            <Chat key={index} modelChat={chat.ai} userChat={chat.user} />
          ))
        ) : (
          <p className="text-xs text-slate-400 italic text-center mt-4">
            Ask something about your cards...
          </p>
        )}
      </div>
      <form
        onSubmit={handleChatBotResponse}
        className="mt-2 shrink-0 pt-2 border-t border-slate-700/60"
      >
        <div className="flex gap-1">
          <input
            type="text"
            className="w-full text-xs p-2 rounded-md bg-slate-900 border border-slate-700 text-white placeholder:text-slate-400 focus:outline-none focus:ring-1 focus:ring-jeskai-blue-light"
            placeholder="Ask a question..."
            value={message}
            onChange={(event) => setMessage(event.target.value)}
          />
          <button type="submit" className="hidden" />
        </div>
      </form>
    </div>
  );
}
