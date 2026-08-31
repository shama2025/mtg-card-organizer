import React from 'react'
import { useState } from 'react'

export default function SearchBar({cardList, setCardList}) {

    const [initialCards,setInitialCards] = useState(cardList)

    function handleCardSearch(event) {
        const cardName = event.target.value
        setCardList(
        initialCards.filter(card => 
            card?.name?.toUpperCase().includes(cardName.toUpperCase())
        )
        )
    }

  return (
    <div className='pb-4'>
        <input type='text' 
        placeholder='Search for a card you own.'
        className='border p-1 rounded-md pb-1'
        onChange={handleCardSearch}
        />
    </div>
  )
}
