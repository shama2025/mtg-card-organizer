import React, { useState } from "react";
import Chat from "../Chat/Chat";
import { fetchModelChat } from "./http";

export default function ChatBotChatContainer() {
  const [chatList, setChatList] = useState([]);
  const [message, setMessage] = useState("");
  const initialChat = {
    user: message,
    ai: null,
  };
  async function handleChatBotResponse(event) {
    event.preventDefault();
    debugger;
    setChatList([...chatList, initialChat]);
    const { modelResponse, errors } = await fetchModelChat(message);
    const chat = {
      user: message,
      ai: modelResponse || errors,
    };
    setMessage("");
    setChatList([...chatList, chat]);
  }

  return (
    <div className="lg:col-span-3 bg-jeskai-card text-jeskai-white-pure p-4 rounded-xl border border-slate-700 shadow-lg">
      <h2 className="text-lg font-bold text-jeskai-blue-light border-b border-slate-700 pb-2 mb-3">
        ChatBot
      </h2>
      <div className="container wrap-normal">
        <div>
          {/* List of chats */}
          {chatList.length > 0
            ? chatList?.map((chat, index) => {
                return (
                  <Chat key={index} modelChat={chat.ai} userChat={chat.user} />
                );
              })
            : null}
        </div>
        <div className="flex justify-center items-center">
          <form onSubmit={handleChatBotResponse}>
            <input
              type="text"
              className="border p-1 rounded-md w-full"
              placeholder="Ask a question ..."
              value={message}
              onChange={(event) => setMessage(event.target.value)}
            />
            <button type="submit"></button>
          </form>
        </div>
      </div>
    </div>
  );
}
