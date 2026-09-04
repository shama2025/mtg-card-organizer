import React, { useState } from "react";
import { BotMessageSquare } from "lucide-react";

export default function ChatBotIcon({ setShowChat, showChat }) {
  return (
    <div className="relative right-7 top-6 z-50">
      <div
        className="bg-jeskai-blue-dark w-fit rounded-2xl p-1 hover:scale-115"
        onClick={() => setShowChat(showChat ? false : true)}
      >
        <BotMessageSquare />
      </div>
    </div>
  );
}
